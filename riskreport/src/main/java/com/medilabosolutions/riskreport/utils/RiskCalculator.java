package com.medilabosolutions.riskreport.utils;

import com.medilabosolutions.riskreport.beans.GenderEnum;
import com.medilabosolutions.riskreport.beans.NoteBean;
import com.medilabosolutions.riskreport.beans.RiskProfilEnum;
import com.medilabosolutions.riskreport.enums.RiskEnum;

import java.util.List;

public class RiskCalculator {

    private final static List<String> RISK_TERMS = List.of("Hémoglobine A1C","Microalbumine","Taille", "Poids", "Fumeur", "Fumeuse", "Anormal", "Cholestérol", "Vertiges", "Rechute", "Réaction", "Anticorps");

    public static long calculateNumberOfRiskTerms(List<NoteBean> patientNotes) {
        return patientNotes.stream()
                .flatMap(note -> RISK_TERMS.stream()
                        .filter(term -> note.getContent().toLowerCase().contains(term.toLowerCase())))
                .count();
    }

    public static RiskProfilEnum defineRiskProfile(int patientAge, GenderEnum patientGender) {
        if(patientAge <= 30 && patientGender.equals(GenderEnum.M)){
            return RiskProfilEnum.MAN_UNDER_THIRTY;
        } else if (patientAge <= 30 && patientGender.equals(GenderEnum.F)){
            return RiskProfilEnum.WOMAN_UNDER_THIRTY;
        } else {
            return RiskProfilEnum.PATIENT_OVER_THIRTY;
        }
    }

    public static String defineLevelRiskBasedOnProfile(RiskProfilEnum riskProfile, int numberOfRiskTerms) {
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
