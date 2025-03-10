package Utils;

import java.util.Base64;

/**
 *
 * @author pedro
 */
public class Cypher {

    public static String encrypt(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String decrypt(String input) {
        return new String(Base64.getDecoder().decode(input));
    }
}