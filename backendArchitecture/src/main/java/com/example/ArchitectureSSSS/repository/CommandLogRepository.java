package com.example.ArchitectureSSSS.repository;

//Görevi: CommandLog nesnelerini veritabanına kaydetmek veya okumak.

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandLogRepository extends JpaRepository<com.example.ArchitectureSSSS.entity.CommandLog, Long> {


    // Bu satır sayesinde:
    //- `save(...)` diyerek veritabanına kayıt yapabilirsin.
    //- `findAll()`, `findById(...)` gibi hazır metotlar gelir.
    //- Bizim sınıfımız `CommandLog`, ID tipi `Long`.

}
