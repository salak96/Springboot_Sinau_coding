package com.sinaucoding.belajar_springboot.controller;
import com.sinaucoding.belajar_springboot.StudentRepository;
import com.sinaucoding.belajar_springboot.model.record.StudentRequestRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.sinaucoding.belajar_springboot.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sinaucoding.belajar_springboot.model.dto.StudentDto;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository studentRepository;

    //1. GET ALL
    @GetMapping("list-student")
    public List<StudentDto> listStudent() {
        List<Student> listStudent = studentRepository.findAll();
        return listStudent.stream().map(student -> {
            StudentDto studentDto = new StudentDto();
            studentDto.setId(student.getId());
            studentDto.setName(student.getName());
            return studentDto;
        }).toList();
    }
    //2. get by id request param
    @GetMapping("get-student")
    public StudentDto getStudent(@RequestParam Integer id) {
        var student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            StudentDto studentDto = new StudentDto();
            studentDto.setId(student.getId());
            studentDto.setName(student.getName());
            return studentDto;
        }
        throw new RuntimeException("Data student tidak ditemukan");
    }

    //3. add students
    @PostMapping("/add-student")
    public String addStudent(@RequestBody StudentRequestRecord request) {
        var student = new Student();
        student.setName(request.name());
        student.setCreatedDate(LocalDateTime.now());
        student.setModifiedDate(LocalDateTime.now());
        studentRepository.save(student);


        return "Berhasil menambahkan data";
    }

    // 4. Edit Student by id
    @PutMapping("/edit-student")
    public String editStudent(@RequestBody StudentRequestRecord request) {
        var student = studentRepository.findById(request.id()).orElse(null);
        if (student == null) {
            return "Data student dengan Id " + request.id() + " tidak ditemukan";
        } else {
            student.setName(request.name());
            student.setCreatedDate(LocalDateTime.now());
            student.setModifiedDate(LocalDateTime.now());
            studentRepository.save(student);
            return "Berhasil mengubah data";
        }
    }

}