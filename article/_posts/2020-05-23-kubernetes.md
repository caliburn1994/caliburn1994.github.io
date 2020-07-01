---
layout: post
title: Kubernetes
date: 2020-06-15 20:00:02
categories: 计算机
tags: [鸦鸦的维基,kubernetes]
comments: 1 
typora-root-url: ..\..
---

Kubernetes是一个开源系统，用于自动化部署、伸缩<sup>scaling</sup>、管理容器化应用。

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
       
---------------------------------------------------------------

            |--------------------|                            
command-->  |   kube-apiserver   | ---> change object's 
            |--------------------|      desired state          
                                             ↑
                                             | watch
                                |---------------------------| 
      	    create object <---  |  		 kube-scheduler 	|
						        |---------------------------|
```

`kubectl cluster-info`可以查看到主控件的IP地址。

创建一个的流程的详细流程可以参考：<u>[Kubernetes in Action的11.2.2 事件链]</u>

#### etc

etcd一致性和高可用的键值存储软件，用于备份 Kubernetes 的所有集群。<sup class="sup" data-tile="Consistent and highly-available key value store used as Kubernetes' backing store for all cluster data.">[[官网]](https://kubernetes.io/docs/concepts/overview/components/)</sup> 如Pod、控制器、服务、密钥等信息均需要要给持久化存储的位置，而该位置就是etc。

#### cloud-controller-manager

云控制器管理器<sup>cloud-controller-manager</sup>让你可以将集群连接上云服务提供商的API。<sup class="sup" data-tile="The cloud controller manager lets you link your cluster into your cloud provider’s API.">[[官网]](https://kubernetes.io/docs/concepts/overview/components/)</sup> 

### 节点

当谈起节点<sup>Node</sup>，默认说的是对象是<u>工作节点</u>，而不是<u>主节点</u>。

- 每一个节点都会运行一个**kubelet**作为代理，与 [Master](#主控件（Master）) 进行通信。kubelet确保容器在Pod中[健康地运行](#健康检查)。<sup class="sup" data-tile="An agent that runs on each node in the cluster. It makes sure that containers are running in a Pod.
  The kubelet takes a set of PodSpecs that are provided through various mechanisms and ensures that the containers described in those PodSpecs are running and healthy. The kubelet doesn’t manage containers which were not created by Kubernetes.">[[官网]](https://kubernetes.io/docs/concepts/overview/components/)</sup> 包括：
  - 创建Node资源进行注册
  - 启动Pod和其容器
  - 监控容器
  - 向API服务器组件<sup>kube-apiserver（API server）</sup>报告
- 每一个节点都回运行一个**kube-proxy**作为网络代理。kube-proxy负责节点的网络规则，通过这些网络规则，你可以在通过集群内外的网络会话访问Pod。<sup class="sup" data-tile="kube-proxy is a network proxy that runs on each node in your cluster, implementing part of the Kubernetes Service concept.
  kube-proxy maintains network rules on nodes. These network rules allow network communication to your Pods from network sessions inside or outside of your cluster.">[[官网]](https://kubernetes.io/docs/concepts/overview/components/)</sup> 包含：
  - 监听[服务](#服务)的正常
  - 更行相关规则。规则的运行模式可扩展阅读[此处](https://blog.fleeto.us/post/iptables-or-ipvs/)。
- 容器运行时<sup>Container runtime</sup>软件负责运行中的容器。<sup class="sup" data-tile="The container runtime is the software that is responsible for running containers.">[[官网]](https://kubernetes.io/docs/concepts/overview/components/)</sup> 

## 存储

存储方案有很多。托管式的有如：[网络附加存储](https://zh.wikipedia.org/wiki/網路附加儲存)（NAS）、数据库、[文件服务器](https://zh.wikipedia.org/wiki/文件服务器)；非托管式的则利用Kubernetes 存储抽象，如：[卷](#卷)。<sup>[[Google Cloud]](https://cloud.google.com/kubernetes-engine/docs/concepts/storage-overview)[[Google Cloud]](https://cloud.google.com/kubernetes-engine/docs/concepts/volumes)[[官网]](https://kubernetes.io/zh/docs/concepts/storage/volumes/)</sup>

### 卷

对于应用而言，将数据写入容器中的磁盘文件是最简单的途径，但这种方法存在缺陷。如果容器因其他任何原因崩溃或停止，文件将会丢失。此外，一个容器内的文件不可供同一 Pod 中运行的其他容器访问。 卷<sup>Volume</sup>可以解决这两个问题。

#### emptyDir

[emptyDir](https://kubernetes.io/zh/docs/concepts/storage/volumes/#emptydir)：用于存储临时数据的简单空目录的[临时卷](#临时卷)。 

应用场景：

- 一个Pod中拥有若干个容器，容器之间共享数据（通过磁盘传输数据）。
- 将`emptyDir.medium`字段设置成`Memory`（存储介质为内存），以提高操作速度，同时又不用更改代码。

*[临时卷]: 临时卷与Pod的生命周期一样，随着包裹其的Pod终止或被删除时，该卷也会随之终止或删除。

#### hostPath 

hostPath：将工作节点的目录挂载到Pod中。 

![Chapter 6. Volumes: attaching disk storage to containers ...](https://dpzbhybb2pdcj.cloudfront.net/luksa/Figures/06fig04_alt.jpg)

应用场景：

- 单节点持久化存储

#### Google Cloud Engine

gcePersistentDisk（GCE持久化磁盘），教程[参考此处](https://kubernetes.io/zh/docs/concepts/storage/volumes/#gcepersistentdisk)。

#### AWS EC2

awsElasticBlockStore。（ Amazon Web Service 弹性块存储卷）。

#### 自搭服务器

可用nfs挂载到pod中的NFS共享卷。 

#### 其他

cinder、 cephfs、 iscsi、 flocker、 glusterfs、 quobyte、 rbd、 flexVolume、 vsphere- Volume、 photonPersistentDisk、 scaleIO 用于挂载其他类型的网络存储。 

configMap、 secret、 downwardAPI：用于 将 Kubernetes部分资源和集群信息 公开给 pod 的特殊类型的 卷。 

### 持久性卷

#### 背景

开发团队中的开发者不应该了解底层硬件内容，而该职责为管理员。[上述](#卷)中的持久化存储卷要求开发者了解硬件，为了解决该问题，产生了持久性卷<sup>Persistent Volumes</sup>进行**解耦**。

#### 概览

```
---------	   -----------		----
|持久化卷 | <---|持久化卷声明| <---|Pod|
---------      -----------		-----
【管理员职责】 	【开  发  者 职  责】
	↓
 创建存储
