---
date: 2017-07-19 23:54:08
---


## 背景原理

MAC（消息认证码) 原理

![消息认证码](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/20180227185209179.png)

关于密钥的生成与导入, 下一篇再讲述

## 代码片段 Snippet

Java(JCE)方式 - javax.crypto.Mac（工厂模式）

	//关于密钥secretKeySpec下一篇才讲
	Mac mac=Mac.getInstance("HmacMD5"); //算法
	mac.init(secretKeySpec); //密钥
	byte[] result = //MAC码
	mac.doFinal(src.getBytes());//「原文」的字节码

BC(多态)

    HMac hMac = new HMac(new MD5Digest());//选择算法
    hMac.init(new KeyParameter(Hex.decodeHex(decodeKey.toCharArray())));//添加密码
    hMac.update(src.getBytes(), 0, src.length());//添加原文
    byte[] hMacBytes = new byte[hMac.getMacSize()];
    hMac.doFinal(hMacBytes, 0);//进行操作字节码

<br>

## 延申：JCE的package解析

**MAC(消息验证码)的package是 :  javax.crypto.Mac;**  
Javax.crypto的功能： 

1. 加密与解密
2. 密钥相关操作
3. 以及相关的SPI   


**digest(摘要)的package是 : java.security.MessageDigest;**

因为摘要算法不是加密算法，所以没有放置于crypto包里。



