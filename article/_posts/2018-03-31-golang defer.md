---
layout: post
title: Golang defer
date: 2018-03-31 00:00:00
categories: 计算机
tags: 编程语言
comments: 1
---



A defer statement defers the execution of a function until the surrounding function returns.

defer 语句用于推迟一个函数的执行，直到外围的函数开始返回结果为止。

<br>

##### 作用

为函数增加了生命周期——结束操作。

<br>

##### 解决问题

好处：

- 延迟执行代码
- 代码集中，增加可维护性、可读性。

<br>

##### 常见例子

IO的打开与关闭，实质上这是IO的同一个职责，然而该职责的代码分散到函数的前与后，使用者容易忘记。

```go
func CopyFile(dstName, srcName string) (written int64, err error) {
    src, err := os.Open(srcName)
    if err != nil {
        return
    }
    defer src.Close()

    dst, err := os.Create(dstName)
    if err != nil {
        return
    }
    defer dst.Close()

    return io.Copy(dst, src)
}
```

参考：https://blog.golang.org/defer-panic-and-recover