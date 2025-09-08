package com.sinaucoding.belajar_springboot;

import com.sinaucoding.belajar_springboot.entity.MataPelajaran;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MataPelajaranRepository extends JpaRepository<MataPelajaran, Integer> {
}