package com.alex.reactivenotes.handlers;

import com.alex.reactivenotes.entities.Note;
import com.alex.reactivenotes.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Validator;
import java.security.Principal;
import java.util.function.Function;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class NoteHandler {
    private final NoteService noteService;
    private final Validator validator;

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.principal().map(Principal::getName).flatMap(user ->
                ok().contentType(MediaType.APPLICATION_JSON)
                        .body(noteService.findById(id,user), Note.class));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return request.principal().map(Principal::getName).flatMap(user ->
                ok().contentType(MediaType.APPLICATION_JSON)
                        .body(noteService.findAll(user),Note.class));
    }

    public Mono<ServerResponse> saveNote(ServerRequest request) {
        return validationBlock(block ->
                request.principal().map(Principal::getName).flatMap(user ->
                        ok().contentType(MediaType.APPLICATION_JSON)
                            .body(fromPublisher(block.flatMap(note -> {
                                note.setEmail(user);
                                return noteService.createNote(note); }),Note.class))),request);
    }

    public Mono<ServerResponse> editNote(ServerRequest request) {
        return validationBlock(block -> request.principal().map(Principal::getName).flatMap(user ->
                        ok().contentType(MediaType.APPLICATION_JSON)
                                .body(fromPublisher(block.flatMap(note ->
                                        noteService.editNote(note,user)),Note.class))),request);
    }

    public Mono<ServerResponse> validationBlock(Function<Mono<Note>,Mono<ServerResponse>> block, ServerRequest request) {
        return request
                .bodyToMono(Note.class)
                .flatMap(body -> validator.validate(body).isEmpty()
                        ? block.apply(Mono.just(body)) : ServerResponse.unprocessableEntity().build());
    }
}
