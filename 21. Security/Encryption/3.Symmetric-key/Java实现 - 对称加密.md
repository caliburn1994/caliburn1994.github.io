---
Last Modified: 2017-08-06
---


## 对称加密 Symmetric-key algorithm

原文+密钥+算法=密文 （[传送门](https://docs.aws.amazon.com/zh_cn/kms/latest/developerguide/crypto_overview.html)）

![Image result for Symmetric-key algorithm](/assets/blog_res/Symmetric_Key_Encryption_sm.png)

## 代码片段 Snippet

DES的代码实现相对简单（但是已经不被推荐了）。 一般使用AES。



JCE代码实现

```java
Cipher desCipher;
desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");//设置算法与模式
desCipher.init(Cipher.ENCRYPT_MODE, 密钥);//加载密钥, 设置为加密模式
byte[] textEncrypted = desCipher.doFinal(text);//得到原文
```

<br>

BC代码实现

```java
Security.addProvider(new BouncyCastleProvider());
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding","BC");
```

传送门:[How to encrypt and decrypt files with bouncy castle (DES) in netbeans java?](https://stackoverflow.com/questions/20452880/how-to-encrypt-and-decrypt-files-with-bouncy-castle-des-in-netbeans-java) 

处理大数据


```java
Cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");//设置算法与模式
Cipher.init(Cipher.ENCRYPT_MODE, 密钥);//加载密钥, 设置为加密模式
while(...){
byte[] data=Cipher.update(数据)//分开处理数据
}
byte[] data=cipher.dofinal();//进行填充(如 PKCS5padding)
```

## 模式 modes

当选择CBC模式时候 ，就需要参数iv值。iv值是类似摘要算法加盐的存在。

*[iv值]:  initialization vector 

iv理应是字节组，可是这里却是一个对象，这让我感觉惊讶。 但如果是看了ASN.1格式的一些代码，就明白，ANS.1格式代码的实现，不使用byte[]等类型，而是使用类来代替

代码实现：

```java
byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};//要16位
IvParameterSpec ivspec = new IvParameterSpec(iv);
Cipher Cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
Cipher.init(Cipher.ENCRYPT_MODE, 密钥,ivspec);//在这里添加参数
byte[] textEncrypted = Cipher.doFinal(text);
```