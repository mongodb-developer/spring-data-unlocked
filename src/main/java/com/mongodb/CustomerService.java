package com.mongodb;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.include;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class CustomerService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final MongoOperations mongoOperations;

    CustomerService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Customer saveCustomer(Customer customer) {
        return mongoOperations.insert(customer);
    }

    public int bulkCustomerSample(List<Customer> customerList) {
        BulkWriteResult result = mongoOperations.bulkOps(BulkOperations.BulkMode.ORDERED, Customer.class)
                .insert(customerList)
                .execute();

        return result.getInsertedCount();
    }

    public List<CustomersByCity> totalCustomerByCity() {

        TypedAggregation<Customer> aggregation = newAggregation(Customer.class,
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

    public String explain() {
        MongoCollection<Document> collection = mongoOperations.getCollection("transactions");
        Document query = new Document("transactionType", "Debit");
        Document explanation = collection.find(query).explain();

        logger.info(explanation.toJson());
        return explanation.toJson();

    }
}
