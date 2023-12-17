package org.aydm.danak.service.facade;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.aydm.danak.service.FileBelongingsService;
import org.aydm.danak.service.FileService;
import org.aydm.danak.service.VersionService;
import org.aydm.danak.service.dto.FileBelongingsDTO;
import org.aydm.danak.service.dto.FileDTO;
import org.aydm.danak.service.dto.VersionDTO;
import org.aydm.danak.web.rest.errors.BadRequestAlertException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface AssetFacade {

    void initializeVersioning(String tag) throws IOException;

    UpdateResponse updateAssets(int fromVersion, int toVersion) throws IOException;


    DownloadResponse download(int version, boolean shouldIncludeSize) throws IOException;
    DownloadResponse download(int version) throws IOException;
    void uploadApk(@NotNull MultipartFile file) throws IOException;

    List<String> checkExistence(int version) throws IOException;
}

@Transactional
@Service
class AssetFacadeImpl implements AssetFacade {
    private final FileService fileService;
    private final FileBelongingsService fileBelongingsService;
    private final VersionService versionService;

    public AssetFacadeImpl(FileService fileService, FileBelongingsService fileBelongingsService, VersionService versionService) {
        this.fileService = fileService;
        this.fileBelongingsService = fileBelongingsService;
        this.versionService = versionService;
    }

    private final Logger log = LoggerFactory.getLogger(AssetFacadeImpl.class);

    //base
    @Value("${asset.diffs}")
    private String diffsPath;

    @Value("${asset.apk.zip}")
    private String zipPath;

    @Value("${asset.apk.base}")
    private String apkBasePath;

    @Value("${asset.versions}")
    private String versionsPath;

    @Value("${asset.lists}")
    private String listsPath;

    @Value("${asset.caches}")
    private String cachePath;
    //base

    //repo
    @Value("${asset.repo.content}")
    private String contentPath;

    @Value("${asset.repo.path}")
    private String repoPath;

    @Value("${asset.repo.gradle}")
    private String gradlePath;


    @Value("${asset.repo.user}")
    private String repoUser;

    @Value("${asset.repo.token}")
    private String repoToken;
    //repo

    //scripts
    @Value("${asset.script.cleanup}")
    private String cleanupScript;

    @Value("${asset.script.copy}")
    private String copyScript;

    @Value("${asset.script.generate-csv}")
    private String generateCSVScript;

    @Value("${asset.script.generate-diff}")
    private String generateDiffScript;

    @Value("${asset.script.remove-unnecessary}")
    private String removeUnnecessaryScript;


    //scripts


    @Override
    public void initializeVersioning(String tag) throws IOException {
        if (!checkout(repoPath, tag)) return;
        versionAsset(tag, getVersionCode(gradlePath));
    }

    public boolean checkout(String repoAddress, String tag) {
        log.info("tag{{}} - start checking out the directory {}", tag, repoAddress);
        try (Repository repository = new FileRepositoryBuilder().setGitDir(new File(repoAddress + "/.git")).build()) {
            CredentialsProvider cp = new UsernamePasswordCredentialsProvider(repoUser, repoToken);
            Git git = new Git(repository);
            git.fetch().setCredentialsProvider(cp).call();
            git.checkout().setName("refs/tags/" + tag).call();
            log.info("tag{{}} - checking out the directory {} is done successfully", tag, repoAddress);
            return true;
        } catch (IOException | GitAPIException e) {
            log.error("tag{{}} - error checking out the directory {}", tag, repoAddress, e);
            return false;
        }
    }

