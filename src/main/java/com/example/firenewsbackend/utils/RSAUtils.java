package com.example.firenewsbackend.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtils {
    // 原有的常量，这里暂时保留，后续可删除
    public static final String PUBLIC_KEY = "N73984NKcetnWLnX";

    // AES 密钥
    public static final String AES_KEY = "N73984NKcetnWLnX";
    

    // AES 加密
    public static String encrypt(String data) throws Exception {
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(AES_KEY), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // AES 解密
    public static String decrypt(String encryptedData) throws Exception {
        System.out.println("接收到的加密数据: " + encryptedData);
        System.out.println("使用的密钥: " + AES_KEY);
        System.out.println("加密模式和填充方式: AES/ECB/PKCS5Padding");

        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        String decryptedData = new String(decryptedBytes, StandardCharsets.UTF_8);
        System.out.println("解密后的数据: " + decryptedData);
        return decryptedData;
    }
}

