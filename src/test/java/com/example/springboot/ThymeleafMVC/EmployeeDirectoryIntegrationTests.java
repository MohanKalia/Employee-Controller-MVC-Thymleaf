package com.example.springboot.ThymeleafMVC;

import com.example.springboot.ThymeleafMVC.dao.EmployeeRepository;
import com.example.springboot.ThymeleafMVC.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:directory;MODE=MySQL",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "DIRECTORY_ADMIN_USERNAME=test-admin",
        "DIRECTORY_ADMIN_PASSWORD=directory-test-password"
})
class EmployeeDirectoryIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EmployeeRepository employees;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        employees.deleteAll();
    }

    @Test
    void anonymousVisitorsMustSignInAndConfiguredAdministratorCanLogIn() throws Exception {
        mvc.perform(get("/employees/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", endsWith("/login")));

        mvc.perform(formLogin().user("test-admin").password("directory-test-password"))
                .andExpect(authenticated().withRoles("ADMIN"))
                .andExpect(redirectedUrl("/employees/list"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void otherRolesCannotReadOrChangeEmployeeRecords() throws Exception {
        mvc.perform(get("/employees/list")).andExpect(status().isForbidden());
        mvc.perform(post("/employees/delete").param("employeeId", "1").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createsEditsAndDeletesAnEmployeeThroughTheWebRoutes() throws Exception {
        mvc.perform(get("/employees/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name=\"_csrf\"")));

        mvc.perform(post("/employees/save").with(csrf())
                        .param("firstName", "Sam").param("lastName", "Taylor").param("email", "sam@example.com"))
                .andExpect(redirectedUrl("/employees/list"));
        Employee saved = employees.findAll().getFirst();
        String id = String.valueOf(saved.getId());

        mvc.perform(get("/employees/showFormForUpdate").param("employeeId", id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("sam@example.com")));

        mvc.perform(post("/employees/save").with(csrf()).param("employeeId", id)
                        .param("firstName", "Sam").param("lastName", "Lee").param("email", "sam@example.com"))
                .andExpect(redirectedUrl("/employees/list"));
        assertThat(employees.findById(saved.getId()).orElseThrow().getLastName()).isEqualTo("Lee");

        mvc.perform(post("/employees/delete").param("employeeId", id))
                .andExpect(status().isForbidden());
        mvc.perform(get("/employees/delete").param("employeeId", id))
                .andExpect(status().isMethodNotAllowed());
        assertThat(employees.existsById(saved.getId())).isTrue();

        mvc.perform(post("/employees/delete").param("employeeId", id).with(csrf()))
                .andExpect(redirectedUrl("/employees/list"));
        assertThat(employees.existsById(saved.getId())).isFalse();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void invalidEditsReturnToTheSameFormWithoutChangingTheRecord() throws Exception {
        Employee employee = employees.save(new Employee("Sam", "Taylor", "sam@example.com"));

        mvc.perform(post("/employees/save").with(csrf())
                        .param("employeeId", String.valueOf(employee.getId()))
                        .param("firstName", "a".repeat(46)).param("lastName", "Taylor")
                        .param("email", "bad-email"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("employeeId", employee.getId()))
                .andExpect(model().attributeHasFieldErrors("employee", "firstName", "email"))
                .andExpect(content().string(containsString("First name must be at most 45 characters")));

        assertThat(employees.findById(employee.getId()).orElseThrow().getFirstName()).isEqualTo("Sam");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void staleEmployeeLinksAndSubmissionsReturnNotFound() throws Exception {
        String missingId = String.valueOf(Integer.MAX_VALUE);
        mvc.perform(get("/employees/showFormForUpdate").param("employeeId", missingId))
                .andExpect(status().isNotFound());
        mvc.perform(post("/employees/save").param("employeeId", missingId).with(csrf())
                        .param("firstName", "Sam").param("lastName", "Taylor").param("email", "sam@example.com"))
                .andExpect(status().isNotFound());
        mvc.perform(post("/employees/delete").param("employeeId", missingId).with(csrf()))
                .andExpect(status().isNotFound());
        assertThat(employees.count()).isZero();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void searchIsCaseInsensitiveAndPaginatesMatchesInLastNameOrder() throws Exception {
        for (int i = 10; i >= 0; i--) {
            employees.save(new Employee("Sam", "Taylor%02d".formatted(i), "sam" + i + "@example.com"));
        }
        employees.save(new Employee("Alex", "Adams", "alex@elsewhere.com"));

        mvc.perform(get("/employees/list").param("q", "  EXAMPLE.COM  "))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Page 1 of 2")))
                .andExpect(content().string(containsString("Taylor00")))
                .andExpect(content().string(not(containsString("Taylor10"))))
                .andExpect(content().string(not(containsString("Adams"))));

        mvc.perform(get("/employees/list").param("q", "Taylor").param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Page 2 of 2")))
                .andExpect(content().string(containsString("Taylor10")))
                .andExpect(content().string(not(containsString("Taylor00"))));

        mvc.perform(get("/employees/list").param("q", "Taylor").param("page", "3"))
                .andExpect(redirectedUrl("/employees/list?page=2&q=Taylor"));
        mvc.perform(get("/employees/list").param("q", "%"))
                .andExpect(content().string(containsString("No employees match your search.")));
    }
}
