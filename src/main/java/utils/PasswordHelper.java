package utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordHelper {

    public static boolean checkPassword(String submittedPassword, String hashedPassword) throws Exception {
        Base64.Encoder enc = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        String stored[] = hashedPassword.split("\\$");
        KeySpec spec1 = new PBEKeySpec(submittedPassword.toCharArray(), decoder.decode(stored[0]), 65536, 128);
        SecretKeyFactory f1 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash1 = f1.generateSecret(spec1).getEncoded();
        return stored[1].equals(enc.encodeToString(hash1));
    }

    public static String goHashThePassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            Base64.Encoder enc = Base64.getEncoder();

            return enc.encodeToString(salt) + "$"+ enc.encodeToString(hash);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
