package com.skinguardian.backend.skinguardian_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skinguardian.backend.skinguardian_backend.entities.Profile;

public interface ProfileRepository extends JpaRepository<Profile,Integer>{

}
