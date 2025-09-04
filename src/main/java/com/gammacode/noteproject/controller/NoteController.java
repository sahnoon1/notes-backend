package com.gammacode.noteproject.controller;

import com.gammacode.noteproject.dto.NoteDto;
import com.gammacode.noteproject.model.Note;
import com.gammacode.noteproject.model.User;
import com.gammacode.noteproject.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;
    private final SecureRandom random = new SecureRandom();
    private static final String SLUG_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<?> createNote(@AuthenticationPrincipal User user, @RequestBody Map<String, String> body) {
        Note note = noteService.createNote(user, body.get("title"), body.get("content"));
        return ResponseEntity.ok(note);
    }

    @GetMapping
    public ResponseEntity<?> getNotes(@AuthenticationPrincipal User user) {
        List<Note> notes = noteService.getNotes(user);
        List<NoteDto> safeNotes = notes.stream()
            .map(NoteDto::fromEntity)
            .toList();
        return ResponseEntity.ok(safeNotes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNote(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        Optional<Note> noteOpt = noteService.getNote(id, user);
        if (noteOpt.isPresent()) {
            return ResponseEntity.ok(NoteDto.fromEntity(noteOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@AuthenticationPrincipal User user, @PathVariable UUID id, @RequestBody Map<String, Object> body) {
        String title = (String) body.get("title");
        String content = (String) body.get("content");
        int version = (int) body.get("version");
        Optional<Note> noteOpt = noteService.updateNote(id, user, title, content, version);
        if (noteOpt.isPresent()) {
            return ResponseEntity.ok(noteOpt.get());
        }
        return ResponseEntity.status(409).body(Map.of("error", "Version conflict"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        boolean deleted = noteService.deleteNote(id, user);
        if (deleted) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<?> shareNote(@AuthenticationPrincipal User user, @PathVariable UUID id, @RequestBody Map<String, Boolean> body) {
        boolean isPublic = body.getOrDefault("isPublic", false);
        String slug = isPublic ? generateSlug(6) : null;
        Optional<Note> noteOpt = noteService.getNote(id, user);
        if (noteOpt.isEmpty()) {
            // Note not found or user is not the owner
            System.out.println("[shareNote] User " + user.getId() + " tried to share/unshare note " + id + " but is not the owner or note not found.");
            return ResponseEntity.status(403).body(Map.of("error", "You are not allowed to share or unshare this note."));
        }
        Optional<Note> updatedNoteOpt = noteService.shareNote(id, user, isPublic, slug);
        if (updatedNoteOpt.isPresent()) {
            Note updatedNote = updatedNoteOpt.get();
            String publicSlug = updatedNote.getPublicSlug() != null ? updatedNote.getPublicSlug() : "";
            String shareUrl = (updatedNote.getIsPublic() && !publicSlug.isEmpty()) ? "https://notes-frontend-coral.vercel.app/public/" + publicSlug : "";
            return ResponseEntity.ok(Map.of(
                "publicSlug", publicSlug,
                "shareUrl", shareUrl
            ));
        }
        return ResponseEntity.status(500).body(Map.of("error", "Failed to update note sharing status."));
    }

    private String generateSlug(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(SLUG_CHARS.charAt(random.nextInt(SLUG_CHARS.length())));
        }
        return sb.toString();
    }
}
