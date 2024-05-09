package com.medilabosolutions.patient.mapper;

import com.medilabosolutions.patient.dto.GenderEnumDto;
import com.medilabosolutions.patient.dto.PatientDto;
import com.medilabosolutions.patient.model.GenderEnum;
import com.medilabosolutions.patient.model.Patient;
import fr.xebia.extras.selma.EnumMapper;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIgnoreFields="com.medilabosolutions.patient.model.Address.id")
public interface PatientMapper {

    @EnumMapper(from = GenderEnum.class, to = GenderEnumDto.class)
    PatientDto asPatientDto(Patient patient);

    @EnumMapper(from = GenderEnumDto.class, to = GenderEnum.class)
    //@Maps(withIgnoreFields={"com.medilabosolutions.patient.model.Patient.id","com.medilabosolutions.patient.dto.PatientDto.id"})
    Patient asPatient(PatientDto patientDto);
}
