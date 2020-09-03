---
layout: post
title: Golang-Some Standards
date: 2020-09-02 00:00:02
tags: [Go]
comments: 1
excerpt: 本篇文章是关于Golang项目基本布局以及项目习惯的笔记。
typora-root-url: ..
---

## Project Layout

关于 Go 的通用 Project Layout<sup>项目布局</sup>，可以参考该 [GitHub项目](https://github.com/golang-standards/project-layout) ，译文在[这里](https://github.com/golang-standards/project-layout/blob/master/README_zh.md)。

## Code Style 

### naming

这里举一些Go语言命名习惯的例子：

1. 名字应该是 MixedCase （驼峰式），如：`ServeHTTP` 、`IDProcessor`
2. Local variable、Function parameter 的名字尽可能地简写，名字过长将会分散注意力，特别是可以从上下文推测出来地变量。如：
   - i => index
   - r => reader
   - b => buffer

简化前

```go
func RuneCount(buffer []byte) int {
    runeCount := 0
    for index := 0; index < len(buffer); {
        if buffer[index] < RuneSelf {
            index++
        } else {
            _, size := DecodeRune(buffer[index:])
            index += size
        }
        runeCount++
    }
    return runeCount
}
```

简化后

```go
func RuneCount(b []byte) int {
    count := 0
    for i := 0; i < len(b); {
        if b[i] < RuneSelf {
            i++
        } else {
            _, n := DecodeRune(b[i:])
            i += n
        }
        count++
    }
    return count
}
```

3. 由于 Return value 的名字与文档相关，并且调用方调用时将会查看到该名字，所以尽量详细清晰。（不应该省略，以至于难以理解）
4. Receiver 和（2）类似，另外需要注意，Receiver 的名字在所有地方都应该一致，方便记忆。

```go
func (b *Buffer) Read(p []byte) (n int, err error)、
func (sh serverHandler) ServeHTTP(rw ResponseWriter, req *Request)
func (r Rectangle) Size() Point
```

5. Exported name。不像 Java，Go语言不需要使用 package 对 Exported name 进行修饰。
   - bytes.Buffer =>  bytes.ByteBuffer
   - strings.Reader => strings.StringReader

6. Interface Type。当 Interface 的 method 只有一个时，可以这样命名：

```go
// Read + er
type Reader interface {
    Read(p []byte) (n int, err error)
}
// Exec + er
type Execer interface {
    Exec(query string, args []Value) (Result, error)
}
```

7. Error type。应该这样：

```go
type ExitError struct {
    ...
}
var ErrFormat = errors.New("image: unknown format")
```

8. package name。包名尽可能不要取暧昧的，如：`util`, `common`。当`go.mod`的 module path 设置为 `github.com/golang/oauth2` 时，那么根目录下的go文件的 package 默认会为 `oauth2`，命名 package name 时请注意这一点。

参考来源

- https://talks.golang.org/2014/names.slide

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

### readme

Go语言的 package 中一般会放置 markdown 文件方便参考，而 Java 的这种行为相对少，不过也有对应的方案 `package-info.java`（[示例](https://github.com/spring-projects/spring-framework/tree/master/spring-orm/src/main/java/org/springframework/orm/jpa)）。

