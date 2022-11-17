package com.jovanovic.stefan.sqlitetutorial;

import java.io.Serializable;

public class Expense implements Serializable {
    private int expense_id;
    private String category;
    private int total_expense;
    private String used_time;
    private String notes;
    private int trip_id;

    public Expense(int expense_id, String category, int total_expense, String used_time,String notes, int trip_id) {
        this.expense_id = expense_id;
        this.category = category;
        this.total_expense = total_expense;
        this.used_time = used_time;
        this.notes = notes;
        this.trip_id=trip_id;
    }
    public Expense(){

    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotal_expense() {
        return total_expense;
    }

    public void setTotal_expense(int total_expense) {
        this.total_expense = total_expense;
    }

    public String getUsed_Time() {
        return used_time;
    }

    public void setUsed_time(String used_time) {
        this.used_time = used_time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }




    public int getId() {
        return expense_id;
    }

    public void setId(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getTripId() {
        return trip_id;
    }

    public void setTripId(int trip_id) {
        this.trip_id = trip_id;
    }
    @Override
    public String toString() {
        return "Expense{" +
                "expense_id=" + expense_id +
                ", category='" + category + '\'' +
                ", total_expense='" + total_expense + '\'' +
                ", notes=" + notes + '\'' +
                ", trip_id=" + trip_id +
                '}';
    }
}
