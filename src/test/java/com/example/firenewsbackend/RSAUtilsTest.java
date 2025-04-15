package com.example.firenewsbackend;

import com.example.firenewsbackend.utils.RSAUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RSAUtilsTest {
    @Test
    public void testKeyPair() throws Exception {
        String original = "HelloWorld123";
        String encrypted = Base64.getEncoder().encodeToString(
                RSAUtils.encryptWithPublicKey(original)
        );

        String decrypted = RSAUtils.decrypt(encrypted);
        assertEquals(original, decrypted, "解密结果应与原始数据一致");
    }

}