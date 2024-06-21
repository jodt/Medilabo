package com.medilabosolutions.note.service;

import com.medilabosolutions.note.model.Note;
import com.medilabosolutions.note.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> findAllNotesByPatientId(Integer id) {
        log.info("Try to find notes for the patient with id {}", id);
        return this.noteRepository.findNoteByPatientId(id);
    }

    @Override
    public Note addNote(Note note) {
        log.info("Try to add note for the patient with id {}", note.getPatientId());
        return this.noteRepository.insert(note);
    }
}
