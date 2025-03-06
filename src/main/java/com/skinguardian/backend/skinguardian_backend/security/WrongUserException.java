/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.skinguardian.backend.skinguardian_backend.security;

/**
 *
 * @author MohamedTheGoat
 */
public class WrongUserException extends Exception {
    public WrongUserException() {
		super("User is not allowed to perform this action");
	}
}
