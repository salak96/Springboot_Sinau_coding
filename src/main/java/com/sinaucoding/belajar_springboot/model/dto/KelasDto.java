package com.sinaucoding.belajar_springboot.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KelasDto {
    Integer id;
    String nama;
    String deskripsi;
    Integer kapasitas;
}
