---
layout: post
title: nanogallery2 现代化相册框架
date: 2018-11-22 00:00:00
categories: 计算机
tags: 前端
comments: 1
typora-root-url: ..
---



[nanogallery2](https://nanogallery2.nanostudio.org/)是用于一款现代化的相册和幻灯片。

> a modern photo / video gallery and lightbox

推荐理由：复制黏贴即可用，不需要过多的前端知识。

<br>

##### 实现效果

![1542898212058](/assets/blog_res/1542898212058.png)

[主页地址](https://nanogallery2.nanostudio.org/)有实现效果的展示。（图片是墙外的，需要翻墙）

##### 简单代码实现



```html
 <html>
        <head>
            <meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">          
            
            <!-- jQuery -->
            <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js" type="text/javascript"></script>
          
            <!-- nanogallery2 -->
            <link  href="https://unpkg.com/nanogallery2@2.3.0/dist/css/nanogallery2.min.css" rel="stylesheet" type="text/css">
            <script  type="text/javascript" src="https://unpkg.com/nanogallery2@2.3.0/dist/jquery.nanogallery2.min.js"></script>
        </head>
        <body>
        
              <h1>gallery made with nanogallery2</h1>

              <!-- ### start of the gallery definition ### -->
              <div id="nanogallery2"
              
                  <!-- gallery settings -->
                  data-nanogallery2 = '{
                        "thumbnailHeight":  150,
                        "thumbnailWidth":   150,
                        "itemsBaseURL":     "http://nanogallery2.nanostudio.org/samples/"
                      }' >
                      
                  <!-- gallery content -->
                  <a href = "berlin1.jpg"   data-ngThumb = "berlin1_t.jpg" > Berlin 1 </a>
                  <a href = "berlin2.jpg"   data-ngThumb = "berlin2_t.jpg" > Berlin 2 </a>
                  <a href = "berlin3.jpg"   data-ngThumb = "berlin2_t.jpg" > Berlin 3 </a>
              </div>
              <!-- ### end of the gallery definition ### -->
            
          </body>
      </html>
```

3个引用：

```html
 <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js" type="text/javascript"></script>
```

```html
<link  href="https://unpkg.com/nanogallery2@2.3.0/dist/css/nanogallery2.min.css" rel="stylesheet" type="text/css">
```

```html
<script  type="text/javascript" src="https://unpkg.com/nanogallery2@2.3.0/dist/jquery.nanogallery2.min.js"></script>
```

图片来源：

```html
<a href = "berlin1.jpg"   data-ngThumb = "berlin1_t.jpg" > Berlin 1 </a>
```

配置：

```html
data-nanogallery2 = '{
                        "thumbnailHeight":  150,	每张图片的高
                        "thumbnailWidth":   150,	每张图片的宽
                        "itemsBaseURL":     	"http://nanogallery2.nanostudio.org/samples/"	图片的网址
                      }' >
```

更多的配置，可以查看[主页](https://nanogallery2.nanostudio.org/)。

<br>

##### 延申



<br>

隐藏内容：

<p style="  color:#ffffff">
关键词：
- 基于框架的Google photos 相册实现。
- 基于框架的500px实现。
- 基于框架的响应式相册的实现。
</p>