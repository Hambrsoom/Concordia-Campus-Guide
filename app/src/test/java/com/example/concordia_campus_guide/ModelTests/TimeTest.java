package com.example.concordia_campus_guide.ModelTests;

import com.example.concordia_campus_guide.models.Time;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTest {
    Time time;

    @Before
    public void init() {
        time = new Time();
    }

    @Test
    public void timeConstructor(){
        assertNotNull(time);
    }
}