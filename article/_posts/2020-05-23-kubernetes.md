---
layout: post
title: Kubernetes
date: 2020-06-04 20:00:02
categories: 计算机
tags: [鸦鸦的维基,kubernetes]
comments: 1 
typora-root-url: ..\..
---

Kubernetes 与传统的 [PaaS](https://zh.wikipedia.org/wiki/平台即服务) 不同，它更多的被称为 容器的PaaS 或者 CaaS（Containers as a service，容器即服务）。

## 概述

开发者使用 Kubernetes 有两种方式：命令行工具（如：[kubectl](https://kubernetes.io/docs/reference/kubectl/overview/) 、 [kubeadm](https://kubernetes.io/docs/reference/setup-tools/kubeadm/kubeadm/)）、[客户端库](https://kubernetes.io/docs/reference/using-api/client-libraries/)。而这些连接方式均是调用REST API，即调用[Kubernetes API](#kubernetes-api)。<sup class="sup" data-title="Most operations can be performed through the kubectlcommand-line interface or other command-line tools, such as kubeadm, which in turn use the API. However, you can also access the API directly using REST calls.">[[官网]](https://kubernetes.io/docs/reference/using-api/api-overview/ )</sup> （本文以[kubectl](https://kubernetes.io/docs/reference/kubectl/overview/)作为示例进行介绍）

和Docker一样，开发者进行部署有两种方式：

- 通过使用YAML文件记录 Kubernetes 相关信息，然后使用`kubectl apply -f  文件名`命令进行运行。
- 直接通过命令行部署。

当应用开发完毕后，应用将通过（Docker）容器进行包裹，通过上传最新版本的容器，可达到“发布应用”的效果。

当多个容器需要紧密的联系时，就需要 [Pod](#Pod) 提供支持。<sup class="sup" data-title="Pods are designed to support multiple cooperating processes (as containers) that form a cohesive unit of service. The containers in a Pod are automatically co-located and co-scheduled on the same physical or virtual machine in the cluster. The containers can share resources and dependencies, communicate with one another, and coordinate when and how they are terminated.">[[官网]](https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/ )</sup> [Pod](#Pod)管理的信息有应用的版本、应用所需的网络资源和存储资源。需理解的是，容器必须存储在[Pod](#Pod)当中，[Pod](#Pod)是K8s部署的最小单位，不管Pod中有一个容器还是多个容器。

K8s中拥有各种**控制器以及对应对象**，有的用于确保集群中Pod的数量；有的用于确保每个节点都拥有一个Pod；有的用于定时操作。<sup class="sup" data-title="Kubernetes also contains higher-level abstractions that rely on controllers to build upon the basic objects, and provide additional functionality and convenience features. ">[[官网]](https://kubernetes.io/docs/concepts/ )</sup> 

在集群里，为了防止应用突然间停止而导致无法使用，一个应用所在的Pod往往有多份副本，其中一个[Pod](#Pod)突然间停止也无所谓，有其他代替品。而每个[Pod](#Pod)都有自身的IP地址，但由于[Pod](#Pod)可能会突然间停止，所以我们不能固定地使用某一个[Pod](#Pod)的IP地址，因此我们希望有一种机制，“当一个[Pod](#Pod)不能使用时，该机制帮我们自动连接到其他[Pod](#Pod)上”。这种机制就叫**服务**。<sup>[[Google Cloud]](https://cloud.google.com/kubernetes-engine/docs/concepts/service#why_use_a_service )</sup> 

k8s的默认服务只能在集群中调用，常见的用法是前端调用后端服务。而当要在集群外进行调用时，就可以使用云服务商提供的负载均衡器。

一个外部入口（如网站）只有一个IP地址，如果想通过一个外部入口访问集群里若干个服务时该怎么办呢？只能通过URL的路径进行映射。如：通过`http://ip地址/服务1` 访问 `服务1`，`http://ip地址/服务2` 访问 `服务2`。这种机制叫做 **Ingress（入口）**

## 架构

![redhat](https://www.redhat.com/cms/managed-files/kubernetes_diagram-v2-770x717.svg)

### Control Plane

控制平面<sup>Control Plane</sup>位于主节点<sup>Master Node</sup>，包含<u>主控件组</u><sup>Master</sup>、etcd。控制平面 通常用于与工作节点进行交互。<sup>[[Redhat]](https://www.redhat.com/en/topics/containers/kubernetes-architecture)</sup>

#### 主控件

由于**Master**是由三个进程组成的，所以可以翻译为“主控件组”。Master所在的工作节点将会被指定为 主节点。<sup class="sup" data-title="The Kubernetes Master is a collection of three processes that run on a single node in your cluster, which is designated as the master node. Those processes are: kube-apiserver, kube-controller-manager and kube-scheduler.">[[官网]](https://kubernetes.io/docs/concepts/)</sup > 主控组件负责管理集群。<sup class="sup" data-title="The Master is responsible for managing the cluster">[[官网]](https://kubernetes.io/docs/tutorials/kubernetes-basics/create-cluster/cluster-intro/)</sup > 

- **API服务器组件<sup>kube-apiserver（API server）</sup>**用于与（集群的）**外界**进行**通讯**。API server将会判断接请求是否有效，如果有效就会处理。`kubectl` 等命令行实质就是和该组件通讯。
- **调度器<sup>kube-scheduler</sup>**用于**调度资源**。观察是否存在新创建的Pod没有指派到节点，如果存在的话，则将其指派到其中一个节点上。
- **控制器管理器<sup>kube-controller-manager</sup>**通过**控制器**进行维护集群。从API server接收到的命令，将会修改集群某些对象的期待状态（desired state），控制器观察到这些期待状态的变化，就会将这些对象的当前状态（current state）变为期待状态（desired state）。<sup>[[官网]](https://kubernetes.io/docs/concepts/architecture/controller/)[[官网]](https://kubernetes.io/zh/docs/concepts/overview/components/)</sup>
  - **节点控制器<sup>Node controller</sup>**：负责监视节点，当节点宕不可用时，进行通知。
  - **复制控制器<sup>Replication controller</sup>**：负责维护每一个<u>复制控制器对象</u>所关联的Pod的数量正确性。
  - **Endpoints controller**：负责填充 [Endpoints对象](#Endpoint)。
  - **服务账号<sup>Service Account</sup> & 令牌控制器<sup>Token controllers</sup>**：创建默认的账号和API访问令牌。

```
            |--------------------|                            
command-->  |   kube-apiserver   | ---> change object's 
            |--------------------|      desired state          
                                             ↑
                                             | watch
                                |---------------------------| 
       change object's  <---    |   kube-contoller-manager  |
       current state            |---------------------------|
```

`kubectl cluster-info`可以查看到主控件的IP地址。

#### etc

etcd一致性和高可用的键值存储软件，用于备份 Kubernetes 的所有集群。<sup class="sup" data-tile="Consistent and highly-available key value store used as Kubernetes' backing store for all cluster data.">[[官网]](https://kubernetes.io/docs/concepts/overview/components/)</sup>  **TODO**

#### cloud-controller-manager

云控制器管理器<sup>cloud-controller-manager</sup>让你可以将集群连接上云服务提供商的API。<sup class="sup" data-tile="The cloud controller manager lets you link your cluster into your cloud provider’s API.">[[官网]](https://kubernetes.io/docs/concepts/overview/components/)</sup> 

### 节点

当谈起节点<sup>Node</sup>，默认说的是对象是<u>工作节点</u>，而不是<u>主节点</u>。

- 每一个节点都会运行一个**kubelet**作为代理，与 [Master](#主控件（Master）) 进行通信。kubelet确保容器在Pod中[健康地运行](#健康检查)。<sup class="sup" data-tile="An agent that runs on each node in the cluster. It makes sure that containers are running in a Pod.
  The kubelet takes a set of PodSpecs that are provided through various mechanisms and ensures that the containers described in those PodSpecs are running and healthy. The kubelet doesn’t manage containers which were not created by Kubernetes.">[[官网]](https://kubernetes.io/docs/concepts/overview/components/)</sup> 
- 每一个节点都回运行一个**kube-proxy**作为网络代理。kube-proxy负责节点的网络规则，通过这些网络规则，你可以在通过集群内外的网络会话访问Pod。<sup class="sup" data-tile="kube-proxy is a network proxy that runs on each node in your cluster, implementing part of the Kubernetes Service concept.
  kube-proxy maintains network rules on nodes. These network rules allow network communication to your Pods from network sessions inside or outside of your cluster.">[[官网]](https://kubernetes.io/docs/concepts/overview/components/)</sup> 

## 存储

存储方案有很多。托管式的有如：[网络附加存储](https://zh.wikipedia.org/wiki/網路附加儲存)（NAS）、数据库、[文件服务器](https://zh.wikipedia.org/wiki/文件服务器)；非托管式的则利用Kubernetes 存储抽象，如：[卷](#卷)。<sup>[[Google Cloud]](https://cloud.google.com/kubernetes-engine/docs/concepts/storage-overview)[[Google Cloud]](https://cloud.google.com/kubernetes-engine/docs/concepts/volumes)[[官网]](https://kubernetes.io/zh/docs/concepts/storage/volumes/)</sup>

### 卷

对于应用而言，将数据写入容器中的磁盘文件是最简单的途径，但这种方法存在缺陷。如果容器因其他任何原因崩溃或停止，文件将会丢失。此外，一个容器内的文件不可供同一 Pod 中运行的其他容器访问。 卷<sup>Volume</sup>可以解决这两个问题。

### 临时卷

临时卷 与Pod的生命周期一样，随着包裹其的Pod终止或被删除时，该卷也会随之终止或删除。

### 持久性卷

持久性卷<sup>Persistent Volumes</sup>，在 Pod 被移除时，系统只是卸载该卷，数据将保留，并可将其数据传递到另一个 Pod。

## 网络

[服务](#服务)相关的内容再此不重复。

### Ingress

![img](https://miro.medium.com/max/2978/1*KIVa4hUVZxg-8Ncabo8pdg.png)

图片参考[此处](https://medium.com/google-cloud/kubernetes-nodeport-vs-loadbalancer-vs-ingress-when-should-i-use-what-922f010849e0)；Ingress的英文解释参考[此处](/Ingress)；

Ingress 用于管理外部流量<sup>traffic</sup>该以什么规则**进入**集群访问服务。解决这些问题：

- 通过一个IP端口如何访问到若干个服务？
- 如果使用HTTPS（证书）？

#### 自签名证书

自签名证书<sup>Self-Signed CA Certificate</sup>签发过程：

1. [生成证书](https://kubernetes.io/zh/docs/concepts/cluster-administration/certificates/)。
2. 添加到[Sercert](https://kubernetes.io/zh/docs/concepts/configuration/secret/)，进行秘密保管。
3. 添加相关配置至YAML文件。<sup>[[官网YAML]](https://kubernetes.io/zh/docs/concepts/services-networking/ingress/#tls)</sup>



## 容器

### 健康检查

K8s的对于容器的健康检查（Health Check）有三种：存活探测器<sup>Liveness Probe</sup>、就绪就绪探测器<sup>Readiness Probe</sup>、启动探测器<sup>Startup Probe</sup>。<sup>[[官网]](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)[[官网]](https://kubernetes.io/docs/concepts/workloads/pods/pod-lifecycle/)</sup>

执行顺序如下：

```
启动容器 -->  Startup Probe --->  Readiness Probe
                            |->  Liveness Probe
```

#### 存活探测器

存活探测器<sup>Liveness Probe</sup>，用于探测容器是否运行（存活）。根据存活探测器类型不同，判断存活条件也将不同：

- 命令行：返回码为0时即存活。<sup class="sup" data-title="the command succeeds, it returns 0, and the kubelet considers the container to be alive and healthy. If the command returns a non-zero value, the kubelet kills the container and restarts it.">[[官网]](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)</sup>
- HTTP：返回码为2xx或3xx即存活。<sup class="sup" data-title="Any code greater than or equal to 200 and less than 400 indicates success. Any other code indicates failure.">[[官网]](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)</sup>
- TCP：连接容器指定的端口，连接成功即存活。<sup class="sup" data-title="Just like the readiness probe, this will attempt to connect to the goproxy container on port 8080. If the liveness probe fails, the container will be restarted">[[官网]](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)</sup>

命令行类型示例：

```yaml
apiVersion: v1
kind: Pod
metadata:
  labels:
    test: liveness
  name: liveness-exec
spec:
  containers:
  - name: liveness
    image: k8s.gcr.io/busybox
    args:
    - /bin/sh
    - -c
    - touch /tmp/healthy; sleep 30; rm -rf /tmp/healthy; sleep 600
    livenessProbe:
      exec:
        command:
        - cat
        - /tmp/healthy
      initialDelaySeconds: 5 # 容器启动后5秒进行探测
      periodSeconds: 5  # 第一次探测后，每隔5秒进行探测
      failureThreshold: 10 # 失败10次后才被视为失败（默认值为3次）
      successThreshold: 2 # 在失败后，需成功2次后才被视为成功（默认为1次）
      timeoutSeconds: 1   # 探针检查时间的长度,当超过1时将会失败（默认1）
```

应用场景：

- 服务器的正常运行

#### 就绪探测器

就绪探测器<sup>Readiness Probe</sup>：当符合条件时，容器将被视为已就绪。就绪探测器的一个用途：当一个Pod的所有容器就绪后，**将会告诉服务该Pod可被使用**。

和[存活探测器](#存活探测器)一样有三种分类：

- 命令行：返回码为0时即就绪。
- HTTP：返回码为2xx或3xx即就绪。
- TCP：连接容器指定的端口，连接成功即就绪。

应用场景：

- 应用启动时间很长
- 依赖外部（不能直接使用）

#### 启动探测器

启动探测器<sup>Startup Probe</sup>：当符合条件时，容器被视为已启动。当应用需要长时间进行启动时，启动探测 会在一定时间内不断地探测应用是否启动成功，当应用启动成功后，存活探测或就绪探测 可被启动；超过探测时间的话，容器将会被杀死，并且根据 `restartPolicy` 来做出相应操作。

## 日志

容器化应用写入 `stdout` 和 `stderr` 的任何数据，都会被容器引擎捕获并被重定向到节点的  `/var/log/containers/`和 `/var/log/pods/` 。<sup>[[Logging Architecture](https://kubernetes.io/docs/concepts/cluster-administration/logging/)]</sup>

```shell
kubectl logs [Pod名字]			 # 查看当前日志
kubectl logs [Pod名字] --previous  # 查看崩溃前日志
```

### 管理方式

**//todohttps://kubernetes.io/zh/docs/concepts/cluster-administration/logging/**

## 术语

### Kubernetes API

Kubernetes API是[REST API](https://zh.wikipedia.org/wiki/REST) 是，Kubernetes 的基础组件。组件间的操作和通信，以及外部用户命令都是通过该API完成的，因此，**Kubernetes平台里的任何对象都可以说是该API的执行对象**。该API由 API服务器（[kube-apiserver](#kube-apiserver)）管理。

### 对象特征

K8s把对象分为两个状态：**期望状态**<sup>Desired State</sup> 和 **当前状态**<sup>Current State</sup>。

通过 `kubectl get pods [Pod名字] -o json` 命令，开发者可以查看对象对象**当前状态**和**期望状态**。

```json
# kubectl get pods [Pod名字] -o json
{
  "apiVersion": "v1",   	# API版本
  "kind": "Pod",			# 对象类型
  "metadata": {...},		# 元数据
  "spec": {...},   			# 期待状态
  "status": {...}, 			# 当前状态
}
```

- `apiVersion` ： Kubernetes API 的版本。
- `kind`：对象的类型。常见的对象有：[Pod](/Kubernetes#Pod)、[Deployment](/Kubernetes#Deployment)
- `metadata` - 帮助识别对象唯一性的数据。
  -  `name` <sup>名字</sup>，每个类型的资源之间名字不可以重复。调用命令时常用的参数就是名字。
  -  UID ，每个生命周期的每个对象UID都不同，用于标识对象的历史。<sup>[[官网]](https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/names/ )</sup>
  -  `namespace`<sup>命名空间</sup>（可选的） ，用于需要跨团队或跨项目的场景。<sup>[[官网]](https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/namespaces/ )</sup>

#### 标签和选择器

选择器分为：

- **标签选择器<sup>Label selector</sup>**，在yaml定义文件名为`selector`，操作对象为**标签**<sup> `label`</sup>
- **字段选择器<sup>Field selector</sup>**，用于命令查找对象时进行筛选，操作对象是对象几乎所有字段。示例：`kubectl get pods --field-selector status.phase=Running`。
- **节点选择器<sup>Node selector</sup>**，在yaml定义文件名名为`nodeSelector`，操作对象为拥有特定标签的节点。

标记物：

- **标签<sup>Label</sup>**，自定义元数据（位置`metatdata/labels`**），标签选择器**和**节点选择器**的标记数据。
- **Annotation**，自定义元数据（位置`metatdata/annotation`**），[其他用途](https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/annotations/)。

<div class="kyakya_collap" value="示例："></div>


```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
  labels:
    app: nginx
  annotation: 
  	....       
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
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
        nodeSelector:
		    accelerator: nvidia-tesla-p100
```

开发者可通过**选择器**<sup>（全称：标签选择器，Label selector）</sup>查找到对应拥有**标签**<sup>（label）</sup>的对象，并其进行指定。**标签**是键值对结构体数据。选择器有两种：**基于相等性的**<sup>equality-based</sup> 和 **基于集合的**<sup>set-based</sup> 。**基于相等性** 意味着运算符是以等号、不等号为主。 **基于集合** 意味着操作符以`in`、`notin` 、`exists`为主。

该如何使用**标签**进行管理呢？[官网](https://kubernetes.io/docs/concepts/overview/working-with-objects/common-labels/)推荐使用 `前缀/名称` 对标签进行命名，前缀是为了区别不同用户的对象。名称的命名可参考：[官网](https://kubernetes.io/docs/concepts/overview/working-with-objects/common-labels/)、[[非官网来源]](https://www.replex.io/blog/9-best-practices-and-examples-for-working-with-kubernetes-labels)

### 未分类对象

#### Endpoint

Endpoint对象<sup><译：终点></sup>是具体的URL，具体可以参考REST的Endpoint。<sup>[官方未定义-2020.06.01]</sup>

### 基础对象

#### Pod

Pod<sup>（直译：豆荚）</sup>是K8s的最小单元<sup>（atomic unit）</sup>。Pod封装了若干个[容器](https://www.docker.com/resources/what-container)，各容器可共享同一[IP地址](https://zh.wikipedia.org/wiki/IP地址)和[端口](https://zh.wikipedia.org/wiki/通訊埠)、存储资源（存储卷）。

通常，一个Pod只会装载一个容器，除非各容器间需要使用[文件系统](https://zh.wikipedia.org/wiki/文件系统)（存储资源）进行通讯。

参考：[官网](https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/)、[Google Cloud](https://cloud.google.com/kubernetes-engine/docs/concepts/pod)

#### 命名空间

通常，集群会生成一个默认命名空间<sup>namespace</sup>去装载Pod、服务、Deployment等。命名空间使用场景：当开发团队和产品团队同时使用，希望不互相影响。命名空间可以从默认切换至自定义。<sup>[[官网]](https://kubernetes.io/docs/tasks/administer-cluster/namespaces-walkthrough/)</sup>

命名空间只是对操作进行隔离，并不对网络进行隔离。<sup>[Kubernetes in Action]</sup>

#### 服务

服务<sup>service</sup>，可以理解为逻辑上的Pod。开发者通过服务的DNS名称名（DNS name）可以找到服务，然后通过服务可以调用某一Pod。<sup>[[官网]](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/ )</sup> 调用方 通过调用服务的方式，避免了调用方与Pod的[耦合](https://zh.wikipedia.org/wiki/耦合性_(計算機科學))，这样当Pod宕机时，也不会影响到调用方，这也可用于[负载均衡](https://zh.wikipedia.org/wiki/负载均衡)、[服务发现](https://zh.wikipedia.org/wiki/服务发现)等场景。<sup>[[官网]](https://kubernetes.io/docs/concepts/services-networking/service/ )</sup> 

```
                         选择一个Endpoint进行调用
            |-----------| -------- Endpoint 1 -----> [Pod 1 暴露端口:80 IP:10.244.0.25]
            |           |        [10.244.0.25:80]
   调用 -->  | service   | -------- Endpoint 2 -----> [Pod 2 暴露端口:80 IP:10.244.0.26] 
            |10.96.92.53|        [10.244.0.26:80]      
            |-----------| -------- Endpoint 3 -----> [Pod 3 暴露端口:80 IP:10.244.0.27] 
                                 [10.244.0.27:80]
```

##### 发布服务

K8s有以下<u>发布服务</u><sup>Publishing Services</sup>方式：<sup>[[官网]](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types)[[Google Cloud]](https://cloud.google.com/kubernetes-engine/docs/how-to/exposing-apps)</sup>

- **ClusterIP（默认类型）**：只有在集群<sup>Cluster</sup>里的IP才能访问该服务。
- **节点端口<sup>NodePort</sup>**：通过每个节点<sup>Node</sup>的某一特定端口<sup>Port</sup>访问该服务。
- **负载均衡器<sup>LoadBalancer</sup>**：通过云服务商的负载均衡器访问该类型服务。
- **外部名字<sup>ExternalName</sup>**：需访问的服务在集群外部, 在集群内部通过访问该服务的DNS名称，从而进行访问外部服务名字。
- **无头服务<sup>Headless Services</sup>**：这里的无头意味着没有clusterIP。而当我们使用[nslookup](https://manpages.debian.org/stretch/dnsutils/nslookup.1.en.html) 等查看该服务的DNS时，将会发现对应的地址指向了该服务使用的Pod。<sup>[[参考示例]](https://dev.to/kaoskater08/building-a-headless-service-in-kubernetes-3bk8)</sup>

通过节点[IP地址](https://zh.wikipedia.org/wiki/IP地址)进行暴露服务，可使用；通过云服务提供商的负载均衡器暴露服务，则使用`LoadBalancer`；而当服务不在集群内，在集群之外，可以使用`ExternalName` 模式的服务进行重定向。

##### 发现服务

在Pod初始化时，将会把服务的IP记录到该Pod的**环境变量**里。`kubectl exec [Pod名] env` 可查看到环境变量的内容。

另一种则是通过[DNS](https://en.wikipedia.org/wiki/Domain_Name_System)的[完整域名](https://en.wikipedia.org/wiki/Fully_qualified_domain_name)<sup>**F**ully **Q**ualified **D**omain **N**ame</sup>查找服务。查找地址：

```shell
# svc:service
[对象名].[命名空间].svc.cluster.local
```

DNS的参考示例在[这里](https://medium.com/kubernetes-tutorials/kubernetes-dns-for-services-and-pods-664804211501)。

##### 会话亲和性

如果浏览器开启 `keep-alive` 设置，则[拥有会话亲和性](#会话亲和性)<sup>Session Affinity</sup>；`service.spec.sessionAffinity` 设置成 `ClientIP` 也拥有会话亲和性。<sup>[[官网]](https://kubernetes.io/docs/concepts/services-networking/service/)[Kubernetes in Action-5.3.3]</sup>

*[拥有会话亲和性]: 每次打开浏览器浏览网站都使用同一个Pod，这叫会话亲和性。

##### 本地服务优先

`spec.externalTrafficPolicy `<sup>外部流量策略</sup>设置为`local`时，服务调用方将优先使用本地的服务，可是存在[缺点](https://kubernetes.io/docs/concepts/services-networking/service/)：本地缺少服务时将挂起、可能导致[服务流量不均匀](#服务流量不均匀)。

*[服务流量不均匀]: 工作节点A有一个Pod，工作节点B有两个Pod，工作节点A的一个Pod和工作节点B的两个Pod总流量一样。参考：kubernetes in action 5.3章

### 高级对象

**高级对象**是依赖于[控制器](https://kubernetes.io/docs/concepts/containers/overview/) ，而[控制器](https://kubernetes.io/docs/concepts/containers/overview/)是建立于基础对象之上，并为之添加功能性和方便性。<sup>[[官网]](https://kubernetes.io/docs/concepts/ )</sup>

#### ReplicationController

ReplicationController 用于确保Pod的数量正常。

#### ReplicaSet

ReplicaSet 是 ReplicationController 的升级版本，ReplicaSet 的标签选择器更加灵活。

#### Deployment

Deployment控制器 提供了[声明式](https://zh.wikipedia.org/wiki/声明式编程)（使用[YAML文件](https://zh.wikipedia.org/wiki/YAML)）的方式，更新[Pod](https://zh.wikipedia.org/wiki/User:九千鸦/k8s#Pod)和[ReplicaSet](https://zh.wikipedia.org/wiki/User:九千鸦/k8s#ReplicaSet)。<sup>[[官网]](https://kubernetes.io/docs/tasks/manage-kubernetes-objects/declarative-config/ )</sup>  **#todo**

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
      restartPolicy: Never # always重启、Never不重启、OnFailure失败重启
  backoffLimit: 4  # 尝试4次会视为失败
  activeDeadlineSeconds: 100 # 100秒内必须执行成功，否则中止
```

#### CronJob

与[Cron任务](https://wiki.archlinux.org/index.php/Cron_(%E7%AE%80%E4%BD%93%E4%B8%AD%E6%96%87))类型。

#### DaemonSet

DaemonSet（[Daemon](https://zh.wikipedia.org/wiki/守护进程)：守护进程） 为了每一个节点分配有且只有一个Pod，使用场景有：<sup>[[官网]](https://kubernetes.io/docs/concepts/workloads/controllers/daemonset/)[[Google CLoud]](https://cloud.google.com/kubernetes-engine/docs/concepts/daemonset)</sup>

- 存储用的守护进程。
- 日志收集的守护进程。
- 节点监控的守护进程。

## 常见讨论

### 容器 vs Pod

**#todo**

### 命令

#### kubectl describe vs get

`kubectl describe pods [pod名字]` 和 `kubectl get pods [pod名字]`究竟有什么区别？通过 `kubectl describe --help` 命令，可以获取describe相关信息：

> Show details of a specific resource or group of resources
>
>  Print a detailed description of the selected resources, including related resources such as events or controllers. You
> may select a single object by name, all objects of that type, provide a name prefix, or label selector. For example:

与 `kubectl get` 的区别在于：

- `kubectl get` 包含资源信息
- `kubectl describe` 包含：资源、事件<sup>event </sup>、控制器<sup>controller</sup>



#### kubectl create vs apply

> `kubectl apply` - Apply or Update a resource from a file or stdin.[<sup>[原址]</sup>](https://kubernetes.io/docs/reference/kubectl/overview/#examples-common-operations)

`kubectl apply`：创建、更新资源；`kubectl create`：创建资源



### 如何使用SSH？

在传统软件开发，当leader等人部署好系统环境后，团队将通过自动或手动的方式将代码传送到服务器上，并对服务进行测试。这里开发者们常见的操作有：**访问服务**<sup>（在指定服务器外进行访问）</sup>、**访问服务器资源**<sup>（在指定服务器内进行访问）</sup>

如果服务器在**局域网**内，开发者们则通过SSH则可以访问服务器资源；直接访问服务。

如果服务器不再**局域网**内，则通过SSH两次跳转的方式（第一次是SSH服务器，第二次是指定服务器），然后访问服务器资源。在SSH两次跳转后，通过<u>SSH端口转发</u>，将本地端口转发指定服务器，通过访问本地的端口，从而间接访问服务器端口。

**k8s 访问服务**<sup>[官网](https://kubernetes.io/docs/tasks/access-application-cluster/port-forward-access-application-cluster/)</sup>：

```shell
$ kubectl port-forward [Pod名字] [本地端口]:[远程端口]
Forwarding from 127.0.0.1:[本地端口] -> [远程端口]
Forwarding from [::1]:[本地端口] -> [远程端口]
Handling connection for [本地端口]
Handling connection for [本地端口]
```

**访问服务器资源**<sup>[官网](https://kubernetes.io/docs/tasks/debug-application-cluster/get-shell-running-container/)</sup>

```shell
# 运行一个软件
kubectl exec [Pod名字] [命令]
# 交互式
kubectl exec -it [Pod名字] -- /bin/bash
# 访问多容器Pod中的某一容器
kubectl exec -it [Pod名字] --container [容器名] -- /bin/bash
```

### 代理服务器

**//TODO**

 `kubectl proxy` 该命令将会生成代理，通过该代理，我们能直接访问 [REST API](https://zh.wikipedia.org/wiki/User:九千鸦/k8s#Kubernetes_API) 。通过`http://[代理IP]:[端口]/api` 等网址可以查看集群各种信息。





