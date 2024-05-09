

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

## 





## 4. Network Appliances

"Network Appliances" 通常指的是专用的硬件设备，用于在网络环境中执行特定的功能，如路由、防火墙保护、负载均衡、网络监控等。这些设备专为网络通信和数据处理设计，以优化性能和提高网络的效率与安全性。

### 4.1. Route

Azure 有两种路由 **System Routes** 和 **User-defined Routes(UDR)**。

![Diagram that shows two subnets that use system routes as described in the text.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240429004039.png)

**System Routes**  是 Azure 自动生成的默认路由，不能创建、删除，但能通过 UDR 覆盖配置。虚拟网络、虚拟网络子网、因特网这三者之间的流量，都是由该路由进行指挥。[["]](https://learn.microsoft.com/en-us/training/modules/configure-network-routing-endpoints/2-review-system-routes)

![Diagram that shows two subnets that use a UDR to access an NVA as described in the text.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240429004439.png)

在用户创建的 **User-defined Routes(UDR)** 可以控制 <u>流量流(traffic flow)</u>，定义 <u>下一个跳跃目标(next hop)</u>。[["]](https://learn.microsoft.com/en-us/training/modules/configure-network-routing-endpoints/3-identify-user-defined-routes) 如上图所示，我们可以设置一个防火墙作为 NVA 去拦截来自前端应用的非法请求或者攻击。

- next hop 可以是 Virtual network gateway、Virtual network、Internet、Network virtual appliance (NVA)

### 4.2. Firewall

TODO

**forced tunneling**

在 Azure 网络中，“forced tunneling”（强制隧道）指的是通过网络设置强制所有出站流量（包括访问互联网的流量和访问 Azure 服务的流量）通过您的本地网络环境或特定的网络虚拟设备。这样做可以让企业对流量进行监控和控制，确保安全性和符合性政策。



## 5. 其他

- **Azure ExpressRoute** 是一种服务，它允许客户通过一个私有的高速连接直接连接到 Microsoft Azure 和 Microsoft 365 服务，绕过公共互联网。

​	适合于对网络性能和安全性有高要求的企业应用，网速差的环境或国家。

- **Azure Network Watcher** 提供一系列工具诊断网络问题。
