---
layout: post
title: 配置文件 INI、YAML、TOML、JSON、XML等比较
date: 2018-11-12 00:00:00
categories: 计算机
tags: 计算机
comments: 1
typora-root-url: ..
---



> 适合人类编写：ini > toml > yaml > json > xml > plist
> 可以存储的数据复杂度：xml > yaml > toml ~ json ~ plist > ini
>
> <div style="text-align: right"> 参考：Belleve</div>
>
> <div style="text-align: right"> YAML，JSON，ini，XML 用来做配置文件，优缺点分别是什么？ - 知乎</div>

<br>

##### INI

```ini
; last modified 1 April 2001 by John Doe
[owner]
name=John Doe
organization=Acme Products

[database]
server=192.0.2.42 ; use IP address in case network name resolution is not working
port=143
file="acme payroll.dat"
```

<br>

##### YAML

```yaml
---
receipt:     Oz-Ware Purchase Invoice
date:        2012-08-06
customer:
    given:   Dorothy
    family:  Gale
   
items:
    - part_no:   A4786
      descrip:   Water Bucket (Filled)
      price:     1.47
      quantity:  4

    - part_no:   E1628
      descrip:   High Heeled "Ruby" Slippers
      size:      8
      price:     133.7
      quantity:  1

bill-to:  &id001
    street: | 
            123 Tornado Alley
            Suite 16
    city:   East Centerville
    state:  KS

ship-to:  *id001   

specialDelivery:  >
    Follow the Yellow Brick
    Road to the Emerald City.
    Pay no attention to the
    man behind the curtain.
...
```

<br>

##### TOML

```toml
# This is a TOML document.

title = "TOML Example"

[owner]
name = "Tom Preston-Werner"
dob = 1979-05-27T07:32:00-08:00 # First class dates

[database]
server = "192.168.1.1"
ports = [ 8001, 8001, 8002 ]
connection_max = 5000
enabled = true

[servers]

  # Indentation (tabs and/or spaces) is allowed but not required
  [servers.alpha]
  ip = "10.0.0.1"
  dc = "eqdc10"

  [servers.beta]
  ip = "10.0.0.2"
  dc = "eqdc10"

[clients]
data = [ ["gamma", "delta"], [1, 2] ]

# Line breaks are OK when inside arrays
hosts = [
  "alpha",
  "omega"
]
```

 TOML是YAML的改进版本。当配置文件过大，层次过高时，YAML的空格用法将成为危害。（其实，编辑器插件就可以解决该问题）

<br>

##### 键值对配置文件 vs XML

![1541942640647](/../assets/blog_res/1541942640647.png)

XML在标签上可以进行扩展，**功能强大**。如果将键值对配置文件比喻成二维图，那么XML即三维图。然而，功能过于强大意味着复杂度也随之增大（这里体现除[约定优于配置](https://zh.wikipedia.org/zh-hans/%E7%BA%A6%E5%AE%9A%E4%BC%98%E4%BA%8E%E9%85%8D%E7%BD%AE)的重要性）。

<br>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!---Students grades are uploaded by months---->
<class_list>
    <student>
        <name>Tanmay</name>
        <grade>A</grade>
    </student>
</class_list>
```

```toml
# This is a TOML document.

title = "TOML Example"

[owner]
name = "Tom Preston-Werner"
dob = 1979-05-27T07:32:00-08:00 # First class dates
```

xml的注释过于同化（同样使用<>符号），难以阅读。单符号的注释，能使注释更接近普通文本。

<br>

##### 总结

- INI文件：单层次的配置。
- YAML、TOML：键值对形式存储，可以有多层次。在有IDE插件情况下，YAML和TOML没有区别。（显示的差异）
- JSON：JSON是YAML的子集。用于数据传输，不用于配置文件。
- XML：数据传输和配置文件均可。功能在JSON、YAML之上，但是可读性差。
- HOCON：配置文件，但是有偏向于编程语言的的倾向。

<br>

<br>

##### 延申

每个仍处于领域，都会经历笼统到精细化，在这当中常常会将没有必要的东西去除，并根据自己领域的特殊性增加自己的需求。

**例子：**

Gralde不使用xml文件格式，原因之一是：Java使用者根本不需要使用到三维这种高度，这种高度的信息无疑是增加了复杂度。

（1）

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
```

在使用Maven时，我们常常会看到这段代码，这段代码做了以下事情

- 添加了标签
- 在Maven代码中配置标签的版本

而作为开发者，根本不关心在打代码时，根本不想去了解标签的版本是多少，有什么标签（暴露过多信息越是容易造成bug）。而且无论是Maven还是Gradle，最终仍旧需要借助外部工具（Google、API文档）

Gradle该点挺好的，通过plugins的形式将没有必要的信息隐藏。

```groovy
plugins {
    id 'java'
    id 'maven'
}
```

从而达到，标签（插件）制作人和使用者的解耦。

<br>

（2）

标签上的attribute可以使用子元素进行代替，根本不需要使用到attribute。HTML上使用到attribute的情况是为了区别**显示内容**与**属性**。而配置文件并没有显示内容。



