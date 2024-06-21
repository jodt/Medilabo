package com.medilabosolutions.note.controller;

import com.medilabosolutions.note.model.Note;
import com.medilabosolutions.note.service.NoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/findByPatientId/{id}")
    public List<Note> findNotesByPatientId(@PathVariable Integer id){
        return this.noteService.findAllNotesByPatientId(id);
    }

    @PostMapping("/add")
    public Note addPatientNote(@RequestBody Note note) {
        return this.noteService.addNote(note);
    }

}
