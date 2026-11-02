 package com.shreeyagupta.expensetrackerapi.entity;

 import java.math.BigDecimal;
 import java.sql.Timestamp;
 import java.sql.Date;

 import org.hibernate.annotations.CreationTimestamp;
 import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import jakarta.persistence.Column;
 import jakarta.persistence.Entity;
 import jakarta.persistence.GeneratedValue;
 import jakarta.persistence.Table;
 import jakarta.validation.constraints.NotBlank;
 import jakarta.validation.constraints.NotNull;
 import jakarta.validation.constraints.Size;
 import jakarta.persistence.Id;
 import jakarta.persistence.GenerationType;

 @Data
 @AllArgsConstructor
 @NoArgsConstructor
 @Entity                        //tells JPA that class represents table in DB
 @Table(name="tbl_users")
public class User {

	   @Id
	   @GeneratedValue(strategy=GenerationType.IDENTITY)
	   private Long id;
	   
	   private String name;
	   
	   @Column(unique=true)
	   private String email;
	   
	   
	   //password field is not included when converting Java object->JSON (sending response from Controller to Postman)
	   //or JSON->Java(Getting request from Postman and Controller RequestBody converting to Java obj)
	   //so that password not displayed as API response
	   //Hence, we use UserModel class instead of User bcs if we directly use User in Controller for save,
	   //during JSON->Java obj, password in request gets ignored and not saved
	   @JsonIgnore 
	   private String password;
	   
	   
	   private Long age;
	   
	   @Column(name="created_at", nullable=false,updatable=false)
	   @CreationTimestamp
	   private Timestamp createdAt;
	   
	   @Column(name="updated_at")
	   @UpdateTimestamp
	   private Timestamp updatedAt;
}
