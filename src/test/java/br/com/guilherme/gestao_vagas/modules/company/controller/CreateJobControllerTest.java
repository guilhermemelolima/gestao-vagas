package br.com.guilherme.gestao_vagas.modules.company.controller;


import br.com.guilherme.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.guilherme.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.guilherme.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.guilherme.gestao_vagas.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class CreateJobControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CompanyRepository companyRepository;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void should_be_able_to_create_a_new_job() throws Exception {

        CompanyEntity company = CompanyEntity.builder()
                .description("COMPANY_DESCRIPTION_TEST")
                .email("TEST@EMAIL.COM")
                .password("1234567890")
                .username("COMPANY_USERNAME_TEST")
                .name("COMPANY_NAME")
                .build();

        company = companyRepository.saveAndFlush(company);

        CreateJobDTO createJobDTO = CreateJobDTO.builder()
                .benefits("BENEFITS_TEST")
                .description("DESCRIPTION_TEST")
                .level("LEVEL_TEST")
                .build();

        var result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/company/job/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(createJobDTO))
                        .header("Authorization",
                                TestUtils.generateToken(company.getId(),"PALAVRA_SECRETA"))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        System.out.println("\n\tTEST COMPLETE AND PASS\n " + result + "\n");
    }

    @Test
    public void should_not_be_able_to_create_a_new_job_if_company_not_found() throws Exception {
        CreateJobDTO createJobDTO = CreateJobDTO.builder()
                .benefits("BENEFITS_TEST")
                .description("DESCRIPTION_TEST")
                .level("LEVEL_TEST")
                .build();

        mvc.perform(
                MockMvcRequestBuilders
                        .post("/company/job/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(createJobDTO))
                        .header("Authorization",
                                TestUtils.generateToken(UUID.randomUUID(),"PALAVRA_SECRETA"))
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
