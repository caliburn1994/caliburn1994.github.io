---
layout: post
title: Amazon eks
date: 2020-08-24 00:00:03
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

#### aws configure

配置aws

```shell
$ aws configure
AWS Access Key ID [None]: AKIAIOSFODNN7EXAMPLE
AWS Secret Access Key [None]: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Default region name [None]: region-code
Default output format [None]: json
```

#### aws iam

AWS Identity and Access Management (IAM)：身份和访问管理

**aws iam**：可用于创建IAM账号等。IAM账号可以理解成子账号：

```shell
# 创建用户
aws iam create-user --user-name ${用户名}
# 创建密钥，并打印出来
aws iam create-access-key --user-name ${用户名} | tee /tmp/PaulAdmin.json
```

#### aws sts

AWS Security Token Service (AWS STS)：安全令牌服务

**aws sts get-caller-identity**：获得调用者的身份。

```shell
$ aws sts get-caller-identity
{
    "UserId": "AIDAZ6XFP3QJWWD4DQEU5I",
    "Account": "6844542806125",
    "Arn": "arn:aws:iam::6844542806125:user/kyakya01"
}
```

#### aws eks

文档网址：[eks — AWS CLI 1.18.97 Command Reference](https://docs.aws.amazon.com/cli/latest/reference/eks/index.html)

在这个命令下，可以创建集群。[更新kubeconfig配置文件](https://docs.aws.amazon.com/cli/latest/reference/eks/update-kubeconfig.html)

##### update-cluster-config

更新集群配置。配置包含：

- 是否将Control Plane的日志导出到CloudWatch
- k8s API服务是否公开、是否私有

#### update-kubeconfig

##### 将集群配置复制到本地k8s环境里（即，配置`${HOME}/.kube/config`）

```shell
$ aws eks update-kubeconfig --name example
Added new context arn:aws:eks:us-west-2:012345678910:cluster/example to /Users/ericn/.kube/config
```

##### update-nodegroup-config

[更新节点群](https://docs.aws.amazon.com/cli/latest/reference/eks/update-nodegroup-config.html)<sup>Node Group</sup>的配置。

### eksctl命令行工具 

```sh
$ eksctl help
The official CLI for Amazon EKS

Usage: eksctl [command] [flags]

Commands:
  eksctl completion                      Generates shell completion scripts for bash, zsh or fish
  eksctl create                          Create resource(s)
  eksctl delete                          Delete resource(s)
  eksctl drain                           Drain resource(s)
  eksctl enable                          Enable features in a cluster
  eksctl generate                        Generate gitops manifests
  eksctl get                             Get resource(s)
  eksctl help                            Help about any command
  eksctl scale                           Scale resources(s)
  eksctl set                             Set values
  eksctl unset                           Unset values
  eksctl update                          Update resource(s)
  eksctl upgrade                         Upgrade resource(s)
  eksctl utils                           Various utils
  eksctl version                         Output the version of eksctl

Common flags:
  -C, --color string   toggle colorized logs (valid options: true, false, fabulous) (default "true")
  -h, --help           help for this command
  -v, --verbose int    set log level, use 0 to silence, 4 for debugging and 5 for debugging with AWS debug logging (default 3)

Use 'eksctl [command] --help' for more information about a command.

```

`eksctl` 和 `asw eks` 做的事情几乎一样，不过`eksctl` 更为方便简洁。

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
