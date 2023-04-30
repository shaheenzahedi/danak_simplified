package org.aydm.danak.service.facade;

public class FileResponse {
    String checksum;
    String path;

    public FileResponse() {
    }

    public FileResponse(String checksum, String path) {
        this.checksum = checksum;
        this.path = path;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

