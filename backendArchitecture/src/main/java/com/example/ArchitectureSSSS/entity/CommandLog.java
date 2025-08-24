package com.example.ArchitectureSSSS.entity;

//Görevi: Her çalıştırılan SSH komutunu veritabanında tutan bir model/sınıf.


import jakarta.persistence.*;  //Bu sınıfın veritabanına karşılık geleceğini belirtmemizi sağlar.
import lombok.Getter;        //Otomatik getter ve setter’lar üretir. Kod yazmamıza gerek kalmaz.
import lombok.Setter;

import java.time.LocalDateTime;  //Tarih ve saat tutmak için.

@Entity
@Getter
@Setter
@Table(name = "command_log")  // Veritabanındaki tablonun adı `"command_log"` olacak.
public class CommandLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //ID otomatik artacak. Veritabanı kendisi belirleyecek.
    private Long id;    //Otomatik artan birincil anahtar (ID).

    private String host;  //Komutun gönderildiği makinenin IP adresi.

    @Column(name = "username")  //SSH kullanıcısı.
    private String username;

    private String command;      //Çalıştırılan komut (örneğin: "ls").

    @Column(columnDefinition = "TEXT")   //Komutun çıktısı. TEXT olması uzun yazılar saklayabilsin diye.
    private String output;

    private LocalDateTime timestamp;  //Ne zaman çalıştırıldığı.

}
