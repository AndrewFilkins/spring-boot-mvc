package com.drewfilkins.spring_boot_mvc.controller;

import com.drewfilkins.spring_boot_mvc.model.PetDto;
import com.drewfilkins.spring_boot_mvc.model.UserDto;
import com.drewfilkins.spring_boot_mvc.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {

    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<PetDto> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public PetDto getPetById(@PathVariable Long id) {
        return petService.getPetById(id);
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@RequestBody @Valid PetDto newPet) {
        PetDto createdPet = petService.createPet(newPet);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody @Valid PetDto petToUpdate) {
        PetDto updatedPet = petService.updatePet(id, petToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetById(@PathVariable Long id) {
        petService.deletePetById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
