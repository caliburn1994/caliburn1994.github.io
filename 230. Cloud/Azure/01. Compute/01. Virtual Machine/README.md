

## 命名规则

| Name element               | Examples                                                 | Description                                                  |
| :------------------------- | :------------------------------------------------------- | :----------------------------------------------------------- |
| **Environment or purpose** | `dev` (development), `prod` (production), `QA` (testing) | A portion of the name should identify the environment or purpose for the machine. |
| **Location**               | `uw` (US West), `je` (Japan East), `ne` (North Europe)   | Another portion of the name should specify the region where the machine is deployed. |
| **Instance**               | `1`, `02`, `005`                                         | For multiple machines that have similar names, include an instance number in the name to differentiate the machines in the same category. |
| **Product or service**     | `Outlook`, `SQL`, `AzureAD`                              | A portion of the name can specify the product, application, or service that the machine supports. |
| **Role**                   | `security`, `web`, `messaging`                           | A portion of the name can specify what role the machine supports within the organization. |

Let's consider how to name the first development web server for your company that's hosted in the US South Central location. In this scenario, you might use the machine name `devusc-webvm01`. `dev` stands for development and `usc` identifies the location. `web` indicates the machine as a web server, and the suffix `01` shows the machine is the first in the configuration. [["]](https://learn.microsoft.com/en-us/training/modules/configure-virtual-machines/3-plan)

## 类型

| Classification                | Description                                                  | Scenarios                                                    |
| :---------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| **General purpose**           | General-purpose virtual machines are designed to have a balanced CPU-to-memory ratio. | - Testing and development - Small to medium databases - Low to medium traffic web servers |
| **Compute optimized**         | Compute optimized virtual machines are designed to have a high CPU-to-memory ratio. | - Medium traffic web servers - Network appliances - Batch processes - Application servers |
| **Memory optimized**          | Memory optimized virtual machines are designed to have a high memory-to-CPU ratio. | - Relational database servers - Medium to large caches - In-memory analytics |
| **Storage optimized**         | Storage optimized virtual machines are designed to have high disk throughput and I/O. | - Big Data - SQL and NoSQL databases - Data warehousing - Large transactional databases |
| **GPU**                       | GPU virtual machines are specialized virtual machines targeted for heavy graphics rendering and video editing. Available with single or multiple GPUs. | - Model training - Inferencing with deep learning            |
| **High performance computes** | High performance compute offers the fastest and most powerful CPU virtual machines with optional high-throughput network interfaces (RDMA). | - Workloads that require fast performance - High traffic networks |

参考 [here](https://learn.microsoft.com/en-us/training/modules/configure-virtual-machines/4-determine-virtual-machine-sizing)

## 存储



![Diagram that shows disks used by an Azure virtual machine, including disks for the OS, data, and temporary storage.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240430052046.png)

![image-20240430052723575](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240430052725.png)

虚拟机有至少有两个磁盘，系统盘(Operating system disk) 和临时盘(Temporary disk) 。[["]](https://learn.microsoft.com/en-us/training/modules/configure-virtual-machines/5-determine-virtual-machine-storage) 这些磁盘实际都是 Azure Storage 里的一个 page blob，但是它们是 managed 的，所以不会以 Azure Storage Account 的形式存在。

### Operating system disk

每个虚拟机机都会有一个操作系统盘(Operating system disk) 。

### Temporary disk

临时盘(temporary disk) 的数据随时都会丢。如上图所示，临时盘是没有显示在 portal 界面上。

- On Windows virtual machines, the temporary disk is labeled as the `D:` drive by default. This drive is used for storing the **pagefile.sys** file.
- On Linux virtual machines, the temporary disk is typically `/dev/sdb`. This disk is formatted and mounted to `/mnt` by the Azure Linux Agent.

### 数据盘

非强制的，用于存储数据。



## 登录

有好几种登录方式，SSH、RDP(windows)、Bastion、portal界面上"Run command"



## Extensions 插件

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/image-20240329154712626.png" alt="image-20240329154712626" width="650" />

我们可以在导航栏的 **"Settings >> Extensions + applications"**，添加想要的插件。

常见的插件：

- Datadog Agent：用于上传日志到 Datadog 的 agent。

- Azure Automanage Machine Configuration extension：添加以下配置到虚拟机里
  - Operating system settings
  - Application configuration or presence
  - Environment settings



