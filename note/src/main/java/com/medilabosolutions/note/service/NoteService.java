package com.medilabosolutions.note.service;

import com.medilabosolutions.note.dto.NoteDto;
import com.medilabosolutions.note.model.Note;

import java.util.List;

public interface NoteService {

    List<NoteDto> findAllNotesByPatientId(Integer id);

    Note addNote(NoteDto note);
}
