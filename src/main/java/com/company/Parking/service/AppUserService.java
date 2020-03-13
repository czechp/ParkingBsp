package com.company.Parking.service;

import com.company.Parking.model.AppUser;
import com.company.Parking.model.UserRoles;
import com.company.Parking.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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



    public Optional<AppUser> findByUsername(String username) {
        try {
            return appUserRepository.findByUsername(username);
        } catch (Exception e) {
            log.error("Error during getting user by username", e);
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

    public boolean existsByEmail(String email) {
        try {
            return appUserRepository.existsByEmail(email);
        } catch (Exception e) {
            log.error("Error during checking exists user by email");
        }

        return false;
    }

    public boolean existById(long id) {
        try {
            return appUserRepository.existsById(id);
        } catch (Exception e) {
            log.error("Error during checking user by Id", e);
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

    public List<AppUser> findAllByAdminVerification(boolean status) {
        try {
            return appUserRepository.findAllByAdminVerification(status);
        } catch (Exception e) {
            log.error("Error during getting users by admin verification", e);
        }

        return new ArrayList<>();
    }

    public boolean enableUser(long id, boolean enable) {
        Optional<AppUser> optAppUser = findById(id);
        if(optAppUser.isPresent()){
            optAppUser.get().setAdminVerification(enable);
            saveUser(optAppUser.get());
            return true;
        }
        return false;
    }

    public boolean setRole(long id, UserRoles role) {
        Optional<AppUser> optAppUser = findById(id);
        if(optAppUser.isPresent()){
            if(role.equals(UserRoles.ADMIN))
                optAppUser.get().setAdminRole();
            else if(role.equals(UserRoles.USER))
                optAppUser.get().setUserRole();

            saveUser(optAppUser.get());
            return true;
        }

        return false;
    }

    public boolean deleteById(long id) {
        if(existById(id)){
            try {
                appUserRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                log.error("Error during removing user", e);
            }
        }
        return false;
    }

    public List<AppUser> findAllByRole(UserRoles role){
        try {
            return appUserRepository.findAllByRole(role.getRoleName());
        } catch (Exception e) {
            log.error("Error during getting user by role", e);
        }
        return new ArrayList<>();
    }
}

