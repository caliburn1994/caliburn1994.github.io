package github.caliburn1994;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyBuilder {

    /**
     * generate a pair of keys
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // 4096 is recommended
        var keyLength = 4096;
        var keyGen=KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keyLength);
        return keyGen.generateKeyPair();
    }


    /**
     * PKCS#1 RSA private key file
     */
    public static KeyPair readFromPemFile(String privateKeyPemFile) throws IOException {
        return PemUtils.readFromPkcs1PrivateKey(privateKeyPemFile);
    }




}
