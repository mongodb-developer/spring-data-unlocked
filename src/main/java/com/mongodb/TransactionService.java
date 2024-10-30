package com.mongodb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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


}
