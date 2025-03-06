
package com.skinguardian.backend.skinguardian_backend.dtos;

import com.skinguardian.backend.skinguardian_backend.entities.Profile;

public class ProfileDTO {
    private String user;
    private String fullname;
    private String email;
    private String phone;
    private Integer age;
    private String sex;
    
    public ProfileDTO(){}
    
    public ProfileDTO(Profile core){
        user = core.getUser().getUserId().toString();
        fullname = core.getFullname();
        email  = core.getEmail();
        phone = core.getPhone();
        age = core.getAge();
        sex = core.getSex();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
