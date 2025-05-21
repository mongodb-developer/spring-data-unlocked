package com.mongodb.application;

import com.mongodb.domain.model.Customer;
import com.mongodb.domain.model.CustomersByCity;
import com.mongodb.domain.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(201).body(customerService.saveCustomer(customer));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<Customer> getCustomerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.findCustomerByEmail(email));
    }

    @GetMapping("/report/total-by-city")
    public ResponseEntity<List<CustomersByCity>> getCustomerTotalsByCity() {
        return ResponseEntity.ok(customerService.totalCustomerByCity());
    }

    @GetMapping("/diagnostics/indexes")
    public ResponseEntity<String> getIndexExplanation() {
        return ResponseEntity.ok(customerService.explain());
    }

    @PatchMapping("/{email}/phone")
    public ResponseEntity<Customer> updateCustomerPhone(@PathVariable String email, @RequestParam String phone) {
        return ResponseEntity.ok(customerService.updatePhoneByEmail(email, phone));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteCustomerByEmail(@PathVariable String email) {
        customerService.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }

}
