---
layout: post
title: ubuntu升级到18，jekyll无法运行，找不到ruby
date :  2018-12-21 22:44
comments: 1
typora-root-url: ..
---

jekyll /usr/bin/env: ‘ruby2.3’: No such file or directory

## 产生原因

背景：从ubuntu 16升级到18。

观察：

当输入

```bash
which ruby
```

很明确发现ruby存在，但是地址并不是/usr/bin/env

<br>

思路：

- [重装ruby](https://jekyllrb.com/docs/installation/windows/)
- 设置环境变量

推荐第一种，因为可能出错的原因并不是环境变量。