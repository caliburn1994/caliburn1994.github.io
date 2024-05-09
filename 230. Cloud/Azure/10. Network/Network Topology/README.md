## 1. Point-to-Site (P2S) VPN

个人电脑，链接到虚拟网络。

- 常见的协议

  - OpenVPN® Protocol
  - Secure Socket Tunneling Protocol (SSTP)
  - IKEv2 VPN

  ![Configure P2S server configuration - certificate authentication: Azure  portal - Azure VPN Gateway | Microsoft Learn](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240509163550.png)

## 2. site-to-site VPN

**连接跨域公网的两个网络。**如：on-premises 网络和 Azure 虚拟网络之间建立。

![Tutorial - Create S2S VPN connection between on-premises network and Azure  virtual network: Azure portal - Azure VPN Gateway | Microsoft Learn](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240509163356.png)

## 3. Azure Virtual Network peering 

作用： Azure virtual network <=> Azure virtual network。不需要走互联网。

特性

- 无传递性。网络 `A <=> B`，`B <=> C`，并不意味着 `A <=> C`
- 同一个 region 使用 local virtual network peering，不同 region 使用 global virtual network peering

### 3.1. 设置选项

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

### 3.2. with VPN Gateway

![Diagram of Gateway transit.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/Azure-virtual-network-with-VPN-gateway.drawio.png)

如果本地的网络想要通过 VPN 访问 vnet-2，那么需要启动 **Allow gateway or route server in 'vnet-1' to forward traffic to the peered virtual network**。这种做法可以避免在每一个网络都配置 VPN Gateway。

推荐文章: [[Extend peering with user-defined routes and service chaining]](https://learn.microsoft.com/en-us/training/modules/configure-vnet-peering/5-determine-service-chaining-uses)