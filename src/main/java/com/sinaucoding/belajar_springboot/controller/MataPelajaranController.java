package com.sinaucoding.belajar_springboot.controller;

import com.sinaucoding.belajar_springboot.MataPelajaranRepository;
import com.sinaucoding.belajar_springboot.entity.MataPelajaran;
import com.sinaucoding.belajar_springboot.model.dto.MataPelajaranDto;
import com.sinaucoding.belajar_springboot.model.repository.MataPelajaranRequestRecord;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/matapelajaran")
@RequiredArgsConstructor
public class MataPelajaranController {

    private final MataPelajaranRepository mataPelajaranRepository;

    // Get All
    @GetMapping("/list-mapel")
    public List<MataPelajaranDto> findAll() {
        List<MataPelajaran> listMataPelajaran = mataPelajaranRepository.findAll();
        return listMataPelajaran.stream()
                .map(mataPelajaran -> {
                    MataPelajaranDto mapelDto = new MataPelajaranDto();
                    mapelDto.setId(mataPelajaran.getId());
                    mapelDto.setNama(mataPelajaran.getNama());
                    mapelDto.setDeskripsi(mataPelajaran.getDeskripsi());
                    return mapelDto;
                })
                .toList();
    }

    // Get ByiD
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest().body("ID harus ada dan lebih besar dari 0");
            }
            var mataPelajaran = mataPelajaranRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Mata Pelajaran dengan ID " + id + " tidak ada"));

            MataPelajaranDto dto = new MataPelajaranDto();
            dto.setId(mataPelajaran.getId());
            dto.setNama(mataPelajaran.getNama());
            dto.setDeskripsi(mataPelajaran.getDeskripsi());

            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan pada server: " + e.getMessage());
        }
    }



    //POST
    @PostMapping("/post-mapel")
    public ResponseEntity<?> createMapel(@Valid @RequestBody MataPelajaranRequestRecord request) {
        try {
            // Validasi: id tidak boleh dikirim
            if (request.id() != null) {
                return ResponseEntity.badRequest().body("Jangan mengirim id saat create");
            }

            // Validasi: nama wajib diisi
            if (request.nama() == null || request.nama().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nama mata pelajaran wajib diisi");
            }

            MataPelajaran mataPelajaran = new MataPelajaran();
            mataPelajaran.setNama(request.nama().trim());
            mataPelajaran.setDeskripsi(
                    request.deskripsi() != null && !request.deskripsi().trim().isEmpty()
                            ? request.deskripsi().trim()
                            : null
            );
            mataPelajaran.setCreatedDate(LocalDateTime.now());
            mataPelajaran.setModifiedDate(LocalDateTime.now());


            mataPelajaranRepository.save(mataPelajaran);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Berhasil menambahkan Mata Pelajaran baru");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Mata Pelajaran sudah ada / duplikat");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan pada server: " + e.getMessage());
        }
    }

    //UPDATE
    @PutMapping("/update-mapel/{id}")
    public ResponseEntity<?> updateMapel(
            @PathVariable int id,
            @Valid @RequestBody MataPelajaranRequestRecord request) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest().body("ID harus lebih besar dari 0");
            }

            MataPelajaran mataPelajaran = mataPelajaranRepository.findById(id)
                    .orElseThrow(() ->
                            new NoSuchElementException("Mata Pelajaran dengan ID " + id + " tidak ada"));

            // Validasi nama & deskripsi
            if (request.nama() == null || request.nama().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nama mata pelajaran wajib diisi");
            }
            if (request.deskripsi() == null || request.deskripsi().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Deskripsi mata pelajaran wajib diisi");
            }
            // Update field
            mataPelajaran.setNama(request.nama().trim());
            mataPelajaran.setDeskripsi(
                    request.deskripsi() != null
                            ? request.deskripsi().trim()
                            : mataPelajaran.getDeskripsi()
            );
            mataPelajaran.setModifiedDate(LocalDateTime.now());

            mataPelajaranRepository.save(mataPelajaran);

            return ResponseEntity.ok("Mata Pelajaran dengan ID " + id + " berhasil diupdate");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan pada server: " + e.getMessage());
        }
    }

    // DELETE by id
    @DeleteMapping("/delete-mapel/{id}")
    public ResponseEntity<?> deleteMapel(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest().body("ID harus lebih besar dari 0");
            }

            MataPelajaran mataPelajaran = mataPelajaranRepository.findById(id)
                    .orElseThrow(() ->
                            new NoSuchElementException("Mata Pelajaran dengan ID " + id + " tidak ada"));

            mataPelajaranRepository.delete(mataPelajaran);

            return ResponseEntity.ok("Mata Pelajaran dengan ID " + id + " berhasil dihapus");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan pada server: " + e.getMessage());
        }
    }
}
