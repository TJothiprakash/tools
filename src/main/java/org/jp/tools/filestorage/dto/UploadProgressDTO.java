package org.jp.tools.filestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadProgressDTO {
    private String fileId;
    private int uploadedChunks;
    private int totalChunks;
    private String status; // e.g., "IN_PROGRESS", "COMPLETED", "FAILED"
}
