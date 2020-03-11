package com.company.Parking.service;

import com.company.Parking.model.VerificationToken;
import com.company.Parking.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VerificationTokenService {

    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public boolean saveNewToken(VerificationToken token){
        return save(token);
    }

    public boolean existsVerificationToken(String token){
        return exist(token);
    }

    public Optional<VerificationToken> findVerificationTokenByToken(String token){
        return findByToken(token);
    }

    private Optional<VerificationToken> findByToken(String token){

        try {
            return  verificationTokenRepository.findVerificationTokenByToken(token);
        } catch (Exception e) {
            log.error("Error during finding verification token by token");
        }

        return Optional.empty();
    }

    private boolean exist(String token) {
        try {
            verificationTokenRepository.existsVerificationTokenByToken(token);
        } catch (Exception e) {
            log.error("Error during checking existing following token", e);
        }
        return false;
    }

    private boolean save(VerificationToken token){
        try {
            verificationTokenRepository.save(token);
            return true;
        } catch (Exception e) {

            log.error("Error during saving Verification token to db",e);
        }

        return false;
    }
}
