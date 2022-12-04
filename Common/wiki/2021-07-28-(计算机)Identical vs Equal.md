---
layout: post
title: (计算机)Identical vs Equal
tags: 其他
comments: 1
---

**Identical (Identity)** : "similar in every detail; exactly alike"，可译成 *相同*。[^1]

**equal** 的英文解释是 "being the same in quantity, size, degree, or value."，可译成 *相等*。[^2]

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

在Java语言中，在比较基本类型时`==`作为"相等"使用[^3]；在比较非基本类型时`==`作为"相同"使用。[^4]

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

## 参考 References

[^1]: [IDENTICAL | Definition of IDENTICAL by Oxford Dictionary](https://www.lexico.com/definition/identical#:~:text=adjective,in every detail%3B exactly alike.&text='For the final day%2C Torrance,that putt 17 years ago.)
[^2]: [equal English Definition and Meaning | Lexico.com](https://www.lexico.com/en/definition/equal#:~:text=adjective,size%2C degree%2C or value.&text='The amount of the deduction,value of the stock contributed.)
[^3]: [Equality, Relational, and Conditional Operators](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html)
[^4]: [java.util.Date](https://docs.oracle.com/javase/8/docs/api/java/util/Date.html#equals-java.lang.Object-)

