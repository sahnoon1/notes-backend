package com.gammacode.noteproject.dto;

import com.gammacode.noteproject.model.Note;
import com.gammacode.noteproject.model.User;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NoteDto {
    private UUID id;
    private OwnerDto owner;
    private String title;
    private String content;
    private boolean isPublic;
    private String publicSlug;
    private int version;
    private Instant createdAt;
    private Instant updatedAt;

    public static NoteDto fromEntity(Note note) {
        NoteDto dto = new NoteDto();
        dto.setId(note.getId());
        dto.setOwner(OwnerDto.fromEntity(note.getOwner()));
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setPublic(note.getIsPublic());
        dto.setPublicSlug(note.getPublicSlug());
        dto.setVersion(note.getVersion());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());
        return dto;
    }

    @Data
    public static class OwnerDto {
        private UUID id;
        private String email;
        public static OwnerDto fromEntity(User user) {
            OwnerDto dto = new OwnerDto();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            return dto;
        }
    }
}
