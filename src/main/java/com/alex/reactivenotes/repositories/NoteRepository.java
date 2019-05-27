package com.alex.reactivenotes.repositories;

import com.alex.reactivenotes.entities.Note;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NoteRepository extends ReactiveMongoRepository<Note,String> {
    Flux<Note> findByEmail(String email);
}
