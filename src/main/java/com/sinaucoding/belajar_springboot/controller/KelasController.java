package com.sinaucoding.belajar_springboot.controller;

import com.sinaucoding.belajar_springboot.KelasRepository;
import com.sinaucoding.belajar_springboot.entity.Kelas;
import com.sinaucoding.belajar_springboot.model.dto.KelasDto;
import com.sinaucoding.belajar_springboot.model.record.KelasRequestRecord;
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
@RequestMapping("/kelas")
@RequiredArgsConstructor
public class KelasController {
    private final KelasRepository kelasRepository;

    // GET ALL
    @GetMapping("/list-kelas")
    public List<KelasDto> findAll() {
        List<Kelas> listKelas = kelasRepository.findAll();
        return listKelas.stream()
                .map(kelas -> {
                    KelasDto kelasDto = new KelasDto();
                    kelasDto.setId(kelas.getId());
                    kelasDto.setNama(kelas.getNama());
                    kelasDto.setDeskripsi(kelas.getDeskripsi());
                    kelasDto.setKapasitas(kelas.getKapasitas());
                    return kelasDto;
                })
                .toList();
    }

    // 2. GET BY ID
    @GetMapping("/get-kelas")
    public ResponseEntity<?> getKelas(@RequestParam Integer id) {
        try {
            var kelas = kelasRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Data kelas tidak ditemukan"));

            KelasDto dto = new KelasDto();
            dto.setId(kelas.getId());
            dto.setNama(kelas.getNama());
            dto.setDeskripsi(kelas.getDeskripsi());
            dto.setKapasitas(kelas.getKapasitas());

            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kelas dengan ID " + id + " tidak ada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan pada server");
        }
    }

    //  3. CREATE
    @PostMapping("/post-kelas")
    public ResponseEntity<String> createKelas(@Valid @RequestBody KelasRequestRecord request) {
        try {
            if (request.id() != null) {
                return ResponseEntity.badRequest().body("Jangan mengirim id saat create");
            }

            Kelas kelas = new Kelas();
            kelas.setNama(request.nama().trim());
            kelas.setDeskripsi(request.deskripsi() != null ? request.deskripsi().trim() : null);
            kelas.setKapasitas(request.kapasitas());
            kelas.setCreatedDate(LocalDateTime.now());
            kelas.setModifiedDate(LocalDateTime.now());
            kelasRepository.save(kelas);

            return ResponseEntity.ok("Berhasil menambahkan kelas baru");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Nama kelas sudah digunakan");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan pada server");
        }
    }

    //  4. UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<String> updateKelas(
            @PathVariable Integer id,
            @Valid @RequestBody KelasRequestRecord request) {

        return kelasRepository.findById(id).map(kelas -> {
            if (request.deskripsi() == null || request.deskripsi().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Deskripsi tidak boleh kosong!");
            }
            if (request.nama() == null || request.nama().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nama tidak boleh kosong!");
            }
            kelas.setNama(request.nama().trim());
            kelas.setDeskripsi(request.deskripsi().trim());
            kelas.setKapasitas(request.kapasitas());

            kelasRepository.save(kelas);

            return ResponseEntity.ok("Kelas berhasil diupdate!");
        }).orElse(ResponseEntity.badRequest()
                .body("Kelas dengan ID " + id + " tidak ditemukan!"));
    }

    //  DELETE BY id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> hapusKelas(@PathVariable Integer id) {
        try {
            var kelas = kelasRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Kelas tidak ditemukan"));

            kelasRepository.delete(kelas);
            return ResponseEntity.ok("Berhasil menghapus kelas dengan Id " + id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kelas dengan Id " + id + " tidak ditemukan");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan pada server");
        }
    }
}
