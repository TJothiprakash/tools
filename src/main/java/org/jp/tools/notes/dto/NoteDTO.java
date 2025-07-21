package org.jp.tools.notes.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NoteDTO {
    private Long id;
    private String title;
    private String content;
}

