package org.jp.tools.notes.repository;

import org.jp.tools.notes.model.Note;
import org.jp.tools.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUser(User user);
    Optional<Note> findByIdAndUser(Long id, User user);
}
