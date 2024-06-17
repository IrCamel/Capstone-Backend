package com.progetto.personale.capstone.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private  UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public UserResponse findById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User inesistente");
        }
        User user = userRepository.findById(id).get();
        UserResponse userResponse = new UserResponse();

        //Codice che sostituisce il mapper
        BeanUtils.copyProperties(user, userResponse);
        return  userResponse;
    }

    public UserResponse createUser(UserRequest userRequest){
        User user = new User();

        BeanUtils.copyProperties(userRequest, user);
        UserResponse userResponse = new UserResponse();

        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }
}
