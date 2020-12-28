package com.example.quranappteacher;

public class Student {
    int Id;
    String name;
    String phone;
    String date;


    public Student(int Id, String name, String phone, String date) {
        this.Id = Id;
        this.name = name;
        this.phone = phone;
        this.date = date;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }
}
