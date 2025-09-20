package app.model.service;

// import app.model.*;
import app.model.entity.Registered;
import app.model.entity.Subject;
import app.model.repo.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class RegistrationService {
    
    private final StudentRepository students;
    private final SubjectRepository subjects;
    private final RegisteredRepository regs;

    public RegistrationService(StudentRepository students, SubjectRepository subjects, RegisteredRepository regs) {
        this.students = students; this.subjects = subjects; this.regs = regs;
    }

    // Back-compat method name; now returns only eligible subjects
    public List<Subject> listAvailableSubjects(String studentId) {
        return listEligibleSubjects(studentId);
    }

    private boolean prereqOk(String studentId, Subject subj) {
        if (subj.getPrereqId() == null) return true;
        return regs.findPassed(studentId, subj.getPrereqId()).isPresent();
    }

    private Optional<String> lockReason(String studentId, Subject subj) {
        if (!prereqOk(studentId, subj)) {
            return Optional.of("Prerequisite not satisfied: " + subj.getPrereqId());
        }
        if (!Validators.canEnroll(subj)) {
            return Optional.of("Subject is full");
        }
        return Optional.empty();
    }

    public List<Subject> listEligibleSubjects(String studentId) {
        Set<String> owned = regs.findByStudent(studentId).stream().map(Registered::getSubjectId).collect(Collectors.toSet());
        return subjects.findAll().stream()
                .filter(s -> !owned.contains(s.getId()))
                .filter(s -> lockReason(studentId, s).isEmpty())
                .toList();
    }

    public Map<Subject, String> listLockedSubjects(String studentId) {
        Set<String> owned = regs.findByStudent(studentId).stream().map(Registered::getSubjectId).collect(Collectors.toSet());
        Map<Subject, String> result = new LinkedHashMap<>();
        for (Subject s : subjects.findAll()) {
            if (owned.contains(s.getId())) continue;
            var reason = lockReason(studentId, s);
            reason.ifPresent(r -> result.put(s, r));
        }
        return result;
    }

    public Optional<Subject> getSubjectDetail(String subjectId) {
        return subjects.findById(subjectId);
    }

    public record Result(boolean ok, String message) {}

    public Result register(String studentId, String subjectId, LocalDate today) {
        var sOpt = students.findById(studentId);
        if (sOpt.isEmpty()) return new Result(false, "Student not found");
        var st = sOpt.get();
        if (!Validators.isAgeAtLeast15(st, today)) return new Result(false, "Age must be >= 15");

        var subjOpt = subjects.findById(subjectId);
        if (subjOpt.isEmpty()) return new Result(false, "Subject not found");
        var subj = subjOpt.get();

        if (regs.exists(studentId, subjectId)) return new Result(false, "Already registered");

        // Prerequisite check
        if (subj.getPrereqId() != null) {
            if (regs.findPassed(studentId, subj.getPrereqId()).isEmpty()) {
                return new Result(false, "Prerequisite not satisfied: " + subj.getPrereqId());
            }
        }
        // Capacity check
        if (!Validators.canEnroll(subj)) return new Result(false, "Subject is full");

        // Perform registration (no grade yet)
        regs.save(new Registered(studentId, subjectId, null));
        subj.incEnroll();
        return new Result(true, "Registered successfully");
    }

    // Expose repositories for read operations (used by profile view)
    public RegisteredRepository getRegisteredRepository() { return regs; }
    public SubjectRepository getSubjectRepository() { return subjects; }
}