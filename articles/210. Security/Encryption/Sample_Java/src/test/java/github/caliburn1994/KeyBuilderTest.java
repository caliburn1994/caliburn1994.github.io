package github.caliburn1994;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

class KeyBuilderTest {

    @Test
    void testPkcs1PrivateKey() throws NoSuchAlgorithmException {
        KeyPair pair = KeyBuilder.generateKeyPair();
        pair.getPrivate(); // private key
        pair.getPublic();  // public key
    }
}