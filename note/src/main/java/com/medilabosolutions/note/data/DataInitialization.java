package com.medilabosolutions.note.data;

import com.medilabosolutions.note.model.Note;
import com.medilabosolutions.note.repository.NoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitialization implements CommandLineRunner {

    private final NoteRepository noteRepository;

    public DataInitialization(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<Note> notes = this.noteRepository.findAll();

        if (!notes.isEmpty()) {
            this.noteRepository.deleteAll();
        }

        Note note1 = Note.builder()
                .patientId(1)
                .content("Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé")
                .build();

        Note note2 = Note.builder()
                .patientId(2)
                .content("Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement")
                .build();

        Note note3 = Note.builder()
                .patientId(2)
                .content("Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale")
                .build();

        Note note4 = Note.builder()
                .patientId(3)
                .content("Le patient déclare qu'il fume depuis peu")
                .build();

        Note note5 = Note.builder()
                .patientId(3)
                .content("Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé")
                .build();

        Note note6 = Note.builder()
                .patientId(4)
                .content("Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments")
                .build();

        Note note7 = Note.builder()
                .patientId(4)
                .content("Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps")
                .build();

        Note note8 = Note.builder()
                .patientId(4)
                .content("Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé")
                .build();

        Note note9 = Note.builder()
                .patientId(4)
                .content("Taille, Poids, Cholestérol, Vertige et Réaction")
                .build();

        this.noteRepository.insert(List.of(note1,note2,note3,note4,note5,note6,note7,note8,note9));
    }

}
