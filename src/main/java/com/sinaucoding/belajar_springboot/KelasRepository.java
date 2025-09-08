package com.sinaucoding.belajar_springboot;

import com.sinaucoding.belajar_springboot.entity.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KelasRepository extends JpaRepository<Kelas,Integer> {
}
