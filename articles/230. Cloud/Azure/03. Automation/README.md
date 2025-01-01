## 1. 概览

![Azure Automation overview | Microsoft Learn](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240528144055.png)

[Azure Automation](https://learn.microsoft.com/en-us/azure/automation/overview) 是一个整体的概念，它所描述的自动化包含上图所示的内容: 

- **流程自动化 (Process Automation)**: 通过 runbook 和 Azure 等其他服务的结合，实现自动化操作。
- **配置管理 (Configuration Management)**
  - **Change Tracking and Inventory**:  用于最终资源的变动。
  - **Azure Automation State Configuration**: 和 [Terraform](https://en.wikipedia.org/wiki/Terraform_(software)) 类似的产品。用于部署和维护。

- **Update Management**: 定期更新系统

## 2. 概念

- [Automation account](https://learn.microsoft.com/en-us/azure/automation/automation-security-overview) 用于管理所有自动化资源。使用自动化操作之前，必须创建或拥有至少一个 Automation account。

## 3. 工具一览

Azure VM

- [Azure Custom Script Extension](https://learn.microsoft.com/en-us/azure/virtual-machines/extensions/custom-script-windows): 在 Windows Server 在启动之后，执行初始化脚本。
- cloud-init: Azure VM 初始化脚本。
- Azure Automation State Configuration (AASC): 类似 [Terraform](https://en.wikipedia.org/wiki/Terraform_(software)) 的服务，是 desired state configuration (DSC)，但它不适用于其他云服务。例: 升级操作系统，更改环境变量。从[文档](https://learn.microsoft.com/en-us/powershell/dsc/reference/resources/linux/lnxenvironmentresource)来看，这个工具只适合仍然使用虚拟机的传统架构，可做的事情不多。

Azure resource

- Terraform: 通用型的 desired state configuration (DSC)。有状态。
- Bicep: Azure 专用的 desired state configuration (DSC)。无状态，增量变更。和 Azure 的结合性很高，可以通过 ARM template 反向编译，因此编写速度和正确性高于 Terraform。