package com.alex.reactivenotes.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    public Note(Note note) {
        id = note.id;
        title = note.title;
        this.note = note.note;
    }

    @Id
    private String id;

    @Size(min = 1, max = 50, message = "Title must not be empty nor exceed 50 characters.")
    @NotNull
    private String title;
    @Size(max = 1000, message = "Note must not exceed 1000 characters.")
    @NotNull
    private String note;

    private Date createdOn;
    private Date lastUpdatedOn;

    private String email;
}
