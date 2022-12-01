---
Last Modified: 2017-07-17 23:53:08
---


## 方法1 - Apache Commons Codec


>Apache Commons Codec (TM) software provides implementations of common encoders and decoders such as Base64, Hex, Phonetic and URLs.

如文档所述 , Apache的该package用于常用的加密和解密，因此暴露的细节更少。

![20170717234551755](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/20170717234551755.png)

```java
//工具包的形式
DigestUtils.sha1Hex("abc");
```

## 方法2 - bouncy castle的crypto包

crypto包含有各种加密方式，其中digest就是摘要算法。  

```java
   Digest digest = new SHA1Digest();//接口多态
   digest.update(src.getBytes(), 0, src.length());//导入数据
   Byte[] shaBytes = new byte[digest.getDigestSize()];//创建byte[],作为结果
   digest.doFinal(shaBytes, 0);//将结果导出至byte[]中
```

## 方法3 - java.security包

```java
//MessageDigest类
MessageDigest messageDigest = MessageDigest.getInstance("SHA");//工厂类
Byte[] shaBytes = messageDigest.digest(src.getBytes());//导入数据，获得结果
```



