---
layout: post
title: Golang defer 解决“职责统一”的问题
date: 2018-03-31 00:00:00
categories: 计算机
tags: 设计思想
comments: 1
---



##### 定义

defer, 意思: 推迟

##### 作用

在函数返回时,执行的动作。换句话说，增加了“func”一环生命周期。

##### 解决问题

解决了编程过程中, **如何延迟执行代码** 的问题. 

对于我而言, **延迟执行的根本需求是 : 职责的统一**.  代码集中化处理意味着修改时, 可以避免更少的错误.



常见defer的例子 , 就是IO的打开与关闭, 实质上这是IO的同一个职责, 然而该职责的代码分散到方法的前与后, 导致代码不集中.


##### Java如何延迟执行
(实际上没人这样做吧?  只是为了职责统一而特意引入)

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

好处

- 删除代码时候, 不会怕漏掉
- 代码能更好的提取 extract method