    public int getVersionCode(String buildGradlePath) throws IOException {
        // Read the build.gradle file
        File buildGradleFile = new File(buildGradlePath);
        BufferedReader reader = new BufferedReader(new FileReader(buildGradleFile, StandardCharsets.UTF_8));
        String line;
        int versionCode = -1;

        // Initialize the regex pattern for the version code
        Pattern pattern = Pattern.compile("versionCode\\s+(\\d+)");

        // Search for the version code in the build.gradle file
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String versionCodeString = matcher.group(1);
                versionCode = Integer.parseInt(versionCodeString);
                break;
            }
        }
        reader.close();

        // Check if the version code was found
        if (versionCode == -1) {
            throw new RuntimeException("Version code not found in build.gradle file");
        }

        return versionCode;
    }

    private void versionAsset(String tag, int version) throws IOException {
        final VersionDTO versionDTO = versionService.saveOrGet(new VersionDTO(
            null, version, tag
        ));
        String newVersionPath = versionsPath + version;
        String newVersionCSVPath = listsPath + version;
        List<CommandData> commands = new ArrayList<>();
        commands.add(
            new CommandData(
                removeUnnecessaryScript + ' ' + listsPath + ' ' + cachePath + ' ' + diffsPath,
                success -> log.info("version{{}} - removing unnecessary files stage success: {}", version, success),
                failed -> log.error("version{{}} - removing unnecessary files stage failed: {}", version, failed)
            )
        );
        commands.add(
            new CommandData(
                copyScript + ' ' + newVersionPath + ' ' + contentPath,
                success -> log.info("version{{}} - copying stage success: {}", version, success),
                failed -> log.error("version{{}} - copying stage failed: {}", version, failed)
            )
        );
        commands.add(
            new CommandData(
                generateCSVScript + ' ' + newVersionPath + ' ' + newVersionCSVPath,
                success -> log.info("version{{}} - generate csv stage success: {}", version, success),
                failed -> log.error("version{{}} - generate csv stage failed: {}", version, failed)
            )
        );
        Optional<Integer> oldVersion = getTheLastVersion();
        if (oldVersion.isPresent()) {
            Integer oldVersionSafe = oldVersion.orElseThrow();
            String oldVersionPath = versionsPath + oldVersionSafe;
            String oldVersionCSVPath = listsPath + oldVersionSafe;
            if (!new File(oldVersionCSVPath).exists()) {
                commands.add(
                    new CommandData(
                        generateCSVScript + ' ' + oldVersionPath + ' ' + oldVersionCSVPath,
                        success -> log.info("version{{}} - generate csv for old version{{}} stage success: {}", version, oldVersion, success),
                        failed -> log.error("version{{}} - generate csv for old version{{}} stage failed: {}", version, oldVersion, failed)
                    )
                );
            }
            commands.add(
                new CommandData(
                    cleanupScript + ' ' + oldVersionPath + ' ' + newVersionPath,
                    success -> log.info("version{{}} - cleanup stage success: {}", version, success),
                    failed -> log.error("version{{}} - cleanup stage failed: {}", version, failed)
                )
            );
            commands.add(
                new CommandData(
                    generateDiffScript + ' ' + listsPath + ' ' + diffsPath,
                    success -> log.info("version{{}} - generate diffs stage success: {}", version, success),
                    failed -> log.error("version{{}} - generate diffs stage failed: {}", version, failed)
                )
            );
        }

        new CommandRunner().runCommands(commands)
            .thenRun(() -> {
                saveAllFiles(oldVersion, versionDTO, version, newVersionCSVPath);
                try {
                    DownloadResponse download = download(version);
                    new ZipDownload().start(
                        download.getFiles(),
                        versionsPath,
                        zipPath + version + ".zip",
                        version,
                        cachePath + getDownloadPathByVersion(version)
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                log.info("version{{}} - All commands finished!", version);
            });
    }

    private List<FileDTO> saveAllFiles(Optional<Integer> oldVersion, VersionDTO versionDTO, int version, String newVersionCSVPath) {
        if (oldVersion.isPresent()) {
            try {
                final List<FileDTO> versionToBeUpdateFiles = fileService.findAllLastVersion(versionService.findIdByVersion(oldVersion.orElseThrow())).stream()
                    .peek(fileDTO -> fileDTO.setPlacement(versionDTO)).collect(Collectors.toList());
                final String diffAddress = diffsPath + oldVersion.orElseThrow() + '_' + version + "_diff.txt";
                File file = new File(diffAddress);
                if (!file.exists()) {
                    log.info("version{{}} - there is no diff so we just update the database", version);
                    List<FileDTO> allFiles = fileService.saveAll(versionToBeUpdateFiles);
                    fileBelongingsService.saveAll(allFiles.stream().map(it -> new FileBelongingsDTO(
                        null,
                        it,
                        versionDTO
                    )).collect(Collectors.toList()));
                    return allFiles;
                }
                final BufferedReader diffBR = new BufferedReader(new FileReader(file, Charset.defaultCharset()));
                final String header = diffBR.readLine();
                String line;
                final List<FileAddress> addresses = new ArrayList<>();
                while ((line = diffBR.readLine()) != null) {
                    if (line.startsWith("<")) {
                        //exists in old version but not in new version,
                        // we will remove these for the list of updated files
                        addresses.add(FileAddress.Companion.fromDiffLine(0, line));
                    } else if (line.startsWith(">")) {
                        //exists in new version but not in old version
                        FileAddress newAddress = FileAddress.Companion.fromDiffLine(0, line);
                        versionToBeUpdateFiles.add(new FileDTO(
                            null, newAddress.getName(), newAddress.getChecksum(), newAddress.getPath(), newAddress.getSize(), versionDTO
                        ));
                    }
                }
                //remove files that was available in old version and not in new version
                final List<FileDTO> notUpdatedFiles = new ArrayList<>();
                for (FileDTO fileDTO : versionToBeUpdateFiles) {
                    for (FileAddress fileAddress : addresses) {
                        if (Objects.equals(fileDTO.getChecksum(), fileAddress.getChecksum()) &&
                            Objects.equals(fileDTO.getName(), fileAddress.getName()) &&
                            Objects.equals(fileDTO.getPath(), fileAddress.getPath())) {
                            notUpdatedFiles.add(fileDTO);
                        }
                    }
                }
                versionToBeUpdateFiles.removeAll(notUpdatedFiles);
                List<FileDTO> allFiles = fileService.saveAll(versionToBeUpdateFiles);
                fileBelongingsService.saveAll(allFiles.stream().map(it -> new FileBelongingsDTO(
                    null,
                    it,
                    versionDTO
                )).collect(Collectors.toList()));
                return allFiles;
            } catch (IOException e) {
                log.error("version{{}} - an error happened during the final stage", version, e);
                return null;
            }
        } else {
            return initializeSaveDB(versionDTO, newVersionCSVPath);
        }
    }

    private List<FileDTO> initializeSaveDB(VersionDTO version, String csvPath) {
        try {
            final List<FileDTO> files = new ArrayList<>();
            final BufferedReader versionBufferedReader = new BufferedReader(new FileReader(csvPath + '/' + "files.csv", Charset.defaultCharset()));
            final String header = versionBufferedReader.readLine();
            String line;
            while ((line = versionBufferedReader.readLine()) != null) {
                final FileAddress fileAddress = FileAddress.Companion.fromCSVLine(version.getVersion(), line);
                files.add(new FileDTO(
                    null,
                    fileAddress.getName(),
                    fileAddress.getChecksum(),
                    fileAddress.getPath(),
                    fileAddress.getSize(),
                    version
                ));
            }
            fileBelongingsService.saveAll(fileService.saveAll(files).stream().map(fileDTO -> new FileBelongingsDTO(
                null, fileDTO, version
            )).collect(Collectors.toList()));
            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public UpdateResponse updateAssets(int fromVersion, int toVersion) throws IOException {
        String pathname = "U_" + fromVersion + "_" + toVersion + ".json";
        Optional<UpdateResponse> cache = lookupCache(pathname, UpdateResponse.class);
        if (cache.isPresent()) return cache.orElseThrow();
        Optional<Integer> lastVersion = getTheLastVersion();
        if (lastVersion.isEmpty())
            throw new BadRequestAlertException("update is pointless when there is no asset on the server", "asset", "");
        List<VersionDTO> allVersions = versionService.findAll();
        Map<Long, Integer> versionIdToVersion = new HashMap<>();
        for (VersionDTO v : allVersions) {
            versionIdToVersion.put(v.getId(), v.getVersion());
        }
        long fromVersionId = allVersions.stream().filter(it -> it.getVersion() == fromVersion).findFirst().orElseThrow().getId();
        long toVersionId = allVersions.stream().filter(it -> it.getVersion() == toVersion).findFirst().orElseThrow().getId();
        List<FileDTO> deletes = fileService.findAllUpdates(fromVersionId, toVersionId);
        List<FileResponse> deleteFileResult = new ArrayList<>();
        long deleteFileSize = 0L;
        for (FileDTO file : deletes) {
            deleteFileSize += Long.parseLong(file.getSize());
            deleteFileResult.add(new FileResponse(file.getChecksum(), file.getFtpPath(versionIdToVersion.get(file.getPlacement().getId()))));
        }
        List<FileDTO> updates = fileService.findAllUpdates(toVersionId, fromVersionId);
        List<FileResponse> updateFilesResult = new ArrayList<>();
        long updateFileSize = 0L;
        for (FileDTO file : updates) {
            updateFileSize += Long.parseLong(file.getSize());
            updateFilesResult.add(new FileResponse(file.getChecksum(), file.getFtpPath(versionIdToVersion.get(file.getPlacement().getId()))));
        }
        UpdateResponse result = new UpdateResponse(
            updateFileSize,
            updateFilesResult,
            deleteFileSize,
            deleteFileResult
        );
        saveCache(pathname, result);
        return result;
    }

    private <T> void saveCache(String pathname, T result) throws IOException {
        File file = new File(cachePath + pathname);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(file, result);
    }


    private <T> Optional<T> lookupCache(String pathname, Class<T> clazz) throws IOException {
        File file = new File(cachePath + pathname);
        if (!file.exists())
            return Optional.empty();
        ObjectMapper objectMapper = new ObjectMapper();
        return Optional.ofNullable(objectMapper.readValue(file, clazz));
    }

    @Override
    public DownloadResponse download(int version) throws IOException {
        return download(version,false);
    }
    @Override
    public DownloadResponse download(int version, boolean shouldIncludeSize) throws IOException {
        String pathname = getDownloadPathByVersion(version);
        Optional<DownloadResponse> cache = lookupCache(pathname, DownloadResponse.class);
        if (cache.isPresent()) return cache.orElseThrow();

        Optional<Integer> lastVersion = getTheLastVersion();
        if (lastVersion.isEmpty())
            throw new BadRequestAlertException("download is pointless when there is no asset on the server", "asset", "");
        List<VersionDTO> allVersions = versionService.findAll();
        Map<Long, Integer> versionIdToVersion = new HashMap<>();
        for (VersionDTO v : allVersions) {
            versionIdToVersion.put(v.getId(), v.getVersion());
        }
        Long versionId = allVersions.stream().filter(it -> it.getVersion() == version).findFirst().orElseThrow().getId();
        List<FileDTO> files = fileService.findAllBelongsToVersion(versionId);
        List<FileResponse> fileResponses = new ArrayList<>();
        long size = 0L;
        for (FileDTO file : files) {
            long longSize = Long.parseLong(file.getSize());
            size = size + longSize;
            fileResponses.add(new FileResponse(
                file.getChecksum(),
                file.getFtpPath(versionIdToVersion.get(file.getPlacement().getId())),
                shouldIncludeSize ? longSize : null
            ));
        }
        DownloadResponse result = new DownloadResponse(
            size,
            fileResponses
        );
        saveCache(pathname, result);
        return result;
    }

    @Override
    public void uploadApk(@NotNull MultipartFile file) throws IOException {
        // Create the folder if it doesn't exist
        File folder = new File(apkBasePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Save the file to the folder
        File saveFile = new File(folder, file.getOriginalFilename());
        Files.write(saveFile.toPath(), file.getBytes());
    }

    @Override
    public List<String> checkExistence(int version) throws IOException {
        DownloadResponse download = download(version);
        List<String> result = new ArrayList<>();
        download.getFiles().forEach(fileResponse -> {
            if (!Paths.get(versionsPath + fileResponse.getPath()).toFile().exists()) {
                result.add(fileResponse.path);
            }
        });
        return result;
    }

    @NotNull
    private static String getDownloadPathByVersion(int version) {
        return "D_" + version + ".json";
    }

    private Optional<Integer> getTheLastVersion() throws IOException {
        List<Integer> existedVersions = getAllExistedVersion();
        return existedVersions.isEmpty() ? Optional.empty() : Optional.ofNullable(existedVersions.get(existedVersions.size() - 1));
    }

    @NotNull
    private List<Integer> getAllExistedVersion() throws IOException {
        return Files.list(Paths.get(versionsPath))
            .sorted()
            .map(path -> path.toFile().getName())
            .filter(str -> str.matches("-?\\d+"))
            .map(Integer::valueOf)
            .collect(Collectors.toList());
    }
}


class CommandRunner {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Logger log = LoggerFactory.getLogger(CommandRunner.class);

    public CompletableFuture<Void> runCommands(List<CommandData> commands) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        executor.execute(() -> {
            for (CommandData commandModel : commands) {
                try {
                    String command = commandModel.getCommand();
                    log.info("executing command: {}", command);
                    CommandResult commandResult = executeCommand(command);
                    if (commandResult.getExitCode() == 0)
                        commandModel.getOnSuccess().accept(commandResult.getStdInput());
                    else commandModel.getOnFailure().accept(commandResult.getStdError());
                } catch (IOException | InterruptedException e) {
                    future.completeExceptionally(e);
                    log.error("error executing command: {} exception is", commandModel.getCommand(), e);
                    return;
                }
            }
            future.complete(null);
        });
        return future;
    }

    private CommandResult executeCommand(String command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command.split(" ")).start();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        StringBuilder stdInputResult = new StringBuilder();
        StringBuilder stdErrorResult = new StringBuilder();
        while ((line = stdInput.readLine()) != null) {
            stdInputResult.append(line).append("\n");
        }
        while ((line = stdError.readLine()) != null) {
            stdErrorResult.append(line).append("\n");
        }
        int exitCode = process.waitFor();
        return new CommandResult(exitCode, stdInputResult.toString(), stdErrorResult.toString());
    }

    public static class CommandResult {
        private final int exitCode;
        private final String stdInput;
        private final String stdError;

        public CommandResult(int exitCode, String stdInput, String stdError) {
            this.exitCode = exitCode;
            this.stdInput = stdInput;
            this.stdError = stdError;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getStdInput() {
            return stdInput;
        }

        public String getStdError() {
            return stdError;
        }
    }
}
