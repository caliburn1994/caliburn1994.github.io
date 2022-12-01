package github.caliburn1994.pem;

import github.caliburn1994.KeyBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

class Pkcs1PemTest {

    @Test
    void writeAndReadPemFile() throws NoSuchAlgorithmException, IOException, URISyntaxException {
        var path = Paths.get(getClass().getResource("/").toURI()).getParent().toString();
        path = Paths.get(path, "PKCS#1-RSA-4096.pem").toString();
        var pemFile = new File((path));

        var keyPair = KeyBuilder.generateKeyPair();
        Pkcs1Pem.writePrivateKey(keyPair.getPrivate(), pemFile);
        Pkcs1Pem.getPrivateKey1(pemFile);
    }

    @Test
    void writeAndReadPemFile2() throws NoSuchAlgorithmException, IOException, URISyntaxException, InvalidKeySpecException {
        var path = Paths.get(getClass().getResource("/").toURI()).getParent().toString();
        path = Paths.get(path, "PKCS#1-RSA-4096.pem").toString();
        var pemFile = new File((path));

        var keyPair = KeyBuilder.generateKeyPair();
        Pkcs1Pem.writePrivateKey(keyPair.getPrivate(), pemFile);
        Pkcs1Pem.getPrivateKey2(pemFile);
    }

}