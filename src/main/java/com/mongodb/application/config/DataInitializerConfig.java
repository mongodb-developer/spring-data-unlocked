package com.mongodb.application.config;

import com.mongodb.domain.model.Customer;
import com.mongodb.domain.service.CustomerService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializerConfig {

    @Bean
    public ApplicationRunner initCustomers(CustomerService customerService) {
        return args -> {
            List<Customer> customers = List.of(
                    new Customer("João Silva", "joao.silva@email.com", "1001", "1199990001",
                            new Customer.Address("Rua das Flores", "São Paulo")),
                    new Customer("Maria Oliveira", "maria.oliveira@email.com", "1002", "1199990002",
                            new Customer.Address("Avenida Diagonal", "Barcelona")),
                    new Customer("Carlos Souza", "carlos.souza@email.com", "1003", "3199990003",
                            new Customer.Address("Via Roma", "Roma")),
                    new Customer("Ana Costa", "ana.costa@email.com", "1004", "2199990004",
                            new Customer.Address("Broadway", "New York")),
                    new Customer("Pedro Lima", "pedro.lima@email.com", "1005", "7199990005",
                            new Customer.Address("Gran Vía", "Madrid")),
                    new Customer("Juliana Rocha", "juliana.rocha@email.com", "1006", "6199990006",
                            new Customer.Address("Champs-Élysées", "Paris")),
                    new Customer("Fernando Dias", "fernando.dias@email.com", "1007", "3199990007",
                            new Customer.Address("Kreuzbergstrasse", "Berlin")),
                    new Customer("Patrícia Almeida", "patricia.almeida@email.com", "1008", "8199990008",
                            new Customer.Address("Queen Street", "Toronto")),
                    new Customer("Rafael Martins", "rafael.martins@email.com", "1009", "5199990009",
                            new Customer.Address("George Street", "Sydney")),
                    new Customer("Camila Ramos", "camila.ramos@email.com", "1010", "6299990010",
                            new Customer.Address("Damrak", "Amsterdam")),
                    new Customer("Lucas Fernandes", "lucas.fernandes@email.com", "1011", "2199990011",
                            new Customer.Address("Oxford Street", "London")),
                    new Customer("Bianca Moreira", "bianca.moreira@email.com", "1012", "3199990012",
                            new Customer.Address("Nanjing Road", "Shanghai")),
                    new Customer("Rodrigo Pires", "rodrigo.pires@email.com", "1013", "4199990013",
                            new Customer.Address("Orchard Road", "Singapore")),
                    new Customer("Larissa Mello", "larissa.mello@email.com", "1014", "3199990014",
                            new Customer.Address("Sathorn", "Bangkok")),
                    new Customer("Thiago Araujo", "thiago.araujo@email.com", "1015", "7199990015",
                            new Customer.Address("Avenida Paulista", "São Paulo"))
            );

            int inserted = customerService.bulkCustomerSample(customers);
            System.out.println("Inserted customers: " + inserted);
        };
    }
}
