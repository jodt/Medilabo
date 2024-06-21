package com.medilabosolutions.note.repository;

import com.medilabosolutions.note.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findNoteByPatientId(Integer id);

}
