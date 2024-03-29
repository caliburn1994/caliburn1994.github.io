---
Last Modified: 2017-08-06
---

## 定义

**数字签名**<sup>Digital signature</sup>用户验证数据的真实性。在现实生活种，签名者用某种事物作为签名标识，标识某一数据的真实；而验证方需使用某种手段签证该签名标识是否真实。

**数字签名**<sup>Digital signature</sup>有两个阶段：签名<sup>sign</sup>和验证<sup>verify</sup>：

![Image for post](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/image-202105300236.png)

<p style="text-align: center;"><a href="https://medium.com/@meruja/digital-signature-generation-75cc63b7e1b4">图片来源</a></p>

签名方<sup>signer</sup>：

1. 数据<sup>Data</sup>通过散列算法<sup>Hash Algorithm</sup>，得到摘要<sup>Digest</sup>。该过程减少了传输数据的体0积。
2. 使用私钥<sup>Private Key</sup>对摘要<sup>Digest</sup>进行加密，得到数字签名<sup>Digital signature</sup>，保证摘要的正确性。
3. 签名方<sup>signer</sup>将数字签名<sup>Digital signature</sup>和数据<sup>Data</sup>发送给验证方<sup>verifier</sup>

验证方<sup>verifier</sup>:

1. 数据<sup>Data</sup>通过散列算法<sup>Hash Algorithm</sup>，得到摘要<sup>Digest</sup>。
2. 使用公钥<sup>Public Key</sup>对数字签名<sup>Digital signature</sup>解密，得到摘要<sup>Digest</sup>。
3. 将两个摘要进行比较，以此确定正确性。

扩展参考：

- [数字签名是什么？- 阮一峰](http://www.ruanyifeng.com/blog/2011/08/what_is_a_digital_signature.html)
- [图解密码技术](https://www.ituring.com.cn/book/1737) -  9.4.1 直接对消息签名的方法

## 代码片段

### 非PKCS#7

源代码[地址](https://www.mkyong.com/java/java-digital-signatures-example/)

#### 签名

```java
	//The method that signs the data using the private key that is stored in keyFile path
	public byte[] sign(String data, String keyFile) throws InvalidKeyException, Exception{
		Signature rsa = Signature.getInstance("SHA1withRSA");
		rsa.initSign(getPrivate(keyFile));//keyFile=私钥
		rsa.update(data.getBytes());//data=原文
		return rsa.sign();//签名
	}
```

#### 验证

```java
private boolean verifySignature(byte[] data, byte[] signature, String keyFile) throws Exception {
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initVerify(getPublic(keyFile));//keyFil= 公钥
		sig.update(data);//data=加密后的摘要
		return sig.verify(signature);//验证
	}
```
### PKCS#7

#### 签名

需要参数：

1.  证书(可以一个, 也可以多个, 目的, 让对方知道自己的身份)
2.  私钥(签名使用)
3.  原文
4.  摘要以及加密算法


```java
public static CMSSignedData generateCMSSignedData(PrivateKey privateKey, X509Certificate signCert, byte[] inputData, String algo)throws Exception {
		Security.addProvider(new BouncyCastleProvider());//这里我们使用BC的代码库
		CMSTypedData msg = new CMSProcessableByteArray(inputData);//
		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		gen.addSignerInfoGenerator(
				new JcaSimpleSignerInfoGeneratorBuilder()
						.setProvider("BC")
						.build(algo, privateKey, signCert));//算法可选"SHA1withRSA"
		gen.addCertificate(new JcaX509CertificateHolder(signCert));
		//gen.addCertificates(new JcaCertStore(Arrays.asList(signCert))); 添加若干个证书时候用
		return gen.generate(msg, true);//这里的true和false, 是原文是否要放在CMSSignedData里
	}
```

#### 验证并获取原文

需要参数：

1. PKCS#7文档（内含：`SignedData`、`SignerInfo`）
   - `SignedData`包含数据
   - `SignerInfo`包含用于验证的加密摘要

2. 公钥

```java
private void doverify(PublicKey publickey,byte[] data) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		CMSSignedData cms = new CMSSignedData(data);//密文
		//获取签名者所有信息
		SignerInformationStore signers = cms.getSignerInfos();
		Iterator it = signers.getSigners().iterator();
		while (it.hasNext()) {
			SignerInformation signer = (SignerInformation) it.next();
			if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder() .setProvider("BC").build(publickey))) {//共要
				System.out.println("verified");
				System.out.println();
			}

		}
	}
```

获取原文：

```java
CMSSignedData cms=...
CMSTypedData signedContent = cms.getSignedContent();
System.out.println(new String((byte[])signedContent.getContent()));//获得原文数据
```

### 参考：ASN.1格式

```
- Signed Data
signed-data PKCS7-CONTENT-TYPE ::= {SignedData
                                    IDENTIFIED BY  id-signed-data
}

SignedData ::= SEQUENCE {
  version           Version,
  digestAlgorithms  DigestAlgorithmIdentifiers,
  contentInfo       ContentInfo,
  certificates      [0]  CertificateSet OPTIONAL,
  crls              [1]  CertificateRevocationLists OPTIONAL,
  signerInfos       SignerInfos
}

SignerInfo ::= SEQUENCE {
  version                    Version,
  signerIdentifier           SignerIdentifier,
  digestAlgorithm            DigestAlgorithmIdentifier,
  authenticatedAttributes    [0]  Attributes OPTIONAL,
  digestEncryptionAlgorithm  DigestEncryptionAlgorithmIdentifier,
  encryptedDigest            EncryptedDigest,
  unauthenticatedAttributes  [1]  Attributes OPTIONAL
}
```

