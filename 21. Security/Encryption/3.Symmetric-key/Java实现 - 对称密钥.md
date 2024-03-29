---
Last Modified: 2017-07-20
---

## 对称加密 Symmetric-key algorithm

原文+密钥+算法=密文 （[传送门](https://docs.aws.amazon.com/zh_cn/kms/latest/developerguide/crypto_overview.html)）

![Image result for Symmetric-key algorithm](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/Symmetric_Key_Encryption_sm.png)

## 代码片段 Snippet

实现1

```java
//自定义密钥
String password="";
SecretKeySpec secretKeySpec=new SecretKeySpec(password.getBytes(),"算法名称");
```
实现2

```java
//随机密钥
KeyGenerator keygenerator  = KeyGenerator.getInstance("DES");
SecretKey secretKey = keygenerator.generateKey();
```

## package结构

`javax.crypto.spec.SecretKeySpec`

[SecretKeySpec](https://docs.oracle.com/javase/7/docs/api/javax/crypto/spec/SecretKeySpec.html)继承了[KeySpec](https://docs.oracle.com/javase/7/docs/api/java/security/spec/KeySpec.html)接口和[SecretKey。](https://docs.oracle.com/javase/7/docs/api/javax/crypto/SecretKey.html)

文档解释KeySpec是：

> A (transparent) specification of the key material that constitutes a cryptographic key.
>
> 这是一个密钥材料的规范，用于构成加密密钥。

把SecretKeySpec比喻成数据包的话，**KeySpec**就是当中的各种协议，而**key和secretkey**就是数据。

## 延申

**SecretKey**有以下方法

1. getAlgorithm 算法名称 
2. getEncoded  (丢个[传送门](https://docs.oracle.com/javase/7/docs/api/java/security/Key.html)， 编码格式，用于传输时用)
3. getFormat 规范格式 (Encoded是返回内容, format是返回该内容格式的名字)

`SecretKeySpec` 继承了两个类，说明它除了提供密钥的内容信息还有其他功能，[SecretKeySpec](https://docs.oracle.com/javase/7/docs/api/javax/crypto/spec/SecretKeySpec.html)提到，它是简易版的钥匙工厂。

```java
//普通的对称密钥工厂类
//可生成对称和非对称密钥。
DESKeySpec desKey = new DESKeySpec(password.getBytes());
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
SecretKey securekey = keyFactory.generateSecret(desKey);
```





