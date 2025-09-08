package com.sinaucoding.belajar_springboot;

import com.sinaucoding.belajar_springboot.entity.Guru;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuruRepository extends JpaRepository<Guru,Integer> {
}
