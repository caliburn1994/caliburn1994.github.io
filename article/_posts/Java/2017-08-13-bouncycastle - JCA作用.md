---
layout: post
title: bouncycastle - JCA作用
date: 2017-08-13 19:14:57
categories: 计算机
tags: 安全 
comments: 1
series: "Java密码学"
---



在使用BC的package的时候，总有一个疑问”JCA是什么？“

<br>

## JCA （Java Cryptography Architecture）

**JCA开头的package的作用：将标准(java.security)和具体实现(BC的具体代码)进行转换。**

作用：Java是将安全的接口和具体实现进行分开。所以代码中，用sun也好, BC的也好，最终转换成(java.security)包的内容就即可。   

例子：

```java
new JcaX509CertificateConverter().setProvider( "BC" ).getCertificate( certificateHolder );
```
这里就是将BC的X509CertificateHolder里的证书转换成java.security下的certificate类，他们虽然是同样的名字, 但是是两个不同的类。


总而言之, JCA就是转换器之类的东西。