package com.shreeyagupta.expensetrackerapi.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.shreeyagupta.expensetrackerapi.entity.Expense;
import com.shreeyagupta.expensetrackerapi.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;
	
	//{{url}}/expenses?page=0&size=2&sort=amount,desc (For pagination, no of elements in one page=2. page= page no & sort For sorting the elements)
	@GetMapping("/expenses")
	public List<Expense> getAllExpenses(Pageable page) {
		Page<Expense>expenses=expenseService.getAllExpenses(page);
		return expenses.toList();
	}
	 
	@GetMapping("/expenses/{id}") //this {id} will be read by Pathvariable and then used in returning in funcn
    public Expense getExpenseById(@PathVariable Long id) {
   	   return expenseService.getExpenseById(id);
    }
	
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@DeleteMapping("/expenses") //localhost:8080/api/v1/expenses?id=45 call like this
	public void deleteExpenseById(@RequestParam Long id) { //RequestParam takes the value of id from the request URL and put it into my Java variable id.
		expenseService.deleteExpenseById(id);
	}
	
	@ResponseStatus(value=HttpStatus.CREATED)
	@PostMapping("/expenses")
	public Expense saveExpenseDetails(@Valid @RequestBody Expense expense) { //Request body Take the JSON data from the request body and convert it into an Expense Java object. Put that object into the expense variable.
		return expenseService.saveExpenseDetails(expense);
	}
	
	@PutMapping("/expenses/{id}")
	public Expense updateExpenseDetails(@RequestBody Expense expense, @PathVariable long id) {
		return expenseService.updateExpenseDetails(id, expense);
	}
	
	@GetMapping("/expenses/category")
	public List<Expense>getAllExpensesByCategory(@RequestParam String category, Pageable page){
		return expenseService.readByCategory(category, page);
	}
	
	@GetMapping("/expenses/name")
	public List<Expense> getAllExpensesByName(@RequestParam String keyword, Pageable page){
		return expenseService.readByName(keyword, page);
	}
	
	@GetMapping("/expenses/date")
	public List<Expense> getAllExpensesByDate(
			                                 @RequestParam(required=false) Date startDate,
	                                         @RequestParam(required=false) Date endDate,
	                                         Pageable page){
	    return expenseService.readByDate(startDate, endDate, page);                                     	 
	}
	
}

     