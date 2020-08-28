---
layout: post
title: Method Signature
date: 2020-08-26 00:00:02
tags: [鸦鸦的维基]
comments: 1
excerpt:
typora-root-url: ..
---



**Method Signature**或**Function Signature**意思相似。Java使用Method Signature区分Method，该区分用于[Overloading（重载）](https://en.wikipedia.org/wiki/Function_overloading)。在python的[PEP 362](https://www.python.org/dev/peps/pep-0362/)，提到希望使用Function Signature进行更高效的introspection（内省）。

## 示例

### Java

下面是一段Method[代码](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)：

```java
public double calculateAnswer(double wingSpan, int numberOfEngines,
                              double length, double grossTons) {
    //do the calculation here
}
```

这段代码对应的Method Signature是：

```java
calculateAnswer(double, int, double, double)
```

### Go

[示例代码](https://golang.org/ref/spec#Function_types)：

```
func()
func(x int) int
func(a, _ int, z float32) bool
func(a, b int, z float32) (bool)
func(prefix string, values ...int)
func(a, b int, z float64, opt ...interface{}) (success bool)
func(int, int, float64) (float64, *[]int)
func(n int) func(p *T)
```





