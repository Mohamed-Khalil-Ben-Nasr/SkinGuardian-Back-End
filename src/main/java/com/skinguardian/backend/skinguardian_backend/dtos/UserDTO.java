
package com.skinguardian.backend.skinguardian_backend.dtos;

public class UserDTO {
    private String name;
    private String password;
    
    public UserDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
