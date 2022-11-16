package github.caliburn1994;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {

    /**
     * generate a pair of keys
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // 3072 is recommended
        var keyLength = 4096;
        var keyGen=KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keyLength);
        return keyGen.generateKeyPair();
    }

}
