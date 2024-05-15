



## 1. Overview

对于 Azure 资源，所有操作都分为两个 control plane(控制平面) 和 data plane(数据平面)。[["]](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/control-plane-and-data-plane)

**control plane** 的操作主要是关于资源管理等的。该操作最终都会发送到 **Azure Resource Manager URL**。现在有两种 **control plane**，一个是新版的 **Azure Resource Manager**，另一个是则是旧版的 Azure Service Manager。[["]](https://docs.microsoft.com/en-us/azure/virtual-machines/migration-classic-resource-manager-faq) 所以我们可以将 `control plane==Azure Resource Manager` 也不过分。

![Diagram of the Resource Manager request model](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/resource-manager-016a1bac.png)

Data plane 可以理解为操作数据，但更具体的是，操作资源 instance 暴露给我们的内容。Data plane 操作的 URL 就像这种 `https://myaccount.blob.core.windows.net/` 。[["]](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/control-plane-and-data-plane)[["]](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/lock-resources)

## 2. SDK

如上图所示，我们有好几种方式操控 Azure 资源。命令行工具（window 系的 [Azure PowerShell](https://docs.microsoft.com/en-us/powershell/azure/install-az-ps)、Bash 系的 [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)）、Azure Protal、REST API、Azure Resource Manager templates、Terraform。运行环境除了本地的 terminal，还有云环境的 [Azure Cloud Shell](https://docs.microsoft.com/en-us/azure/cloud-shell/overview)。

关于



## 3. 案例 - cosmos DB

![image-20240515225105044](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240515225120.png)

从上图可知，control plane([Cosmos DB Resource Provider REST API](https://docs.microsoft.com/en-us/rest/api/cosmos-db-resource-provider/)) 和 data plane([Cosmos DB REST API](https://docs.microsoft.com/en-us/rest/api/cosmos-db/)) 可能可以做相同的事情。[["]](https://docs.microsoft.com/en-us/rest/api/cosmos-db/)[["]](https://docs.microsoft.com/en-us/rest/api/cosmos-db-resource-provider/2021-04-01-preview/sql-resources)

由于 Azure CLI 是使用 control plane，所以如果我们要查询数据时，就只能使用 SDK。









We can use Azure Portal, Azure PowerShell and Azure CLI use Cosmos API to insert data[^6], so they use both Cosmos DB REST API and Cosmos DB Resource Provider REST API.

The SDK code about Azure Resource Manager is placed  the `management` group.[^7] Here is the [sample code](https://github.com/Azure/azure-sdk-for-js/issues/9980) to create a Cosmos DB Account. The Azure SDK also can use the data plane.

**Terraform** is the only tool that just use the control plane but not use the data plane.[^2]

## See also

- [Azure SDK - GitHub](https://github.com/Azure/azure-sdk)

## References

[^1]: https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/control-plane-and-data-plane
[^2]: [Docs overview | hashicorp/azurerm  Terraform Registry](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs)
[^6]: [What is Azure PowerShell?](https://docs.microsoft.com/en-us/powershell/azure/what-is-azure-powershell)
[^7]: [General Guidelines: API Design  Azure SDKs](https://azure.github.io/azure-sdk/general_design.html)


