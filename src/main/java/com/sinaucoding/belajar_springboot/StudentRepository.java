package com.sinaucoding.belajar_springboot;
import com.sinaucoding.belajar_springboot.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Integer> {

}
