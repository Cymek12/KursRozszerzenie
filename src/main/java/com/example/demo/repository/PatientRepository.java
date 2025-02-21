package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository {
    private final List<Patient> patients;

    public PatientRepository(List<Patient> patients) {
        this.patients = patients;
    }

    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findAny();
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void deletePatient(Patient patient) {
        patients.remove(patient);
    }

    public void editPatient(Patient patient, Patient newPatientData) {
        patient.setEmail(newPatientData.getEmail());
        patient.setPassword(newPatientData.getPassword());
        patient.setIdCardNo(newPatientData.getIdCardNo());
        patient.setFirstName(newPatientData.getFirstName());
        patient.setLastName(newPatientData.getLastName());
        patient.setPhoneNumber(newPatientData.getPhoneNumber());
        patient.setBirthday(newPatientData.getBirthday());
    }
}
