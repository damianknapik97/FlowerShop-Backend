package com.dknapik.flowershop.controller;

import com.dknapik.flowershop.constants.AccountMessage;
import com.dknapik.flowershop.dto.MessageResponseDto;
import com.dknapik.flowershop.dto.account.AccountDto;
import com.dknapik.flowershop.model.AccountRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void createAccountTest() throws Exception {
        /* Create DTO and map it to JSON request */
        //MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        AccountDto accountDto =
                new AccountDto("Test Name", "example@test.pl", "Password12345!", AccountRole.USER);
        String jsonRequest = objectMapper.writeValueAsString(accountDto);

        /* Create request payload */
        MockHttpServletRequestBuilder requestBuilder =
                post("/account").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE);

        /* Perform request, check status and retrieve results by mapping them to DTO */
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        MessageResponseDto message =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDto.class);

        /* Check if results match desired value */
        Assertions.assertThat(message.getMessage()).isEqualTo(AccountMessage.ENTITY_CREATION_SUCCESSFUL.toString());
    }

    @Test
    public void retrieveAccount() {

    }

    @Test
    public void updateAccount() {

    }

    @Test
    public void updatePassword() {

    }

    @Test
    public void deleteAccount() {

    }
}
