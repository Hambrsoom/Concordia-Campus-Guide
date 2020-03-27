package com.example.concordia_campus_guide.Helper;

import android.content.Context;
import android.os.Handler;

import com.example.concordia_campus_guide.Activities.MainActivity;
import com.example.concordia_campus_guide.Database.AppDatabase;
import com.example.concordia_campus_guide.Models.CalendarEvent;
import com.example.concordia_campus_guide.Models.Helpers.CalendarViewModel;
import com.example.concordia_campus_guide.Models.Place;
import com.example.concordia_campus_guide.Models.RoomModel;


public class Notification {
    private MainActivity mainActivity;
    private AppDatabase appDatabase;
    private CalendarEvent previousCalendarEvent;

    public Notification(MainActivity mainActivity, AppDatabase appDatabase){
        this.mainActivity =  mainActivity;
        this.appDatabase  = appDatabase;
        this.previousCalendarEvent = new CalendarEvent("","","");
    }

    public void checkUpCalendarEvery5Minutes() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                CalendarEvent calendarEvent = getNextClassCalendar();
                if(calendarEvent  != null && validateCalendarEvent(calendarEvent)){
                    if(!calendarEvent.getTitle().equalsIgnoreCase(previousCalendarEvent.getTitle())){
                        previousCalendarEvent = calendarEvent;
                        mainActivity.popUp(calendarEvent);
                    }
                }
                handler.postDelayed(this, 3000);
            }
        }, 3000);
    }

    public boolean validateCalendarEvent(CalendarEvent calendarEvent){
        return calendarEvent.getLocation().length() > 2 && calendarEvent.getTitle().length() > 7 && calendarEvent.getStartTime().length() >2;
    }

    public CalendarEvent getNextClassCalendar(){
        CalendarViewModel calendarViewModel = new CalendarViewModel(mainActivity.getApplication());
        return  calendarViewModel.getEvent(mainActivity);
    }

    public RoomModel getRoom(String location){
        String floorCode ="";
        String roomCode  ="";
        if(location == null) { return null; }

        if(location.substring(location.indexOf('-')+1).length() > 3 ){
            floorCode = location.substring(0,location.indexOf('-')+3);
            roomCode  = location.substring(location.indexOf('-')+1);
        }
        else {
            floorCode = location.substring(0,location.indexOf('-')+2);
            roomCode  = location.substring(location.indexOf('-')+1);
        }
        return appDatabase.roomDao().getRoomByRoomCodeAndFloorCode(roomCode,floorCode);
    }
}
