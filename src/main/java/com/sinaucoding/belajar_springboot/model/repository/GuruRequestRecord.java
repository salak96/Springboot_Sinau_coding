package com.sinaucoding.belajar_springboot.model.repository;

public record GuruRequestRecord(
        Integer id,
        String nama,
        String nip,
        String nomorHp,
        String alamat
){}
