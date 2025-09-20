package app.model.repo;

import app.model.entity.Subject;
import java.util.*;

public class SubjectRepository {
    private final Map<String, Subject> byId = new HashMap<>();

    public void saveAll(Collection<Subject> subjects) { subjects.forEach(s -> byId.put(s.getId(), s)); }
    public Optional<Subject> findById(String id) { return Optional.ofNullable(byId.get(id)); }
    public Collection<Subject> findAll() { return byId.values(); }
}
