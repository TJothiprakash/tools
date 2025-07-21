package org.jp.tools.notes.controller;

import lombok.RequiredArgsConstructor;
import org.jp.tools.notes.dto.NoteDTO;
import org.jp.tools.notes.dto.NoteResponse;
import org.jp.tools.notes.model.Note;
import org.jp.tools.notes.service.NoteService;
import org.jp.tools.security.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public NoteResponse createNote(@AuthenticationPrincipal User user, @RequestBody NoteDTO dto) {
        Note note = noteService.createNote(user, dto);
        return toNoteResponse(note);
    }

    @GetMapping
    public List<Note> getNotes(@AuthenticationPrincipal User user) {
        return noteService.getUserNotes(user);
    }

    @GetMapping("/{id}")
    public NoteResponse getNoteById(@AuthenticationPrincipal User user,
                                    @PathVariable Long id) {
        Note note = noteService.getNoteById(user, id);
        return toNoteResponse(note);
    }

    @PutMapping("/{id}")
    public NoteResponse updateNote(@AuthenticationPrincipal User user,
                                   @PathVariable Long id,
                                   @RequestBody NoteDTO dto) {
        Note updatedNote = noteService.updateNote(user, id, dto);
        return toNoteResponse(updatedNote);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@AuthenticationPrincipal User user, @PathVariable Long id) {
        noteService.deleteNote(user, id);
    }

    private NoteResponse toNoteResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());
        return response;
    }
}
