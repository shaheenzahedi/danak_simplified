package org.aydm.danak.service.facade;

import java.io.*;
import java.security.MessageDigest;
import java.util.List;
import java.util.Objects;
import java.util.zip.*;

import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipDownload {
    private final Logger log = LoggerFactory.getLogger(ZipDownload.class);

    public void start(List<FileResponse> allFiles, String prefixPath, String zipDir, int version, String jsonFilePath, String password) {
        try {
            // Create the zip file
            File zipFile = new File(zipDir);
            zipFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            // Set password for the zip file
            if (password != null && !password.isEmpty()) {
                zos.setMethod(ZipOutputStream.DEFLATED);
                zos.setComment(password);
            }

            // Add the JSON file to the root folder of the zip file
            if (jsonFilePath != null && !jsonFilePath.isEmpty()) {
                File jsonFile = new File(jsonFilePath);
                if (jsonFile.exists()) {
                    ZipEntry jsonEntry = new ZipEntry(jsonFile.getName());
                    zos.putNextEntry(jsonEntry);
                    FileInputStream jsonFis = new FileInputStream(jsonFile);
                    byte[] jsonBuffer = new byte[1024];
                    int jsonLen;
                    while ((jsonLen = jsonFis.read(jsonBuffer)) > 0) {
                        zos.write(jsonBuffer, 0, jsonLen);
                    }
                    jsonFis.close();
                    zos.closeEntry();
                } else {
                    log.warn("version{{}} - JSON file does not exist: {}", version, jsonFilePath);
                }
            }

            // Add each file to the zip file
            for (FileResponse fileDTO : allFiles) {
                String path = fileDTO.getPath();
                String checksum = fileDTO.getChecksum();

                // Verify the checksum of the file
                if (!verifyChecksum(prefixPath + "/" + path, checksum)) {
                    log.error("version{{}} - Checksum verification failed for file: " + prefixPath + "/" + path, version);
                    zos.close();
                    zipFile.delete();
                    return;
                }

                // Add the file to the zip file
                ZipEntry zipEntry = new ZipEntry(path);
                zos.putNextEntry(zipEntry);
                FileInputStream fis = new FileInputStream(new File(prefixPath, path));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                fis.close();
                zos.closeEntry();
            }

            // Close the zip file
            zos.close();

            log.info("version{{}} - Zip file created: {}", version, zipFile.getAbsolutePath());
        } catch (IOException | JSONException e) {
            log.error("version{{}} - Error: ", version, e);
        }
    }
    // Verify the checksum of a file
    private static boolean verifyChecksum(String filepath, String checksum) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(filepath);
            byte[] dataBytes = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, bytesRead);
            }
            byte[] mdBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte mdByte : mdBytes) {
                sb.append(Integer.toString((mdByte & 0xff) + 0x100, 16).substring(1));
            }
            fis.close();
            return sb.toString().equals(checksum);
        } catch (Exception e) {
            return false;
        }
    }

    // Zip a directory and its contents
    private static void zipDirectory(File sourceDir, File zipFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zipSubDirectory("", sourceDir, zos);
        zos.close();
        fos.close();
    }

    // Zip a subdirectory and its contents
    private static void zipSubDirectory(String basePath, File dir, ZipOutputStream zos) throws IOException {
        File[] files = dir.listFiles();
        byte[] buffer = new byte[1024];
        int bytesRead;
        for (File file : Objects.requireNonNull(files)) {
            if (file.isDirectory()) {
                String path = basePath + file.getName() + "/";
                zos.putNextEntry(new ZipEntry(path));
                zipSubDirectory(path, file, zos);
                zos.closeEntry();
            } else {
                FileInputStream fis = new FileInputStream(file);
                String path = basePath + file.getName();
                zos.putNextEntry(new ZipEntry(path));
                while ((bytesRead = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, bytesRead);
                }
                fis.close();
                zos.closeEntry();
            }
        }
    }

    // Delete a directory and its contents
    private static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
}

