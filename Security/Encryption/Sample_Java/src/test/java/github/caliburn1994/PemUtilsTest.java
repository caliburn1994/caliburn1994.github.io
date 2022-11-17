package github.caliburn1994;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

class PemUtilsTest {

    @Test
    void writeAndReadPemFile() throws NoSuchAlgorithmException, IOException {
        String pemFile = "my-key.pem";

        var keyPair = KeyBuilder.generateKeyPair();
        PemUtils.writePkcs1PrivateKey(keyPair.getPrivate(), pemFile);
        PemUtils.readFromPkcs1PrivateKey(pemFile);
    }
}