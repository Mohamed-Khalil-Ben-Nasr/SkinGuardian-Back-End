package com.skinguardian.backend.skinguardian_backend.entities;


import com.skinguardian.backend.skinguardian_backend.dtos.DiagnosisDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="diagnoses")
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(45)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID diagnosisId;

    @ManyToOne
    @JoinColumn(name="user")
    private User user;

    private String sex;
    private String localization;
    private Integer age;
    private String imageUrl;
    private String diagnosisResult;

    public Diagnosis(){}

    public Diagnosis(DiagnosisDTO core){
        sex = core.getSex();
        localization = core.getLocalization();
        age = core.getAge();
        imageUrl = core.getImageUrl();
        diagnosisResult = core.getDiagnosisResult();
    }

    public UUID getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(UUID diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDiagnosisResult() {
        return diagnosisResult;
    }

    public void setDiagnosisResult(String diagnosisResult) {
        this.diagnosisResult = diagnosisResult;
    }
}
