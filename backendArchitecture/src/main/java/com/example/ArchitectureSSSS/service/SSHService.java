package com.example.ArchitectureSSSS.service;

//Bu sınıfın görevi: SSH bağlantısı kurmak ve komutu çalıştırmak.


import com.jcraft.jsch.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service   // Bu bir servis sınıfı. Controller'dan çağrılır.
public class SSHService {

    public String executeCommand(String host, String user, String password, String command) {    //Komutun çalıştırılacağı metot. Girdi olarak ip, kullanıcı, şifre, komut alır. Çıktı olarak komut sonucu verir.
        StringBuilder output = new StringBuilder();
        Session session = null;          //Bu, SSH oturumu (session) nesnesidir.
                                         //Yani: "Uzak bilgisayara bağlanacağım, bir oturum açmam gerekiyor" diyorsun.
        ChannelExec channel = null;    //channel, SSH üzerinden **"komut çalıştırma kanalı"**dır.

        try {
            JSch jsch = new JSch();          // SSH kütüphanesi olan `JSch` kullanılıyor.
                                             // - `22`: SSH'nın kullandığı porttur
            session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();                             // Şifreyi ayarlıyoruz ve bağlantı kuruluyor.

            //  StrictHostKeyChecking → Güvenlik ayarıdır, no diyerek ilk defa bağlanmayı kolaylaştırıyoruz.

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            InputStream in = channel.getInputStream();       //  Komutun çıktısını ve hatalarını okumak için iki tane veri akışı (stream) alıyoruz.
            InputStream err = channel.getErrStream();

            channel.connect();

            byte[] buffer = new byte[1024];
            int readCount;

            // Standart çıktıyı oku
            while ((readCount = in.read(buffer)) != -1) {
                output.append(new String(buffer, 0, readCount, StandardCharsets.UTF_8));
            }    //Komuttan gelen çıktı (stdout) okunur ve output değişkenine eklenir.

            // Hata çıktısını da oku (stderr)
            while ((readCount = err.read(buffer)) != -1) {
                output.append(new String(buffer, 0, readCount, StandardCharsets.UTF_8));
            }

            // Komut tamamen bitene kadar bekle
            while (!channel.isClosed()) {
                Thread.sleep(100);  //çok hızlı çalışmasın diye 0.1 saniye uyur
            }

        } catch (Exception e) {    // Herhangi bir hata olursa, program çökmesin diye `try-catch` ile hatayı yakalıyoruz.

            e.printStackTrace();
            return "SSH HATASI: " + e.getMessage();
        } finally {
            if (channel != null) channel.disconnect();                      //Bağlantı her durumda kapatılır. Bellek sızdırmasın, kaynak boşa harcanmasın diye.
            if (session != null) session.disconnect();
        }

        return output.toString().trim();             //Sonuç geri döndürülür.
    }
}
