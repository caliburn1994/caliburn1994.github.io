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

 亚马逊弹性Kubernetes服务Amazon **E**lastic **K**ubernetes **S**ervice (Amazon EKS)

## 学习指南

[官网](https://aws.amazon.com/cn/eks/getting-started/?nc1=h_ls)有两个教程：

- [使用 Amazon EKS 部署 Kubernetes 应用程序](https://aws.amazon.com/getting-started/projects/deploy-kubernetes-app-amazon-eks/)（[Deploy a Kubernetes Application with Amazon EKS](https://aws.amazon.com/getting-started/projects/deploy-kubernetes-app-amazon-eks/)）
- [完成 Amazon EKS 微服务研讨会](https://eksworkshop.com/)（[Complete Amazon EKS Microservices Workshop](https://eksworkshop.com/)）

![image-20200629162120214](/../assets/blog_res/image-20200629162120214.png)

### 教程一

不建议将<u>用户指南</u>作为入门教程。

### 教程二

[教程二](https://www.eksworkshop.com/010_introduction/)使用的是云IDE编辑器Cloud9，教程教的也是Amazon EKS的功能。而云编辑器是运行在云服务器上，所以需要付费，该费用并<u>不包含在免费使用之内</u>。该教程比教程一更容易入门，因为是一个教程接着一个教程。

## 命令行工具

eks有三个命令行工具`aws`、`eksctl`、`kubectl`。其中`kubectl`是[Kubernetes](/kubernetes)的命令行工作。

### aws命令行工具

**aws configure**：在配置完aws账户中的访问密钥ID、密钥、地区等之后，我们的命令行就算是登陆成功。[示例](https://docs.aws.amazon.com/zh_cn/eks/latest/userguide/getting-started-eksctl.html)

**aws iam**：可用于创建IAM账号等。IAM账号可以理解成子账号：

```shell
# 创建用户
aws iam create-user --user-name ${用户名}
# 创建密钥，并打印出来
aws iam create-access-key --user-name ${用户名} | tee /tmp/PaulAdmin.json
```

iam创建

### eksctl命令行工具 

**eksctl create cluster**：创建集群（以及受管理的节点群）。[示例1](https://docs.aws.amazon.com/zh_cn/eks/latest/userguide/getting-started-eksctl.html)、[示例2](https://www.eksworkshop.com/030_eksctl/launcheks/#create-an-eks-cluster)

```shell
eksctl create cluster \
--name prod \
--version 1.16 \
--region us-west-2 \
--fargate
```

**eksctl get iamidentitymapping**：获得iam映射

```shell
$ eksctl get iamidentitymapping --cluster ${集群名}
arn:aws:iam::xxxxxxxxxx:role/eksctl-quick-nodegroup-ng-fe1bbb6-NodeInstanceRole-1KRYARWGGHPTT	system:node:{{EC2PrivateDNSName}}	system:bootstrappers,system:nodes
arn:aws:iam::xxxxxxxxxx:role/k8sAdmin           admin					system:masters
arn:aws:iam::xxxxxxxxxx:role/k8sDev             dev-user
arn:aws:iam::xxxxxxxxxx:role/k8sInteg           integ-user
```





## 概念

### Amazon EKS

Amazon EKS（**Amazon** **E**lastic Container Service for **K**ubernete**s**）亚马逊Kubernetes弹性容器服务。

### AWS STS

AWS Security Token Service (STS)，AWS安全令牌服务：

### AWS IAM

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


