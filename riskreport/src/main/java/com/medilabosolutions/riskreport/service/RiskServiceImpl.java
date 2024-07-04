package com.medilabosolutions.riskreport.service;

import com.medilabosolutions.riskreport.beans.GenderEnum;
import com.medilabosolutions.riskreport.beans.NoteBean;
import com.medilabosolutions.riskreport.beans.PatientBean;
import com.medilabosolutions.riskreport.beans.RiskProfilEnum;
import com.medilabosolutions.riskreport.exceptions.ResourceNotFoundException;
import com.medilabosolutions.riskreport.proxies.NoteProxy;
import com.medilabosolutions.riskreport.proxies.PatientProxy;
import com.medilabosolutions.riskreport.utils.AgeCalculator;
import com.medilabosolutions.riskreport.utils.RiskCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RiskServiceImpl implements RiskService {

    private final PatientProxy patientProxy;

    private final NoteProxy noteProxy;


    public RiskServiceImpl(PatientProxy patientProxy, NoteProxy noteProxy) {
        this.patientProxy = patientProxy;
        this.noteProxy = noteProxy;
    }

    public String calculPatientRisk(Integer id) throws ResourceNotFoundException {
        log.info("Try to assess patient risk with id {}", id);

        List<NoteBean> patientNotes = this.noteProxy.findNotesByPatientId(id);
        PatientBean patient = patientProxy.getPatientById(id);

        int patientAge = AgeCalculator.calculPatientAge(patient.getDateOfBirth());
        log.info("The patient's age is {} years old", patientAge);
        GenderEnum patientGender = patient.getGender();
        log.info("The gender of the patient is : {}",patientGender.toString());
        int numbersOfRiskTerms = (int) RiskCalculator.calculateNumberOfRiskTerms(patientNotes);

        RiskProfilEnum riskProfile = RiskCalculator.defineRiskProfile(patientAge, patientGender);
        log.info("The patient profile is {}", riskProfile.toString());

        String riskLevel =  RiskCalculator.defineLevelRiskBasedOnProfile(riskProfile, numbersOfRiskTerms);
        log.info("The patient risk level is {}", riskLevel);
        return riskLevel;
    }


}
