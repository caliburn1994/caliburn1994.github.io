package github.caliburn1994;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyPair;

public class PemUtils {

    /**
     *
     *
     * @param privateKey
     * @param fileName
     * @throws IOException
     */
    public static void writePrivateKeyAsPemFile(Object privateKey, String fileName) throws IOException {
        try (var op=new OutputStreamWriter(FileUtils.openOutputStream(new File(fileName)))){
            JcaPEMWriter pemWriter = new JcaPEMWriter(op);
            pemWriter.writeObject(privateKey);
            pemWriter.close();
        }
    }

    /**
     * 支持得类型如下：
     *         parsers.put(TYPE_CERTIFICATE_REQUEST, new PKCS10CertificationRequestParser());
     *         parsers.put(TYPE_NEW_CERTIFICATE_REQUEST, new PKCS10CertificationRequestParser());
     *         parsers.put(TYPE_CERTIFICATE, new X509CertificateParser());
     *         parsers.put(TYPE_TRUSTED_CERTIFICATE, new X509TrustedCertificateParser());
     *         parsers.put(TYPE_X509_CERTIFICATE, new X509CertificateParser());
     *         parsers.put(TYPE_X509_CRL, new X509CRLParser());
     *         parsers.put(TYPE_PKCS7, new PKCS7Parser());
     *         parsers.put(TYPE_CMS, new PKCS7Parser());
     *         parsers.put(TYPE_ATTRIBUTE_CERTIFICATE, new X509AttributeCertificateParser());
     *         parsers.put(TYPE_EC_PARAMETERS, new ECCurveParamsParser());
     *         parsers.put(TYPE_PUBLIC_KEY, new PublicKeyParser());
     *         parsers.put(TYPE_RSA_PUBLIC_KEY, new RSAPublicKeyParser());
     *         parsers.put(TYPE_RSA_PRIVATE_KEY, new KeyPairParser(new RSAKeyPairParser()));
     *         parsers.put(TYPE_DSA_PRIVATE_KEY, new KeyPairParser(new DSAKeyPairParser()));
     *         parsers.put(TYPE_EC_PRIVATE_KEY, new KeyPairParser(new ECDSAKeyPairParser()));
     *         parsers.put(TYPE_ENCRYPTED_PRIVATE_KEY, new EncryptedPrivateKeyParser());
     *         parsers.put(TYPE_PRIVATE_KEY, new PrivateKeyParser());
     *
     * @param pemFile pem file stored a private key
     * @return
     * @throws IOException
     */
    public static KeyPair readKeyPairFromPrivateKeyPemFile(String pemFile) throws IOException {
        try (var reader=new FileReader(new File(pemFile))){
            var parser = new PEMParser(reader);
            var pemKeyPair = (PEMKeyPair) parser.readObject();
            return new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        }

    }

}