```

在静态<sup>Static</sup>模式下，

1. 集群管理员需要创建<u>持久化卷</u>
2. 开发者想要为Pod配置持久化存储时，必须创建<u>持久化卷声明</u><sup>PersistentVolumeClaim </sup>（通过它进行声明将要使用一个<u>持久化卷</u>）。
3. 创建声明后，在Pod的配置中添加<u>持久化声明</u>即可。

```
---------	   -----------		----
|存储类   | <---|持久化卷声明| <---|Pod|
---------      -----------		-----
【管理员职责】 	【开  发  者 职  责】
						  ↓
 						创建存储							
```

在动态<sup>Dynamic</sup>模式下，

- 管理员
  1. 创建存储类<sup>Storage Class</sup>，存储类用于描述<u>可供使用的持久化方案有那些</u>。
- 开发者
  1. 创建<u>持久化卷声明</u>。（持久化声明将自动根据存储类而 创建持久化卷）
  2. 创建声明后，在Pod的配置中添加<u>持久化声明</u>即可。

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

### DNS

在后续版本中，官网推荐使用 CoreDNS 代替 kube-dns 作为DNS服务器。<sup>[[官网]](https://kubernetes.io/docs/tasks/administer-cluster/dns-custom-nameservers/)</sup> 通过容器中的`/etc/resolv.conf`可以查看到域名服务器<sup>nameserver</sup>，如想修改该文件中的配置，应该通过kubectl命令行修改对应的ConfigMap而实现。

## 容器

容器章节内容可查看[Docker - 维基百科](https://zh.wikipedia.org/wiki/Docker)。

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

### 开发与部署的配置

#### 背景

开发环境与产品环境是存在差异，该差异体现在配置。通过环境变量的形式进行配置应用，可以不修改容器的情况下，同时在开发环境与产品环境下进行部署应用。但通过YAML描述文件配置环境变量存在一定的缺陷，如：

- 环境变量数量过多
- 传输对象仅限于字符串，不能传输文件。<sup>[k8s in action 代码清单7.13]</sup>
- 如何保持<u>环境变量</u>处于最新状态。
- ...

[ConfigMap](https://kubernetes.io/docs/concepts/configuration/configmap/) 作为一个对象，对上述问题拥有更好的处理。而对于需保密内容，应该使用[Secret](https://kubernetes.io/docs/concepts/configuration/secret/)，Secret使用内存存储数据。

#### Secret

**传输数据，环境变量调用**：

```yaml
apiVersion: v1
...
# Sercet存储数据
data: # data->stringData 数据将不可见
  username: YWRtaW4=
  password: MWYyZDFlMmU2N2Rm

