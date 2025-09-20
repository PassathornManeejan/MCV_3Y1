package app.model.util;

// import app.model.*;
import app.model.entity.Grade;
import app.model.entity.Registered;
import app.model.entity.Student;
import app.model.entity.Subject;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class CsvLoader {
    public static List<Student> loadStudents(String path) throws IOException {
        List<Student> list = new ArrayList<>();
        try (var br = Files.newBufferedReader(Path.of(path))) {
            String line; boolean first=true;
            while ((line = br.readLine()) != null) {
                if (first) { first=false; continue; }
                String[] t = line.split(",");
                list.add(new Student(t[0], t[1], t[2], t[3], LocalDate.parse(t[4]), t[5], t[6]));
            }
        }
        return list;
    }

    public static List<Subject> loadSubjects(String path) throws IOException {
        List<Subject> list = new ArrayList<>();
        try (var br = Files.newBufferedReader(Path.of(path))) {
            String line; boolean first=true;
            while ((line = br.readLine()) != null) {
                if (first) { first=false; continue; }
                String[] t = line.split(",");
                String prereq = t[4].isBlank()? null : t[4];
                list.add(new Subject(t[0], t[1], Integer.parseInt(t[2]), t[3], prereq, Integer.parseInt(t[5]), Integer.parseInt(t[6])));
            }
        }
        return list;
    }

    public static List<Registered> loadRegistered(String path) throws IOException {
        List<Registered> list = new ArrayList<>();
        try (var br = Files.newBufferedReader(Path.of(path))) {
            String line; boolean first=true;
            while ((line = br.readLine()) != null) {
                if (first) { first=false; continue; }
                String[] t = line.split(",");
                Grade g = Grade.fromString(t.length>2? t[2] : null);
                list.add(new Registered(t[0], t[1], g));
            }
        }
        return list;
    }
}
