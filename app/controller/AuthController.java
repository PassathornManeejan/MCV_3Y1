package app.controller;

import java.util.*;

public class AuthController {
    private final Map<String, String> userToStudentId = new HashMap<>();

    public void registerUser(String username, String studentId) { userToStudentId.put(username, studentId); }
    public Optional<String> login(String username) { return Optional.ofNullable(userToStudentId.get(username)); }
}
