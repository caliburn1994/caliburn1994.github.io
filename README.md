---
typora-root-url: ./
typora-copy-images-to: assets\blog_res\README
---

## 文章

每一篇使用 markdown 编写的文章有以下内容

```yaml
layout: post 
title: <文章标题>
tags: <文章标签>
comments: 1 # 评论功能
typora-root-url: .\  # 在typora编辑器插入图片时，根目录的位置
typora-copy-images-to: xxx # 在typora编辑器插入图片时，将插入到哪个地方
```

文章的写法如下：

```yaml
## XXX的介绍

这里是参考XXX的[^1]....

段落 **主语** 使用两个星星去包着。段落中有 *关键语句* 使用一个星星去包着。

## 参考 References

[^1]: [Why xxxxx? - StackOverflow](网址)
```

## 功能

- [jekyll-last-modified-at](https://github.com/gjtorikian/jekyll-last-modified-at) 获得文件的最后修改时间
- jekyll-paginate-v2 分页功能
- [TOC（文章目录）功能](https://github.com/allejo/jekyll-toc)
- Disqus
- Google Analytics

## 参考 References

- [页面原型](https://shawnteoh.github.io/matjek/) 
