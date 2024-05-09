## 1. public IP address prefix

public IP address prefix 是指一段连续的公开 IP 地址，如  `50.220.110.196/30` 则包含了 `50.220.110.196-50.220.110.199` 四个 IP 地址。public IP address prefix 的来源有两个，一个是客户自己提供[["]](https://learn.microsoft.com/en-us/azure/virtual-network/ip-services/custom-ip-address-prefix)，一种是 Azure[["]](https://learn.microsoft.com/en-us/azure/virtual-network/ip-services/public-ip-address-prefix)。

应用场景：

- 防火墙通过设置 prefix（如：`50.220.110.196/30`），可以一次性将所有 IP 地址都放入白名单，方便管理。

  