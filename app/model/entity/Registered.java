package app.model.entity;

public class Registered {
    private final String studentId;
    private final String subjectId;
    private final Grade grade; // null if not graded yet

    public Registered(String studentId, String subjectId, Grade grade) {
        this.studentId = studentId; 
        this.subjectId = subjectId; 
        this.grade = grade;
    }

    public String getStudentId() { 
        return studentId; 
    }

    public String getSubjectId() { 
        return subjectId; 
    }

    public Grade getGrade() { 
        return grade; 
    }
}
