## 1. 介绍

[Azure Network Watcher](https://azure.microsoft.com/en-us/products/network-watcher) 是网络监控和诊断服务，专注于网络资源的可见性和性能。**只适用于虚拟网络和虚拟机**。Azure VM 虚拟机需要安装 Network Watcher Agent，on-premise 则需要安装 Azure Monitor agent(旧: Log Analytics agent)。

## 2. Azure Network Watcher

![Looking at Azure Network Watcher](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240518154516.png)

这里针对对象主要是虚拟网络。图片来源 [[here]](https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.georgeollis.com%2Fnetwork-watcher-become-a-great-troubleshooter%2F&psig=AOvVaw2_opQTX4bZFAvBG0d025Bk&ust=1716101061835000&source=images&cd=vfe&opi=89978449&ved=0CBQQjhxqFwoTCLCLzOPMloYDFQAAAAAdAAAAABAJ)。

[**NSG flow logs**](https://learn.microsoft.com/en-us/azure/network-watcher/nsg-flow-logs-overview): 记录了从 NSG 通往 VM 的流量。[["]](https://learn.microsoft.com/en-us/azure/network-watcher/nsg-flow-logs-tutorial)

[**Traffic analytics**](https://learn.microsoft.com/en-us/azure/network-watcher/traffic-analytics): 用于分析流量。数据存储于 Log Analytics workspace，来源于 [NSG flow logs](https://learn.microsoft.com/en-us/azure/network-watcher/nsg-flow-logs-overview)。

**[Connection Monitor](https://learn.microsoft.com/en-us/azure/network-watcher/connection-monitor-overview)**: 用于监控 E2E 的连通。如：监控前后端 VM/scale set 的连通性。Connection Monitor 使用 Azure Monitor agent 去收集数据。[["]](https://learn.microsoft.com/en-us/azure/network-watcher/connection-monitor-overview)











