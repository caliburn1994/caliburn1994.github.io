package github.caliburn1994.pem;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;

public class Pkcs8Pem {

    static {
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
    }

    public static PrivateKey getPrivateKey(File pemFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var pem = FileUtils.readFileToString(pemFile, Charset.defaultCharset());

        Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*-*$.*");
        String encoded = parse.matcher(pem).replaceFirst("$1");
        byte[] bytes = Base64.getMimeDecoder().decode(encoded);

        var spec = new PKCS8EncodedKeySpec(bytes);
        var factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(RSAPrivateCrtKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var spec = new RSAPublicKeySpec(privateKey.getModulus(), privateKey.getPublicExponent());
        var factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }



    public static void writePrivateKey(PrivateKey privateKey, File fileName) throws IOException {
        try (var op=new OutputStreamWriter(FileUtils.openOutputStream(fileName))){
            try (JcaPEMWriter w = new JcaPEMWriter(op)) {
                w.writeObject(new JcaPKCS8Generator(privateKey, null));
            }
        }
    }
}
