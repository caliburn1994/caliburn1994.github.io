---
layout: post
title: Golang defer 解决“职责统一”的问题
date: 2018-03-31 00:00:00
categories: 计算机
tags: 设计思想
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

<br>

##### Java代码模仿

代码实现（1）

```java
class A {
    List<Method> list = new LinkedList();
    
    public void doA(){
    	//...dosth
        list.put(new method{
            ///...
        });
        
        //其他操作
        
        //遍历释放
        for(int i=0;i<list.size();i++) {
        list.get(i).dosth();
        }
        
    }
       
}
```

代码实现（2）

```Java

```

<br>