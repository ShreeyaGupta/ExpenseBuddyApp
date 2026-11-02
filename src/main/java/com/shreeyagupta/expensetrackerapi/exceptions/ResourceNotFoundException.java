package com.shreeyagupta.expensetrackerapi.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	//Serialization means converting an object → bytes so that the object can be stored or transferred.
	private static final long serialVersionUID = 1L; //It's basically a version number for the serialized class and Java uses it to check:Is the class I'm trying to deserialize compatible with the version that was originally serialized?

	public ResourceNotFoundException(String message) {
		super(message); //sends that message to the parent RuntimeException constructor so that it can store as ExpenseNotFoundExcep doesnt have storage mechanism.
	}
}
