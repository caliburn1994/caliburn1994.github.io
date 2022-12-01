package github.caliburn1994;

import github.caliburn1994.pem.Pkcs1Pem;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;

public class KeyBuilder {

    {
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
    }

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
    public static KeyPair readFromPem(File pemFile) throws IOException {
        return Pkcs1Pem.getPrivateKey1(pemFile);
    }



}
