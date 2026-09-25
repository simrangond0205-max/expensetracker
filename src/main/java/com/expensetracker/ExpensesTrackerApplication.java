package com.SpringBootMVC.ExpensesTracker;

import com.SpringBootMVC.ExpensesTracker.entity.Role;
import com.SpringBootMVC.ExpensesTracker.entity.Category;
import com.SpringBootMVC.ExpensesTracker.repository.CategoryRepository;
import com.SpringBootMVC.ExpensesTracker.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpensesTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpensesTrackerApplication.class, args);

	}

	@Bean
	CommandLineRunner initializeRoles(RoleRepository roleRepository, CategoryRepository categoryRepository) {
		return args -> {
			if (roleRepository.findByName("ROLE_STANDARD") == null) {
				roleRepository.save(new Role(2, "ROLE_STANDARD"));
			}

			String[] categoryNames = {
				"groceries", "Utilities(bills)", "transportation", "dining out",
				"entertainment", "shopping", "travel", "education", "salary"
			};
			for (int index = 0; index < categoryNames.length; index++) {
				if (categoryRepository.findByName(categoryNames[index]) == null) {
					categoryRepository.save(new Category(index + 1, categoryNames[index]));
				}
			}
		};
	}

}
