package app.model.repo;

import app.model.entity.Student;
import java.util.*;

public class StudentRepository {
    private final Map<String, Student> byId = new HashMap<>();

    public void saveAll(Collection<Student> students) { students.forEach(s -> byId.put(s.getId(), s)); }
    public Optional<Student> findById(String id) { return Optional.ofNullable(byId.get(id)); }
    public Collection<Student> findAll() { return byId.values(); }
}
