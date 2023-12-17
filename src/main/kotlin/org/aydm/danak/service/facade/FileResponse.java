package org.aydm.danak.service.facade;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FileResponse {
    String checksum;
    String path;
    Long size;

    public FileResponse() {
    }

    public FileResponse(String checksum, String path) {
        this.checksum = checksum;
        this.path = path;
    }

    public FileResponse(String checksum, String path, Long size) {
        this.checksum = checksum;
        this.path = path;
        this.size = size;
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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}

