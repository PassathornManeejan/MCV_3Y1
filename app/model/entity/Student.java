package app.model.entity;

import java.time.LocalDate;

public class Student {
    private final String id; // 8 digits, starts with 69
    private final String title;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthdate;
    private final String school;
    private final String email;

    public Student(String id, String title, String firstName, String lastName, LocalDate birthdate, String school, String email) {
        this.id = id; 
        this.title = title; 
        this.firstName = firstName; 
        this.lastName = lastName;
        this.birthdate = birthdate; 
        this.school = school; 
        this.email = email;
    }

    public String getId() { 
        return id; 
    }

    public String getTitle() { 
        return title; 
    }

    public String getFirstName() { 
        return firstName; 
    }

    public String getLastName() { 
        return lastName; 
    }

    public LocalDate getBirthdate() { 
        return birthdate; 
    }

    public String getSchool() { 
        return school; 
    }
    
    public String getEmail() { 
        return email; 
    }

    @Override public String toString() { 
        return id + " " + firstName + " " + lastName; 
    }
}
