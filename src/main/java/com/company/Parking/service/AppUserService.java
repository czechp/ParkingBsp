package com.company.Parking.service;

import com.company.Parking.model.AppUser;
import com.company.Parking.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AppUserService {

    private AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public Optional<AppUser> findByUsername(String username){
        try {
            return appUserRepository.findByUsername(username);
        } catch (Exception e) {
            log.error("Error during getting user by username",e);
        }
        return Optional.empty();
    }

    public boolean existsByUserName(String username) {
        try {
            return appUserRepository.existsByUsername(username);
        } catch (Exception e) {
            log.error("Error during checking exists by username");
        }
        return false;
    }

    public boolean existsByEmail(String email){
        try {
            return appUserRepository.existsByEmail(email);
        } catch (Exception e) {
            log.error("Error during checking exists user by email");
        }

        return false;
    }

    public void saveUser(AppUser user) {
        try {
            appUserRepository.save(user);
        } catch (Exception e) {
            log.error("Error during saving new user", e);
        }
    }

    public Optional<AppUser> findById(long id) {
        try {
            return appUserRepository.findById(id);
        } catch (Exception e) {
            log.error("Error during finding user by id", e);
        }

        return Optional.empty();
    }

    public List<AppUser> findAll() {
        try {
            return appUserRepository.findAll();
        } catch (Exception e) {
            log.error("Error during getting all users", e);
        }


        return new ArrayList<>();
    }
}

