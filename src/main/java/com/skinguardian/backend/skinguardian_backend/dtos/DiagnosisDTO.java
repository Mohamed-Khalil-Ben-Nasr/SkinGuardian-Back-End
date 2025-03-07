package com.skinguardian.backend.skinguardian_backend.dtos;

import com.skinguardian.backend.skinguardian_backend.entities.Diagnosis;

public class DiagnosisDTO {
    private String diagnosisId;
    private String user;
    private String sex;
    private Integer age;
    private String localization;
    private String imageUrl;
    private String diagnosisResult;

    public DiagnosisDTO(){}

    public DiagnosisDTO(Diagnosis core){
        diagnosisId = core.getDiagnosisId().toString();
        user = core.getUser().getUserId().toString();
        sex = core.getSex();
        age = core.getAge();
        localization = core.getLocalization();
        imageUrl = core.getImageUrl();
        diagnosisResult = core.getDiagnosisResult();
    }

    public String getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(String diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }
}
