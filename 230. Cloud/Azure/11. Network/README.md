

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



### 3. Azure Private Link

![img](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240426001255.png)

如上图所示，Azure Private Link 通过放置 Azure Storage 的 private endpoint 到虚拟网络里，让虚拟机能在虚拟网络里直接访问 Storage。[["]](https://learn.microsoft.com/en-us/azure/private-link/private-link-overview) [["]](https://learn.microsoft.com/en-us/azure/private-link/tutorial-private-endpoint-storage-portal?tabs=dynamic-ip)