package com.sinaucoding.belajar_springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@EqualsAndHashCode()
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_mata_pelajaran", indexes = {
        @Index(name = "idx_m_mata_pelajaran_created_date", columnList = "created_date"),
        @Index(name = "idx_m_mata_pelajaran_modified_date", columnList = "modified_date")
})
public class MataPelajaran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //biar auto increment stp nambh 1
    private Integer id;


    @NotBlank(message = "Nama mata pelajaran wajib diisi")
    @Size(max = 20, message = "Nama maksimal 20 karakter")
    @Column(nullable = false, length = 20)
    private String nama;

    @NotBlank(message = "Deskripsi wajib diisi")
    @Size(max = 255, message = "Deskripsi maksimal 255 karakter")
    @Column(nullable = false, length = 255)
    private String deskripsi;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime modifiedDate;

}
