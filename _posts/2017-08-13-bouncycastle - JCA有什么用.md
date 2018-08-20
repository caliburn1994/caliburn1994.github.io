---
layout: post
title: bouncycastle - JCA有什么用
date: 2017-08-13 19:14:57
categories: 计算机
tags: 安全 
comments: 1
---



在使用BC的package的时候, 我们总有一个疑问, JCA是什么鬼

**JCA开头的包, 作用在于将标准(java.security)和具体实现(BC的具体代码)进行转换**

为什么需要这种转换呢? 这种转换关键在于Java是将安全的架构和具体实现进行分开, 所以代码中, 我们可以用sun也好, BC的也好, 但是最终转换成(java.security)这个包的内容就可以了.   

java.security 相当于一个标准



```
	new JcaX509CertificateConverter().setProvider( "BC" )
  .getCertificate( certificateHolder );
```
这里就是将BC的X509CertificateHolder里的证书转换成java.security下的certificate类, 他们虽然是同样的名字, 但是具体代码是不一样


= =  总而言之, JCA就是转换器之类的东西