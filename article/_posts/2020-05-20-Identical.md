---
layout: post
title: Identical
date: 2022-05-19 00:00:02
categories: 计算机
tags: 鸦鸦的维基
comments: 1
---

**Identical** （**Identity**）: "similar in every detail; exactly alike"，可译成**相同**。Identical与equal与的意思十分相近，**equal**的英文解释是"being the same in quantity, size, degree, or value."，可译成**相等**。

## 例子

### python

python：[`operator`](https://docs.python.org/3/library/operator.html#module-operator) 

> operator.is_(a, b)
> Return a is b. Tests object identity

`is`操作用于判断对象是否**相同**。

```python
>>> sys.version # python版本
'3.6.9 (default, Apr 18 2020, 01:56:04) \n[GCC 8.4.0]'
>>> a = [1, 2, 3]
>>> b = [1, 2, 3]
>>> a == b # 相等
True
>>> a is b # 不相同
False
```

### Java

在Java语言中，在比较基本类型时`==`作为"相等"使用<sup>[[1]][1]</sup> ；在比较非基本类型时`==`作为"相同"使用<sup>[2][2]</sup> 。

比较基本类型：

```java
int value1 = 1;
int value2 = 1;
System.out.println(value1 == value2);
```

比较对象时：

```java
Date a = new Date(123);
Date b = new Date(123);
System.out.println(a==b); //false 不相同
System.out.println(a.equals(b)); //true 相等
```

[1]: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html "Equality, Relational, and Conditional Operators "
[2]: https://docs.oracle.com/javase/8/docs/api/java/util/Date.html#equals-java.lang.Object- "java.util.Date "
