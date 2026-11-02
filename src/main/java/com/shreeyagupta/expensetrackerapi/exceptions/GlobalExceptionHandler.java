package com.shreeyagupta.expensetrackerapi.exceptions;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.shreeyagupta.expensetrackerapi.entity.ErrorObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice //means this class will handle exceptions coming from my controllers
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ResourceNotFoundException.class)//Handle exceptions whose type is ResourceNotFoundException.
	public ResponseEntity<ErrorObject>handleExpenseNotFoundException(ResourceNotFoundException ex,WebRequest request){ //WebRequest is current url passed eg /GET/expenses/10 
		ErrorObject errorObject=new ErrorObject();
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value()); //status=404
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());
		return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.NOT_FOUND); //ResponseEntity constructor = ResponseEntity<>(body,status)
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)// class presents in Spring and handle exceptions where there is mismatch in value of request expectation ex: GET/expenses/xyz //int expected
	public ResponseEntity<ErrorObject>handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException ex,WebRequest request){ 
		ErrorObject errorObject=new ErrorObject();
		errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value()); //status=400
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());
		return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.BAD_REQUEST); //ResponseEntity constructor = ResponseEntity<>(body,status)
	}
	
	@ExceptionHandler(Exception.class)// for all other exceptions 
	public ResponseEntity<ErrorObject>handleGeneralException(Exception ex,WebRequest request){ 
		ErrorObject errorObject=new ErrorObject();
		errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); //status=500
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());
		return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.INTERNAL_SERVER_ERROR); //ResponseEntity constructor = ResponseEntity<>(body,status)
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,//when validation fails eg @NOT_NULL/@NOT_BLANK
			HttpHeaders headers, HttpStatusCode status, WebRequest request) { //method comes from ResponseEntityExceptionHandler
		Map<String,Object>body=new HashMap<>();
		body.put("timestamp",new Date());
		body.put("statusCode", HttpStatus.BAD_REQUEST.value());
		List<String>errors=ex.getBindingResult() //get validation results
		.getFieldErrors() //gets field errors like "Name should not be null"
		.stream() //processes each error one by one
		.map(x->x.getDefaultMessage()) //for every error, give me inly its message
		.collect(Collectors.toList()); //collects messages and adds them to List errors
		body.put("messages", errors);
		return new ResponseEntity<Object>(body,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ItemAlreadyExistsException.class)// for Item already exists exception for User email
	public ResponseEntity<ErrorObject>handleItemExistsException(ItemAlreadyExistsException ex,WebRequest request){ 
		ErrorObject eObject=new ErrorObject();
		eObject.setStatusCode(HttpStatus.CONFLICT.value()); //status=409
		eObject.setMessage(ex.getMessage());
		eObject.setTimestamp(new Date());
		return new ResponseEntity<ErrorObject>(eObject,HttpStatus.CONFLICT); //ResponseEntity constructor = ResponseEntity<>(body,status)
	}

}
