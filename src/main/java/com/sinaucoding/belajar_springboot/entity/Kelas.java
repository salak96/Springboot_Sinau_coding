package com.sinaucoding.belajar_springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode()
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_kelas", indexes = {
        @Index(name = "idx_m_kelas_created_date", columnList = "created_date"),
        @Index(name = "idx_m_kelas_modified_date", columnList = "modified_date")
})
public class Kelas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Integer id;

    @NotBlank(message = "Nama kelas tidak boleh kosong")
    @Size(min = 3, max = 50, message = "Nama kelas harus antara 3 - 50 karakter")
    @Column(nullable = false)
    private String nama;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Deskripsi harus antara 3 - 100 karakter")
    @Column(nullable = false, unique = true)
    private String deskripsi;

    @NotNull(message = "Kapasitas wajib diisi")
    @Min(value = 1, message = "Kapasitas minimal 1")
    @Column(nullable = false)
    private Integer kapasitas;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedDate;
}
