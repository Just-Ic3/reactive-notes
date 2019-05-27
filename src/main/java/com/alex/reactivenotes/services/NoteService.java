package com.alex.reactivenotes.services;

import com.alex.reactivenotes.entities.Note;
import com.alex.reactivenotes.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public Flux<Note> findAll(String email) {
        return noteRepository.findByEmail(email);
    }

    public Mono<Note> findById(String id, String email) {
        return noteRepository.findById(id).map(note -> {
            if(!note.getEmail().equals(email)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"This note doesn't belong to you.");
            }
            return note;
        });
    }

    public Mono<Note> createNote(Note note) {
        note.setCreatedOn(new Date());
        note.setLastUpdatedOn(note.getCreatedOn());
        return noteRepository.save(note);
    }

    public Mono<Note> editNote(Note note, String email) {
        return noteRepository
                .findById(note.getId())
                .map(n -> {
                    if(!n.getEmail().equals(email)) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"This note doesn't belong to you.");
                    }
                    n.setTitle(note.getTitle());
                    n.setNote(note.getNote());
                    n.setLastUpdatedOn(new Date());
                    return n;
                })
                .flatMap(noteRepository::save);
    }
}
