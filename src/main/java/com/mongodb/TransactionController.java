package com.mongodb;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @GetMapping("/page")
    public PagedModel<Transaction> findAll(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "100") int sizePerPage,
                                           @RequestParam(defaultValue = "ID") String sortField,
                                           @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {

        return new PagedModel<>(transactionService.findAll(page, sizePerPage, sortField, sortDirection));
    }


    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }



}

