package com.medilabosolutions.riskreport.utils;

import com.medilabosolutions.riskreport.enums.GenderEnum;
import com.medilabosolutions.riskreport.beans.NoteBean;
import com.medilabosolutions.riskreport.enums.RiskProfilEnum;
import com.medilabosolutions.riskreport.enums.RiskEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility class that provides methods for calculating
 * the patient's risk of developing diabetes
 */
@Component
public class RiskCalculator {

    /**
     * Returns the number of trigger terms found in notes to calculate risk
     * @param patientNotes all patient notes
     * @return the number of trigger terms found
     */
    public static long calculateNumberOfRiskTerms(List<NoteBean> patientNotes, List<String> riskTerms) {
        return patientNotes.stream()
                .flatMap(note -> riskTerms.stream()
                        .filter(term -> note.getContent().toLowerCase().contains(term.toLowerCase())))
                .count();
    }

    /**
     * Establishes a profile of the patient (man under 30, woman under 30, patient over 30)
     * based on their age and gender which will be used to calculate the risk level
     * @param patientAge
     * @param patientGender
     * @return
     */
    public static RiskProfilEnum defineRiskProfile(int patientAge, GenderEnum patientGender) {
        if(patientAge <= 30 && patientGender.equals(GenderEnum.M)){
            return RiskProfilEnum.MAN_UNDER_THIRTY;
        } else if (patientAge <= 30 && patientGender.equals(GenderEnum.F)){
            return RiskProfilEnum.WOMAN_UNDER_THIRTY;
        } else {
            return RiskProfilEnum.PATIENT_OVER_THIRTY;
        }
    }

    /**
     * Establishes the risk level based on the patient profile and the number of terms found
     * @param riskProfile
     * @param numberOfRiskTerms
     * @return the risk level
     */
    public static  String defineLevelRiskBasedOnProfile(RiskProfilEnum riskProfile, int numberOfRiskTerms) {
        switch (riskProfile) {
            case MAN_UNDER_THIRTY -> {return defineRiskForManUnderThirty(numberOfRiskTerms);}
            case WOMAN_UNDER_THIRTY -> {return defineRiskForWomanUnderThirty(numberOfRiskTerms);}
            default -> {return defineRiskForPatientOverThirty(numberOfRiskTerms);}
        }
    }

    private static String defineRiskForManUnderThirty(int numberOfRiskTerms) {
        if (3<=numberOfRiskTerms && numberOfRiskTerms<=4) {
            return RiskEnum.IN_DANGER.getDisplayName();
        } else if (numberOfRiskTerms>=5){
            return RiskEnum.EARLY_ONSET.getDisplayName();
        } else {
            return RiskEnum.NONE.getDisplayName();
        }
    }

    private static String defineRiskForWomanUnderThirty(int numberOfRiskTerms){
        if (4<=numberOfRiskTerms && numberOfRiskTerms<=6) {
            return RiskEnum.IN_DANGER.getDisplayName();
        } else if (numberOfRiskTerms>=7){
            return RiskEnum.EARLY_ONSET.getDisplayName();
        } else {
            return RiskEnum.NONE.getDisplayName();
        }
    }

    private static String defineRiskForPatientOverThirty(int numberOfRiskTerms){
        if (2<=numberOfRiskTerms && numberOfRiskTerms<=5) {
            return RiskEnum.BORDERLINE.getDisplayName();
        } else if (6<=numberOfRiskTerms && numberOfRiskTerms<=7){
            return RiskEnum.IN_DANGER.getDisplayName();
        } else if (numberOfRiskTerms>=8){
            return RiskEnum.EARLY_ONSET.getDisplayName();
        } else {
            return RiskEnum.NONE.getDisplayName();
        }
    }

}
