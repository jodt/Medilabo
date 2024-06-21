package com.medilabosolutions.note.service;

import com.medilabosolutions.note.dto.NoteDto;
import com.medilabosolutions.note.model.Note;
import com.medilabosolutions.note.repository.NoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;


    @Test
    @DisplayName("Should find all notes for one patient")
    void findAllNotesByPatientId() {

        Note noteOne = Note.builder()
                .id("1")
                .content("First Note")
                .patientId(1)
                .build();

        Note noteTwo = Note.builder()
                .id("2")
                .content("Second Note")
                .patientId(1)
                .build();

        NoteDto noteDtoOne = NoteDto.builder()
                .content("First Note")
                .patientId(1)
                .build();

        NoteDto noteDtoTwo = NoteDto.builder()
                .content("Second Note")
                .patientId(1)
                .build();

        List<Note> patientNotes = List.of(noteOne,noteTwo);

        when(this.noteRepository.findNoteByPatientId(1)).thenReturn(patientNotes);

        List<NoteDto> result = this.noteService.findAllNotesByPatientId(1);

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(noteDtoOne, result.get(0));
        assertEquals(noteDtoTwo, result.get(1));

        verify(this.noteRepository).findNoteByPatientId(1);
    }

    @Test
    @DisplayName("Should add patient note")
    void addNote() {

        Note newNote = Note.builder()
                .content("New Note")
                .patientId(1)
                .build();

        NoteDto newNoteDto= NoteDto.builder()
                .content("New Note")
                .patientId(1)
                .build();

        when(this.noteRepository.insert(newNote)).thenReturn(newNote);

        Note result = this.noteService.addNote(newNoteDto);

        assertNotNull(result);
        assertEquals(newNote, result);

        verify(this.noteRepository).insert(newNote);
    }
}