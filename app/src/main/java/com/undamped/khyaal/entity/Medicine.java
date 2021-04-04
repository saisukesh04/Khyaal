package com.undamped.khyaal.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicineDb")
public class Medicine {

    @PrimaryKey(autoGenerate = true)
    private long primary_key;

    private String name;
    private int days;
    private boolean morning, afternoon, evening;

    public Medicine() {
        morning = false;
        afternoon = false;
        evening = false;
    }

    public String getDosage(){
        char[] dose = {'0','-','0','-','0'};
        if(morning)
            dose[0] = '1';
        if (afternoon)
            dose[2] = '1';
        if (evening)
            dose[4] = '1';
        return new String(dose);
    }

    public long getPrimary_key() {
        return primary_key;
    }

    public void setPrimary_key(long primary_key) {
        this.primary_key = primary_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public boolean isMorning() {
        return morning;
    }

    public void setMorning(boolean morning) {
        this.morning = morning;
    }

    public boolean isAfternoon() {
        return afternoon;
    }

    public void setAfternoon(boolean afternoon) {
        this.afternoon = afternoon;
    }

    public boolean isEvening() {
        return evening;
    }

    public void setEvening(boolean evening) {
        this.evening = evening;
    }
}
