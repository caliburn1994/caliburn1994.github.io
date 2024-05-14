## 1. Overview

Azure 有两种 kuberenetes 服务：**Azure Kubernetes** 和 **Azure Container Apps**。后者托管的内容更多。

## 2. Azure Container Apps

Container Apps 有两种运行模式：

[sidecar containers](https://learn.microsoft.com/en-us/azure/container-apps/containers#sidecar-containers)：辅助用的容器。如：后台程序、agent

> 在计算机科学中，"sidecar" 一词源于摩托车的概念，是指连接到摩托车侧边的附加座位或附件车。在容器化和微服务架构中，"sidecar" 是一种设计模式，指的是将一个或多个附加的辅助容器与主要应用容器一起运行的方式。
>
> Sidecar 模式的主要思想是将辅助功能与主要应用程序分离，这些功能通常包括日志收集、监控、认证、安全性增强等。通过将这些功能作为独立的容器（即 sidecar 容器）运行，可以将应用程序本身与这些功能解耦，从而提高了应用程序的灵活性和可维护性

[init containers](https://learn.microsoft.com/en-us/azure/container-apps/containers#init-containers)：在 primary app 运行之前进行初始化操作。如：下载数据和准备环境变量。[["]](https://learn.microsoft.com/en-us/azure/container-apps/containers)

## 3. Azure Container Apps jobs

jobs 用于运行一次性的任务。和 Function 的定位差不多。[["]](https://learn.microsoft.com/en-us/azure/container-apps/jobs?tabs=azure-cli)

![Azure Container Apps jobs overview.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240514221353.png)

### 3.1. 概念

- **Job**：模版

- **Job execution**：根据模版生成的实例。

- **Job replica**：副本。2 个副本就是执行 2 个。

### 3.2. 触发器

- **Manual**: Manual jobs are triggered on-demand.
- **Schedule**: Scheduled jobs are triggered at specific times and can run repeatedly.
- **Event**: Event-driven jobs are triggered by events such as a message arriving in a queue.

