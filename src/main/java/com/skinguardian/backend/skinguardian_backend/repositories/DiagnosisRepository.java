package com.skinguardian.backend.skinguardian_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skinguardian.backend.skinguardian_backend.entities.Diagnosis;

import java.util.UUID;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, UUID> {
}
