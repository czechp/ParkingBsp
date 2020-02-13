package com.company.Parking.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertionService {

    private ConvertionService() {
    }

    public static LocalDate stringToLocalDate(String textDate) {
        String[] dates = textDate.split("-");
        List<Integer> datesInt = Arrays.stream(dates)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        if ((datesInt.get(1) >= 1 && datesInt.get(1) <= 12) && (datesInt.get(2) >= 1 && datesInt.get(2) <= 31)) {
            return LocalDate.of(datesInt.get(0), datesInt.get(1), datesInt.get(2));
        }
        return null;
    }
}
