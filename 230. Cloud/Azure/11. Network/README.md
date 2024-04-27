

## 1. 虚拟网络

![Diagram that shows how application security groups combine with network security groups to protect applications.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240423192458.png)

虚拟网络 (virtual network) 包含以下部分：

- **Network Interface Card(NIC)**: 虚拟网卡，用来上网的。NIC 可以附着于 NSG，使用 NSG 的各种规则。
- **Application security groups**: 附着于 NSG 下，方便更精细化地管理一组虚拟机的出入规则。[["]](https://learn.microsoft.com/en-us/azure/virtual-network/network-security-groups-overview#application-security-groups)
- **Network security groups (NSG)**: 能指定交易出入规则
  - 必须集合 subnet 或者 network interface
  - 通过 NSG 可以构成一个 DMZ



## 2. DNS & domain

### 2.1. Azure DNS Zone

可以有两条一样的记录在 DNS Zone，如下:

```
www.contoso.com.        3600    IN    A     134.170.185.46
www.contoso.com.        3600    IN    A     134.170.188.221
```

起到负载均衡以及故障转移的功效



### 2.2. Azure Private DNS zone

目的：在虚拟网络里可以随意创建 DNS 记录。如：把虚拟机一号叫做 vm1，就可以直接 ping 虚拟机一号

```bash
ping db.internal.contoso.com
```

好处：不用在 Azure DNS Zone 添加配置，方便增加和删除。



## 3. Azure Private Link

作用： Azure Resource <=> virtual network

![img](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240426001255.png)

如上图所示，Azure Private Link 通过放置 Azure Storage 的 private endpoint 到虚拟网络里，让虚拟机能在虚拟网络里直接访问 Storage。[["]](https://learn.microsoft.com/en-us/azure/private-link/private-link-overview) [["]](https://learn.microsoft.com/en-us/azure/private-link/tutorial-private-endpoint-storage-portal?tabs=dynamic-ip)

## 4. Azure Virtual Network peering 

作用： Azure virtual network <=> Azure virtual network

特性

- 无传递性。网络 `A <=> B`，`B <=> C`，并不意味着 `A <=> C`
- 同一个 region 使用 local virtual network peering，不同 region 使用 global virtual network peering



### 4.1. 设置选项

创建 peering 的时候有四个选项

![image-20240427205537262](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240427205540.png)

- Allow 'vnet-1' to access the peered virtual network

  允许访问 peered virtual network**（推荐打狗）**

- Allow 'vnet-1' to receive forwarded traffic from the peered virtual network

  允许接受转发流量。如 vnet-2 有一个负载均衡器，发送到这个负载均衡器的流量要转发给 vnet-1，启动了这个配置就可以接受相关流量。

- Allow gateway or route server in 'vnet-1' to forward traffic to the peered virtual network

  允许网关和路由转发流量

- Enable 'vnet-1' to use the peered virtual networks' remote gateway or route server

  允许使用 vnet-2 的网关和路由。也就是流量可以发送到 vnet-2 的网关和路由。

### 4.2. with VPN Gatewa y

![Diagram of Gateway transit.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/Azure-virtual-network-with-VPN-gateway.drawio.png)

如果本地的网络想要通过 VPN 访问 vnet-2，那么需要启动 **Allow gateway or route server in 'vnet-1' to forward traffic to the peered virtual network**。这种做法可以避免在每一个网络都配置 VPN Gateway。

推荐文章: [[Extend peering with user-defined routes and service chaining]](https://learn.microsoft.com/en-us/training/modules/configure-vnet-peering/5-determine-service-chaining-uses)
