package org.aydm.danak.service.facade;


import org.aydm.danak.service.FileBelongingsService;
import org.aydm.danak.service.FileService;
import org.aydm.danak.service.VersionService;
import org.aydm.danak.service.dto.FileBelongingsDTO;
import org.aydm.danak.service.dto.FileDTO;
import org.aydm.danak.service.dto.VersionDTO;
import org.aydm.danak.web.rest.errors.BadRequestAlertException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public interface AssetFacade {

    void versionAsset(int version) throws IOException;

    UpdateResonse updateAssets(int fromVersion, int toVersion) throws IOException;


    List<FileAddress> download(int version) throws IOException;
}

@Service
class AssetFacadeImpl implements AssetFacade {
    FileService fileService;
    FileBelongingsService fileBelongingsService;
    VersionService versionService;

    public AssetFacadeImpl(FileService fileService, FileBelongingsService fileBelongingsService, VersionService versionService) {
        this.fileService = fileService;
        this.fileBelongingsService = fileBelongingsService;
        this.versionService = versionService;
    }

    private final Logger log = LoggerFactory.getLogger(AssetFacadeImpl.class);

    @Value("${asset.source-path}")
    private String sourcePath;
    @Value("${asset.diffs-path}")
    private String diffsPath;
    @Value("${asset.scripts.cleanup}")
    private String cleanupScript;
    @Value("${asset.scripts.copy}")
    private String copyScript;
    @Value("${asset.scripts.generate-csv}")
    private String generateCSVScript;
    @Value("${asset.scripts.generate-diff}")
    private String generateDiffScript;
    @Value("${asset.versions-path}")
    private String versionsPath;
    @Value("${asset.lists-path}")
    private String listsPath;

