package com.alex.reactivenotes.routers;

import com.alex.reactivenotes.handlers.NoteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class NoteRouter {
    @Bean
    public RouterFunction<ServerResponse> route(NoteHandler noteHandler) {
        return RouterFunctions
                .route(GET("/notes").and(accept(MediaType.APPLICATION_JSON)),noteHandler::findAll)
                .andRoute(GET("/notes/{id}").and(accept(MediaType.APPLICATION_JSON)),noteHandler::findById)
                .andRoute(POST("/notes").and(accept(MediaType.APPLICATION_JSON)),noteHandler::saveNote)
                .andRoute(PUT("/notes").and(accept(MediaType.APPLICATION_JSON)),noteHandler::editNote);
    }
}
