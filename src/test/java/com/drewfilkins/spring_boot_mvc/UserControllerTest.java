package com.drewfilkins.spring_boot_mvc;

import com.drewfilkins.spring_boot_mvc.model.UserDto;
import com.drewfilkins.spring_boot_mvc.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessfullyCreateUser() throws Exception {
        var user = new UserDto(null, "Jack", "jack@mail.ru", 20, new ArrayList<>());
        String userJson = objectMapper.writeValueAsString(user);

        String createdUser = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userResponse = objectMapper.readValue(createdUser, UserDto.class);

        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertEquals(user.getName(), userResponse.getName());
    }

    @Test
    void shouldNotCreateUser() throws Exception {
        var user = new UserDto(null, null, "jack@mail.ru", 20, new ArrayList<>());
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(400));
    }

    @Test
    void shouldSuccessfullyFindUserById() throws Exception {
        var user = new UserDto(null, "Jack", "jack@mail.ru", 20, new ArrayList<>());
        user = userService.createUser(user);

        String userJson = mockMvc.perform(get("/user/{id}", user.getId()))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto foundUser = objectMapper.readValue(userJson, UserDto.class);

        org.assertj.core.api.Assertions.assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(foundUser);
    }

    @Test
    void shouldNotFindUserById() throws Exception {
        mockMvc.perform(get("/user/{id}", 999))
                .andExpect(status().is(404));
    }

    @Test
    void shouldSuccessfullyDeleteUserById() throws Exception {
        var user = new UserDto(null, "Jack", "jack@mail.ru", 20, new ArrayList<>());
        user = userService.createUser(user);

        mockMvc.perform(delete("/user/{id}", user.getId()))
                .andExpect(status().is(204));
    }

    @Test
    void shouldNotDeleteUserById() throws Exception {
        mockMvc.perform(get("/user/{id}", 999))
                .andExpect(status().is(404));
    }
}
