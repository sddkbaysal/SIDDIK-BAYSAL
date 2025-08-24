package com.example.ArchitectureSSSS.controller;

//Bu sınıfın görevi:
// Kullanıcıdan gelen komutu alıp, SSH üzerinden çalıştırmak ve sonucu veritabanına kaydetmek.


import com.example.ArchitectureSSSS.entity.CommandLog;                   //- `CommandLog`: Komut geçmişini temsil eden sınıf.
import com.example.ArchitectureSSSS.repository.CommandLogRepository;     //- `CommandLogRepository`: Komutları veritabanına kaydetmek için kullanılır.
import com.example.ArchitectureSSSS.service.SSHService;                  //- `SSHService`: SSH bağlantısını ve komut çalıştırmayı yapan servis.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;                          //- `ResponseEntity`: HTTP cevabı için.
import org.springframework.web.bind.annotation.*;                        //- `@RestController`, `@RequestMapping`, `@PostMapping`, `@RequestBody`: Spring’in web isteklerini yakalamak için kullandığı özel işaretler.


import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/ssh")

//  Bu sınıf, bir web isteğini dinleyecek anlamına gelir.
//  api/ssh adresine gelen istekleri dinler.


public class SSHController {

    private final CommandLogRepository commandLogRepository;  //Veritabanına komut kayıtlarını eklemek için.
    private final SSHService sshService;                      //SSH ile uzak sunucuya bağlanıp komut çalıştırmak için.

    @Autowired  // 🛠 Spring framework, bu sınıfı **başlatırken**, gerekli servisleri otomatik olarak getiriyor. Yani sen elle oluşturmak zorunda kalmıyorsun.
    public SSHController(CommandLogRepository commandLogRepository, SSHService sshService) {
        this.commandLogRepository = commandLogRepository;
        this.sshService = sshService;
    }
                                                   // Bu metod, bir HTTP POST isteği alır. Yani dışardan birisi /api/ssh/execute adresine bir komut gönderdiğinde bu çalışır.
    @PostMapping("/execute")
    public ResponseEntity<?> executeCommand(@RequestBody Map<String, String> payload) {  //JSON formatında gelen veriyi bir harita gibi alır.
        String host = payload.get("ip");
        String user = payload.get("user");             //JSON’dan gelen değerleri tek tek değişkenlere alıyoruz.
        String password = payload.get("password");
        String command = payload.get("command");

        if (host == null || user == null || password == null || command == null) {
            return ResponseEntity.badRequest().body("Eksik parametre");            //Kullanıcı eksik bilgi gönderirse, hata döneriz: "Eksik parametre"
        }

        String output = sshService.executeCommand(host, user, password, command);  //🚀 SSH bağlantısını açıp, komutu çalıştırmak için `sshService` çağrılıyor.


        System.out.println("Komut çıktısı: \n" + output); // Debug için loglama      🖨 Konsola çıktıyı basıyoruz. Bu sadece geliştirici için, uygulamanın içinde görünmez.



        CommandLog log = new CommandLog();
        log.setHost(host);
        log.setUsername(user);
        log.setCommand(command);
        log.setOutput(output);
        log.setTimestamp(LocalDateTime.now());

        commandLogRepository.save(log);              // Yeni bir komut kaydı oluşturuyoruz:
                                                     //- IP, kullanıcı adı, komut, çıktı ve zaman bilgisi ekleniyor.
                                                     //- `save(...)` metodu ile veritabanına **kayıt** yapıyoruz

        return ResponseEntity.ok(output);            // Komutun çıktısını kullanıcıya geri döneriz. HTTP 200 (başarılı) ile birlikte.
    }
}
