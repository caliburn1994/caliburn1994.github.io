---
Last MOdified: 2022-08-16
---



# 命令行工具 Command-line tool 

官方介绍了三种命令行工具：[Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)、[Azure PowerShell](https://docs.microsoft.com/en-us/powershell/azure/install-az-ps)、[Azure Cloud Shell](https://docs.microsoft.com/en-us/azure/cloud-shell/overview)。接下来介绍了以下他们的区别。[^1]

- [Azure PowerShell](https://docs.microsoft.com/en-us/powershell/azure/install-az-ps)：window系
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)： Bash系的
- [Azure Cloud Shell](https://docs.microsoft.com/en-us/azure/cloud-shell/overview)：允许通过浏览器运行命令工具。 Azure Cloud Shell 可运行 CLI 也可以运行 PowerShell。

官网建议，习惯用哪个就用哪个。



## SDK vs CLI

Azure 有不同语言版本的SDK，究竟推荐使用那个变成语言的 SDK 呢？

**场景一**：只使用某个服务的 [数据平面（Data Plane）](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/control-plane-and-data-plane)

举例：数据库增删改查、Azure Storage 的上传下载功能

那么需要根据团队会的技术栈，并考量 Azure SDK，选择编程语言。如果团队偏向 Java 的话，那么就应用使用 Azure Java SDK。



**场景二**：更多的时候使用的是，控制平面（Control Plane）

举例：想制作一个工具去创建 Azure App Service、Azure Front Door 等等

那么就应该考虑使用 Azure CLI 或 Azure Python SDK。Azure CLI 是基于 Azure Python SDK 的， CLI 更加用户友好的，通常一个命令就可以完成若干件事情，但 SDK 需要若干步才能完成。[^2]

有以下搭配：

- Python + Azure Python SDK
- Python + Azure CLI
- Python + Azure Python SDK + Azure CLI：混搭使用

上述的用法都是可以的。





[^1]: [Choose the right Azure command-line tool - Microsoft Docs](https://docs.microsoft.com/en-us/cli/azure/choose-the-right-azure-command-line-tool)
[^2]: [Azure CLI vs Python SDK - Stack Overflow](https://stackoverflow.com/questions/49994422/azure-cli-vs-python-sdk)