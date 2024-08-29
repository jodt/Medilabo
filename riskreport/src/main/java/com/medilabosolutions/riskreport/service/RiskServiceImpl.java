package com.medilabosolutions.riskreport.service;

import com.medilabosolutions.riskreport.beans.GenderEnum;
import com.medilabosolutions.riskreport.beans.NoteBean;
import com.medilabosolutions.riskreport.beans.PatientAgeGenderBean;
import com.medilabosolutions.riskreport.beans.RiskProfilEnum;
import com.medilabosolutions.riskreport.exceptions.ResourceNotFoundException;
import com.medilabosolutions.riskreport.proxies.NoteProxy;
import com.medilabosolutions.riskreport.proxies.PatientProxy;
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

    /**
     * Retrieves the level of risk for the patient to develop diabetes
     * @param id unique patient identifier
     * @return the level of risk
     * @throws ResourceNotFoundException
     */
    public String calculPatientRisk(Integer id) throws ResourceNotFoundException {
        log.info("Try to assess patient risk with id {}", id);

        List<NoteBean> patientNotes = this.noteProxy.findNotesByPatientId(id);
        PatientAgeGenderBean patient = patientProxy.getPatientAgeGender(id);

        log.info("The patient's age is {} years old", patient.getAge());
        GenderEnum patientGender = patient.getGender();
        log.info("The gender of the patient is : {}",patientGender.toString());
        int numbersOfRiskTerms = (int) RiskCalculator.calculateNumberOfRiskTerms(patientNotes);

        RiskProfilEnum riskProfile = RiskCalculator.defineRiskProfile(patient.getAge(), patientGender);
        log.info("The patient profile is {}", riskProfile.toString());

        String riskLevel =  RiskCalculator.defineLevelRiskBasedOnProfile(riskProfile, numbersOfRiskTerms);
        log.info("The patient risk level is {}", riskLevel);
        return riskLevel;
    }


}
