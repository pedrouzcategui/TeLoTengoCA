package Utils;

import java.util.Base64;

/**
 *
 * @author pedro
 */
public class Cypher {

    // Toma el nombre de archivo y lo convierte en Base 64
    public static String encrypt(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    // Toma el Base64 y lo decrypta
    public static String decrypt(String input) {
        return new String(Base64.getDecoder().decode(input));
    }
}