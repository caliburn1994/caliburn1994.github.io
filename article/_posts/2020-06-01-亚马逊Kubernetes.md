---
layout: post
title: Amazon eks
date: 2020-06-01 00:00:02
tags: 鸦鸦的维基
comments: 1
excerpt:
typora-root-url: ..
---

## 定义

Amazon Elastic Kubernetes Service

## 学习指南

[官网](https://aws.amazon.com/cn/eks/getting-started/?nc1=h_ls)有两个教程：

- [使用 Amazon EKS 部署 Kubernetes 应用程序](https://aws.amazon.com/getting-started/projects/deploy-kubernetes-app-amazon-eks/)（[Deploy a Kubernetes Application with Amazon EKS](https://aws.amazon.com/getting-started/projects/deploy-kubernetes-app-amazon-eks/)）
- [完成 Amazon EKS 微服务研讨会](https://eksworkshop.com/)（[Complete Amazon EKS Microservices Workshop](https://eksworkshop.com/)）

![image-20200629162120214](/../assets/blog_res/image-20200629162120214.png)

### 教程一





## 入门实操

### 搜索教程

1. `aws k8s tutorial`搜索🔍教程

2. 点击[官网地址](https://aws.amazon.com/cn/getting-started/hands-on/deploy-kubernetes-app-amazon-eks/)了解到Amazon EKS是关于K8s的服务，点击并进行[教程](https://docs.aws.amazon.com/zh_cn/eks/latest/userguide/getting-started.html)。

### 教程

1. [安装](https://docs.aws.amazon.com/zh_cn/eks/latest/userguide/getting-started-eksctl.html) aws
2. 添加 aws 的[自动补充](/usr/local/bin/aws_completer)<sup>Tab completion</sup>
3. 为aws添加[配置](https://docs.aws.amazon.com/zh_cn/cli/latest/userguide/cli-chap-configure.html)/凭证：

配置中需要创建一个IAM 管理员，创建后为aws添加该管理员的密钥ID和接入密钥。

- 地区（Default region name）通过搜索`aws region for [地区名]`可搜到。

...

4. 安装`kubectl`

5. 安装 `eksctl`

6. 要开始创建集群，这里需要用到STS，不过我们先跳过，用管理员权限执行。参考[笔者StackOverflow的回答](https://stackoverflow.com/a/62513754/4883754)。


##节点

[教程地址](https://docs.aws.amazon.com/zh_cn/eks/latest/userguide/cluster-autoscaler.html)

### Managed node groups

EKS受管理的节点组<sup>Amazon EKS managed node groups</sup>将对节点<sup>Node</sup>自动与配置以及生命周期管理。好处：<sup>[[官网]](https://docs.aws.amazon.com/zh_cn/eks/latest/userguide/managed-node-groups.html)</sup>

- 通过命令就可以管理节点
- 节点可以自动伸缩<sup>Auto Scaling</sup>

在Amazon中<u>节点</u>与<u>Amazon EC2实体</u><sup>instance</sup> 是等价





## 概念

### Amazon EKS

Amazon EKS（**Amazon** **E**lastic Container Service for **K**ubernete**s**）亚马逊Kubernetes弹性容器服务。

### AWS STS

AWS Security Token Service (STS)，AWS安全令牌服务：

### IAM

#### 如何添加策略？

[参考地址](https://docs.aws.amazon.com/zh_cn/eks/latest/userguide/security_iam_id-based-policy-examples.html)

首先，选择创建Policy

![image-20200622012139828](/../assets/blog_res/image-20200622012139828.png)

创建方式有两种：

- 通过点击选择内容
- 通过JSON提交内容

选择/填写内容后提交即可。

![image-20200622012359875](/../assets/blog_res/image-20200622012359875.png)

#### 常见的IAM策略





### Amazon EC2

Amazon **E**lastic **C**ompute **C**loud (Amazon **EC2**) 是一种 Web 服务，可以在云中提供安全并且可调整大小的计算容量。换言之，EC2是虚拟服务器。

### AWS Fargate

一种适用于容器的无服务器计算引擎 **todo**