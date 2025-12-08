package com.drewfilkins.spring_boot_mvc.service;

import com.drewfilkins.spring_boot_mvc.model.UserDto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private Long idCounter = 3L;
    private Map<Long, UserDto> userDtoMap = new HashMap();

    private PetService petService;

    public UserService(PetService petService) {
        this.petService = petService;
    }

    @PostConstruct
    private void initialize() {
        UserDto user1 = new UserDto(1L, "Andrew", "and@mail.ru", 25, new ArrayList<>());
        UserDto user2 = new UserDto(2L, "Bob", "bob@mail.ru", 28, new ArrayList<>());
        userDtoMap.put(user1.getId(), user1);
        userDtoMap.put(user2.getId(), user2);
    }

    public List<UserDto> getAllUsers() {
        return userDtoMap.values().stream().toList();
    }

    public UserDto getUserById(Long id) {
        Optional<UserDto> userDto = Optional.ofNullable(userDtoMap.get(id));
        if (userDto.isPresent())
            return userDto.get();
        else
            throw new NoSuchElementException("No user with id=%d!".formatted(id));
    }

    public UserDto createUser(UserDto userToCreate) {
        Long newId = idCounter++;
        UserDto newUser = new UserDto(newId, userToCreate.getName(), userToCreate.getEmail(), userToCreate.getAge(),
                userToCreate.getPets().stream().map(pet -> petService.createPet(pet)).toList());
        userDtoMap.put(newId, newUser);
        return newUser;
    }

    public UserDto updateUser(Long id, UserDto userToUpdate) {
        if (userDtoMap.get(id) == null)
            throw new NoSuchElementException("No user with id=%d!".formatted(id));
        else {
            UserDto updatedUser = new UserDto(id, userToUpdate.getName(), userToUpdate.getEmail(), userToUpdate.getAge(),
                    userToUpdate.getPets().stream().map(pet -> petService.createPet(pet)).toList());
            userDtoMap.put(id, updatedUser);
            return updatedUser;
        }
    }

    public void deleteUserById(Long id) {
        var result = userDtoMap.remove(id);
        if (result == null) {
            throw new NoSuchElementException("No user with id=%d!".formatted(id));
        }
    }
}
