package com.medilabosolutions.clientui.controller;

import com.medilabosolutions.clientui.beans.NoteBean;
import com.medilabosolutions.clientui.proxies.NoteProxy;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class NoteController {

    private final NoteProxy noteProxy;
    private static final String ERROR_NOTE_MESSAGE = "Note cannot be empty";

    public NoteController(NoteProxy noteProxy) {
        this.noteProxy = noteProxy;
    }

    @PostMapping("/note/add")
    public String  addPatientnote(@Valid @ModelAttribute(name = "newNote") NoteBean note, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.info("Errors in form validation on field {}", fieldError.getField());
            }
            redirectAttributes.addFlashAttribute("errorMessage",ERROR_NOTE_MESSAGE);
            return (("redirect:/patient/infos/" + note.getPatientId()));
        }
        this.noteProxy.addPatientNote(note);
        return ("redirect:/patient/infos/" + note.getPatientId());
    }

}