apiVersion: v1
...
# Pod访问环境变量
spec:
  containers:
	...
    env:
    - name: SECRET_USERNAME # 环境变量名
      valueFrom:
        secretKeyRef:
          name: test-secret # Sercet名
          key: username # Sercet的键
```

示例：[地址1](https://kubernetes.io/zh/docs/tasks/inject-data-application/distribute-credentials-secure/#%E5%88%9B%E5%BB%BA%E9%80%9A%E8%BF%87%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F%E8%AE%BF%E9%97%AE-secret-%E6%95%B0%E6%8D%AE%E7%9A%84-pod)、[地址2](https://kubernetes.io/zh/docs/tasks/inject-data-application/distribute-credentials-secure/#%E5%88%9B%E5%BB%BA%E9%80%9A%E8%BF%87%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F%E8%AE%BF%E9%97%AE-secret-%E6%95%B0%E6%8D%AE%E7%9A%84-pod)

**传输数据，通过文件方式访问**，文件可以通过base64形式存储。

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: secret-test-pod
spec:
  containers:
    - name: test-container
      image: nginx
      volumeMounts:
        - name: [自定义卷名]
          mountPath: [secret挂载的位置]
  volumes:
    - name: [自定义卷名]
      secret:
        secretName: [secret名]
```

```bash
# secret中只有两个键时候
$ ls [secret挂载的位置]
password username
```

