package com.skinguardian.backend.skinguardian_backend.dtos;

public class DiagnosisMetadataDTO {
    private String localization;
    private String sex;
    private Integer age;

    public DiagnosisMetadataDTO(){}

    public DiagnosisMetadataDTO(Integer age, String localization, String sex){
        this.localization = localization;
        this.sex = sex;
        this.age = age;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
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
}
