---
layout: post
title: 编程语法——注解annotation
date: 2019-06-30 20:17:01
categories: 计算机
tags: 编程语言
comments: 1
excerpt: 注解annotation的小小介绍。
typora-root-url: ..
---

注解annotation的作用：

```go
func methodA(){}

func methodB(){
    A()
}

func methodC(){
    A()
}
//或者
func platform(){
    A();B()
    A();C()
}
```

转换成

```go
func methodA(){}

//@A()
func methodB(){}

//@A()
func methodC(){}
```

作用：

- 避免平台和某些函数的耦合
- 与功能类进行分离，作用：更为显眼。