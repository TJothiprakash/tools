package org.jp.tools.notes.dto;

import lombok.Data;

@Data
public class NoteRequest {
    private String title;
    private String content;
}
