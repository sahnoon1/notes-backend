package com.gammacode.noteproject.service;

import com.gammacode.noteproject.model.Note;
import com.gammacode.noteproject.model.User;
import com.gammacode.noteproject.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(User owner, String title, String content) {
        Note note = new Note();
        note.setOwner(owner);
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedAt(Instant.now());
        note.setUpdatedAt(Instant.now());
        return noteRepository.save(note);
    }

    public List<Note> getNotes(User owner) {
        return noteRepository.findByOwner(owner);
    }

    public Optional<Note> getNote(UUID id, User owner) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        return noteOpt.filter(note -> note.getOwner().equals(owner));
    }

    public Optional<Note> updateNote(UUID id, User owner, String title, String content, int version) {
        Optional<Note> noteOpt = getNote(id, owner);
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            if (note.getVersion() != version) return Optional.empty();
            note.setTitle(title);
            note.setContent(content);
            note.setVersion(note.getVersion() + 1);
            note.setUpdatedAt(Instant.now());
            return Optional.of(noteRepository.save(note));
        }
        return Optional.empty();
    }

    public boolean deleteNote(UUID id, User owner) {
        Optional<Note> noteOpt = getNote(id, owner);
        if (noteOpt.isPresent()) {
            noteRepository.delete(noteOpt.get());
            return true;
        }
        return false;
    }

    public Optional<Note> shareNote(UUID id, User owner, boolean isPublic, String slug) {
        Optional<Note> noteOpt = getNote(id, owner);
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            note.setIsPublic(isPublic);
            note.setPublicSlug(isPublic ? slug : null);
            note.setUpdatedAt(Instant.now());
            return Optional.of(noteRepository.save(note));
        }
        return Optional.empty();
    }

    public Optional<Note> getPublicNote(String slug) {
        return noteRepository.findByPublicSlug(slug).filter(Note::getIsPublic);
    }
}
