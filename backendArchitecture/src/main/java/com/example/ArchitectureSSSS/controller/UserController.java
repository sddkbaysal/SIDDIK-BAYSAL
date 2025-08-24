package com.example.ArchitectureSSSS.controller;


//Kullanıcı kayıt ve giriş işlemlerini yapan REST controller.


import com.example.ArchitectureSSSS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")     //- Bu sınıf bir REST API'dir.
                                     // - `/api/users/...` ile başlayan HTTP isteklerini burası dinler.
public class UserController {

    @Autowired
    private UserService userService;

    //UserService adında bir servis sınıfını kullanıyoruz. Bu sınıf, kayıt ve giriş işlemlerini yapacak olan mantığı içeriyor. Spring bunu otomatik enjekte eder.

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> user) {  //POST /api/users/register adresine JSON veri geldiğinde bu metot çalışır.
        String email = user.get("email");          //Kullanıcıdan gelen verileri JSON olarak alır
        String password = user.get("password");       // Gelen JSON’dan email ve password’ü çekiyoruz.
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Email ve şifre gerekli");
        }   //Eğer kullanıcı email ya da şifre yollamazsa, hata döneriz.

        boolean created = userService.register(email, password);         //UserService’teki register() metodunu çağırıyoruz.
        if (!created) {
            return ResponseEntity.badRequest().body("Email zaten kayıtlı");
        }                                                //Eğer kullanıcı daha önce kayıt olmuşsa hata, yoksa "Kayıt başarılı" mesajı döneriz.
        return ResponseEntity.ok("Kayıt başarılı");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> user) {
        String email = user.get("email");                          //  POST /api/users/login için kullanıcı bilgisi alıyoruz.
        String password = user.get("password");
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,                             //Email ya da şifre yoksa, hata döneriz ve JSON olarak başarısızlık mesajı veririz.
                    "message", "Email ve şifre gerekli"
            ));
        }

        boolean success = userService.login(email, password);         //Kullanıcı giriş bilgilerini kontrol ediyoruz.
        if (!success) {
            return ResponseEntity.status(401).body(Map.of(            //Giriş başarısızsa, HTTP 401 (unauthorized) dönüyoruz.
                    "success", false,
                    "message", "Geçersiz giriş bilgileri"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,                              //✅ Giriş başarılıysa, JSON şeklinde başarı mesajı döneriz.
                "message", "Giriş başarılı"
        ));
    }


}
