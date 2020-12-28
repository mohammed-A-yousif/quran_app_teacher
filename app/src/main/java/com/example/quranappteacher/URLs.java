package com.example.quranappteacher;

public class URLs {

    public  static String BaseUrl = "http://10.0.2.2:5000/";
//    public  static String BaseUrl = "https://aletgan-api.herokuapp.com/";
//    public  static String BaseUrl = "https://aletgan-api-dev.herokuapp.com/";

    public static String Login = BaseUrl + "teacher_login/";

    public static String GetStudents = BaseUrl + "students/";
    public static String AddTask = BaseUrl + "add_task/";
    public static String GetTask = BaseUrl + "tasks_teacher/";

    public static String AddStudent = BaseUrl + "student_register/";

    public static String getBaseUrl() {
        return BaseUrl;
    }
}
