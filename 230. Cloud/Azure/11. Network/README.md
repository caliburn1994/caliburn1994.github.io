

## 虚拟网络

![Diagram that shows how application security groups combine with network security groups to protect applications.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240423192458.png)

虚拟网络 (virtual network) 包含以下部分：

- **Network Interface Card(NIC)**: 虚拟网卡，用来上网的。NIC 可以附着于 NSG，使用 NSG 的各种规则。
- **Application security groups**: 附着于 NSG 下，方便更精细化地管理一组虚拟机的出入规则。[["]](https://learn.microsoft.com/en-us/azure/virtual-network/network-security-groups-overview#application-security-groups)
- **Network security groups (NSG)**: 能指定交易出入规则
  - 必须集合 subnet 或者 network interface
  - 通过 NSG 可以构成一个 DMZ