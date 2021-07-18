---
layout: post
title: Identifier
tags: Others
comments: 1
typora-root-url: ..
---



Identifier可译为标识、标识物。日语中则译为识别子。在编程语言中，通过对Identifier进行规定，从而间接地对method名、variable名等实体的<u>命名规则</u>进行规定。<sup>[[来源]](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/variables.html)</sup>

由于大多编程语言具有Keyword，Keyword是已被使用的Identifier，因此不可再被使用。<sup>[[来源]](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html)</sup>

## 示例参考

### Go

一些实体（如：variables、types），它们的名称统称为Identifier。<sup>[[来源]](https://golang.org/ref/spec#Identifiers)</sup> 在Go中，以下Identifier是事先且隐式地被声明了的：<sup>[[来源]](https://golang.org/ref/spec#Predeclared_identifiers)</sup>

```
Types:
	bool byte complex64 complex128 error float32 float64
	int int8 int16 int32 int64 rune string
	uint uint8 uint16 uint32 uint64 uintptr

Constants:
	true false iota

Zero value:
	nil

Functions:
	append cap close complex copy delete imag len
	make new panic print println real recover
```