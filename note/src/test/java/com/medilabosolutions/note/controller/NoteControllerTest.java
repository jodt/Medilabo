package com.medilabosolutions.note.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.note.dto.NoteDto;
import com.medilabosolutions.note.model.Note;
import com.medilabosolutions.note.service.NoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    private final static String URL_PREFIX = "/v1/api/notes";

    @Test
    @DisplayName("Should get notes by patient id")
    void shouldFindNotesByPatientId() throws Exception {

        NoteDto noteDtoOne = NoteDto.builder()
                .content("First Note")
                .patientId(1)
                .build();

        NoteDto noteDtoTwo = NoteDto.builder()
                .content("Second Note")
                .patientId(1)
                .build();

        List<NoteDto> patientNotes = List.of(noteDtoOne, noteDtoTwo);

        when(this.noteService.findAllNotesByPatientId(1)).thenReturn(patientNotes);
        mockMvc.perform(get(URL_PREFIX + "/findByPatientId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].patientId").value(1))
                .andExpect(jsonPath("$[0].content").value("First Note"))
                .andExpect(jsonPath("$[1].patientId").value(1))
                .andExpect(jsonPath("$[1].content").value("Second Note"))
                .andDo(print());

        verify(this.noteService).findAllNotesByPatientId(1);
    }

    @Test
    @DisplayName("should add a new note to a patient")
    void addPatientNote() throws Exception {

        NoteDto newNoteDto = NoteDto.builder()
                .content("New note")
                .patientId(1)
                .build();

        Note newNote = Note.builder()
                .id("id")
                .content("New note")
                .patientId(1)
                .build();

        when(this.noteService.addNote(newNoteDto)).thenReturn(newNote);

        mockMvc.perform(post(URL_PREFIX + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNoteDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("id"))
                .andExpect(jsonPath("$.patientId").value("1"))
                .andExpect(jsonPath("$.content").value("New note"))
                .andDo(print());

        verify(this.noteService).addNote(newNoteDto);
    }
}