package org.aydm.danak.service.facade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class UpdateResponse {

    @JsonProperty("updateSize")
    Long updateSize;

    List<FileResponse> updates;

    @JsonProperty("deleteSize")
    Long deleteSize;

    List<FileResponse> deletes;

    public UpdateResponse() {}

    public UpdateResponse(Long updateSize, List<FileResponse> updates, Long deleteSize, List<FileResponse> deletes) {
        this.updateSize = updateSize;
        this.updates = updates;
        this.deleteSize = deleteSize;
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
