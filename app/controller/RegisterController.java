package app.controller;

import app.model.entity.Registered;
import app.model.entity.Student;
import app.model.entity.Subject;
import app.model.service.RegistrationService;


import java.time.LocalDate;
import java.util.*;

public class RegisterController {
    private final RegistrationService service;

    public RegisterController(RegistrationService service) { this.service = service; }

    public List<Subject> showAvailableSubjects(String studentId) { return service.listEligibleSubjects(studentId); }
    public Map<Subject,String> showLockedSubjects(String studentId) { return service.listLockedSubjects(studentId); }
    public Optional<Subject> getSubjectDetail(String subjectId) { return service.getSubjectDetail(subjectId); }
    public RegistrationService.Result register(String studentId, String subjectId) { return service.register(studentId, subjectId, LocalDate.now()); }

    // Profile helpers
    public List<Registered> getRegistered(String studentId) {
        return service.getRegisteredRepository().findByStudent(studentId);
    }
    public int totalRegisteredCredits(String studentId) {
        var regs = getRegistered(studentId);
        int sum = 0;
        var subjRepo = service.getSubjectRepository();
        for (var r : regs) {
            var sOpt = subjRepo.findById(r.getSubjectId());
            if (sOpt.isPresent()) sum += sOpt.get().getCredits();
        }
        return sum;
    }
    public Optional<Student> getStudent(String studentId) {
        return Optional.ofNullable(service.getStudentRepository().findById(studentId).orElse(null));
    }
}
