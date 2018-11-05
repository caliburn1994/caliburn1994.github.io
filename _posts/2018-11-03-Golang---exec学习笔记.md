---
layout: post
title: Golang - exec学习笔记
date: 2018-11-03 00:00:01
categories: 计算机
tags: Go
comments: 1
---

os.exec应该如何学习？

<br>

##### 环境

操作系统：window 10

IDE：Goland		

*[Goland]: intellij系列的go语言IDE编辑器。

Go：1.10

<br>

##### 案例

**简单例子**

参考：[Golang - exec控制台乱码](/2018/11/03/Golang-exec控制台乱码)

<br>

```go
	cmd := exec.Command("git", "push","origin","master")
	cmd.Dir = "F:/GitHub/caliburn1994.github.io/_includes"
	out, err := cmd.CombinedOutput()
	if err != nil {
		log.Fatal(err)
		fmt.Println(fmt.Sprint(err) + ": " + string(out))
	}

	gbk, err := GbkToUtf8(out)
	if err != nil {
		log.Fatal(err)
		fmt.Println(err)
	}
	fmt.Printf("Result: %s", gbk)

```

问题：

1.  git push origin master为何要拆分成若干参数？
2. 复合command该如何写