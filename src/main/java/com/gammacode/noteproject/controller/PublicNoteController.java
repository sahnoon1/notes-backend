package com.gammacode.noteproject.controller;

import com.gammacode.noteproject.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/notes")
public class PublicNoteController {
    private final NoteService noteService;

    public PublicNoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getPublicNote(@PathVariable String slug) {
        return noteService.getPublicNote(slug)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
