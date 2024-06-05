



## 1. 基础知识

对于 Azure 资源，所有操作都分为两个 control plane(控制平面) 和 data plane(数据平面)。[["]](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/control-plane-and-data-plane)

**control plane** 的操作主要是关于资源管理等的。该操作最终都会发送到 **Azure Resource Manager URL**。现在有两种 **control plane**，一个是新版的 **Azure Resource Manager**，另一个是则是旧版的 Azure Service Manager。[["]](https://docs.microsoft.com/en-us/azure/virtual-machines/migration-classic-resource-manager-faq) 所以我们可以将 `control plane==Azure Resource Manager` 也不过分。

![Diagram of the Resource Manager request model](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/resource-manager-016a1bac.png)

Data plane 可以理解为操作数据，但更具体的是，操作资源 instance 暴露给我们的内容。Data plane 操作的 URL 就像这种 `https://myaccount.blob.core.windows.net/` 。[["]](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/control-plane-and-data-plane)[["]](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/lock-resources)

|           | Data plane | Control plane |
| --------- | ---------- | ------------- |
| Portal    | ⭕️          | ⭕️             |
| CLI       | ⭕️(不全)    | ⭕️             |
| Terraform |            | ⭕️             |
|           |            |               |



## 2. SDK

如 overview 所示，我们有好几种方式通过 control plane 操控 Azure 资源。命令行工具（window 系的 [Azure PowerShell](https://docs.microsoft.com/en-us/powershell/azure/install-az-ps)、Bash 系的 [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)）、Azure Protal、REST API、Azure Resource Manager templates、Terraform。运行环境除了本地的 terminal，还有云环境的 [Azure Cloud Shell](https://docs.microsoft.com/en-us/azure/cloud-shell/overview)。

值得注意的是，命令行、Portal 等工具 <u>**可能**</u> 可通过 data plane 操作数据。如：Cosmos DB CLI 若干年前之前操控数据，最近的版本不支持了。Storage 可以上传、删除 blob。[["]](https://learn.microsoft.com/en-us/cli/azure/storage/blob?view=azure-cli-latest)



### 2.1. 编程选择

![image-20240515225105044](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/Azure-SDK-20240516.png)

使用什么样的工具，可以参考上图，以及网上一些讨论。[["]](https://learn.microsoft.com/en-us/training/modules/create-azure-storage-account/4-choose-an-account-creation-tool)[["]](https://stackoverflow.com/questions/49994422/azure-cli-vs-python-sdk/78405175#78405175) 

- 涉及复杂的资源管理相关的编程，尽可能使用 Python 和 Azure CLI 的混搭的方法，原因：[["]](https://learn.microsoft.com/en-us/training/modules/create-azure-storage-account/4-choose-an-account-creation-tool) 

  - Azure CLI 是基于 Azure Python SDK 的。python 是脚本语言，更方便执行。

  - CLI 更加用户友好，以及 CLI 一个命令就可以完成若干件事情，但 SDK 需要若干步才能完成。

- 创建 Storage account 等十分稳定的资源，且创建操作只会执行一次，那么就可以在 portal 执行。

文章参考：

- [Azure SDK - GitHub](https://github.com/Azure/azure-sdk)
-  [General Guidelines: API Design  Azure SDKs](https://azure.github.io/azure-sdk/general_design.html) 
- [Docs overview | hashicorp/azurerm  Terraform Registry](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs)







### 2.2. SDK案例 - cosmos DB

![image-20240515225105044](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240515225120.png)

从上图可知，control plane([Cosmos DB Resource Provider REST API](https://docs.microsoft.com/en-us/rest/api/cosmos-db-resource-provider/)) 和 data plane([Cosmos DB REST API](https://docs.microsoft.com/en-us/rest/api/cosmos-db/)) 可能可以做相同的事情。[["]](https://docs.microsoft.com/en-us/rest/api/cosmos-db/)[["]](https://docs.microsoft.com/en-us/rest/api/cosmos-db-resource-provider/2021-04-01-preview/sql-resources)

由于 Azure CLI 是使用 control plane，所以如果我们要查询数据时，就只能使用 SDK。



### 2.3. Azure Resource Manager template

用于部署 Azure 资源的 JSON 文件。但无法通过修改这些 JSON 文件从而影响已有的资源。

#### 2.3.1. 结构

```json
{
    "$schema": "http://schema.management.azure.com/schemas/2019-04-01/deploymentTemplate.json#", // 模版语言
    "contentVersion": "",  // 该 JSON 文件的版本
    "parameters": {}, // 输入参数
    "variables": {}, // 变量
    "functions": [], // User-defined functions
    "resources": [], // 资源
    "outputs": {} // 部署后返回的数值
}
```

#### 2.3.2 用法

- 通过命令行以及 ARM template 文件，创建资源
- 通过 Azure Portal 界面将存档的 ARM template 进行部署。在 Resource group>>Automation>>Add to library 可以看到相关的按钮。



## 3. Plane 与 Role

Azure Role 也是按照 data plane 和 control plane 分离。内置角色的 Owner 是没有操作数据的权限（DataActions）。但是 Onwer 是可以看到 Storage 或 DB 里的数据。Owner 通过通过 key 来查看数据。所以我们在看 activity 的时候，会发现很多 list keys 的操作。

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240516164730.png" alt="image-20240516164727107" width="400"  />

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240516165829.png" alt="image-20240516165825645" width="400" />

一旦禁止 key access 之后，Owner 会没有权限访问数据。但只要添加相关的角色以及开启 **Default to Microsoft Entra authorization in the Azure portal**，就可以查看数据了。这也说明了 Owner 没有访问数据的权限。





## 4. DSC

在需要用脚本进行重复性的 infra/control plane 操作时候，大家往往会考虑到 Terraform。Terraform 是 desired state configuration (DSC) ，除此以外 Bicep。[["]](https://learn.microsoft.com/en-us/azure/developer/terraform/comparing-terraform-and-bicep?tabs=comparing-bicep-terraform-integration-features)

1. Terraform：通用型的 desired state configuration (DSC)。
   - 状态（state）存在 `terraform.tfstate` 或远程磁盘里。

2. Bicep: Azure 专用的 desired state configuration (DSC)。

   - 和 Azure 的结合性很高，可以通过 ARM template 反向编译[["]](https://learn.microsoft.com/en-us/azure/azure-resource-manager/bicep/decompile?tabs=azure-cli)，因此编写速度和正确性高于 Terraform。

   - 无 state 文件，是 incremental deployment。

> [!CAUTION]
> 使用 Terrafomr 或者 ARM 的 complete mode 都需要注意，可能存在误删操作。而 Bicep 是 incremental 的，不存在此担忧。[["]](https://github.com/Azure/bicep/discussions/9529)

> [!TIP]
> Always use the [what-if operation](https://learn.microsoft.com/en-us/azure/azure-resource-manager/templates/deploy-what-if) before deploying a template in complete mode. What-if shows you which resources will be created, deleted, or modified. Use what-if to avoid unintentionally deleting resources.[["]](https://learn.microsoft.com/en-us/azure/azure-resource-manager/templates/deployment-modes)

