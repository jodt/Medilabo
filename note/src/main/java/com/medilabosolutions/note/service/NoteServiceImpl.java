package com.medilabosolutions.note.service;

import com.medilabosolutions.note.dto.NoteDto;
import com.medilabosolutions.note.mapper.NoteMapper;
import com.medilabosolutions.note.model.Note;
import com.medilabosolutions.note.repository.NoteRepository;
import fr.xebia.extras.selma.Selma;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper = Selma.builder(NoteMapper.class).build();

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<NoteDto> findAllNotesByPatientId(Integer id) {
        log.info("Try to find notes for the patient with id {}", id);
        List<NoteDto> patientNotes = this.noteRepository.findNoteByPatientId(id).stream().map(noteMapper::asNoteDto).collect(Collectors.toList());
        return patientNotes;
    }

    @Override
    public Note addNote(NoteDto noteDto) {
        log.info("Try to add note for the patient with id {}", noteDto.getPatientId());
        Note noteToAdd = noteMapper.asNote(noteDto);
        return this.noteRepository.insert(noteToAdd);
    }
}
