package com.mongodb.domain.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.domain.model.Transaction;
import com.mongodb.resources.TransactionRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    public Page<Transaction> findAll(
            int page,
            int sizePerPage,
            String sortField,
            Sort.Direction sortDirection
    ) {
        Pageable pageable = PageRequest.of(
                page,
                sizePerPage,
                Sort.by(sortDirection, sortField)
        );
        return transactionRepository.findAll(pageable);
    }

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Page<Transaction> findPageableTransactions(
            Pageable pageable
    ) {
        return transactionRepository.findAll(pageable);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findByTransactionTypeAndCurrency(String type, String currency) {
        return transactionRepository.findByTransactionTypeAndCurrency(type, currency);
    }

    public List<Transaction> findByTransactionType(String type) {
        return transactionRepository.findByTransactionType(type);
    }

    public List<Transaction> findByStatus(String status) {
        return transactionRepository.getTotalAmountByTransactionType(status);
    }

    public void exportErrorTransactions() {
        transactionRepository.exportErrorTransactions();
    }


}
