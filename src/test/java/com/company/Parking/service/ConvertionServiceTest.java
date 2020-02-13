package com.company.Parking.service;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class ConvertionServiceTest {

    @Test
    public void stringToLocalDate_Test(){
        //given
        String textDate = "2020-02-13";
        LocalDate except = LocalDate.of(2020, 02,13);
        //when
        LocalDate result = ConvertionService.stringToLocalDate(textDate);
        //then
        assertEquals(except, result);

    }

    @Test
    public void stringToLocalDate_TestOutOfRange(){
        //given
        String textDate = "2020-13-32";
        LocalDate except = LocalDate.of(2020, 02,13);
        //when
        LocalDate result = ConvertionService.stringToLocalDate(textDate);
        //then
        assertNull(result);

    }

}