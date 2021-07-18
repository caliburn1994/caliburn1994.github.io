---
layout: post
title: Declaration
tags: Others
comments: 1
typora-root-url: ..
---

在编程语言中，Declaration和Definition两个概念常被搞混。Declaration译为宣言、声明，不包含任何实际内容。Definition则包含了具体的逻辑代码或具体的数据。

## 编程语言

### Golang

在Golang中，创建一个新的type的过程称为：type definition（类型定义）。<sup>[[来源]](https://golang.org/ref/spec#Type_definitions)</sup>

```go
type TreeNode struct {
	left, right *TreeNode
	value *Comparable
}
```

Alias declaration（别名声明）将会把[Identifier](/Identifier)与给定的type进行绑定。<sup>[[来源]](https://golang.org/ref/spec#Alias_declarations)</sup>

```go
type myint int
type (
	nodeList = []*Node  
	Polar    = polar    
)
```

Alias declaration并没有创建新的type，仅仅是进行declaration（声明），声明右侧的type与左侧的type是identical（相同的）。<sup>[[来源]](https://golang.org/ref/spec#Alias_declarations)</sup>

### Java

谈及variable时，declaration和definition可以理解成：<sup>[[来源]](https://stackoverflow.com/a/11721417/4883754)</sup>

> **declaration**
> A statement that establishes an identifier and associates attributes with it, without necessarily reserving its storage (for data) or providing the implementation (for methods). See also *definition*
>
> **definition**
> A declaration that reserves storage (for data) or provides implementation (for methods). See also *declaration*.

```java
List i;              // declaration - variable on the stack  
i = new ArrayList(); // definition - gives variable a reference
```

variable中的declaration和definition的定义，与Golang中类似。

**不同声音。**但是谈及method declaration时，[Oracle文档](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)却给出不同的声音。文档中将method declarations分为六个部分，其中竟包含了**具体的实现**（method body，包括`{}`中的代码）。

如像上述Go语言所示那样，Declaration是不应该包含任何实现，应与StackOverflow上的[回答](https://stackoverflow.com/a/11715494/4883754)类似，但Oracle文档如此记述，因此本词条不将Java文档作为参考（Java/Oracle中的解释常常与其他编程语言有差异）。



