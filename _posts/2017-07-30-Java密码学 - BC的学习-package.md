---
layout: post
title: Java密码学 - BC的学习-package
date: 2017-07-20 22:10:52
categories: 计算机
tags: 安全 
comments: 1
---



﻿之前被bouncy castle的包弄得很烦恼, 因为BC的包的结构相当不明确, 本文章, 就是为了梳理清楚

[latest releases - bouncycastle.org](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0ahUKEwj565Sf7K7VAhUMW7wKHcgUBYQQFggnMAA&url=https%3A%2F%2Fwww.bouncycastle.org%2Flatest_releases.html&usg=AFQjCNGuk_xrtkIs9xGOt1eaGOjHJoDSiQ)

官网上, 我们按ctrl+F 搜索 "**SIGNED JAR FILES**"

可以看到以下内容

JDK 1.5 - JDK 1.8的jar包, 其名字是jdk15on, 也就是 jdk1*on, 就是从JDK *到最新版本的意思

也就是, 我们安装的时候下载jar包的时候, **只需要下载\**jdkon即可**. 其他的兼容性差



另外值得注意

> From release 1.40 some implementations of encryption algorithms were removed from the regular jar files at the request of a number of users. Jars with names of the form ***-ext-\*** still include these



-ext包为删除了算法的包, 这个包我不打算用, 在网上找例子的时候肯定回踩坑.

<br>

在[latest releases - bouncycastle.org](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0ahUKEwj565Sf7K7VAhUMW7wKHcgUBYQQFggnMAA&url=https%3A%2F%2Fwww.bouncycastle.org%2Flatest_releases.html&usg=AFQjCNGuk_xrtkIs9xGOt1eaGOjHJoDSiQ),我们看到, 他们的package根据以下功能分了包

- **JCE with provider and lightweight API**    bcprov包 , 这是基础包
- DTLS/TLS API/JSSE Provider   由名字可知道, 是TSL相关, 即网络相关
- **PKIX/CMS/EAC/PKCS/OCSP/TSP/OPENSS**L bcpkix-jdk**on  PKIX是 Public-Key Infrastructure (X.509)的简写 , 这里我们看到公钥和私钥的处理, 各种乱七八糟的规范和编码
- OpenPGP/BCPG   PGP=>Pretty Good Privacy, 非主流, 所以不会使用到
- **SMIME   bcmail** [[S/MIME - 维基百科，自由的百科全书](https://zh.wikipedia.org/zh-hans/S/MIME)](https://zh.wikipedia.org/zh-hans/S/MIME) 该包PKCS#7的数字信封和加密, 以及加密邮件会用到

<br>

评论:   

1. BC的jar包与常规的jar包不同,  常规的包不会带有JDK16,15这种前缀, 好处在于方便使用, 同时会给新手带来一些迷惑
2. 其jar包的分工也是挺带专业知识, 而且需要了解JCE那边的框架才能明确知道其作用. 这一点有点无奈