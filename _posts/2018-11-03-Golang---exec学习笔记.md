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

**例1（简单）**

参考：[Golang - exec控制台乱码](/2018/11/03/Golang-exec控制台乱码)

<br>

##### 例2

```go
	cmd := exec.Command("git", "push","origin","master")
	cmd.Dir = "F:/GitHub/caliburn1994.github.io/_includes"
	out, err := cmd.CombinedOutput()
	if err != nil {
		log.Fatal(err)
		fmt.Println(fmt.Sprint(err) + ": " + string(out))
	}

	result, err := GbkToUtf8(out)
	if err != nil {
		log.Fatal(err)
		fmt.Println(err)
	}
	fmt.Printf("Result: %s", result)

```

API:

**func Command(name string, arg ...string)  ** 

参数1需是Path存在的指令，如：Ping、git、Java。且

```bash
git push origin master
```

不能放到同一参数中（如：exec.Command("git push origin master")）。

代替方案：

```go
// Split string 分割参数
r := csv.NewReader(strings.NewReader(command))
r.Comma = ' ' // space
paras, err := r.Read()
if err != nil {
   fmt.Println(err)
   return
}
```

<br>

**CombinedOutput()、Output()、run()关系**

run()：运行指令

Output()：运行指令，并分别返回错误和正常的结果。

CombinedOutput()：运行指令，不管正常与否，均打印在一起。

*[在一起]:combine

