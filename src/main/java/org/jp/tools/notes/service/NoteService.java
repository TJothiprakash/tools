package org.jp.tools.notes.service;

import lombok.RequiredArgsConstructor;
import org.jp.tools.notes.model.Note;
import org.jp.tools.notes.dto.NoteDTO;
import org.jp.tools.notes.repository.NoteRepository;
import org.jp.tools.security.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public Note createNote(User user, NoteDTO dto) {
        Note note = Note.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();
        return noteRepository.save(note);
    }

    public List<Note> getUserNotes(User user) {
        return noteRepository.findAllByUser(user);
    }

    public Note getNoteById(User user, Long noteId) {
        return noteRepository.findByIdAndUser(noteId, user)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    public Note updateNote(User user, Long noteId, NoteDTO dto) {
        Note note = getNoteById(user, noteId);
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        return noteRepository.save(note);
    }

    public void deleteNote(User user, Long noteId) {
        Note note = getNoteById(user, noteId);
        noteRepository.delete(note);
    }
}
