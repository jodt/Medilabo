package com.medilabosolutions.note.service;

import com.medilabosolutions.note.model.Note;

import java.util.List;

public interface NoteService {

    List<Note> findAllNotesByPatientId(Integer id);

    Note addNote(Note note);
}
