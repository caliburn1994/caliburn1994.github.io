package github.caliburn1994.pem;

import github.caliburn1994.KeyBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;

class Pkcs8PemTest {

    @Test
    void writeAndReadPemFile() throws NoSuchAlgorithmException, IOException, URISyntaxException, InvalidKeySpecException {
        var path = Paths.get(getClass().getResource("/").toURI()).getParent().toString();
        path = Paths.get(path, "PKCS#8-RSA-4096.pem").toString();
        var pemFile = new File((path));

        var keyPair = KeyBuilder.generateKeyPair();
        Pkcs8Pem.writePrivateKey(keyPair.getPrivate(),pemFile);
        var privateKey = Pkcs8Pem.readPrivateKeyPem(pemFile);
        Pkcs8Pem.getPublicKey((RSAPrivateCrtKey) privateKey);
    }
}