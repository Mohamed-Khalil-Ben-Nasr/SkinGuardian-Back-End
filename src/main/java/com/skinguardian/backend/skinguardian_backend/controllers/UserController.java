/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.skinguardian.backend.skinguardian_backend.controllers;

import com.skinguardian.backend.skinguardian_backend.entities.Diagnosis;
import com.skinguardian.backend.skinguardian_backend.repositories.DiagnosisRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.skinguardian.backend.skinguardian_backend.services.UserService;
import com.skinguardian.backend.skinguardian_backend.entities.User;
import com.skinguardian.backend.skinguardian_backend.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import com.skinguardian.backend.skinguardian_backend.services.DuplicateException;
import com.skinguardian.backend.skinguardian_backend.dtos.ProfileDTO;
import com.skinguardian.backend.skinguardian_backend.entities.Profile;
import com.skinguardian.backend.skinguardian_backend.dtos.DiagnosisDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import com.skinguardian.backend.skinguardian_backend.security.SkinguardianUserDetails;
import com.skinguardian.backend.skinguardian_backend.security.WrongUserException;
import com.skinguardian.backend.skinguardian_backend.security.JwtService;



@RestController
@RequestMapping("/users")
@CrossOrigin(origins="*")
public class UserController {
    
    private UserService us;
    private JwtService jwtService;
    
    public UserController(UserService us, JwtService jwtService){
        this.us = us;
        this.jwtService = jwtService;
    }


    // this works in postman
    @PostMapping("/login")
    public ResponseEntity<String> checkLogin(@RequestBody UserDTO user){
        User result = us.findByNameAndPassword(user.getName(), user.getPassword());
        if (result == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user name or password");
        }
        String token = jwtService.makeJwt(result.getUserId().toString());
        return ResponseEntity.ok().body(token);
    }

    // this works in postman
    @PostMapping
    public ResponseEntity<String> save(@RequestBody UserDTO user){
        if (user.getName().isBlank() || user.getPassword().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty username or password");
        }
        String key;
        try{
            // this will make key = userId
            key = us.save(user);
        } catch (DuplicateException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this name already exists");
        }
        String token = jwtService.makeJwt(key);
        return ResponseEntity.status(HttpStatus.CREATED).body(token); 
    }

    // works on postman
    @PostMapping("/profile")
    public ResponseEntity<String> saveProfile(Authentication authentication,@RequestBody ProfileDTO profile){
        SkinguardianUserDetails details = (SkinguardianUserDetails) authentication.getPrincipal();
        UUID id = UUID.fromString(details.getUsername());
        try{
            us.saveProfile(id, profile);
        } catch(WrongUserException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user id");
        } catch (DuplicateException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate profile");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Profile created");
    }

    // works on postman
    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile(Authentication authentication){
        SkinguardianUserDetails details = (SkinguardianUserDetails) authentication.getPrincipal();
        UUID id = UUID.fromString(details.getUsername());
        Profile result = us.findProfile(id);
        if (result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ProfileDTO response = new ProfileDTO(result);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/diagnoses")
    public ResponseEntity<List<DiagnosisDTO>> getUserDiagnoses(Authentication authentication){
        SkinguardianUserDetails details = (SkinguardianUserDetails) authentication.getPrincipal();
        UUID id = UUID.fromString(details.getUsername());
        List<Diagnosis> diagnosis = us.findDiagnoses(id);
        if (diagnosis == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<DiagnosisDTO> result = new ArrayList<DiagnosisDTO>();
        for (Diagnosis d : diagnosis){
            result.add(new DiagnosisDTO(d));
        }

        return ResponseEntity.ok().body(result);
    }


    
    
    
}
