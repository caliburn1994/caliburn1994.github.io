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

##### JSON vs XML

![1541942640647](/assets/blog_res/1541942640647.png)

XML和JSON区别：XML能在标签上进行扩展。如果将JSON比喻成二维图，那么XML即三维图。

<br>

##### 总结&&延申

- INI文件：单层次的配置。
- YAML、TOML：键值对形式存储，可以有多层次。在有IDE插件情况下，YAML和TOML没有区别。（显示的差异）
- JSON：JSON是YAML的子集。用于数据传输，不用于配置文件。
- XML：数据传输和配置文件均可。功能在JSON、YAML之上，但是可读性差。
- HOCON：配置文件，但是有偏向于编程语言的的倾向。