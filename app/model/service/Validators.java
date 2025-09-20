package app.model.service;

import app.model.entity.Student;
import app.model.entity.Subject;
import java.time.*;

public class Validators {
    public static boolean isValidStudentId(String id) {
        return id != null && id.matches("69\\d{6}");
    }
    public static boolean isValidSubjectId(String id) {
        return id != null && id.matches("(0550\\d|9069)\\d{3}");
    }
    public static boolean isAgeAtLeast15(Student s, LocalDate today) {
        if (s.getBirthdate() == null) return false;
        return Period.between(s.getBirthdate(), today).getYears() >= 15;
    }
    public static boolean canEnroll(Subject subj) {
        return !subj.hasCap() || subj.getCurrentEnroll() < subj.getMaxEnroll();
    }
}
