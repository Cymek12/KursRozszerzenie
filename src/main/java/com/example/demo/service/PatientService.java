package com.example.demo.service;

import com.example.demo.exceptions.CannotChangeIdCardNoException;
import com.example.demo.exceptions.PatientAlreadyExistException;
import com.example.demo.exceptions.PatientDataIsNullException;
import com.example.demo.exceptions.PatientNotFoundException;
import com.example.demo.model.PasswordRequest;
import com.example.demo.model.Patient;
import com.example.demo.model.PatientDTO;
import com.example.demo.model.PatientMapper;
import com.example.demo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDTO> getPatients() {
        return patientMapper.map(patientRepository.findAll());
    }

    public PatientDTO getPatientByEmail(String email) {
        return patientMapper.map(patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist")));
    }

    public void addPatient(Patient patient) {
        if(isPatientDataNull(patient)){
            throw new PatientDataIsNullException("Patient fields cannot be null");
        }
        if(patientRepository.existsByEmail(patient.getEmail())){
            throw new PatientAlreadyExistException("Patient with email: " + patient.getEmail() + " already exist");
        }
        patientRepository.save(patient);
    }

    public void deletePatientByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        patientRepository.delete(patient);
    }

    public void editPatient(String email, Patient newPatientData) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        validateNewPatientData(patient, newPatientData);
        editNewPatient(patient, newPatientData);
        patientRepository.save(patient);
    }

    public void editPassword(String email, PasswordRequest passwordRequest) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        patient.setPassword(passwordRequest.newPassword());
        patientRepository.save(patient);
    }

    private void validateNewPatientData(Patient patient, Patient newPatientData) {
        if (!patient.getIdCardNo().equals(newPatientData.getIdCardNo())) {
            throw new CannotChangeIdCardNoException("Id card number is unchangeable");
        }
        if(isPatientDataNull(newPatientData)){
            throw new PatientDataIsNullException("Patient fields cannot be null");
        }
        if(patientRepository.existsByEmail(patient.getEmail()) && !patient.getEmail().equals(newPatientData.getEmail())){
            throw new PatientAlreadyExistException("Patient with email: " + newPatientData.getEmail() + " already exist");
        }
    }

    public void editNewPatient(Patient patient, Patient newPatientData) {
        patient.setEmail(newPatientData.getEmail());
        patient.setPassword(newPatientData.getPassword());
        patient.setFirstName(newPatientData.getFirstName());
        patient.setLastName(newPatientData.getLastName());
        patient.setPhoneNumber(newPatientData.getPhoneNumber());
        patient.setBirthday(newPatientData.getBirthday());
    }

    public boolean isPatientDataNull(Patient patient) {
        return patient.getEmail() == null ||
                patient.getPassword() == null ||
                patient.getIdCardNo() == null ||
                patient.getFirstName() == null ||
                patient.getLastName() == null ||
                patient.getPhoneNumber() == null ||
                patient.getBirthday() == null;
    }
}
