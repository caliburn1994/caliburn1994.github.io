---
layout: post
title: Kubernetes描述文件
date: 2020-05-26 22:00:02
categories: 计算机
tags: [鸦鸦的维基,kubernetes]
comments: 1
---

## 概要

K8s把对象分为两个状态：**期望状态**<sup>Desired State</sup> 和 **当前状态**<sup>Current state</sup>。

通过`kubectl get pods [Pod名字] -o json`开发者可以查看对象对象**当前状态**和**期望状态**。

```json
{
  "apiVersion": "v1",   	# API版本
  "kind": "Pod",			# 对象类型
  "metadata": {...},		# 元数据
  "spec": {...},   			# 期待状态
  "status": {...}, 			# 当前状态
}
```

K8s的文档基本上是遵循

## 描述文件

### apiVersion & kind & metadata

示例文件如下，完整示例点击[此处](https://kubernetes.io/docs/concepts/overview/working-with-objects/kubernetes-objects/)：

```yaml
apiVersion: apps/v1 # for versions before 1.9.0 use apps/v1beta2
kind: Deployment
metadata:
  name: nginx-deployment
```

- `apiVersion` ： Kubernetes API 的版本。
- `kind`：对象的类型。常见的对象有：[Pod](/Kubernetes#Pod)、[Deployment](/Kubernetes#Deployment)
- `metadata` - 帮助识别对象唯一性的数据。
  -  `name` <sup>名字</sup>，每个类型的资源之间名字不可以重复。调用命令时常用的参数就是名字。
  - UID ，每个生命周期的每个对象UID都不同，用于标识对象的历史。<sup>[[1]][1]</sup>
  -  `namespace`<sup>命名空间</sup>（可选的） ，用于需要跨团队或跨项目的场景。<sup>[[2]][2]</sup>

### spec

```yaml
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 2	#
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

- selector<sup>标签选择器</sup>：在不同对象中有不同的解释，在 [Deployment](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)解释为帮助 Deployment找到对应的Pod进行管理。<sup>[[3]][3]</sup>
- replicas：创建的个数。<sup>[[3]][3]</sup>
- template：PodTemplates用于创建Pod的式样书，用于 [Deployment](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)、[Job](https://kubernetes.io/docs/concepts/jobs/run-to-completion-finite-workloads/)、[DaemonSet](https://kubernetes.io/docs/concepts/workloads/controllers/daemonset/)等对象。<sup>[[3]][3][[4]][4]</sup>



[1]: https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/names/
[2]: https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/namespaces/
[3]: https://kubernetes.io/docs/concepts/workloads/controllers/deployment/
[4]: https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/#pod-templates

