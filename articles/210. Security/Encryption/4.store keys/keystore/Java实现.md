---
Last Modified: 2017-08-06
---

## 代码片段 Snippet

源码(Javadoc)对keystore的操作做了很好的说明

```java
//读取keystore
KeyStore keystore = KeyStore.getInstance("JKS");//证书类型 常见: JKS,PKCS12
InputStream is = new FileInputStream(keystore路径);
keystore.load(is, keystore密码);
```

<br>

```java
//创建keystore
keyStore = KeyStore.getInstance("JKS");
keyStore.load(null, null);//这一步不可以省略
//...  操作
keyStore.store(FileUtils.openOutputStream(new File(储存的文件名)), 密码.toCharArray());
```

<br>

**读取证书**

```java
keyStore.getCertificateChain(名称);
```

当不知道alias时候，且keystore只有一张时 ，可以这样做

```java
KeyStore keyStore=...;
keyStore.getCertificateChain(Collections.list(keyStore.aliases()).get(0));
```
因为只有keyStore.aliases()方法读取名称, 而且其返回类型 Enumeration, 可用Enumeration遍历的方式, 如果是JDK 8  则可以用lambda简单处理

<br>

读取密钥

```java
Key key =  keyStore.getKey(名称, key的密码);
```

<br>

## 探讨

keystore 源码解释：
> This class represents a storage facility for cryptographic keys and certificates.

keystore是储存密钥和证书的设备。KeyStore可以存储三种类型数据

1. PrivateKey 私钥
2. SecretKey 对称密钥
3. TrustedCertificateEntry 证书

每种数据都有自己的名称(alias) 

**1.储存SecretKey**([源码](https://docs.oracle.com/javase/7/docs/api/java/security/KeyStore.html))

```java
KeyStore.ProtectionParameter protParam =  new KeyStore.PasswordProtection(password);
// save my secret key
    javax.crypto.SecretKey mySecretKey;
    KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(mySecretKey);
    ks.setEntry("secretKeyAlias", skEntry, protParam);
```
**2. 储存私钥和证书链**
 私钥只能自己保存，而公钥的目的是为了生成证书，因此，私钥是和证书链一起管理。

```java
PrivateKey privateKey=...;
Certificate[] chain=...;
keyStore.setKeyEntry(名称, privateKey, 密码, chain);//密码可以缺省, 即使用keystore的密码
```
**3. 增加证书到证书链**
这里没有现成的方法, 只能自己手动读取, 再进行覆盖

<br>

## 番外

keystore常见有两种存储：PKCS12和JKS

1. PKCS12可以直接导入浏览器，但它不是证书。   
2. JKS只能用于Java间使用

<br>

PFX的作用：申请证书时，**服务端代替客户端生成密钥对**，并签名，随后将证书和私钥一同发回客户端。
