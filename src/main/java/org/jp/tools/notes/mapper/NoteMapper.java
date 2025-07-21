package org.jp.tools.notes.mapper;

import org.jp.tools.notes.dto.NoteDTO;
import org.jp.tools.notes.model.Note;

public class NoteMapper {

    public static NoteDTO toDto(Note note) {
        return NoteDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .build();
    }

    public static Note toEntity(NoteDTO dto) {
        return Note.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }
}
