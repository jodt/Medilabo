package com.medilabosolutions.note.controller;

import com.medilabosolutions.note.dto.NoteDto;
import com.medilabosolutions.note.model.Note;
import com.medilabosolutions.note.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findByPatientId/{id}")
    public List<NoteDto> findNotesByPatientId(@PathVariable Integer id) {
        log.info("GET /findByPatientId/{} called -> start process of finding notes for the patient with id {}", id, id);
        List<NoteDto> notes = this.noteService.findAllNotesByPatientId(id);
        log.info("{} note(s) found for the patient {}", notes.size(), id);
        return notes;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Note> addPatientNote(@RequestBody NoteDto note) {
        log.info("POST /add called -> start the process to add a note for the patient with id {}", note.getPatientId());
        Note noteAdded = this.noteService.addNote(note);
        log.info("Note added successfully");
        return new ResponseEntity<>(noteAdded, HttpStatus.CREATED);
    }

}
