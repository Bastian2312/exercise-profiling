package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author muhammad.khadafi
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<StudentCourse> getAllStudentsWithCourses() {
        List<Student> students = studentRepository.findAll();

        List<StudentCourse> allStudentCourses = studentCourseRepository.findAll();

        Map<Long, List<StudentCourse>> studentCoursesMap = allStudentCourses.stream()
                .collect(Collectors.groupingBy(sc -> sc.getStudent().getId()));

        List<StudentCourse> result = new ArrayList<>();

        for (Student student : students) {
            List<StudentCourse> courses = studentCoursesMap.getOrDefault(student.getId(), new ArrayList<>());
            for (StudentCourse course : courses) {
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudent(student);
                studentCourse.setCourse(course.getCourse());
                result.add(studentCourse);
            }
        }
        return result;
    }

    public Optional<Student> findStudentWithHighestGpa() {
        return studentRepository.findAll().stream()
                .max(Comparator.comparingDouble(Student::getGpa));
    }

    public String joinStudentNames() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Student student : students) {
            result.append(student.getName()).append(", ");
        }
        // Remove the last comma and space
        result.setLength(result.length() - 2);

        return result.toString();
    }
}