参考：[地址1](https://kubernetes.io/zh/docs/tasks/inject-data-application/distribute-credentials-secure/#%E5%88%9B%E5%BB%BA%E5%8F%AF%E4%BB%A5%E9%80%9A%E8%BF%87%E5%8D%B7%E8%AE%BF%E9%97%AE-secret-%E6%95%B0%E6%8D%AE%E7%9A%84-pod)

#### ConfigMap

ConfigMap用法和[Sercet](#Sercet)类似，暂时省略。

### 开发应用

在开发应用期间，需要将源代码传输运行环境并运行，运行环境可以是普通的虚拟机，也可以是容器，但是考虑到开发环境要尽量与部署环境一致，以及基于K8s的开发与传统开发存在一定的差异，所以我们应该在<u>容器</u>中进行运行测试。

容器运行应用：需要将源代码传输到容器中。可以在容器中安装SSH，通过SFTP进行传输；通过更新Docker容器方式同步文件。而现在有不少工具进行辅助，参考：[地址一](https://kubernetes.io/zh/blog/2018/05/01/developing-on-kubernetes/)，[地址二](https://blog.fleeto.us/post/draft-vs-gitkube-vs-helm-vs-ksonnet-vs-metaparticle-vs-skaffold/)。

同时，部署环境还要考虑到使用<u>本地</u>还是<u>非本地</u>，本地常常使用Minikube。

### 获取上层信息

容器的上一层是Pod，拥有IP、主机名等信息（元数据）。容器获得Pod信息有以下方式。

#### 通过Downward API传输

```yaml
# 方式1：将元数据以文件形式存储到容器里
spec:
  containers:
    - name: [容器名]
	  ...
      volumeMounts:
        - name: [自定义卷名]
          mountPath: [容器路径]
volumes:
    - name: [自定义卷名]
      downwardAPI:
        items:
          - path: "存储该元数据的文件名称" 
            fieldRef:
              fieldPath: metadata.labels #元数据的数据名
```

```yaml
# 方式1：将元数据以环境变量形式存储到容器里
spec:
  containers:
    - name: test-container
      ...
      env:
        - name: [环境变量名]
          valueFrom:
            fieldRef:
              fieldPath: spec.nodeName #元数据的数据名
```

参考：[地址1](https://kubernetes.io/docs/tasks/inject-data-application/downward-api-volume-expose-pod-information/)、[地址2](https://kubernetes.io/docs/tasks/inject-data-application/environment-variable-expose-pod-information/)

#### 访问Rest API

通过访问[Kubernetes API](#Kubernetes API)，直接获得信息。

- 直接访问，需要手动导入证书。<sup>[[官网]](https://kubernetes.io/docs/tasks/administer-cluster/access-cluster-api/)</sup>
- 通过代理访问，需要启动代理，并从代理处访问。<sup>[[官网]](https://kubernetes.io/docs/tasks/administer-cluster/access-cluster-api/)</sup>
- Ambassador代理容器，从代理处访问。<sup>[k8s in action - 8.2.3]</sup>

## 安全

### 资源访问

资源访问和管理可以分以下层次：

当我们登陆AWS、Google Cloud等云服务时，需要用到账号，而每个账号能进行什么操作什么资源，这由**IAM**<sup>Identity and Access Management</sup>管理。通过IAM，我们可以让某些账号只能访问kubernetes集群而不能写和删除。

而云服务除了kubernetes以外还有其他，在kubernetes集群中的资源管理，是由**RBAC**<sup>Role-based access control</sup>进行管理。举例，通过RBAC，我们可以控制某些Pod只能访问kubernetes API中的哪些资源。

在更下一层次，就是容器。该层次和操作系统类似，有些资源只有超级用户root或者指定用户才访问，因此该层次的策略是**指定容器操作的用户权限**，如：指定某个容器内的操作全是**普通用户权限运行**或**超级用户权限**。

#### 云服务层次资源

IAM模型分为三个部分：

- 成员：Google 帐号、IAM账号等
- [角色](#角色)：角色包含授权的具体内容
- 政策<sup>Policy</sup>： 权限

一个账号（主账号）可以生成若干个IAM账号，分发给其他人，主账号管理者其他IAM账号。而为了方便管理，产生了群<sup>Group</sup>概念：

```
		  |-[user-1]
 [group]--|-[user-2]
	↑	  |-[user-3] 		 
  attach
    |
 [policy]
[某个群]下面的[用户]都拥有[某个权限]
```

```
  [user]
	↑	  	 
  attach
    |
 [policy]
[某个用户]拥有[某个权限]
```

当我们有两个主账号时，仅仅通过上述方式的话，账号1的权限无法授予给账号2，我们必须通过**角色**：

```
|-------------------|
|[amazon account-1]	|
|   [policies]		|
|       |			|
|     attach		|
|       ↓			|
|-----[role]--------|
	   ↑
	   |
|----[policy]-------|
|       |			|
|	 attach			|
|	   ↓			|
|  [IAM account]	|
|  					|
|[amazon account-2]	|
|-------------------|
```

过程如下：

1. 账号2将权限**授予**给账号1
2. 账号1去访问角色，从而获得权限，而通常会把这权限分配给旗下的IAM用户（账号）

#### 集群层次资源

##### 服务账号

背景：默认情况下在容器中，我们可以`/var/run/secrets/kubernetes.io/serviceaccount/token`中`token`直接访问API中的所有资源<sup>[[官网]](https://kubernetes.io/docs/tasks/access-application-cluster/access-cluster/)</sup>。然而，这种行为是十分危险。

为了对访问权限进行限制，我们可以创建自定义服务账号<sup>ServiceAccount</sup>,再通RBAC授权进行关联，从而达到目的。

服务账号的配置文件如下：

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: [服务账号名]
```

并不包含任何权限配置，所以与需要RBAC进行关联。示例可参考[此处](https://medium.com/better-programming/k8s-tips-using-a-serviceaccount-801c433d0023#:~:text=A%20ServiceAccount%20is%20used%20by,resources%20involved%20in%20the%20process.)

##### RBAC授权

RBAC授权<sup>Role-based access control  Authorization</sup>，可译为基于角色的访问控制授权。

RBAC API中有四种对象：

- 角色<sup>Role</sup> 与 角色绑定<sup>RoleBinding</sup>
- 集群角色<sup>ClusterRole</sup> 与 集群角色绑定<sup>ClusterRoleBinding</sup>

操作范围：`集群角色>角色`

###### 角色

角色：用于设置<u>命名空间</u>的权限。

示例[参考](https://kubernetes.io/docs/reference/access-authn-authz/rbac/#role-examples)：

```yaml
# 授权对命名空间"default"中的Pods的读取权限
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default	# 角色所在的命名空间
  name: pod-reader	
rules:
- apiGroups: [""] # "" 对象API是kubrnetes API
  resources: ["pods"]  # 允许操作Pods
  verbs: ["get", "watch", "list"] # 允许get list watch
```

###### 角色绑定

角色绑定是讲<u>角色</u>与<u>用户</u>绑定在一起。<sup>[[官网]](https://kubernetes.io/docs/reference/access-authn-authz/rbac/#rolebinding-example)</sup>

```yaml
apiVersion: rbac.authorization.k8s.io/v1
# 讲角色"pod-reader"与用户"jane"绑定
kind: RoleBinding
metadata:
  name: read-pods
  namespace: default
subjects:
- kind: User # User/Group/ServiceAccount
  name: jane # "name" is case sensitive
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: Role # Role/ClusterRole
  name: pod-reader # 角色
  apiGroup: rbac.authorization.k8s.io
```

###### 集群角色

集群角色：用于设定<u>非命名空间</u>资源的权限。<sup>[[官网]](https://kubernetes.io/zh/docs/reference/access-authn-authz/rbac/#rolebinding-%E5%92%8C-clusterrolebinding)</sup>

```yaml
# 授权对所有/单个命名空间中的secrets的读取权限
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  # 此处的 "namespace" 被省略掉是因为 ClusterRoles 是没有命名空间的。
  name: secret-reader
rules:
- apiGroups: [""]
  resources: ["secrets"]
  verbs: ["get", "watch", "list"]
```

###### 集群角色绑定

```yaml
apiVersion: rbac.authorization.k8s.io/v1
# 这个角色绑定允许 "dave" 用户在 "development" 命名空间中有读取 secrets 的权限。 
kind: RoleBinding
metadata:
  name: read-secrets
  namespace: [命名空间]。 # 如development
subjects:
- kind: User
  name: dave # 名称区分大小写
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: secret-reader
  apiGroup: rbac.authorization.k8s.io
```

#### 容器层次资源

##### 语境

默认语境<sup>Context</sup>下，容器是使用是超级用户root。

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: security-context-demo
spec:
  securityContext:
    runAsUser: 1000   # 用户ID，超级用户是0
    runAsGroup: 3000
    fsGroup: 2000
  volumes:
  - name: sec-ctx-vol
    emptyDir: {}
  containers:
  - name: sec-ctx-demo
    image: busybox
    command: [ "sh", "-c", "sleep 1h" ]
    volumeMounts:
    - name: sec-ctx-vol
      mountPath: /data/demo
    securityContext:
      allowPrivilegeEscalation: false
```

不为超级用户：<sup>[[参考手册]](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.18/#podsecuritycontext-v1-core)</sup>

```yaml
  securityContext:  
    runAsNonRoot: true
```









## 日志

容器化应用写入 `stdout` 和 `stderr` 的任何数据，都会被容器引擎捕获并被重定向到节点的  `/var/log/containers/`和 `/var/log/pods/` 。<sup>[[Logging Architecture](https://kubernetes.io/docs/concepts/cluster-administration/logging/)]</sup>

```shell
kubectl logs [Pod名字]			 # 查看当前日志
kubectl logs [Pod名字] --previous  # 查看崩溃前日志
```

### 管理方式

**//todohttps://kubernetes.io/zh/docs/concepts/cluster-administration/logging/**

## K8s扩展

K8s得扩展包含:

- 自定义对象
- 自定义控制器
- 自定义API服务器<sup>API Server</sup>

在没有使用<u>自定义对象</u>之前，我们使用K8s可以说是基于对象的编程，但是一旦自定义对象后，我们可以视之为面向对象的编程。就像Java等面向对象编程语言那样，通过自定义类/模板文件，可以简化系统。

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

Deployment控制器是[ReplicaSet](#ReplicaSet)的升级版本，并且基于[ReplicaSet](#ReplicaSet)，对<u>更新Pod</u>该操作进行优化。

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

#### StatefulSet

背景：

- **状态**：当我们谈起对象存在状态否时，谈论的主语是**一组对象**而**不是一个对象**。有状态<sup>stateful</sup>，是指一组对象互相间存在差异。无状态<sup>stateful</sup>，则是指一组对象不存在差异。
- 集群下的操作对象往往是一个集合的Pod，而批量<sup>batch</sup>处理集合必然会忽略Pod的**状态**。

使用StatefulSet创建Pod后，通过网络标识直接访问该Pod的REST API内容。该Pod的存储内容也是唯一的，不与其他同类Pod共享存储空间。

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

#### kubectl attach vs exec

前者是附属于主程序，后者是在容器中运行一个进程。

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

### 部署很麻烦！

有时候我们想要一次性创建Deployment、Service、Loaderbalance，我们可以通过脚本一次性执行，也可以通过工具[helm](https://helm.sh/)（头盔）



