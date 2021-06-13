---
layout: post
title: Java版本和供应商
tags: Java
comments: 1
typora-root-url: ..\..\..
typora-copy-images-to: ..\..\..\assets\blog_res
---

Vendor 中文意思是供应商。本章节聊聊该如何选择 vendor 以及 Java 版本。

## Vendor 列表 Vendor list

在安装好 [sdkman](https://sdkman.io/)，通过以下命令可以查看到有什么 vendor 。

```bash
$ sdk list java
================================================================================
Available Java Versions
================================================================================
 Vendor        | Use | Version      | Dist    | Status     | Identifier
--------------------------------------------------------------------------------
 AdoptOpenJDK  |     | 16.0.1.j9    | adpt    |            | 16.0.1.j9-adpt      
               |     | 16.0.1.hs    | adpt    |            | 16.0.1.hs-adpt      
               |     | 11.0.11.j9   | adpt    |            | 11.0.11.j9-adpt     
               |     | 11.0.11.hs   | adpt    |            | 11.0.11.hs-adpt     
               |     | 8.0.292.j9   | adpt    |            | 8.0.292.j9-adpt     
               |     | 8.0.292.hs   | adpt    |            | 8.0.292.hs-adpt     
 Alibaba       |     | 11.0.9.4     | albba   |            | 11.0.9.4-albba      
               |     | 8.5.5        | albba   |            | 8.5.5-albba         
 Amazon        |     | 16.0.1.9.1   | amzn    |            | 16.0.1.9.1-amzn     
               | >>> | 15.0.1       | amzn    | local only | 15.0.1-amzn         
               |     | 11.0.11.9.1  | amzn    |            | 11.0.11.9.1-amzn    
               |     | 8.292.10.1   | amzn    |            | 8.292.10.1-amzn     
 Azul Zulu     |     | 16.0.1       | zulu    |            | 16.0.1-zulu         
               |     | 16.0.1.fx    | zulu    |            | 16.0.1.fx-zulu      
               |     | 11.0.11      | zulu    |            | 11.0.11-zulu        
               |     | 11.0.11.fx   | zulu    |            | 11.0.11.fx-zulu     
               |     | 8.0.292      | zulu    |            | 8.0.292-zulu        
               |     | 8.0.292.fx   | zulu    |            | 8.0.292.fx-zulu     
               |     | 7.0.302      | zulu    |            | 7.0.302-zulu        
               |     | 6.0.119      | zulu    |            | 6.0.119-zulu        
 BellSoft      |     | 16.0.1.fx    | librca  |            | 16.0.1.fx-librca    
               |     | 16.0.1       | librca  |            | 16.0.1-librca       
               |     | 11.0.11.fx   | librca  |            | 11.0.11.fx-librca   
               |     | 11.0.11      | librca  |            | 11.0.11-librca      
               |     | 8.0.292.fx   | librca  |            | 8.0.292.fx-librca   
               |     | 8.0.292      | librca  |            | 8.0.292-librca      
 GraalVM       |     | 21.1.0.r16   | grl     |            | 21.1.0.r16-grl      
               |     | 21.1.0.r11   | grl     |            | 21.1.0.r11-grl      
               |     | 21.1.0.r8    | grl     |            | 21.1.0.r8-grl       
               |     | 21.0.0.2.r11 | grl     |            | 21.0.0.2.r11-grl    
               |     | 21.0.0.2.r8  | grl     |            | 21.0.0.2.r8-grl     
               |     | 20.3.2.r11   | grl     |            | 20.3.2.r11-grl      
               |     | 20.3.2.r8    | grl     |            | 20.3.2.r8-grl       
               |     | 20.3.1.2.r11 | grl     |            | 20.3.1.2.r11-grl    
               |     | 20.3.1.2.r8  | grl     |            | 20.3.1.2.r8-grl     
               |     | 19.3.6.r11   | grl     |            | 19.3.6.r11-grl      
               |     | 19.3.6.r8    | grl     |            | 19.3.6.r8-grl       
               |     | 19.3.5.r11   | grl     |            | 19.3.5.r11-grl      
               |     | 19.3.5.r8    | grl     |            | 19.3.5.r8-grl       
 Java.net      |     | 18.ea.1      | open    |            | 18.ea.1-open        
               |     | 17.ea.26     | open    |            | 17.ea.26-open       
               |     | 17.ea.25     | open    |            | 17.ea.25-open       
               |     | 17.ea.7.lm   | open    |            | 17.ea.7.lm-open     
               |     | 17.ea.3.pma  | open    |            | 17.ea.3.pma-open    
               |     | 16.0.1       | open    |            | 16.0.1-open         
               |     | 11.0.11      | open    |            | 11.0.11-open        
               |     | 11.0.10      | open    |            | 11.0.10-open        
               |     | 11.0.2       | open    |            | 11.0.2-open         
               |     | 8.0.292      | open    |            | 8.0.292-open        
               |     | 8.0.282      | open    |            | 8.0.282-open        
               |     | 8.0.265      | open    |            | 8.0.265-open        
 Mandrel       |     | 21.1.0.0     | mandrel |            | 21.1.0.0-mandrel    
               |     | 20.3.2.0     | mandrel |            | 20.3.2.0-mandrel    
 Microsoft     |     | 16.0.1.9.1   | ms      |            | 16.0.1.9.1-ms       
               |     | 11.0.11.9.1  | ms      |            | 11.0.11.9.1-ms      
 SAP           |     | 16.0.1       | sapmchn |            | 16.0.1-sapmchn      
               |     | 11.0.11      | sapmchn |            | 11.0.11-sapmchn     
 TravaOpenJDK  |     | 11.0.9       | trava   |            | 11.0.9-trava        
               |     | 8.0.232      | trava   |            | 8.0.232-trava       
================================================================================
Use the Identifier for installation:

    $ sdk install java 11.0.3.hs-adpt
================================================================================

```

## Vendor 是什么？What is a vendor?

当我们谈及 Java 等编程语言时，我们首先会想到 <u>它可能是一个开源的编程语言</u>，那么，它为什么会有 vendor呢？

我们通过命令行，获得以下的环境变量：

```bash
$ java -XshowSettings:properties -version
Property settings:
    file.encoding = UTF-8
    file.separator = /
    java.class.path = 
    java.class.version = 59.0
    java.home = /home/xiaojie/.sdkman/candidates/java/15.0.1-amzn
    java.io.tmpdir = /tmp
    java.library.path = /usr/java/packages/lib
        /usr/lib64
        /lib64
        /lib
        /usr/lib
    java.runtime.name = OpenJDK Runtime Environment
    java.runtime.version = 15.0.1+9
    java.specification.name = Java Platform API Specification
    java.specification.vendor = Oracle Corporation
    java.specification.version = 15
    java.vendor = Amazon.com Inc.
    java.vendor.url = https://aws.amazon.com/corretto/
    java.vendor.url.bug = https://github.com/corretto/corretto-jdk/issues/
    java.vendor.version = Corretto-15.0.1.9.1
    java.version = 15.0.1
    java.version.date = 2020-10-20
    java.vm.compressedOopsMode = Zero based
    java.vm.info = mixed mode, sharing
    java.vm.name = OpenJDK 64-Bit Server VM
    java.vm.specification.name = Java Virtual Machine Specification
    java.vm.specification.vendor = Oracle Corporation
    java.vm.specification.version = 15
    java.vm.vendor = Amazon.com Inc.
    java.vm.version = 15.0.1+9
    jdk.debug = release
    line.separator = \n 
    os.arch = amd64
    os.name = Linux
    os.version = 5.8.0-50-generic
    path.separator = :
    sun.arch.data.model = 64
    sun.boot.library.path = /home/xiaojie/.sdkman/candidates/java/15.0.1-amzn/lib
    sun.cpu.endian = little
    sun.io.unicode.encoding = UnicodeLittle
    sun.java.launcher = SUN_STANDARD
    sun.jnu.encoding = UTF-8
    sun.management.compiler = HotSpot 64-Bit Tiered Compilers
    user.country = US
    user.dir = /home/xiaojie
    user.home = /home/xiaojie
    user.language = en
    user.name = xiaojie

openjdk version "15.0.1" 2020-10-20
OpenJDK Runtime Environment Corretto-15.0.1.9.1 (build 15.0.1+9)
OpenJDK 64-Bit Server VM Corretto-15.0.1.9.1 (build 15.0.1+9, mixed mode, sharing)

```

其中需要注意的是：

```properties
java.specification.vendor=Oracle Corporation

java.vendor=Amazon.com Inc.
java.vendor.url=https://aws.amazon.com/corretto/
java.vendor.url.bug=https://github.com/corretto/corretto-jdk/issues/
java.vendor.version=Corretto-15.0.1.9.1

java.vm.specification.vendor=Oracle Corporation
java.vm.vendor=Amazon.com Inc.


openjdk version "15.0.1" 2020-10-20
OpenJDK Runtime Environment Corretto-15.0.1.9.1 (build 15.0.1+9)
OpenJDK 64-Bit Server VM Corretto-15.0.1.9.1 (build 15.0.1+9, mixed mode, sharing)
```

这里有三个内容：

- `java.vendor` : Java运行时的 vendor。
- `java.vm`: Java VM(虚拟机)的vendor。此处，可能读者会在想，Java VM 的 implementation 不应该是 [Oracle Corporation](https://en.wikipedia.org/wiki/Oracle_Corporation)  提供的 [HotSpot](https://en.wikipedia.org/wiki/HotSpot_(virtual_machine)) 吗？由于 Amazon 的环境变量中没有记载 HotSpot 等信息，所以我们无法准确地判定，但是我们通过
  - 通过 `OpenJDK build 15.0.1+9`  关键词搜索
  - https://github.com/corretto/corretto-jdk/blob/release-15.0.0.36.1/doc/building.md，文档内容可以推测是使用 HotSpot。
- `java.specification.vendor`: 文档地 vendor

回到我们的问题，为什么会有各种各样的 vendor？

我们通过 [Amazon Corretto](https://aws.amazon.com/cn/corretto/) 的<u>介绍</u>以及 [常见问题](https://aws.amazon.com/cn/corretto/faqs/)，可以了解到，Amazon 作为 Vendor，<u>为 OpenJDK 增加了性能增强和安全修复</u>，也就是说，我们可以理解 **Amazon Corretto 是 OpenJDK 的扩展包**。

## 该用哪个版本？ Which version of Java should I use?

[为何应该升级 Java？](https://www.java.com/zh-CN/download/help/why_upgrade.html)  一文中介绍了我们为什么要升级到 Java 8。由于 Java 8之前、8、11、17都是 Long Term Support(LTS) 版本，因此，我们可以同理得到结论，应该使用最新的LST版本的 Java。也就是说，在17出来前，我们应该选择11，而不是8。

截止2021年6月13日未知，维基百科上的 Java version history 如下：

![image-20210613031740408](/assets/blog_res/image-20210613031740408.png)

另一个问题是，究竟使用长期版本，还是最新的短期版本呢？如：11 vs 12~16

支持使用 LST 的一方认为：

- 由于有团队维护LST版本的 Java，所以当前 Java 出现（安全）问题，那么只要补丁即可。而 <u>非LST版本</u>的 Java 出现问题后，当 support 时间已经截止，那么只能等待下一个版本的 Java，这个等待时间是相对比较长。
  - 【思考】：笔者曾呆过的一个团队，当时使用的是 Java 13，听说使用的理由是为了展示给客户”自己的研发部门有能力“。而在开发中遇到奇怪的问题，最终在GitHub上提问并确认是 Java 13 的问题，没有认为解决方法，只能等待下次更新。

支持使用最新版本的一方认为：

- 即便当前的 Java 出现问题，可以使用 compile命令使用 LST 版本，所以没有那种担忧。
  - 【思考】：那么对应的依赖包怎么办？ Spring 中有部分依赖包是依赖最新版本的 Java，并不是向下兼容，如果回退版本，很有可能会有一些问题。
- 性能和阅读性会改善
  - 【思考】：中小型的代码，往往瓶颈是数据库。
- 可以使用最新的依赖包。
  - 如果依赖包是使用最新版本的 Java，我们如果想用最新版本的功能，那么我们就要升级 Java。

综上：

- 如果**不在意最新版本的功能**以及**性能**，那么可以选择已经发行一两年的 LST。
- 如果是为了**学习**，可以使用最新版本的 Java，通过学习他们的思路从而提高认知，即便之后要使用 LST，也可以跟着造轮子。

## 延申阅读

- [Don't ever put a non-Java LTS release into production[?!] - reddit](https://www.reddit.com/r/java/comments/eadlwg/dont_ever_put_a_nonjava_lts_release_into/)
- [Java version history - wiki](https://en.wikipedia.org/wiki/Java_version_history) 
- [Java 14都快出来了，为什么还有那么多人执着于Java 8？- 知乎](https://www.zhihu.com/question/360985479)

