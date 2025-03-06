/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.skinguardian.backend.skinguardian_backend.services;

/**
 *
 * @author MohamedTheGoat
 */
public class DuplicateException extends Exception {
	public DuplicateException() {
		super("Attempt to insert duplicate element.");
	}
}
