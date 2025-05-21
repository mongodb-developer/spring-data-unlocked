package com.mongodb.application.web;

import com.mongodb.domain.model.Transaction;
import com.mongodb.domain.service.TransactionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/pageable")
    public PagedModel<Transaction> findAll(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "100") int sizePerPage,
                                           @RequestParam(defaultValue = "ID") String sortField,
                                           @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
        Pageable pageable = PageRequest.of(page, sizePerPage, Sort.by(sortDirection, sortField));
        return new PagedModel<>(transactionService.findPageableTransactions(pageable));
    }

    @GetMapping("/type/{type}/currency/{currency}")
    public List<Transaction> byTypeAndCurrency(@PathVariable String type, @PathVariable String currency) {
        return transactionService.findByTransactionTypeAndCurrency(type, currency);
    }

    @GetMapping("/status/{status}")
    public List<Transaction> status(@PathVariable String status) {
        return transactionService.findByStatus(status);
    }

    @GetMapping("/type/{type}")
    public List<Transaction> findByTransactionType(@PathVariable String type) {
        return transactionService.findByTransactionType(type);
    }

    @GetMapping("/export")
    public void findByTransactionType() {
        transactionService.exportErrorTransactions();
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(transaction));
    }
}

