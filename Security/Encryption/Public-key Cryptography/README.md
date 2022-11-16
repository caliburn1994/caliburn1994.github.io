---
Last Modified: 2022-11-12
---



## 简介 Introduction

非对称加密 (asymmetric cryptography)，又叫做 公钥加密 (Public-key cryptography)。该加密有两个密钥：公钥、私钥。

通常而言，发送者使用公钥加密，接收者使用私钥进行解密。

![Image result for asymmetric cryptography](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/250px-Public_key_encryption.svg.png)

## 算法 Algorithm

最常见的非对称加密算法是 RSA，除此以外还有 Elgamal、Rabin方式、椭圆曲线密码等加密算法。[^1]

而针对如何在程序代码中时候这些算法，就有了 [PKCS](https://en.wikipedia.org/wiki/PKCS) 。有的密钥文件是根据 PKCS#1 生成的，如果程序使用库的是 PKCS#8，那么程序将文法识别该密钥文件。

下文是 PKCS#1 全称RSA密码编译标准 (RSA Cryptography Standard)， 的PEM文件内容格式 : 

```
-----BEGIN RSA PRIVATE KEY-----
BASE64 ENCODED DATA
-----END RSA PRIVATE KEY-----
```

PKCS#8 全称私钥消息表示标准 (Private-Key Information Syntax Standard)，个是 PKCS#8 的PEM文件内容格式 :

```
-----BEGIN PRIVATE KEY-----
BASE64 ENCODED DATA
-----END PRIVATE KEY-----
```

```
-----BEGIN ENCRYPTED PRIVATE KEY-----
BASE64 ENCODED DATA
-----END ENCRYPTED PRIVATE KEY-----
```

更多相关的详情，可以查看 [Stackoverflow的这个回答](https://stackoverflow.com/questions/48958304/pkcs1-and-pkcs8-format-for-rsa-private-key)、[ASN.1 key structures in DER and PEM](https://polarssl.org/kb/cryptography/asn1-key-structures-in-der-and-pem/)。

针对最常使用的 RSA算法，最常被使用的标准是 PKCS#1、PKCS#8。其中 PKCS#1 是专门针对 RSA算法的，而 PKCS#8 则为通用标准。



`openssl rsa` 命令行文档里是这样描述的：

> The **rsa** command processes RSA keys. They can be converted between various forms and their components printed out. **Note** this command uses the traditional SSLeay compatible format for private key encryption: newer applications should use the more secure PKCS#8 format using the **pkcs8** utility.

一般而言，推荐使用 PKCS#8。



## 数据结构和存储文件 Data Structure and File

ASN.1 抽象地定义了数据该怎么定义，但没有指定特定地编码方式。这意味着，我们需要一种表现方式（编码方式）去表示用 ASN.1 定义的数据，这种表现方式有：DER (8进制字节)、XER (XML)、JSON 等等。[^3]

而这当中，DER 格式是最常用。可以理解为，在代码里数据结构是以 <u>字节</u> 的方式存储。

正式由于 DER 是以字节存储，有些场景需要可见的文字而不是整个文件，所以 DER 不方便传输。而为了方便传输，就有了 PEM文件格式，PEM 将字节转换为 BASE-64 并增加一些可识别符号。如：[^2]

```
-----BEGIN RSA PRIVATE KEY-----
BASE64 ENCODED DATA
-----END RSA PRIVATE KEY-----
```

我们就可以一眼看出，这是一个基于 RSA 的密钥文件。



## 参考 References

[^1]:[Public-key cryptography - Wikipedia](https://en.wikipedia.org/wiki/Public-key_cryptography#Public_discovery)

[^2]: [ASN.1 key structures in DER and PEM](https://polarssl.org/kb/cryptography/asn1-key-structures-in-der-and-pem/)
[^3]: [ASN.1 - Wikipedia](https://en.wikipedia.org/wiki/ASN.1)
