package com.medilabosolutions.riskreport.service;

import com.medilabosolutions.riskreport.beans.GenderEnum;
import com.medilabosolutions.riskreport.beans.NoteBean;
import com.medilabosolutions.riskreport.beans.PatientAgeGenderBean;
import com.medilabosolutions.riskreport.configuration.RiskTermsProperties;
import com.medilabosolutions.riskreport.exceptions.ResourceNotFoundException;
import com.medilabosolutions.riskreport.proxies.NoteProxy;
import com.medilabosolutions.riskreport.proxies.PatientProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiskServiceImplTest {

    @Mock
    PatientProxy patientProxy;

    @Mock
    NoteProxy noteProxy;

    @Mock
    RiskTermsProperties riskTermsProperties;
    @InjectMocks
    RiskServiceImpl riskService;

    private final static List<String> RISK_TERMS = List.of("Hémoglobine A1C","Microalbumine","Taille", "Poids", "Fumeur", "Fumeuse", "Anormal", "Cholestérol", "Vertiges", "Rechute", "Réaction", "Anticorps");

    @Test
    @DisplayName("Should calculate the risk for a man under thirty -> None")
    void ShouldCalculatePatientRiskForManUnderThirtyWithRiskNone() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé")
                .build();

        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(25)
                .gender(GenderEnum.M)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));
        when(this.riskTermsProperties.getTerms()).thenReturn(RISK_TERMS);

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("None", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);
        verify(this.riskTermsProperties).getTerms();
    }

    @Test
    @DisplayName("Should calculate the risk for a man under thirty -> InDanger")
    void ShouldCalculatePatientRiskForManUnderThirtyWithRiskInDanger() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("Le patient, fumeur, déclare qu'il a des vertiges. Poids supérieur au poids recommandé")
                .build();


        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(25)
                .gender(GenderEnum.M)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));
        when(this.riskTermsProperties.getTerms()).thenReturn(RISK_TERMS);

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("InDanger", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);
        verify(this.riskTermsProperties).getTerms();
    }

    @Test
    @DisplayName("Should calculate the risk for a man under thirty -> EarlyOnset")
    void ShouldCalculatePatientRiskForManUnderThirtyWithRiskEarlyOnSet() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("Le patient, fumeur, déclare qu'il a des vertiges. Poids supérieur au poids recommandé. Cholestérol anormal")
                .build();


        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(25)
                .gender(GenderEnum.M)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));
        when(this.riskTermsProperties.getTerms()).thenReturn(RISK_TERMS);

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("EarlyOnset", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);
        verify(this.riskTermsProperties).getTerms();
    }

    @Test
    @DisplayName("Should calculate the risk for a woman under thirty -> None")
    void ShouldCalculatePatientRiskForWomanUnderThirtyWithRiskNone() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("La patiente déclare qu'elle 'se sent très bien' Poids égal ou inférieur au poids recommandé")
                .build();

        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(25)
                .gender(GenderEnum.F)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));
        when(this.riskTermsProperties.getTerms()).thenReturn(RISK_TERMS);

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("None", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);
        verify(this.riskTermsProperties).getTerms();
    }

    @Test
    @DisplayName("Should calculate the risk for a woman under thirty -> InDanger")
    void ShouldCalculatePatientRiskForWomanUnderThirtyWithRiskInDanger() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("La patiente, fumeuse, déclare qu'elle a des vertiges et réaction allergique aux medicaments. Poids supérieur au poids recommandé")
                .build();


        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(25)
                .gender(GenderEnum.F)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));
        when(this.riskTermsProperties.getTerms()).thenReturn(RISK_TERMS);

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("InDanger", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);
        verify(this.riskTermsProperties).getTerms();
    }

    @Test
    @DisplayName("Should calculate the risk for a woman under thirty -> EarlyOnset")
    void ShouldCalculatePatientRiskForWomanUnderThirtyWithRiskEarlyOnset() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("La patiente, fumeuse, déclare qu'elle a des vertiges et réaction allergique aux medicaments. Poids supérieur au poids recommandé. Cholestérol anormal. Rechute")
                .build();


        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(25)
                .gender(GenderEnum.F)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));
        when(this.riskTermsProperties.getTerms()).thenReturn(RISK_TERMS);

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("EarlyOnset", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);
        verify(this.riskTermsProperties).getTerms();
    }

    @Test
    @DisplayName("Should calculate the risk for a man over thirty -> None")
    void ShouldCalculatePatientRiskForManOverThirtyWithRiskNone() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé")
                .build();

        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(60)
                .gender(GenderEnum.M)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("None", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);

    }

    @Test
    @DisplayName("Should calculate the risk for a man over thirty -> Borderline")
    void ShouldCalculatePatientRiskForManOverThirtyWithRiskBorderline() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("Poids supérieur au poids recommandé. Cholestérol anormal")
                .build();

        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(60)
                .gender(GenderEnum.M)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));
        when(this.riskTermsProperties.getTerms()).thenReturn(RISK_TERMS);

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("Borderline", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);
        verify(this.riskTermsProperties).getTerms();
    }

    @Test
    @DisplayName("Should calculate the risk for a man over thirty -> InDanger")
    void ShouldCalculatePatientRiskForManOverThirtyWithRiskInDanger() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("Poids supérieur au poids recommandé. Fumeur. Cholestérol anormal. Vertiges. Rechute.")
                .build();

        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(60)
                .gender(GenderEnum.M)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));
        when(this.riskTermsProperties.getTerms()).thenReturn(RISK_TERMS);

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("InDanger", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);
        verify(this.riskTermsProperties).getTerms();
    }

    @Test
    @DisplayName("Should calculate the risk for a man over thirty -> EarlyOnset")
    void ShouldCalculatePatientRiskForManOverThirtyWithRiskEarlyOnset() throws ResourceNotFoundException {

        NoteBean note = NoteBean.builder()
                .patientId(1)
                .content("Poids supérieur au poids recommandé. Fumeur. Cholestérol anormal. Vertiges. Rechute. Réaction allergique. Anticorps bas")
                .build();

        PatientAgeGenderBean patient = PatientAgeGenderBean.builder()
                .age(60)
                .gender(GenderEnum.M)
                .build();

        when(this.patientProxy.getPatientAgeGender(1)).thenReturn(patient);
        when(this.noteProxy.findNotesByPatientId(1)).thenReturn(List.of(note));
        when(this.riskTermsProperties.getTerms()).thenReturn(RISK_TERMS);

        String result = riskService.calculPatientRisk(1);

        assertNotNull(result);
        assertEquals("EarlyOnset", result);

        verify(this.patientProxy).getPatientAgeGender(1);
        verify(this.noteProxy).findNotesByPatientId(1);
        verify(this.riskTermsProperties).getTerms();
    }

}