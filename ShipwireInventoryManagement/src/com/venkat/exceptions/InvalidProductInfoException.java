package com.venkat.exceptions;

/**
 * If the input file for inventory is invalid, InvalidProductInfoException is raised
 * 
 * @author venkat
 *
 */

public class InvalidProductInfoException extends Exception {
	
	private static final long serialVersionUID = 12345678916L;
	
	public InvalidProductInfoException(){
		super();
	}
	
	public InvalidProductInfoException(String message){
		super(message);
	}
}
