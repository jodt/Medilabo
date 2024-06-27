package com.medilabosolutions.clientui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.clientui.beans.NoteBean;
import com.medilabosolutions.clientui.proxies.NoteProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NoteController.class)
class NoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NoteProxy noteProxy;

    private static final String ERROR_NOTE_MESSAGE = "Note cannot be empty";

    @Test
    @DisplayName("Should add patient note")
    void shouldAddPatientNote() throws Exception {

        NoteBean newNote = NoteBean.builder()
                .patientId(1)
                .content("new patient note")
                .build();

        when(this.noteProxy.addPatientNote(newNote)).thenReturn(new ResponseEntity<NoteBean>(newNote, HttpStatus.CREATED));

        this.mockMvc.perform(post("/note/add")
                .flashAttr("newNote", newNote))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/infos/1"));
    }

    @Test
    @DisplayName("Should not add patient note -> field error")
    void shouldNotAddPatientNote() throws Exception {

        NoteBean newNote = NoteBean.builder()
                .patientId(1)
                .build();

        this.mockMvc.perform(post("/note/add")
                .flashAttr("newNote", newNote))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/infos/1"))
                .andExpect(flash().attribute("errorMessage",ERROR_NOTE_MESSAGE));
    }
}