package org.aydm.danak.service.facade;

import java.util.List;

public class UpdateResponse {
    List<FileResponse> updates;
    List<FileResponse> deletes;

    public UpdateResponse() {
    }

    public UpdateResponse(List<FileResponse> updates, List<FileResponse> deletes) {
        this.updates = updates;
        this.deletes = deletes;
    }

    public List<FileResponse> getUpdates() {
        return updates;
    }

    public void setUpdates(List<FileResponse> updates) {
        this.updates = updates;
    }

    public List<FileResponse> getDeletes() {
        return deletes;
    }

    public void setDeletes(List<FileResponse> deletes) {
        this.deletes = deletes;
    }
}
