## 1. network policy

通过设置 network policy，可以限制 pod 之间的网络流量。如: 只允许前端的流量发往后段。[["]](https://learn.microsoft.com/en-us/azure/aks/use-network-policies)

azure 为 network policy 的实现提供了三种引擎: 

- *Cilium* for AKS clusters that use [Azure CNI Powered by Cilium](https://learn.microsoft.com/en-us/azure/aks/azure-cni-powered-by-cilium).
- *Azure Network Policy Manager*.
- *Calico*, an open-source network and network security solution founded by [Tigera](https://www.tigera.io/).

| Capability                               | Azure Network Policy Manager                     | Calico                                                       | Cilium                                           |
| :--------------------------------------- | :----------------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------- |
| Supported platforms                      | Linux, Windows Server 2022 (Preview).            | Linux, Windows Server 2019 and 2022.                         | Linux.                                           |
| Supported networking options             | Azure CNI.                                       | Azure CNI (Linux, Windows Server 2019 and 2022) and kubenet (Linux). | Azure CNI.                                       |
| Compliance with Kubernetes specification | All policy types supported                       | All policy types are supported.                              | All policy types are supported.                  |
| Other features                           | None.                                            | Extended policy model consisting of Global Network Policy, Global Network Set, and Host Endpoint. For more information on using the `calicoctl` CLI to manage these extended features, see [calicoctl user reference](https://docs.tigera.io/calico/3.25/reference/calicoctl/). | None.                                            |
| Support                                  | Supported by Azure Support and Engineering team. | Supported by Azure Support and Engineering team.             | Supported by Azure Support and Engineering team. |

- Azure CNI = Azure Container Networking Interface
- Azure 推荐使用 Cilium
- 三种当中，只有 Calico 支持 kubenet