/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.skinguardian.backend.skinguardian_backend.services;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skinguardian.backend.skinguardian_backend.repositories.ProfileRepository;
import com.skinguardian.backend.skinguardian_backend.repositories.UserRepository;
import com.skinguardian.backend.skinguardian_backend.dtos.UserDTO;
import com.skinguardian.backend.skinguardian_backend.dtos.ProfileDTO;
import com.skinguardian.backend.skinguardian_backend.entities.User;
import com.skinguardian.backend.skinguardian_backend.entities.Profile;
import com.skinguardian.backend.skinguardian_backend.security.WrongUserException;

@Service
public class UserService {
    @Autowired
    PasswordService passwordService;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ProfileRepository profileRepository;
    
    public String save(UserDTO user) throws DuplicateException {
        List<User> existing = userRepository.findByName(user.getName());
        if(!existing.isEmpty())
            throw new DuplicateException();

        User newUser = new User();
        newUser.setName(user.getName());
        String hash = passwordService.hashPassword(user.getPassword());
        newUser.setPassword(hash);
        userRepository.save(newUser);
        return newUser.getUserId().toString();
    }
    
    public User findByNameAndPassword(String name,String password) {
        List<User> existing = userRepository.findByName(name);
        if(existing.size() != 1)
                return null;
        User u = existing.get(0);
        if(passwordService.verifyHash(password, u.getPassword())) {
            u.setPassword("Undisclosed");
        } else {
            u = null;
        }
        return u;	
    }
    
    public void saveProfile(UUID userid,ProfileDTO profile) throws WrongUserException, DuplicateException {
        Optional<User> maybeUser = userRepository.findById(userid);
        if(maybeUser.isEmpty())
            throw new WrongUserException();

        User user = maybeUser.get();
        if(user.getProfile() != null)
            throw new DuplicateException();

        Profile newProfile = new Profile(profile);
        newProfile.setUser(user);
        profileRepository.save(newProfile);
    }
    
    public Profile findProfile(UUID userid) {
        Optional<User> maybeUser = userRepository.findById(userid);
        if(maybeUser.isEmpty())
            return null;

        return maybeUser.get().getProfile();
    }
    
}
