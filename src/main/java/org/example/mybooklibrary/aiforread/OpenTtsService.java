package org.example.mybooklibrary.aiforread;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.net.URL;

@Service
public class OpenTtsService {

    private static final String TTS_API_URL = "https://api.streamelements.com/kappa/v2/speech";

    public String generateAudio(String text, String voice, String fileName) {
        try {
            String encodedText = URLEncoder.encode(text, "UTF-8");
            String url = TTS_API_URL + "?voice=" + voice + "&text=" + encodedText;

            // Ensure the audio directory exists
            File audioDir = new File("src/main/resources/audio/");
            if (!audioDir.exists()) {
                audioDir.mkdirs();
            }

            InputStream in = new URL(url).openStream();
            FileOutputStream out = new FileOutputStream(new File(audioDir, fileName));

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            out.close();
            in.close();

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("TTS failed", e);
        }
    }
}
