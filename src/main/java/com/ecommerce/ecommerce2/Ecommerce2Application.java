package com.ecommerce.ecommerce2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ecommerce.ecommerce2.Service.CustomerService;

@SpringBootApplication
public class Ecommerce2Application {

	public static void main(String[] args) {
		SpringApplication.run(Ecommerce2Application.class, args);


	}
	@Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

	// @Bean
	// CommandLineRunner createAdmin(CustomerService customerService){
	// 	return args->{
	// 		customerService.addRole("ADMIN");

	// 		customerService.addNewUser("admin", "admin");

	// 		customerService.addRoleToUser("admin","ADMIN");
	// 		customerService.addRoleToUser("admin","USER");
	// 	};
	// }
}
	