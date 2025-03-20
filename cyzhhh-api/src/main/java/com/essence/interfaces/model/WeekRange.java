package com.essence.interfaces.model;

import java.time.LocalDate;

public class WeekRange {
    private LocalDate start;
    private LocalDate end;
 
    public WeekRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }
 
    public LocalDate getStart() {
        return start;
    }
 
    public LocalDate getEnd() {
        return end;
    }
}