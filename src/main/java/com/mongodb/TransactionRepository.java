package com.mongodb;

import org.springframework.data.mongodb.repository.*;
import org.springframework.data.mongodb.repository.ReadPreference;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    @ReadPreference("secondaryPreferred")
    List<Transaction> findByTransactionType(String type);
    List<Transaction> findByAmountGreaterThan(double amount);
    void deleteByTransactionType(String type);


    @Query(
            value= "{ 'status' : ?0 }",
            fields="{ 'createdAt':  1, 'accountDetails' : 1, 'amount' : 1}",
            sort = "{ createdAt:  -1}"
    )
    List<Transaction> findByStatus(String status);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'status' : ?1 } }")
    void updateStatus(String id, String status);

    @Aggregation(pipeline = {
            "{ '$match': { 'transactionType': ?0 } }",
            "{ '$group': { '_id': '$transactionType', 'amount': { '$sum': '$amount' } } }",
            "{ '$project': { 'amount': 1 } }"
    })
    List<Transaction> getTotalAmountByTransactionType(String transactionType);

    @Aggregation(pipeline = {
            "{ '$match': { 'status': 'error' } }",
            "{ '$project': { '_id': 1, 'amount': 1, 'status': 1, 'description': 1, 'createdAt': 1} }",
            "{ '$out': 'error_transactions' }"
    })
    void exportErrorTransactions();

    @Aggregation(pipeline = {
            "{ '$lookup': { " +
                    "'from': 'customer', " +
                    "'localField': 'accountDetails.originator.accountNumber', " +
                    "'foreignField': 'accountNumber', " +
                    "'as': 'originatorCustomerDetails' } }",
            "{ '$project': { " +
                    "'amount': 1, " +
                    "'status': 1, " +
                    "'accountDetails': 1, " +
                    "'originatorCustomerDetails': 1 } }"
    })
    List<CustomerOriginatorDetail> findTransactionsWithCustomerDetails();
}



