package com.example.concordia_campus_guide.ModelTests;

import com.example.concordia_campus_guide.models.Coordinates;
import com.example.concordia_campus_guide.models.ListOfCoordinates;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class ListOfCoordinatesTest {

    ListOfCoordinates coordinates;
    List<Coordinates> testingList;
    @Before
    public void init(){
        testingList = new ArrayList<>();
        testingList.add(new Coordinates(-73.57907921075821, 45.49702057370776));
        testingList.add(new Coordinates(-73.57921063899994, 45.49707133596979));
        coordinates = new ListOfCoordinates();
    }

    @Test
    public void equalsTest(){
        List<Coordinates> testingListEquals = new ArrayList<>();
        testingListEquals.add(new Coordinates(-73.57907921075821, 45.49702057370776));
        testingListEquals.add(new Coordinates(-73.57907921075821, 45.49702057370776));
        assertTrue(testingListEquals.get(0).equals(testingListEquals.get(1)));

    }

    @Test
    public void getAndSetCoordinatesTest(){
        assertEquals(coordinates.getListOfCoordinates(), null);
        coordinates.setListOfCoordinates(testingList);
        assertEquals(coordinates.getListOfCoordinates(), testingList);
    }
}
