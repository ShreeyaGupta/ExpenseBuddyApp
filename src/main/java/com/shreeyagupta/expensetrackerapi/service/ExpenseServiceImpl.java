package com.shreeyagupta.expensetrackerapi.service;

import java.sql.Date;
import java.util.List;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shreeyagupta.expensetrackerapi.entity.Expense;
import com.shreeyagupta.expensetrackerapi.exceptions.ResourceNotFoundException;
import com.shreeyagupta.expensetrackerapi.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepo;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Page<Expense> getAllExpenses(Pageable page) {
		return expenseRepo.findByUserId(userService.getLoggedInUser().getId(),page);
	}

	@Override
	public Expense getExpenseById(Long id) {
		Optional<Expense>expense=expenseRepo.findByUserIdAndId(userService.getLoggedInUser().getId(),id);
		if(expense.isPresent()) {
			return expense.get();
		}
		throw new ResourceNotFoundException("Expense not found for id "+ id);
	}

	@Override
	public void deleteExpenseById(Long id) {
		Expense expo=getExpenseById(id);
		expenseRepo.delete(expo);
	}

	@Override
	public Expense saveExpenseDetails(Expense expense) {
		expense.setUser(userService.getLoggedInUser());
		return expenseRepo.save(expense);
	}

//	Find the existing expense, update only the fields that were provided,
//	keep the old values for everything else, and save it.
	@Override
	public Expense updateExpenseDetails(Long id, Expense expense) {
		Expense existingExpense=getExpenseById(id);
		existingExpense.setName(expense.getName()!=null?expense.getName():existingExpense.getName());
		existingExpense.setDescription(expense.getDescription()!=null?expense.getDescription():existingExpense.getDescription());
		existingExpense.setCategory(expense.getCategory()!=null?expense.getCategory():existingExpense.getCategory());
		existingExpense.setAmount(expense.getAmount()!=null?expense.getAmount():existingExpense.getAmount());
		existingExpense.setDate(expense.getDate()!=null?expense.getDate():existingExpense.getDate());
		return expenseRepo.save(existingExpense);
	}

	@Override
	public List<Expense> readByCategory(String category, Pageable page) {
		return expenseRepo.findByUserIdAndCategory(userService.getLoggedInUser().getId(),category,page).toList();
	}

	@Override
	public List<Expense> readByName(String name, Pageable page) {
		return expenseRepo.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(),name, page).toList();
	}

	@Override
	public List<Expense> readByDate(Date startDate, Date endDate, Pageable page) {
		if(startDate==null) {
			startDate=new Date(0); //Jan 1, 1970
		}
		if(endDate==null){
			endDate=new Date(System.currentTimeMillis());
		}
		Page<Expense>pages=expenseRepo.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(),startDate, endDate, page);
		return pages.toList();
	}
	
	

}
