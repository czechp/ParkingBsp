package com.company.Parking.service;

import com.company.Parking.model.VerifiactionToken;
import com.company.Parking.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerifiactionTokenService {

    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerifiactionTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    private boolean save(VerifiactionToken token){
        try {
            verificationTokenRepository.save(token);
            return true;
        } catch (Exception e) {
            log.error("Error during saving Verification token to db",e);
        }

        return false;
    }
}
