

ASN.1 抽象地定义了数据该怎么定义，但没有指定特定地编码方式。这意味着，我们需要一种表现方式（编码方式）去表示用 ASN.1 定义的数据，这种表现方式有：DER (8进制字节)、XER (XML)、JSON 等等。[^3]

常见的密钥存储文件格式有：`.crt`, `.cer`, `.pem`, or `.der`。而这当中，DER 格式是最常用。可以理解为，在代码里数据结构是以 <u>字节</u> 的方式存储。

正式由于 DER 是以字节存储，有些场景需要可见的文字而不是整个文件，所以 DER 不方便传输。而为了方便传输，就有了 PEM文件格式，PEM 将字节转换为 BASE-64 并增加一些可识别符号。如：[^2]

```
-----BEGIN RSA PRIVATE KEY-----
BASE64 ENCODED DATA
-----END RSA PRIVATE KEY-----
```

我们就可以一眼看出，这是一个基于 RSA 的密钥文件。