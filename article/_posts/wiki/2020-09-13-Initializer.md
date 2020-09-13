---
layout: post
title: Initializer
date: 2020-09-13 00:00:02
tags: [鸦鸦的维基]
comments: 1
excerpt:
typora-root-url: ..
---

initialize 有两个名词形态："Initializer"、"Initialization"。Initialization 译为初始化，而将 Initializer 视为专有名词即可，无需翻译。而普遍意义上，Initializer 是指可以一次性 declare<sup>声明</sup> 指定的若干个 variable 的 construct 。

## Object-oriented programming

在**面向对象的编程**中，Initialization code<sup>初始化代码</sup> 有两种，一种是 constructor <sup>构造器</sup>，另一种是 initializer 。区别在于，constructor 初始化参数是固定的，如果初始化参数有两个的话，就必须输入两个参数。

```java
class MyObject{
    public MyObject(param1, param2){...}
}
// 可运行
MyObject myObjectInstance = new MyObject(param1, param2);
// 不允许 × 只填其中一个
MyObject myObjectInstance = new MyObject(param1);
```

initializer 是在 constructor 之后的函数，它可以指定特定的 field/member variable进行初始化。

```c#
MyObject myObjectInstance = new MyObject(param1, param2)
{
    MyProperty = someUsefulValue // 初始化某一个参数
};
```

## 其他语言

在Go语言里，可以这样使用 initializer ：<sup>[来源](https://tour.golang.org/basics/9)</sup>

```go
package main

import "fmt"

var i, j int = 1, 2

func main() {
	var c, python, java = true, false, "no!" // Initializer 
	fmt.Println(i, j, c, python, java)
}
```

## 其他参考文献

- [What's the difference between an object initializer and a constructor?](https://stackoverflow.com/questions/740658/whats-the-difference-between-an-object-initializer-and-a-constructor)

- [Initialization (programming)](https://en.wikipedia.org/wiki/Initialization_(programming))

  