---
layout: post
title: Storage [AWS] - 1.简介
tags: [Cloud]
comments: 1
typora-root-url: ..
excerpt: 关于AWS的Storage Services的笔记。
---

## Amazon Elastic Block Store(EBS)

底层存储，相当于硬盘。

- 一个EBS只能挂载一个EC2，不能挂在两个EC2上。
- 延迟低（比EFS低，EFS是通过网络传输的）

## Amazon Elastic File System(EFS)

文件系统，可以远程连接和共享。一个EFS能挂载多个EC2。

## Amazon Simple Storage Service(S3)

以对象形式进行存储。

- 可以在任何地方访问的 Internet API 来提供数据访问。自由度高。
- 价格便宜

## Amazon S3 Glacier

用于存储很少使用的内容。



- https://aws.amazon.com/cn/efs/faq/
- https://stackoverflow.com/questions/29575877/aws-efs-vs-ebs-vs-s3-differences-when-to-use