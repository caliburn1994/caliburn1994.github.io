

## 概览

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

## 应用开发

Azure 有不同语言版本的SDK，究竟应该使用哪个语言的 SDK 呢？

**场景一**：只使用某个服务的 [数据平面（Data Plane）](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/control-plane-and-data-plane)

举例：数据库增删改查、Azure Storage 的上传下载功能

那么需要根据团队会的技术栈，并考量 Azure SDK，选择编程语言。如果团队偏向 Java 的话，那么就应用使用 Azure Java SDK。

**场景二**：控制平面（Control Plane）的管理操作使用得多

举例：想制作一个工具去创建 Azure App Service、Azure Front Door 等等

那么就应该考虑使用 Azure CLI 或 Azure Python SDK。Azure CLI 是基于 Azure Python SDK 的， CLI 更加用户友好的，通常一个命令就可以完成若干件事情，但 SDK 需要若干步才能完成。参考: [Azure CLI vs Python SDK - Stack Overflow](https://stackoverflow.com/questions/49994422/azure-cli-vs-python-sdk)

有以下搭配：

- Python + Azure Python SDK
- Python + Azure CLI
- Python + Azure Python SDK + Azure CLI：混搭使用

上述的用法都是可以的。

**经验总结:** 如果使用者非客户，而是工程师，尽量使用 CLI。尽量不要用 SDK 进行架构搭建，因为架构搭建的使用次数低，工程师往往不会考虑重试操作。以及要在每一个地方都捕捉并处理异常，这个成本也是很高。以及使用 SDK 失败的比例真的不低，不知道是 SDK 没做好还是什么原因。CLI 则方便 debug 和重试。



## Azure Resource Manager template

用于部署 Azure 资源的 JSON 文件。但无法通过修改这些 JSON 文件从而影响已有的资源。

### 结构

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




