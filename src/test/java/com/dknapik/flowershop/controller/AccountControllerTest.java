package com.dknapik.flowershop.controller;

import com.dknapik.flowershop.constants.AccountMessage;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.account.AccountDTO;
import com.dknapik.flowershop.dto.account.AccountDetailsDTO;
import com.dknapik.flowershop.dto.account.PasswordChangeDTO;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.AccountRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc()
@AutoConfigureRestDocs(value = "build/generated-snippets/account")
final class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    private String username = "TestUser";


    @BeforeEach
    void setup(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        /* Check if provided Account entity exists, and remove it if true */
        Optional<Account> account = accountRepository.findByName(username);
        account.ifPresent(value -> accountRepository.delete(value));
        accountRepository.flush();
    }

    @Test
    void createAccount() throws Exception {
        /* Create DTO and map it to JSON request */
        //MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        AccountDTO accountDto =
                new AccountDTO(username, "example@test.pl", "Password12345!", AccountRole.ROLE_USER);
        String jsonRequest = objectMapper.writeValueAsString(accountDto);

        /* Create request*/
        MockHttpServletRequestBuilder requestBuilder =
                post("/account").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE);

        /* Perform request, check status and retrieve results by mapping them to DTO */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
        MessageResponseDTO message =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);

        /* Check if results match desired value */
        Assertions.assertThat(message.getMessage()).isEqualTo(AccountMessage.ENTITY_CREATION_SUCCESSFUL.toString());
    }

    @Test
    void retrieveAccount() throws Exception {
        /* Prepare Account and Control Object  test */
        Account newEntity = new Account(username, "TestPassword", "Test@TestMail.com", AccountRole.ROLE_USER);
        accountRepository.saveAndFlush(newEntity);
        AccountDTO controlObject = new AccountDTO(newEntity.getId(), newEntity.getName(), newEntity.getEmail(),
                newEntity.getPassword(), newEntity.getRole());

        /* Create request*/
        MockHttpServletRequestBuilder requestBuilder = get("/account").with(user(username));

        /* Perform request, check status and retrieve results by mapping them to DTO */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
        AccountDetailsDTO accountDetailsDTO =
                objectMapper.readValue(result.getResponse().getContentAsString(), AccountDetailsDTO.class);

        /* Check if results match desired control object */
        Assertions.assertThat(accountDetailsDTO).isEqualToComparingFieldByField(controlObject);
    }

    @Test
    void updateAccount() throws Exception {
        /* Prepare Account and payload */
        Account newEntity = new Account(username, "TestPassword", "Test@TestMail.com", AccountRole.ROLE_USER);
        accountRepository.saveAndFlush(newEntity);
        AccountDetailsDTO accountDetailsDto = new AccountDetailsDTO("NewEmail@Test.com");

        /* Create request*/
        String jsonContent = objectMapper.writeValueAsString(accountDetailsDto);
        MockHttpServletRequestBuilder requestBuilder = put("/account").content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON_VALUE).with(user(username));

        /* Perform request, check status and retrieve results by mapping them to DTO */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
        MessageResponseDTO response =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);

        /* Check if results match desired control object */
        Assertions.assertThat(response.getMessage()).isEqualTo(AccountMessage.ENTITY_UPDATE_SUCCESSFUL.toString());
    }

    /**
     * This test requires encoded password
     */
    @Test
    void updatePassword() throws Exception {
        /* Prepare Account by encoding password and saving entity to database */
        String password = "TestPassword1@";
        String newPassword = "TestPassword2!@#";
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        Account newEntity = new Account(username, encoder.encode(password), "Test@TestMail.com", AccountRole.ROLE_USER);
        accountRepository.saveAndFlush(newEntity);

        /* Prepare dto for request */
        PasswordChangeDTO passwordChangeDto =
                new PasswordChangeDTO(newEntity.getId(), password, newPassword, newPassword);

        /* Create request */
        String jsonContent = objectMapper.writeValueAsString(passwordChangeDto);
        MockHttpServletRequestBuilder requestBuilder = put("/account/password").content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON_VALUE).with(user(username));

        /* Perform request, check status and retrieve results by mapping them to DTO */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
        MessageResponseDTO response =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);

        /* Check if results match desired control object */
        Assertions.assertThat(response.getMessage()).isEqualTo(AccountMessage.ENTITY_UPDATE_SUCCESSFUL.toString());
    }

    @Test
    void deleteAccount() throws Exception {
        /* Prepare Account entity by saving it to database */
        String password = "TestPassword!@#";
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        Account newEntity = new Account(username, encoder.encode(password), "Test@TestMail.com", AccountRole.ROLE_USER);
        accountRepository.saveAndFlush(newEntity);

        /* Create request */
        MockHttpServletRequestBuilder requestBuilder =
                delete("/account").param("password", password).with(user(username));

        /* Perform request, check status and retrieve results by mapping them to DTO */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
        MessageResponseDTO response =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);

        /* Check if results match desired control object */
        Assertions.assertThat(response.getMessage()).isEqualTo(AccountMessage.ENTITY_DELETE_SUCCESSFUL.toString());
    }
}
