---
layout: post
title: (计算机)Initializer
tags: 其他
comments: 1
typora-root-url: ..
---

initialize 有两个名词形态："Initializer"、"Initialization"。Initialization 译为初始化，而将 Initializer 视为专有名词即可，无需翻译。

而普遍意义上，an Initializer 是指 对一个 variable 进行初始化赋值。

## Object-oriented programming

在**面向对象的编程**中，初始化代码 (Initialization code) 分为两种，一种是构造器 (constructor) ，另一种是初始化器 (initializer) 。区别在于，constructor 初始化参数是固定的，如果初始化参数有两个的话，就必须输入两个参数。

```java
class MyObject{
    public MyObject(param1, param2){...}
}
// 可运行
MyObject myObjectInstance = new MyObject(param1, param2);
// 不允许 × 只填其中一个
MyObject myObjectInstance = new MyObject(param1);
```

object initializer是在 constructor 之后，对特定可见字段赋值：[^1]

```c#
MyObject myObjectInstance = new MyObject(param1, param2)
{
    MyProperty = someUsefulValue // an object initializer
};
```

## 其他语言

在Go语言里，可以这样一次性使用若干个 Initializers：[^2]

```go
package main

import "fmt"

var i, j int = 1, 2

func main() {
	var c, python, java = true, false, "no!" // Initializers 
	fmt.Println(i, j, c, python, java)
}
```

## 延申 Also see

- [What's the difference between an object initializer and a constructor?](https://stackoverflow.com/questions/740658/whats-the-difference-between-an-object-initializer-and-a-constructor)

- [Initialization (programming)](https://en.wikipedia.org/wiki/Initialization_(programming))

## 参考 References

[^1]: [Object and Collection Initializers (C# Programming Guide))](https://docs.microsoft.com/en-us/dotnet/csharp/programming-guide/classes-and-structs/object-and-collection-initializers)

[^2]: [Variables with initializers - Go]()

