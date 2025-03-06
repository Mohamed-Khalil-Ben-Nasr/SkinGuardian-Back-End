/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.skinguardian.backend.skinguardian_backend.services;

/**
 *
 * @author MohamedTheGoat
 */
import org.springframework.stereotype.Service;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;

@Service
public class PasswordService {
	
	static private final String secret="CMSC455";
	
	public String hashPassword(String password) {
		BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

		Hash hash = Password.hash(password)
		                    .addPepper(secret)
		                    .with(bcrypt);

		return hash.getResult();
	}
	
	public boolean verifyHash(String password,String hash) {
		BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

		return Password.check(password, hash)
		               .addPepper(secret)
		               .with(bcrypt);
	}
}

