package com.medilabosolutions.note.service;

import com.medilabosolutions.note.model.Note;
import com.medilabosolutions.note.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> findAllNotesByPatientId(Integer id) {
        return this.noteRepository.findNoteByPatientId(id);
    }

    @Override
    public Note addNote(Note note) {
       return this.noteRepository.insert(note);
    }
}
