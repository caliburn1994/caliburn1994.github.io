---
layout: post
title: Golang - exec控制台乱码
date: 2018-11-03 00:00:0
categories: 计算机
tags: Go
comments: 1
---

环境：window	

IDE：Goland		

*[Goland] : intellij系列的go语言IDE编辑器。

Go：1.10



<br>

简单例子

```go
cmd := exec.Command("ping", "10.0.0.1")
cmd.Dir = "F:\\GitHub\\caliburn1994.github.io\\_includes\\"
out, err := cmd.Output()
if err != nil {
   log.Fatal(err)
   fmt.Println(err)
}
fmt.Print("输出："+string(out))
```

得到结果

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

**乱码**

输出台（console）乱码，而数据是从windows command里获取。因此，需查看command的编码（encoding）。

```shell
#查看当前编码
# windows 是936（中国- 简体中文(GB2312)）
chcp
```

原因：

Golang默认输出为UTF-8，而从command获得字节码是GB2312，因此转换错误

解决方案：

- 将数据转换为GB2312。
- 将command（数据源）设置为UTF-8（获得数据前就转换完毕）

<br>

参考：[Golang 中的 UTF-8 与 GBK 编码转换](http://mengqi.info/html/2015/201507071345-using-golang-to-convert-text-between-gbk-and-utf-8.html)

```go

```

