package com.drewfilkins.spring_boot_mvc;

import com.drewfilkins.spring_boot_mvc.model.PetDto;
import com.drewfilkins.spring_boot_mvc.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {

    @Autowired
    private PetService petService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessfullyCreatePet() throws Exception {
        var pet = new PetDto(null, "Barsik", 1L);
        String petJson = objectMapper.writeValueAsString(pet);

        String createdPet = mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto petResponse = objectMapper.readValue(createdPet, PetDto.class);

        Assertions.assertNotNull(petResponse.getId());
        Assertions.assertEquals(pet.getName(), petResponse.getName());
    }

    @Test
    void shouldNotCreatePet() throws Exception {
        var pet = new PetDto(null, "Barsik", 0L);
        String petJson = objectMapper.writeValueAsString(pet);

        mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().is(404));
    }

    @Test
    void shouldSuccessfullyFindPetById() throws Exception {
        var pet = new PetDto(null, "Barsik", 1L);
        pet = petService.createPet(pet);

        String petJson = mockMvc.perform(get("/pet/{id}", pet.getId()))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto foundPet = objectMapper.readValue(petJson, PetDto.class);

        org.assertj.core.api.Assertions.assertThat(pet)
                .usingRecursiveComparison()
                .isEqualTo(foundPet);
    }

    @Test
    void shouldNotFindPetById() throws Exception {
      mockMvc.perform(get("/pet/{id}", 999))
                .andExpect(status().is(404));
    }

    @Test
    void shouldSuccessfullyDeletePetById() throws Exception {
        var pet = new PetDto(null, "Barsik", 1L);
        pet = petService.createPet(pet);

        mockMvc.perform(delete("/pet/{id}", pet.getId()))
                .andExpect(status().is(204));
    }

    @Test
    void shouldNotDeletePetById() throws Exception {
        mockMvc.perform(get("/pet/{id}", 999))
                .andExpect(status().is(404));
    }
}
