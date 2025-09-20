package app.model.repo;

import app.model.entity.Registered;
import java.util.*;

public class RegisteredRepository {
    private final List<Registered> rows = new ArrayList<>();

    public void save(Registered r) { rows.add(r); }
    public void saveAll(Collection<Registered> list) { rows.addAll(list); }

    public boolean exists(String studentId, String subjectId) {
        return rows.stream().anyMatch(r -> r.getStudentId().equals(studentId) && r.getSubjectId().equals(subjectId));
    }

    public Optional<Registered> findPassed(String studentId, String subjectId) {
        return rows.stream().filter(r -> r.getStudentId().equals(studentId) && r.getSubjectId().equals(subjectId) && r.getGrade() != null).findFirst();
    }

    public List<Registered> findByStudent(String studentId) {
        return rows.stream().filter(r -> r.getStudentId().equals(studentId)).toList();
    }
}
