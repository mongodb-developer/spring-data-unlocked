package com.mongodb.domain.service;

import com.mongodb.domain.model.Customer;
import com.mongodb.domain.model.CustomersByCity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void clearDatabase() {
        mongoOperations.dropCollection(Customer.class);
    }

    @Test
    void shouldInsertAndFindCustomer() {
        var customer = createTestCustomer("ricardo@email.com", "Ricardo", "123", "8888", "Lisbon");

        customerService.insert(customer);
        Customer found = customerService.findCustomerByEmail("ricardo@email.com");

        assertThat(found).isNotNull();
        assertThat(found.name()).isEqualTo("Ricardo");
    }

    @Test
    void shouldUpdatePhoneByEmail() {
        customerService.insert(createTestCustomer("ana@email.com", "Ana", "456", "1234", "Porto"));

        Customer updated = customerService.updatePhoneByEmail("ana@email.com", "99999999");

        assertThat(updated.phone()).isEqualTo("99999999");
    }

    @Test
    void shouldReturnTotalCustomersByCity() {
        customerService.insert(createTestCustomer("1@email.com", "User1", "1", "1", "London"));
        customerService.insert(createTestCustomer("2@email.com", "User2", "2", "2", "London"));
        customerService.insert(createTestCustomer("3@email.com", "User3", "3", "3", "Berlin"));

        List<CustomersByCity> result = customerService.totalCustomerByCity();

        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(c -> c.id().equals("London") && c.total() == 2);
        assertThat(result).anyMatch(c -> c.id().equals("Berlin") && c.total() == 1);
    }

    @Test
    void shouldDeleteCustomerByEmail() {
        customerService.insert(createTestCustomer("delete@email.com", "Del", "999", "999", "Paris"));

        customerService.deleteByEmail("delete@email.com");

        assertThat(mongoOperations.findAll(Customer.class)).isEmpty();
    }

    private Customer createTestCustomer(String email, String name, String account, String phone, String city) {
        return new Customer(
                name,
                email,
                account,
                phone,
                new Customer.Address("Rua A", city)
        );
    }
}
