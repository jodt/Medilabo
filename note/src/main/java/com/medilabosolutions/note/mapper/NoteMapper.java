package com.medilabosolutions.note.mapper;

import com.medilabosolutions.note.dto.NoteDto;
import com.medilabosolutions.note.model.Note;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIgnoreFields = "com.medilabosolutions.note.model.Note.id")
public interface NoteMapper {

    NoteDto asNoteDto(Note note);

    Note asNote(NoteDto noteDto);

}
