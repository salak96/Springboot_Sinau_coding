package com.sinaucoding.belajar_springboot.controller;
import com.sinaucoding.belajar_springboot.GuruRepository;
import com.sinaucoding.belajar_springboot.entity.Guru;
import com.sinaucoding.belajar_springboot.model.dto.GuruDto;
import com.sinaucoding.belajar_springboot.model.record.GuruRequestRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/guru")
@RequiredArgsConstructor
public class GuruController {
    //interface dipakai untuk akses database.
    private final GuruRepository guruRepository;

    //get all
    @RequestMapping("/list-guru")
    public List<GuruDto> listGuru() {
        //ambil semua data guru dr database
        List<Guru> listGuru = guruRepository.findAll();
        //dirubah menjadi object
        return listGuru.stream()
                .map(guru -> {
                    GuruDto guruDto = new GuruDto();
                    guruDto.setId(guru.getId());
                    guruDto.setNama(guru.getNama());
                    guruDto.setNip(guru.getNip());
                    guruDto.setNomorHp(guru.getNomorHp());
                    guruDto.setAlamat(guru.getAlamat());
                    return guruDto;
                })
                //mengembalikan asilnya dalam bentuk List<GuruDto>.
                .toList();
    }

    //2. GET BY ID
    @GetMapping("/get-guru")
    public ResponseEntity<?> getGuru(@RequestParam Integer id) {
        try {
            var guru = guruRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Data guru tidak tersedia"));

            GuruDto dto = new GuruDto();
            dto.setId(guru.getId());
            dto.setNama(guru.getNama());
            dto.setNip(guru.getNip());
            dto.setNomorHp(guru.getNomorHp());
            dto.setAlamat(guru.getAlamat());

            return ResponseEntity.ok(dto);

        } catch (NoSuchElementException e) {
            // data tidak ditemukan -> 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // error tak terduga -> 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Terjadi kesalahan pada server"));
        }
    }

    //3. add Guru
    @PostMapping("/add-guru")
    public ResponseEntity<String> addGuru(@RequestBody GuruRequestRecord request) {
        try {
            // 1) Validasi input
            if (request.id() != null) {
                return ResponseEntity.badRequest().body("Jangan mengirim id saat create");
            }

            String nama    = request.nama();
            String nip     = (request.nip() == null) ? null : String.valueOf(request.nip());
            String nomorHp = (request.nomorHp() == null) ? null : String.valueOf(request.nomorHp());
            String alamat  = request.alamat();

            if (nama == null || nama.isBlank()) {
                return ResponseEntity.badRequest().body("Nama guru tidak boleh kosong");
            }
            if (nip == null || !nip.matches("\\d{8,20}")) {
                return ResponseEntity.badRequest().body("NIP harus angka 8–20 digit");
            }
            if (nomorHp == null || !nomorHp.matches("\\d{10,15}")) {
                return ResponseEntity.badRequest().body("Nomor HP harus angka 10–15 digit");
            }
            if (alamat == null || alamat.isBlank()) {
                return ResponseEntity.badRequest().body("Alamat tidak boleh kosong");
            }

            // 2) Simpan data
            Guru guru = new Guru();
            guru.setNama(nama);
            guru.setNip(nip);
            guru.setNomorHp(nomorHp);
            guru.setAlamat(alamat);
            guru.setCreatedDate(LocalDateTime.now());
            guru.setModifiedDate(LocalDateTime.now());

            guruRepository.saveAndFlush(guru);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Berhasil menambahkan data Guru baru (id: " + guru.getId() + ")");

        } catch (DataIntegrityViolationException e) {
            // Biasanya karena unique constraint (nip/nomorHp)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("NIP atau Nomor HP sudah digunakan");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan pada server");
        }
    }
    //4.Edit guru by id
    @PostMapping("/edit-guru")
    public ResponseEntity<String> editGuru(@RequestBody GuruRequestRecord request) {
        // Validasi input
        if (request.id() == null) {
            return ResponseEntity.badRequest().body("ID guru wajib diisi!");
        }
        if (request.nama() == null || request.nama().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nama guru tidak boleh kosong!");
        }
        if (request.nip() == null || request.nip().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("NIP guru tidak boleh kosong!");
        }

        // Cek apakah guru ada
        var guru = guruRepository.findById(request.id()).orElse(null);
        if (guru == null) {
            return ResponseEntity
                    .status(404)
                    .body("Data Guru dengan Id " + request.id() + " tidak ditemukan");
        }

        // Update field
        guru.setNama(request.nama().trim());
        guru.setNip(request.nip().trim());
        guru.setNomorHp(request.nomorHp());
        guru.setAlamat(request.alamat());
        guru.setModifiedDate(LocalDateTime.now());

        guruRepository.save(guru);

        return ResponseEntity.ok("Berhasil mengubah data Guru dengan Id " + request.id());
    }

    // 5. Delete by id
    @DeleteMapping("/hapus-guru/{id}")
    public ResponseEntity<String> hapusGuru(@PathVariable Integer id) {
        var guru = guruRepository.findById(id).orElse(null);

        if (guru == null) {
            return ResponseEntity.status(404)
                    .body("Data Guru dengan Id " + id + " tidak ditemukan");
        }

        guruRepository.delete(guru);
        return ResponseEntity.ok("Berhasil menghapus data Guru dengan Id " + id);
    }

}
