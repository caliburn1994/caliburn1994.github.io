---
layout: post
title: 未命名
date: 2020-05-21 00:00:02
categories: 计算机
tags: 鸦鸦的维基,kubernetes
comments: 1
---

## 概要

K8s把对象分为两个状态：**期望状态**<sup>Desired State</sup> 和 **当前状态**<sup>Current state</sup>。 通过使用`kubectl describe`命令，开发者可以查看对象对象**当前状态**；通过定义文件，开发者可以了解对象的**期望状态**

## 定义文件

示例文件如下：

```yaml
apiVersion: apps/v1 # for versions before 1.9.0 use apps/v1beta2
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 2 # tells deployment to run 2 pods matching the template
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.14.2
        ports:
        - containerPort: 80
```

- `apiVersion` ： Kubernetes API 的版本。
- `kind`：对象的类型。对象
- `metadata` - 帮助识别对象唯一性的数据，包括一个 `name` 字符串、UID 和可选的 `namespace`



```
<sup>[[1]][1]</sup>
<sup>[[2]][2]</sup>
<sup>[[3]][3]</sup>
<sup>[[4]][4]</sup>
```



## 外部链接

K8s的参考地址有：

- [官方 -  Understanding Kubernetes Objects](https://kubernetes.io/docs/concepts/overview/working-with-objects/kubernetes-objects/)