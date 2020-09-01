---
layout: post
title: Golang-Project Layout
date: 2020-09-02 00:00:02
tags: [Go]
comments: 1
excerpt:
typora-root-url: ..
---

官方文档 [How to Write Go Code](https://golang.org/doc/code.html) 介绍了 Go 的 module 、package 等概念，也有助于读者学习如何使用 Go，以及理解 Go 项目的结构。读者可以参考 [How to Write Go Code 译文](https://kyakya.icu/how-to-write-go-code-译文) 进行学习。

## Project Layout

关于 Go 的通用 Project Layout<sup>项目布局</sup>，可以参考该 [GitHub项目](https://github.com/golang-standards/project-layout) ，对应的中文文档在[这里](https://github.com/golang-standards/project-layout/blob/master/README_zh.md)。

## 对比

### package

#### Java

在Java语言中，无论是interface，还是class，都会占用一个`.java`文件，即每一个内容均会占用一个 package name。

```java
// Myclass.java
import mysite.com.projectname;
class MyClass {
    //content
}
```

```java
// MyInterface.java
import mysite.com.projectname;
class MyInterface {
    //content
}
```

而在调用这些内容时，就会像这样调用：

```java
import mysite.com.name.MyClass;
import mysite.com.name.MyInterface;
```

#### Go

而在Go语言中，源代码存储在 repository 中。而一个 repository 中有若干个 module。而 module 是拥有名字的，如：`github.com/facebook/ent`，该名字通常是这样组成的 `url/项目名`。module 的名字存储在 `go.mod` 中。

```
root
|-- go.mod
|-- main.go/main
|-- dir1
	 |--- code1.go
	 |--- dir2
	 	   |--- code2.go
```

`code1.go `和 `code2.go` 的 package名 分别为：

```go
//code1.go
package dir1

// code2.go
package dir2
```

虽然 `dir1` 和 `dir2` 并不在同一层上，但是 package 命名却同样只有一层。

另外在 module path 上可以放置任何 pacakge 的 go文件，但是不能放置不同 package 的两个 go 文件在同一路径上。

规模相对小的项目如果有 executable file（`package main` 开头的go文件）的话，会把其 放置于 module path 上，规模较大的项目则是将 executable file 放置在 `main` 目录下。

```go
//main.go
package main
```

除此以外，有的[项目](https://github.com/v2ray/v2ray-core)会将非 带`package main` 的go文件 放置于 module path 上。因为可以是：

- 与 executable file 相似，充当入口。
- 作为核心代码

### readme

Go语言的 package 中一般会放置 markdown 文件方便参考，而 Java 的这种行为相对少，不过也有对应的方案 `package-info.java`（[示例](https://github.com/spring-projects/spring-framework/tree/master/spring-orm/src/main/java/org/springframework/orm/jpa)）。

