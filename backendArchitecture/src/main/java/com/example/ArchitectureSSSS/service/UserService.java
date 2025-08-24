package com.example.ArchitectureSSSS.service;

//Görevi: Kayıt ve giriş işlemlerini yapan servis.



import com.example.ArchitectureSSSS.entity.User;
import com.example.ArchitectureSSSS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service    //Spring bunu servis olarak tanır.
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) return false;      // Email zaten kayıtlı mı diye kontrol eder.

        User user = new User();
        user.setEmail(email);
        user.setPassword(rawPassword); // düz şifre — sadece test için!
        user.setCreatedAt(LocalDateTime.now());   //Yeni kullanıcı oluşturur ve şimdiki zamanı verir.

        userRepository.save(user);
        return true;       //Kullanıcıyı kaydeder ve true döner.
    }

    public boolean login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);          //Email ile kullanıcıyı bulur.
        return userOpt.isPresent() && userOpt.get().getPassword().equals(rawPassword);        //Eğer kullanıcı varsa ve şifresi doğruysa true, değilse false döner.
    }
}
