package com.company.Parking.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CarService {
    public List<String> getFieldsName() {
        return Arrays.asList(
          "Id",
          "Numer rejestracji",
          "Kolor",
          "Marka",
          "Model"
        );
    }
}
