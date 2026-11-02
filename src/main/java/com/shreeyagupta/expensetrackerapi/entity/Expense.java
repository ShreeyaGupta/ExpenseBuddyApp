package com.shreeyagupta.expensetrackerapi.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_expenses")
public class Expense {
   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   private Long id;
   
   @Column(name="expense_name")
   @NotBlank(message="Expense name must not be null")
   @Size(min=3,message="Expense name must be atleast 3 characters")
   private String name;
   private String description;
   
   @Column(name="expense_amount")
   @NotNull(message="Expense amount must not be null")
   private BigDecimal amount;
   
   @NotBlank(message="Expense category must not be null")
   private String category;
   
   @NotNull(message="Date must not be null")
   private Date date;
   
   @Column(name="created_at", nullable=false,updatable=false)
   @CreationTimestamp
   private Timestamp createdAt;
   
   @Column(name="updated_at")
   @UpdateTimestamp
   private Timestamp updatedAt;
   
   @ManyToOne(fetch=FetchType.LAZY,optional=false)
   @JoinColumn(name="user_id",nullable=false)
   @OnDelete(action= OnDeleteAction.CASCADE)
   @JsonIgnore
  private User user;
}
