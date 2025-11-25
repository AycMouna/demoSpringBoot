package com.example.demo;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.example.demo.Student;
import com.example.demo.repository.StudentRepository;

@SpringBootTest
public class DemoApplicationTests {

    @Test
    void contextLoads() {
    }
}

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControllerTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @Order(1)
    void shouldSaveStudent() {
        Student student = new Student();
        student.setName("Charlie");
        student.setAddress("Algeria");

        studentRepository.save(student);

        assertThat(studentRepository.count()).isEqualTo(1L);
    }

    @Test
    @Order(2)
    void shouldFindAllStudents() {
        List<Student> students = studentRepository.findAll();

        assertThat(students).hasSize(1);
        assertThat(students.get(0).getName()).isEqualTo("Charlie");
    }
}


