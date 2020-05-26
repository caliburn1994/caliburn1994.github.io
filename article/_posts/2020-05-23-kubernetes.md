---
layout: post
title: Kubernetes
date: 2020-05-23 00:00:02
categories: 计算机
tags: [鸦鸦的维基,kubernetes]
comments: 1 
typora-root-url: ..\..
---

Kubernetes 与传统的 [PaaS](https://zh.wikipedia.org/wiki/平台即服务) 不同，它更多的被称为 容器的PaaS 或者 CaaS（Containers as a service，容器即服务）。

## 概述

<!--
Most operations can be performed through the [kubectl](https://kubernetes.io/docs/reference/kubectl/overview/) command-line interface or other command-line tools, such as [kubeadm](https://kubernetes.io/docs/reference/setup-tools/kubeadm/kubeadm/), which in turn use the API. However, you can also access the API directly using REST calls.
-->

开发者使用 Kubernetes 有两种方式：命令行工具（如：[kubectl](https://kubernetes.io/docs/reference/kubectl/overview/) 、 [kubeadm](https://kubernetes.io/docs/reference/setup-tools/kubeadm/kubeadm/)）、[客户端库](https://kubernetes.io/docs/reference/using-api/client-libraries/)。而这些连接方式均是调用REST API，即调用[Kubernetes API](#kubernetes-api)。<sup>[[1]][1]</sup> （本文以[kubectl](https://kubernetes.io/docs/reference/kubectl/overview/)作为示例进行介绍）

和Docker一样，开发者进行部署有两种方式：

- 通过使用YAML文件记录 Kubernetes 相关信息，然后使用`kubectl apply -f  文件名`命令进行运行。
- 直接通过命令行部署。

当应用开发完毕后，应用将通过（Docker）容器进行包裹，通过上传最新版本的容器，可达到“发布应用”的效果。

<!--
Pods are designed to support multiple cooperating processes (as containers) that form a cohesive unit of service. The containers in a Pod are automatically co-located and co-scheduled on the same physical or virtual machine in the cluster. The containers can share resources and dependencies, communicate with one another, and coordinate when and how they are terminated.
-->

当多个容器需要紧密的联系时，就需要 [Pod](#Pod) 提供支持。<sup>[[2]][2]</sup> [Pod](#Pod)管理的信息有应用的版本、应用所需的网络资源和存储资源。需理解的是，容器必须存储在[Pod](#Pod)当中，[Pod](#Pod)是K8s部署的最小单位，不管Pod中有一个容器还是多个容器。

<!--
Kubernetes also contains higher-level abstractions that rely on controllers to build upon the basic objects, and provide additional functionality and convenience features. 
-->

K8s中拥有各种**控制器以及对应对象**，有的用于确保集群中Pod的数量；有的用于确保每个节点都拥有一个Pod；有的用于定时操作。<sup>[[3]][3]</sup> 

在集群里，为了防止应用突然间停止而导致无法使用，一个应用所在的Pod往往有多份副本，其中一个[Pod](#Pod)突然间停止也无所谓，有其他代替品。而每个[Pod](#Pod)都有自身的IP地址，但由于[Pod](#Pod)可能会突然间停止，所以我们不能固定地使用某一个[Pod](#Pod)的IP地址，因此我们希望有一种机制，“当一个[Pod](#Pod)不能使用时，该机制帮我们自动连接到其他[Pod](#Pod)上”。这种机制就叫**服务**。<sup>[[4]][4]</sup> 

k8s的默认服务只能在集群中调用，常见的用法是前端调用后端服务。而当要在集群外进行调用时，就可以使用云服务商提供的负载均衡器。

一个外部入口（如网站）只有一个IP地址，如果想通过一个外部入口访问集群里若干个服务时该怎么办呢？只能通过URL的路径进行映射。如：通过`http://ip地址/服务1` 访问 `服务1`，`http://ip地址/服务2` 访问 `服务2`。这种机制叫做 **Ingress（入口）**

## 术语

### Kubernetes API

Kubernetes API是[REST API](https://zh.wikipedia.org/wiki/REST) 是，Kubernetes 的基础组件。组件间的操作和通信，以及外部用户命令都是通过该API完成的，因此，**Kubernetes平台里的任何对象都可以说是该API的执行对象**。该API由 API服务器（[kube-apiserver](#kube-apiserver)）管理。

### 基础对象

#### Pod

Pod<sup>（直译：豆荚）</sup>是K8s的最小单元<sup>（atomic unit）</sup>。Pod封装了若干个[容器](https://www.docker.com/resources/what-container)，各容器可共享同一[IP地址](https://zh.wikipedia.org/wiki/IP地址)和[端口](https://zh.wikipedia.org/wiki/通訊埠)、存储资源（存储卷）。

通常，一个Pod只会装载一个容器，除非各容器间需要使用[文件系统](https://zh.wikipedia.org/wiki/文件系统)（存储资源）进行通讯。

参考：[官网](https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/)、[Google Cloud](https://cloud.google.com/kubernetes-engine/docs/concepts/pod)

#### 服务（service）

服务<sup>（service）</sup>，可以理解为逻辑上的Pod。开发者通过服务的DNS名称名（DNS name）可以找到服务，然后通过服务可以调用某一Pod。<sup>[[5]][5]</sup> 调用方 通过调用服务的方式，避免了调用方与Pod的[耦合](https://zh.wikipedia.org/wiki/耦合性_(計算機科學))，这样当Pod宕机时，也不会影响到调用方，这也可用于[负载均衡](https://zh.wikipedia.org/wiki/负载均衡)、[服务发现](https://zh.wikipedia.org/wiki/服务发现)等场景。<sup>[[6]][6]</sup> 

```
                         选择一个Endpoint进行调用
            |-----------| -------- Endpoint 1 -----> [Pod 1 暴露端口:80 IP:10.244.0.25]
            |           |        [10.244.0.25:80]
   调用 -->  | service   | -------- Endpoint 2 -----> [Pod 2 暴露端口:80 IP:10.244.0.26] 
            |10.96.92.53|        [10.244.0.26:80]      
            |-----------| -------- Endpoint 3 -----> [Pod 3 暴露端口:80 IP:10.244.0.27] 
                                 [10.244.0.27:80]
```

#### 标签和选择器

开发者可通过**选择器**<sup>（全称：标签选择器，Label selector）</sup>查找到对应拥有**标签**<sup>（label）</sup>的对象，并其进行指定。**标签**是键值对结构体数据。

### 高级对象

**高级对象**是依赖于[控制器](https://kubernetes.io/docs/concepts/containers/overview/) ，而[控制器](https://kubernetes.io/docs/concepts/containers/overview/)是建立于基础对象之上，并为之添加功能性和方便性。<sup>[[7]][7]</sup>

#### ReplicationController

ReplicationController 用于确保Pod的数量正常。

#### ReplicaSet

ReplicaSet 是 ReplicationController 的升级版本，ReplicaSet 的标签选择器更加灵活。

#### Deployment

Deployment控制器 提供了[声明式](https://zh.wikipedia.org/wiki/声明式编程)（使用[YAML文件](https://zh.wikipedia.org/wiki/YAML)）的方式，更新[Pod](https://zh.wikipedia.org/wiki/User:九千鸦/k8s#Pod)和[ReplicaSet](https://zh.wikipedia.org/wiki/User:九千鸦/k8s#ReplicaSet)。<sup>[[8]][8]</sup>  **#todo**

#### Job

Job用于执行一次性<sup>one-off</sup>任务。

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: pi
spec:
  template:
    spec:
      containers:
      - name: pi
        image: perl
        command: ["perl",  "-Mbignum=bpi", "-wle", "print bpi(2000)"]
      restartPolicy: Never
  backoffLimit: 4
```

## 常见讨论

### 容器 vs Pod





[1]: https://kubernetes.io/docs/reference/using-api/api-overview/	"Kubernetes API Overview"
[2]: https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/	"Pod Overview"
[3]: https://kubernetes.io/docs/concepts/	"Concepts - kubernetes.io"
[4]: https://cloud.google.com/kubernetes-engine/docs/concepts/service#why_use_a_service	"为何使用 Service？ - Google cloud"
[5]: https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/
[6]: https://kubernetes.io/docs/concepts/services-networking/service/
[7]: https://kubernetes.io/docs/concepts/
[8]: https://kubernetes.io/docs/tasks/manage-kubernetes-objects/declarative-config/

