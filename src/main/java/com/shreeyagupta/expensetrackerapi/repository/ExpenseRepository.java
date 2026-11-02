package com.shreeyagupta.expensetrackerapi.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shreeyagupta.expensetrackerapi.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	//spring reads these findByCategory etc as sql queries bcs of predefined naming convention for repository methods
    
	Page<Expense> findByUserIdAndCategory(Long userId,String category,Pageable page); // Select* from tbl_expenses WHERE user_id=? AND category=?
    
    Page<Expense> findByUserIdAndNameContaining(Long userId,String keyword,Pageable page); //Select * FROM tbl_expenses WHERE user_id=? AND name LIKE '%keyword%'
    
    Page<Expense> findByUserIdAndDateBetween(Long userId,Date startDate,Date endDate,Pageable page); //Select * from tbl_expenses WHERE user_id=? AND date BETWEEN 'startDate' AND 'endDate'

    Page<Expense> findByUserId(Long userId,Pageable page); //Select * From tbl_expenses WHERE user_id=?

    Optional<Expense> findByUserIdAndId(Long userId, Long expenseId); // Select * from tbl_expenses WHERE user_id=? AND id=?

}
