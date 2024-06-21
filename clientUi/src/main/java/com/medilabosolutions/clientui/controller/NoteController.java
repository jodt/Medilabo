package com.medilabosolutions.clientui.controller;

import com.medilabosolutions.clientui.beans.NoteBean;
import com.medilabosolutions.clientui.proxies.NoteProxy;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {

    private final NoteProxy noteProxy;

    public NoteController(NoteProxy noteProxy) {
        this.noteProxy = noteProxy;
    }

    @PostMapping("/note/add")
    public String  addPatientnote(@Valid @ModelAttribute(name = "newNote") NoteBean note) {
        System.out.println("Note Patient ID: " + note.getPatientId());
        System.out.println("Note Content: " + note.getContent());
        this.noteProxy.addPatientNote(note);
        return ("redirect:/patient/infos/" + note.getPatientId());
    }

}
