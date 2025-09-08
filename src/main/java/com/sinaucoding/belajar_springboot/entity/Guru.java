package com.sinaucoding.belajar_springboot.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@EqualsAndHashCode()
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "m_guru",
        indexes = {
                @Index(name = "idx_guru_created_date", columnList = "createdDate"),
                @Index(name = "idx_guru_modified_date", columnList = "modifiedDate")
        }
)

public class Guru {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nama Guru tidak boleh kosong")
    @Size(min=3,max = 100, message = "Nama harus 3-100 karakter")
    @Column(nullable = false,length = 100)
    private String nama;

    @NotBlank(message = "NIP tidak boleh kosong")
    @Pattern(regexp = "^\\d{8,20}$", message = "NIP harus angka 8–20 digit")
    @Column(nullable = false,unique = true)
    private String nip;

    @NotBlank(message = "Nomor HP tidak boleh kosong")
    @Pattern(regexp = "^\\d{10,15}$", message = "Nomor HP harus angka 10–15 digit")
    @Column(nullable = false, unique = true, length = 15)
    private String nomorHp;


    @NotBlank(message = "Alamat tidak boleh kosong")
    @Size(max = 255, message = "Alamat maksimal 255 karakter")
    @Column(nullable = false, length = 255)
    private String alamat;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime modifiedDate;



}
