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

#### 案例

##### 例1（简单）

参考：[Golang - exec控制台乱码](/Golang-exec控制台乱码)

<br>

##### 例2（不使用cmd）

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

**func Command(name string, arg ...string)** 

参数1需是Path存在的指令，如：Ping、git、Java。且

```bash
git push origin master
```

不能放到同一参数中（如：exec.Command("git push origin master")）。

代替方案：

```go
    // Split string 分割参数
    // 通过csv文档的特性进行分割
    r := csv.NewReader(strings.NewReader(command))
    r.Comma = ' ' // space
    paras, err := r.Read()
    if err != nil {
       fmt.Println(err)
       return
    }
    //运行
    cmd := exec.Command("git", paras...)
```

<br>

##### 例3（使用cmd，建议）

使用cmd的话，则比较简单

```go
cmd := exec.Command("cmd","/c","git push origin master")
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
window下，使用cmd可以直接使用各种命令，而非cmd则只能使用[环境变量](https://baike.baidu.com/item/%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F)中Path下的程序。如：java、ping、git等，而指令start则不可以。

然而例2的方式可在Linux下使用。（cmd是window的指令）

参考：[cmd是什么？](/what-is-cmd)



<br>

**CombinedOutput()、Output()、run()关系**

run()：运行指令

Output()：运行指令，并分别返回错误和正常的结果。

CombinedOutput()：运行指令，不管正常与否，均打印在一起。

*[在一起]:combine