    @Override
    public void versionAsset(int version) throws IOException {
        final VersionDTO versionDTO = versionService.save(new VersionDTO(
            null, version
        ));
        String newVersionPath = versionsPath + version;
        String newVersionCSVPath = listsPath + version;
        List<CommandData> commands = new ArrayList<>();
        commands.add(
            new CommandData(
                copyScript + ' ' + newVersionPath + ' ' + sourcePath,
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
                if (oldVersion.isPresent()) {
                    try {
                        final List<FileDTO> allFiles = fileService.findAllLastVersion(oldVersion.orElseThrow()).stream()
                            .peek(fileDTO -> fileDTO.setPlacement(versionDTO)).collect(Collectors.toList());
                        final String diffAddress = diffsPath + oldVersion.orElseThrow() + '_' + version + "_diff.txt";
                        File file = new File(diffAddress);
                        if (!file.exists()) {
                            log.info("version{{}} - there is no diff so we just update the database", version);
                            fileService.saveAll(allFiles);
                            return;
                        }
                        final BufferedReader diffBR = new BufferedReader(new FileReader(file, Charset.defaultCharset()));
                        final String header = diffBR.readLine();
                        String line;
                        final List<FileAddress> addresses = new ArrayList<>();
                        while ((line = diffBR.readLine()) != null) {
                            if (line.startsWith("<")) {
                                addresses.add(FileAddress.Companion.fromDiffLine(0, line));
                            } else if (line.startsWith(">")) {
                                FileAddress newAddress = FileAddress.Companion.fromDiffLine(0, line);
                                allFiles.add(new FileDTO(
                                    null, newAddress.getName(), newAddress.getChecksum(), newAddress.getPath(), versionDTO
                                ));
                            }
                        }
                        final List<FileDTO> notUpdatedFiles = new ArrayList<>();
                        for (FileDTO fileDTO : allFiles) {
                            for (FileAddress fileAddress : addresses) {
                                if (Objects.equals(fileDTO.getChecksum(), fileAddress.getChecksum()) &&
                                    Objects.equals(fileDTO.getName(), fileAddress.getName()) &&
                                    Objects.equals(fileDTO.getPath(), fileAddress.getPath())) {
                                    notUpdatedFiles.add(fileDTO);
                                }
                            }
                        }
                        allFiles.removeAll(notUpdatedFiles);
                        fileService.saveAll(allFiles);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    initializeSaveDB(versionDTO, newVersionCSVPath);
                }
                log.info("version{{}} - All commands finished!", version);
            });
    }

    private void initializeSaveDB(VersionDTO version, String csvPath) {
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
                    version
                ));
            }
            fileBelongingsService.saveAll(fileService.saveAll(files).stream().map(fileDTO -> new FileBelongingsDTO(
                null, fileDTO, version
            )).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public UpdateResonse updateAssets(int fromVersion, int toVersion) throws IOException {
        Optional<Integer> lastVersion = getTheLastVersion();
        if (lastVersion.isEmpty())
            throw new BadRequestAlertException("update is pointless when there is no asset on the server", "asset", "");
        if (toVersion == lastVersion.orElseThrow()) {
            return finalizeFiles(fromVersion, toVersion);
        }
        return null;
    }

    @Override
    public List<FileAddress> download(int version) throws IOException {
        Optional<Integer> lastVersion = getTheLastVersion();
        if (lastVersion.isEmpty())
            throw new BadRequestAlertException("update is pointless when there is no asset on the server", "asset", "");
        String lastFilePath = listsPath + lastVersion.orElseThrow() + File.separator + "files.csv";
        BufferedReader lastBufferedReader = new BufferedReader(new FileReader(lastFilePath, Charset.defaultCharset()));
        String lastHeader = lastBufferedReader.readLine();
        if (version == lastVersion.orElseThrow()) {
            List<FileAddress> result = new ArrayList<>();
            String line;
            while ((line = lastBufferedReader.readLine()) != null) {
                result.add(FileAddress.Companion.fromCSVLine(version, line));
            }
            return result;
        }
        String diffPath = diffsPath + version + '_' + lastVersion.orElseThrow() + "_diff.txt";
        BufferedReader diffBufferedReader = new BufferedReader(new FileReader(diffPath, Charset.defaultCharset()));
        String lineDiff;
        List<FileAddress> onlyInVersion = new ArrayList<>();
        while ((lineDiff = diffBufferedReader.readLine()) != null) {
            if (lineDiff.startsWith(">")) {
                onlyInVersion.add(FileAddress.Companion.fromDiffLine(version, lineDiff));
            }
        }
        String versionFilePath = listsPath + version + File.separator + "files.csv";
        BufferedReader versionBufferedReader = new BufferedReader(new FileReader(versionFilePath, Charset.defaultCharset()));
        String versionHeader = versionBufferedReader.readLine();
        List<FileAddress> result = new ArrayList<>();
        String versionLine;
        while ((versionLine = versionBufferedReader.readLine()) != null) {
            FileAddress fileAddress = FileAddress.Companion.fromCSVLine(lastVersion.orElseThrow(), versionLine);
            for (FileAddress address : onlyInVersion) {
                if (address.equals(fileAddress)) {
                    fileAddress.setVersion(version);
                }
            }
        }
        return result;
    }

    private UpdateResonse finalizeFiles(int fromVersion, int lastVersion) throws IOException {
        String fromFilePath = listsPath + fromVersion + File.separator + "files.csv";
        String lastFilePath = listsPath + lastVersion + File.separator + "files.csv";
        String diffPath = diffsPath + fromVersion + '_' + lastVersion + "_diff.txt";
        BufferedReader br1 = new BufferedReader(new FileReader(lastFilePath, Charset.defaultCharset()));
        BufferedReader br2 = new BufferedReader(new FileReader(fromFilePath, Charset.defaultCharset()));
        BufferedReader brDiff = new BufferedReader(new FileReader(diffPath, Charset.defaultCharset()));

        // Skip the header line in lastFilePath and fromFilePath
        String header1 = br1.readLine();
        String header2 = br2.readLine();
        List<FileAddress> updates = new ArrayList<>();
        List<FileAddress> deletes = new ArrayList<>();
        // Read the diff file and merge changes from fromFilePath into lastFilePath
        String lineDiff;
        while ((lineDiff = brDiff.readLine()) != null) {
            if (lineDiff.startsWith(">")) {
                // This line exists only in fromFilePath, so return it with a prefix indicating the source file
                String line = br2.readLine();
                deletes.add(FileAddress.Companion.fromCSVLine(fromVersion, line));
            } else if (lineDiff.startsWith("<")) {
                // This line exists only in lastFilePath, so skip it
                String line = br1.readLine();
                updates.add(FileAddress.Companion.fromCSVLine(fromVersion, line));
            } else {
                // This line exists in both files, so skip it in fromFilePath
                br2.readLine();
            }
        }
        return new UpdateResonse(
            updates,
            deletes
        );
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
                    log.error("error executing command: {} exception is: {}", commandModel.getCommand(), e);
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
