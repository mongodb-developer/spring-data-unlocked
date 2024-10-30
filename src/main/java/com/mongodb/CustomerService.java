package com.mongodb;

import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class CustomerService {

    private final MongoOperations mongoOperations;

    CustomerService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Customer saveCustomer(Customer customer) {
        return mongoOperations.insert(customer);
    }

    public void bulkCustomer(List<Customer> customerList) {
        BulkWriteResult result = mongoOperations.bulkOps(
                        BulkOperations.BulkMode.ORDERED, Customer.class
                ).insert(customerList)
                .execute();

        System.out.println(result.getInsertedCount());
    }

    public List<CustomersByCity> totalCustomerByCity() {

        TypedAggregation<Customer> aggregation =  newAggregation(Customer.class,
                        group("address.city")
                                .count().as("total"),
                        Aggregation.sort(Sort.Direction.ASC, "_id"),
                        project(Fields.fields("total", "_id")));


        AggregationResults<CustomersByCity> result = mongoOperations.aggregate(aggregation, CustomersByCity.class);
        return result.getMappedResults();
    }

    public Customer findCustomerByEmail(String email) {
        return mongoOperations.query(Customer.class)
                .matching(query(where("email").is(email)))

                .one()
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
    }
}
