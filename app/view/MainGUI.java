package app.view;

import app.controller.AuthController;
import app.controller.RegisterController;
import app.model.repo.RegisteredRepository;
import app.model.repo.StudentRepository;
import app.model.repo.SubjectRepository;
import app.model.service.RegistrationService;
import app.model.util.CsvLoader;

import javax.swing.*;

public class MainGUI {
    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Load Repositories
                var stuRepo = new StudentRepository();
                var subRepo = new SubjectRepository();
                var regRepo = new RegisteredRepository();
                stuRepo.saveAll(CsvLoader.loadStudents("students.csv"));
                subRepo.saveAll(CsvLoader.loadSubjects("subjects.csv"));
                regRepo.saveAll(CsvLoader.loadRegistered("registered.csv"));

                var service = new RegistrationService(stuRepo, subRepo, regRepo);
                var regController = new RegisterController(service);
                var auth = new AuthController();

                // Pre-register some users
                auth.registerUser("anan", "69000001");
                auth.registerUser("bua", "69000002");
                auth.registerUser("chai", "69000003");

                // Show login dialog
                String username = LoginDialog.prompt(null);
                if (username == null) {
                    System.exit(0);
                    return;
                }
                var sidOpt = auth.login(username);
                if (sidOpt.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Login failed for user: " + username + " \nPlease enter username (e.g., anan, bua, chai)",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                    return;
                }
                String studentId = sidOpt.get();

                // Open Main Frame
                var frame = new MainFrame(username, studentId, regController);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setSize(900, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
