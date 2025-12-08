package com.drewfilkins.spring_boot_mvc.service;

import com.drewfilkins.spring_boot_mvc.model.PetDto;
import com.drewfilkins.spring_boot_mvc.model.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PetService {

    private Long idCounter = 0L;

    private UserService userService;

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    public PetDto createPet(PetDto petToCreate) {
        Long userId = petToCreate.getUserId();
        Optional<UserDto> userDto = Optional.ofNullable(userService.getUserById(userId));
        if (userDto.isPresent()) {
            Long newId = idCounter++;
            PetDto newPet = new PetDto(newId, petToCreate.getName(), userId);
            userDto.get().getPets().add(newPet);
            return newPet;
        } else
            throw new NoSuchElementException("No user with id=%d!".formatted(userId));
    }

    public List<PetDto> getAllPets() {
        return userService.getAllUsers().stream().map(UserDto::getPets).filter(pets -> !pets.isEmpty()).flatMap(List::stream).toList();
    }

    public PetDto getPetById(Long id) {
        Optional<PetDto> petDto = getAllPets().stream().filter(pet -> pet.getId().equals(id)).findFirst();
        if (petDto.isPresent())
            return petDto.get();
        else
            throw new NoSuchElementException("No pet with id=%d!".formatted(id));
    }

    public PetDto updatePet(Long id, @Valid PetDto petToUpdate) {
        Optional<PetDto> petDto = getAllPets().stream().filter(pet -> pet.getId().equals(id)).findFirst();
        if (petDto.isPresent()) {
            Long userId = petDto.get().getUserId();
            if (userId.equals(petToUpdate.getUserId())) {
                UserDto user = userService.getUserById(userId);
                PetDto petDto1 = petDto.get();
                petDto1.setName(petToUpdate.getName());
                List<PetDto> updatedPetList = user.getPets().stream().map(pet -> {
                    if (pet.getId().equals(id)) {
                        pet.setName(petToUpdate.getName());
                    }
                    return pet;
                }).toList();
                user.setPets(updatedPetList);
                return getPetById(id);
            }
            else {
                throw new RuntimeException("You can't change the userId of a pet!");
            }
        } else {
            throw new NoSuchElementException("No pet with id=%d!".formatted(id));
        }
    }

    public void deletePetById(Long id) {
        Optional<PetDto> petDto = getAllPets().stream().filter(pet -> pet.getId().equals(id)).findFirst();
        if (petDto.isPresent()) {
            PetDto pet = petDto.get();
            UserDto user = userService.getUserById(pet.getUserId());
            List<PetDto> pets = user.getPets();
            pets.remove(pet);
            user.setPets(pets);
        } else {
            throw new NoSuchElementException("No pet with id=%d!".formatted(id));
        }
    }
}
