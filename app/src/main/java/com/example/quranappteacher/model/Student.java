package com.example.quranappteacher.model;

public class Student {
    int Id;
    String name;
    String TeacherName;
    String Address;
    String phone;
    String date;


    public Student(int Id, String name, String TeacherName, String Address, String phone, String date) {
        this.Id = Id;
        this.name = name;
        this.TeacherName = TeacherName;
        this.Address = Address;
        this.phone = phone;
        this.date = date;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public String getAddress() {
        return Address;
    }

    public String getName() {
        return name;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }
}
