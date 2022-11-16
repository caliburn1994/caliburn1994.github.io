package github.caliburn1994;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    @Test
    void testGenerateKeyPair() throws NoSuchAlgorithmException {
        KeyPair pair = KeyGenerator.generateKeyPair();
        pair.getPrivate(); // private key
        pair.getPublic();  // public key
    }
}