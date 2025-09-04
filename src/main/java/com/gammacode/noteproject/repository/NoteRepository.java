package com.gammacode.noteproject.repository;

import com.gammacode.noteproject.model.Note;
import com.gammacode.noteproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {
    List<Note> findByOwner(User owner);
    Optional<Note> findByPublicSlug(String publicSlug);
}
