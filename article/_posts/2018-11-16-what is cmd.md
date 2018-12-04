---
layout: post
title: cmd是什么？
date: 2018-11-16 00:00:00
categories: 计算机
tags: 计算机
comments: 1
typora-root-url: ..
---

cmd是什么？

> - CMD
>
>    is commonly used to mean "command":
>
>   - [CMD.EXE](https://en.wikipedia.org/wiki/CMD.EXE), Command Prompt on the OS/2 and Windows NT families if operating systems
>
>   - [CMD file (CP/M)](https://en.wikipedia.org/wiki/CMD_file_(CP/M)), the filename extension used by executable programs
>
>     <div style="text-align: right">From wiki</div>



##### 1. cmd是一个exe文件

![1542338002203](/assets/blog_res/what is cmd(1).png)

同类的文件有：java.exe、git.exe、ping.exe等软件。这些软件通过在环境变量。

非同类：”start 路径名“，该类指令并不以exe文件形式存在。 

就命令行窗口而言，这两种方式的表现方法是完全一样，区别在于是否能直接调用。ping、cmd、java等exe文件是可以直接调用并输入参数，而start等指令只能通过调用cmd的形式再进行调用。

因此，在编程时，为了同时能使用命令和exe文件，建议使用

```powershell
cmd /c  命令
```

评价：使用者不得不使用cmd /c命令，也说明当初设计的不恰当。

<br>

##### 2.cmd 文件

cmd和bat文件是几乎相同的，用于存储指令，并直接在命令行窗口里运行。

<br>

##### 命令窗口

cmd和powershell

> PowerShell是跨平台的，cmd是Windows专用的。
> PowerShell有面向对象的管道。
> PowerShell能够调用.NET的很多功能。

参考：PowerShell 与 cmd 有什么不同？ - 桃又的回答 - 知乎
https://www.zhihu.com/question/22611859/answer/252256310