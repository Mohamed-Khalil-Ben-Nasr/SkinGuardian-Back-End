package com.skinguardian.backend.skinguardian_backend.controllers;


import com.skinguardian.backend.skinguardian_backend.dtos.DiagnosisMetadataDTO;
import com.skinguardian.backend.skinguardian_backend.entities.Profile;
import com.skinguardian.backend.skinguardian_backend.security.SkinguardianUserDetails;
import com.skinguardian.backend.skinguardian_backend.security.WrongUserException;
import com.skinguardian.backend.skinguardian_backend.services.DiagnosisService;
import com.skinguardian.backend.skinguardian_backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.skinguardian.backend.skinguardian_backend.dtos.DiagnosisDTO;
import com.skinguardian.backend.skinguardian_backend.entities.Diagnosis;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
@RequestMapping("/diagnosis")
@CrossOrigin(origins = "*")
public class DiagnosisController {

    private UserService us;
    private DiagnosisService ds;

    public DiagnosisController(DiagnosisService ds){
        this.ds = ds;
    }


    /*
    I am going to use Spring’s @RequestPart annotation to handle multipart form-data:
    @RequestPart("metadata") → Extracts JSON metadata (e.g., localization).
    @RequestPart("image") → Extracts the image file.
     */
    @PostMapping
    public ResponseEntity<String> save(Authentication authentication, @RequestPart("metadata") DiagnosisMetadataDTO metadata, @RequestPart("image") MultipartFile image ) throws WrongUserException {
        // get user id
        SkinguardianUserDetails details = (SkinguardianUserDetails) authentication.getPrincipal();
        UUID id = UUID.fromString(details.getUsername());

        // get profile
        Profile profile = us.findProfile(id);
        String sex = profile.getSex();
        Integer age = profile.getAge();

        // update the metadata, which is the DiagnosisMetadataDTO, that we are going to pass to the inference point
        metadata.setAge(age);
        metadata.setSex(sex);

        // process the diagnosis in the diagnosisService and get back the diagnosisDTO as a result
        String diagnosisResult = ds.processDiagnosis(id, metadata, image);

        return ResponseEntity.status(HttpStatus.CREATED).body(diagnosisResult);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DiagnosisDTO> findById(@PathVariable UUID id){
        Diagnosis diagnosis = ds.findById(id);
        DiagnosisDTO result = new DiagnosisDTO(diagnosis);
        return ResponseEntity.ok().body(result);
    }

}
