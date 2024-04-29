

## 1. 概览

![Diagram of the Resource Manager request model](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/resource-manager-016a1bac.png)

我们可以通过以下方式访问 Azure 资源：

- SDK

  - [Azure PowerShell](https://docs.microsoft.com/en-us/powershell/azure/install-az-ps)：window系

  - [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)： Bash系的
- Azure Protal
- REST API
- Azure Resource Manager templates
- Terraform

访问平台除里 Windows、Mac、Linux 以外，还可以使用  [Azure Cloud Shell](https://docs.microsoft.com/en-us/azure/cloud-shell/overview) 进行操作。[Azure Cloud Shell](https://docs.microsoft.com/en-us/azure/cloud-shell/overview) 是一个 terminal 工具，里面安装了 Azure CLI 和 Azure Powershell。

## 2. 工具选择

Storage accounts are typically based on an analysis of your data, so they tend to be relatively stable. As a result, storage-account creation is usually a one-time operation done at the start of a project. For one-time activities, the portal is the most common choice.

In the rare cases where you need automation, the decision is between a programmatic API or a scripting solution. Scripts are typically faster to create and less work to maintain because there's no need for an IDE, NuGet packages, or build steps. If you have an existing client application, the management libraries might be an attractive choice; otherwise, scripts are a better option. [["]](https://learn.microsoft.com/en-us/training/modules/create-azure-storage-account/4-choose-an-account-creation-tool)[["]](https://stackoverflow.com/questions/49994422/azure-cli-vs-python-sdk/78405175#78405175)



## 3. 客户端应用开发

考虑到 Azure CLI 是基于 Azure Python SDK 的， CLI 更加用户友好，以及 CLI 一个命令就可以完成若干件事情，但 SDK 需要若干步才能完成。[["]](https://learn.microsoft.com/en-us/training/modules/create-azure-storage-account/4-choose-an-account-creation-tool) 建议 Python + Azure CLI 可以混搭使用。



## 4. Azure Resource Manager template

用于部署 Azure 资源的 JSON 文件。但无法通过修改这些 JSON 文件从而影响已有的资源。

### 4.1 结构

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



