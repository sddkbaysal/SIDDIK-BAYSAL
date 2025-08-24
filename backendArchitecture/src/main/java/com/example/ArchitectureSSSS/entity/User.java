package com.example.ArchitectureSSSS.entity;

// Görevi: Kullanıcı verilerini temsil eden veritabanı tablosu.



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data                    //Lombok ile getter, setter, toString, vs. otomatik oluşur.
@Entity                  //Bu sınıf veritabanında bir tabloya karşılık gelir
@Table(name = "users")   //Tablo adı users olur.
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;         //Otomatik artan birincil anahtar

    @Column(nullable = false, unique = true)
    private String email;    //Email boş olamaz ve benzersiz olmalı.

    @Column(nullable = false)
    private String password;    //🔐 Şifre de zorunlu.

    // Opsiyonel alanlar için nullable = true
    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String surname;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;                  //Kayıt zamanı (boş olamaz).
}
