package org.aydm.danak.service.facade;

import java.io.*;
import java.security.MessageDigest;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipException;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipDownload {
    private final Logger log = LoggerFactory.getLogger(ZipDownload.class);

    public void start(List<FileResponse> allFiles, String prefixPath, String zipDir, int version, String jsonFilePath, String password) {
        log.info("version{{}} - Zip file creation started", version);
        try (ZipFile zipFile = new ZipFile(zipDir)){
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            zipFile.setPassword(password.toCharArray());
            // Add the JSON file to the root folder of the zip file
            if (jsonFilePath != null && !jsonFilePath.isEmpty()) {
                File jsonFile = new File(jsonFilePath);
                if (jsonFile.exists()) {
                    zipFile.addFile(jsonFile, zipParameters);
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
                    zipFile.getFile().delete();
                    return;
                }

                // Add the file to the zip file
                zipParameters.setFileNameInZip(path);
                zipFile.addStream(new FileInputStream(new File(prefixPath, path)), zipParameters);
            }

            log.info("version{{}} - Zip file created: {}", version, zipFile.getFile().getAbsolutePath());
        } catch (IOException e) {
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
}

