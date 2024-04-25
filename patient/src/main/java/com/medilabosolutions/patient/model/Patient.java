package com.medilabosolutions.patient.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String lastName;

    private String firstName;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "address-id", referencedColumnName = "id")
    private Address address;

    private String phoneNumber;

}
