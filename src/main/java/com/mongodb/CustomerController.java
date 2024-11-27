package com.mongodb;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public void post(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
    }

    @GetMapping("/totalCustomerByCity")
    public List<CustomersByCity> totalCustomerByCity() {
       return customerService.totalCustomerByCity();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> post(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerService.findCustomerByEmail(email));
    }

    @GetMapping("/indexes")
    public ResponseEntity<String> explain() {
         return ResponseEntity.ok(customerService.explain());
    }

}
