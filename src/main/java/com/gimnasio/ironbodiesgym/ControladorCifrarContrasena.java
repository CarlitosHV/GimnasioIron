package com.gimnasio.ironbodiesgym;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class ControladorCifrarContrasena {

    static String value = "0123456789abcdef";

    public static String encript(String text) throws Exception {
        Key aesKey = new SecretKeySpec(IndexApp.key.getBytes(), "AES");
        IvParameterSpec val = new IvParameterSpec(value.getBytes());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, val);

        byte[] encrypted = cipher.doFinal(text.getBytes());

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encrypted) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encrypted.replace("\n", ""));

        Key aesKey = new SecretKeySpec(IndexApp.key.getBytes(), "AES");
        IvParameterSpec val = new IvParameterSpec(value.getBytes());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, val);

        String decrypted = new String(cipher.doFinal(encryptedBytes));

        return decrypted;
    }

}
