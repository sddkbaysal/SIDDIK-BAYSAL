package com.example.ArchitectureSSSS.repository;

//Görevi: Kullanıcıları veritabanına kaydetmek, sorgulamak.


import com.example.ArchitectureSSSS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
                                                      //JpaRepository sayesinde:
                                                      //save(...), findById(...), findAll() gibi hazır veritabanı işlemleri gelir.
    boolean existsByEmail(String email);              //Email zaten var mı kontrol eder.
    Optional<User> findByEmail(String email);         //Email ile kullanıcıyı getirir.
}
