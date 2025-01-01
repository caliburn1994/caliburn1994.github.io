---
Last Modified: 2020-5-31
---

## 1. 概览

本文只泛泛讨论下述文件的区别，但由于这些功能与语法上仍旧有细微的区别，可能不适用于某些特殊情况。

**可读性：ini > toml ~ yaml > json > xml** 

**可以存储的数据复杂度：xml > yaml ~ toml ~ json > ini**

- INI文件：伪二维的文件，极其简单。
- YAML、TOML：二维文件，区别在于语法（样子）。
- JSON：二维文件，可读性相对弱，**无法使用注释**，不应用于配置文件。现广泛用于数据传输，。
- XML：数据传输和配置文件均可。功能在JSON、YAML之上，但是可读性差。

## 2. 概念

### 2.1 维度

我们根据文件的延展性，大致将定义文件分以下几种：

#### 2.1.1 一维

一维文件可y轴自由衍生。这类文件我们可以视为仅仅的[key-value](https://en.wikipedia.org/wiki/Attribute%E2%80%93value_pair)形式。

```
[key-value]
     |
[key-value]
     |
[key-value]
     |
    ...
    
     ↑
    [y轴]
```

#### 2.1.2 二维

二维文件可x轴、y轴自由延伸。此类文件的值<sup>value</sup>数据结构从<u>单纯的值</u>变成<u>对象</u>，即<u>key-object</u>，对象当中可以存放值<sup>value</sup>、数组<sup>array</sup>、键<sup>key</sup>等等，通过存放键<sup>key</sup>从而获得x轴延伸能力。

```
[  key  ]                   	
    |--[key]--[key]--[key]--[...]--[value]   ←x轴
[key-value]
    |
[key-value]
    |
[key-value]
	|
  [...]
    ↑
   y轴
```

#### 2.1.3 伪二维

```
[section]                    ←x轴延伸了一个单位
     |---[key-value]
     |---[key-value]
     |---[key-value]
    ...
    
     ↑
    [y轴]
```

#### 2.1.4 三维

三维文件则是在二维之上，键<sup>key</sup>的数据机构从<u>单纯的值</u>变成<u>对象</u>，从而获得z轴延伸能力。而当键<sup>key</sup>中可存放键<sup>key</sup>时，z轴可无限伸展。

```
[  key  ]                   	
    |--[key]--[key]--[key]--[...]--[value]   ←x轴
[key-value]             \
    |				   [key]
[key-value]				   \
    |					  [key]
[key-value]                  \
	|						[....]  ←z轴
  [...]
    ↑
   y轴
```

#### 2.1.5 伪三维

```
[  key  ]                   	
    |--[key]--[key]--[key]--[...]--[value]   ←x轴
[key-value]             \
    |				 [attribute] ←z轴延伸了一个单位
[key-value]				  
    |					
[key-value]               
	|						
  [...]
    ↑
   y轴
```

### 2.2 结构表达方式

根据结构表达方式的不同，可读性也是会不同的。IDE等外部软件可以提高结构表达方式的效果。常见的有以下：

#### 2.2.1 Tab

Tab符号有助于层次分明。但如果**纯粹使用Tab符号切割模块**且**层数过多**，如示例所见，我们及有可能将val8误认为与val4层数相同，这意味我们将难以插入数据与查看数据：

```
val1:
	val2:
		val3:
			val4:
				val5:
					val6:
						val7:
						...
						...
						...
		val8:
```

我们在编写Python语言时，也是会出现这种问题。

#### 2.2.2 括号

括号<sup>bracket</sup>，能很好的定义结构，因为有开始符号与结束符号。过多的括号将会占用阅读空间，降低可读性。如下：

```
  "phoneNumbers": [
    {
      "type": "home",
      "number": "212 555-1234"
    },
    {
      "type": "office",
      "number": "646 555-4567"
    }
  ],
```

```
  "phoneNumbers": [
    - "type": "home",
      "number": "212 555-1234"
    - "type": "office",
      "number": "646 555-4567"
  ],
```

#### 2.2.3 标签

xml等文件的阅读性比括号更为差，因为标签<sup>tag</sup>占用的空间更大。但是标签<sup>tag</sup>自身可被视为一个对象，通过添加属性<sup>attribute</sup>可增加延伸性。

```
<tag attribute="">
</tag>
```



## 3. 详解

### 3.1 INI

INI文件可视为**伪二维**。

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

### 3.2 JSON

JSON文件可视为**二维**。

```json
{
  "firstName": "John",
  "lastName": "Smith",
  "isAlive": true,
  "age": 27,
  "address": {
    "streetAddress": "21 2nd Street",
    "city": "New York",
    "state": "NY",
    "postalCode": "10021-3100"
  },
  "phoneNumbers": [
    {
      "type": "home",
      "number": "212 555-1234"
    },
    {
      "type": "office",
      "number": "646 555-4567"
    }
  ],
  "children": [],
  "spouse": null
}
```

### 3.3 YAML

YAML文件可视为**二维**。

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

### 3.4 TOML

TOML文件可视为**二维**。

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

### 3.5 XML

XML文件可视为**伪三维**。XML通过属性<sup>attribute</sup>进行进一步维度延伸，功能比上述的其他配置文件强大，但是正因如此，可读性更弱。如图所示：

**XML vs JSON**

![1541942640647.png](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/100efa26afd258b89d00469447d2f9d98b6a0f00/images/202212181437692.png)



**XML vs TOML**

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

## 4. 应用

每个仍处于领域，都会经历笼统到精细化，在这当中常常会将没有必要的东西去除，并根据自己领域的特殊性增加自己的需求。

### 4.1 HTML

标签上的属性<sup>attribute</sup>通常可使用元素进行代替。HTML上使用到属性<sup>attribute</sup>是为了区别**显示内容**与**内容的属性**。而配置文件并没有显示内容。

### 4.2 Gradle

Gradle不使用xml文件格式，原因之一是：Java使用者根本不需要使用到伪三维这种高度，Gradle的键值不需要属性。但Maven却使用了XML，这是为什么呢？

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
```

在使用Maven时，我们只在这段代码看到属性<sup>attribute</sup>，这段配置做了以下事情：

- 选择标签版本
- 根据标签版本，增加可用标签以及使用规则

而作为开发者，根本不想去了解标签的版本是多少，有什么标签。Gradle则是通过plugins的形式将没有必要的信息隐藏，配置文件相关信息放在其他配置文件里。

```groovy
plugins {
    id 'java'
    id 'maven'
}
```

## 延伸阅读

- [TOML#Comparison to other formats](https://en.wikipedia.org/wiki/TOML)