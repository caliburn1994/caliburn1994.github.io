---
layout: post
title: AWS-AIM-Service Role示例
date: 2020-06-29 00:00:02
tags: [AWS]
comments: 1
typora-root-url: ..
---

> A service role is a role that an AWS service assumes to perform actions on your behalf.

Service Role是一种role，由AWS服务承担并负责的。该AWS服务的行为代表着你的行为。传统意义上，Role是由用户、用户群进行承担负责的，用户承担角色后就拥有权限进行一系列操作，但是有些场景需要由AWS服务进行承担负责。

外部连接：[官方文档 - IAM Service Roles](https://docs.aws.amazon.com/aws-backup/latest/devguide/iam-service-roles.html)

## 教程

官网教程示例可参考[此处](https://www.eksworkshop.com/020_prerequisites/iamrole/)。

### 创建角色服务

创建角色服务<sup>Service Role</sup>步骤如下：

步骤一：

![image-20200629165803671](/../assets/blog_res/image-20200629165803671.png)

步骤二：

![image-20200629165905083](/../assets/blog_res/image-20200629165905083.png)

步骤三：

![image-20200629165934212](/../assets/blog_res/image-20200629165934212.png)

步骤四：

![image-20200629165959635](/../assets/blog_res/image-20200629165959635.png)

步骤五：

![image-20200629170032254](/../assets/blog_res/image-20200629170032254.png)

结果：

![image-20200629170242891](/../assets/blog_res/image-20200629170242891.png)

### 作用

在这个[教程](https://www.eksworkshop.com/020_prerequisites/workspace/)中，我们创建了Cloud9虚拟机实例。AWS Cloud9 是一种基于云的集成开发环境 (IDE)。创建后：

![image-20200629171059650](/../assets/blog_res/image-20200629171059650.png)

此时我们将为服务添加角色：

步骤一：

![c9instancerole](https://www.eksworkshop.com/images/c9instancerole.png)

步骤二：

![c9attachrole](https://www.eksworkshop.com/images/c9attachrole.png)

添加后，在更新一下Cloud9的[凭证](https://www.eksworkshop.com/020_prerequisites/workspaceiam/)，Cloud9就有用了管理员权限（[AdministratorAccess](https://console.aws.amazon.com/iam/home#/policies/arn%3Aaws%3Aiam%3A%3Aaws%3Apolicy%2FAdministratorAccess?section=permissions)）了。

## 观察与总结

角色服务是用于赋予并管理某一群服务权限。