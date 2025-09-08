package com.sinaucoding.belajar_springboot.model.repository;

public record KelasRequestRecord(
        Integer id,
        String nama,
        String deskripsi,
        Integer kapasitas
){}
