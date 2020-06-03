---
layout: post
title: Golang - exec控制台乱码
date: 2018-11-03 00:00:0
categories: 计算机
tags: Go
comments: 1
typora-root-url: ..
excerpt: 在window下，os.exec执行command命令出现乱码情况，该如何解决？
---

## 环境

操作系统：window 10

IDE：[Goland](#Golang_abbr)		

*[Goland]: intellij系列的go语言IDE编辑器。

Go：1.10

## 概览

![1541584784598](/../assets/blog_res/1541584784598.png)

### 类型1——控制台encoding（系统自带指令）

```go
package main

import (
	"fmt"
	"log"
	"os/exec"
)

func main() {
	cmd := exec.Command("ping", "10.0.0.1")
	out, err := cmd.Output()
	if err != nil {
		log.Fatal(err)
		fmt.Println(err)
	}
	fmt.Print("输出："+string(out))
}
```

得到结果：

> Result: 
>
> ���� Ping 10.0.0.1 ���� 32 �ֽڵ�����:
>
> ���� 10.0.0.1 �Ļظ�: �ֽ�=32 ʱ��<1ms TTL=128
>
> ���� 10.0.0.1 �Ļظ�: �ֽ�=32 ʱ��<1ms TTL=128
>
> ���� 10.0.0.1 �Ļظ�: �ֽ�=32 ʱ��<1ms TTL=128
>
> ���� 10.0.0.1 �Ļظ�: �ֽ�=32 ʱ��<1ms TTL=128
>
> 10.0.0.1 �� Ping ͳ����Ϣ:
>
> ​    ���ݰ�: �ѷ��� = 4���ѽ��� = 4����ʧ = 0 (0% ��ʧ)��
>
> �����г̵Ĺ���ʱ��(�Ժ���Ϊ��λ):
>
> ​    ��� = 0ms��� = 0ms��ƽ�� = 0ms

控制台<sup>console</sup>乱码，而数据是从windows command里获取。因此，需查看command的编码（encoding）。

```shell
# 查看当前编码
# windows 是936（中国- 简体中文(GB2312)）
$ chcp
Active code page: 936
```

原因：

Golang默认输出为UTF-8，而从CLI获得数据的编码是GB2312，因此出现转换错误

解决方案：

- 将CLI或操作系统（数据源）的编码修改为UTF-8

[win10下,cmd,power shell设置默认编码为‘UTF-8’? ](https://www.zhihu.com/question/54724102/answer/380875686)

- 将获得的数据转换为GB2312。

**将数据转换成GB2312**

参考：[Golang 中的 UTF-8 与 GBK 编码转换](http://mengqi.info/html/2015/201507071345-using-golang-to-convert-text-between-gbk-and-utf-8.html)

```go
import (
	"golang.org/x/text/transform"
	"bytes"
	"golang.org/x/text/encoding/simplifiedchinese"
	"io/ioutil"
)

func GbkToUtf8(s []byte) ([]byte, error) {
	reader := transform.NewReader(bytes.NewReader(s), simplifiedchinese.GBK.NewDecoder())
	d, e := ioutil.ReadAll(reader)
	if e != nil {
		return nil, e
	}
	return d, nil
}
```

 新增功能后的例子：

```go
func main() {
	cmd := exec.Command("ping", "10.0.0.1")
	out, err := cmd.Output()
	if err != nil {
		log.Fatal(err)
		fmt.Println(err)
	}
	utf8,_:=GbkToUtf8(out)
	fmt.Print("输出："+string(utf8))
}
```

<br>

### 类型2——软件encoding

大部分软件，现今支持UTF-8，但是部分（功能）仍旧不支持。

如：Git软件处理文件名时，中文符号将转化成八进制编码。

参考文章：[Git - 路径乱码](/Git-路径乱码)

<br>

### 延申

若是日后，window系统统一使用UTF-8编码，那么我们遇到乱码的几率就会越来越少。然而现在仍需考虑window系统下的各种编码问题，以及解决方案的成本。

对于通用软件，则需要对每个指令定制化处理编码，或者更为友好的方式是使用现成的API。

对于小众的软件，则建议去调系统编码。

