package github.caliburn1994.pem;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * Load
 */
public class Pkcs1Pem {

    public static void writePrivateKey(Object privateKey, File fileName) throws IOException {
        try (var op=new OutputStreamWriter(FileUtils.openOutputStream(fileName))){
            try (JcaPEMWriter w = new JcaPEMWriter(op)) {
                w.writeObject(privateKey);
            }
        }
    }

    /**
     * parsers support these things:
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
     * ref: https://stackoverflow.com/questions/11787571/how-to-read-pem-file-to-get-private-and-public-key
     *
     * @param pemFile pem file stored a private key
     */
    public static KeyPair getPrivateKey1(File pemFile) throws IOException {
        try (var reader=new FileReader(pemFile)){
            var parser = new PEMParser(reader);
            var pemKeyPair = (PEMKeyPair) parser.readObject();
            return new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        }
    }


    public static PrivateKey getPrivateKey2(File pemFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var pem = FileUtils.readFileToString(pemFile, Charset.defaultCharset());

        Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*-*$.*");
        String encoded = parse.matcher(pem).replaceFirst("$1");
        byte[] decoded = Base64.getMimeDecoder().decode(encoded);

        /* Add PKCS#8 formatting */
        var v = new ASN1EncodableVector();
        v.add(new ASN1Integer(0));
        var v2 = new ASN1EncodableVector();
        v2.add(new ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()));
        v2.add(DERNull.INSTANCE);
        v.add(new DERSequence(v2));
        v.add(new DEROctetString(decoded));
        decoded = new DERSequence(v).getEncoded("DER");

        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

}
