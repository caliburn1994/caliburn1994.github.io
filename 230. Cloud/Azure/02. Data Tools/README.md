





## 1. 概览

数据工具分为以下：

1. Azure 自带的**数据备份与恢复**，如: point-to-restore

   场景: 数据保护，预防误删与误操作等。

   而 Azure 与定义的数据备份与恢复，根据 Service 不同分为两种

   - Data Service 的备份
   - Compute Sevice 的备份

2. **数据迁移**: 如 Azure Data Factory

   场景: 数据移动。如: 复制测试数据等。是 (1) 的补足



## 2. Compute Service

因为很多 compute service 也可以用于存放数据库（如：MySQL），所以 Azure 也会提供数据恢复的功能。但数据备份大多是定期备份，每天、每周等形式进行备份。

而 Azure 提供了集中管理备份的服务 **Azure Recovery Services Vault(RSV)** 和 **Azure Backup Vault(BV)**。 这两个服务都是 **Azure Backup** 的一部分 [?待确认] ，可以在 **Backup Center** 创建以及管理。

![image-20240505145421110](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240505145425.png)

从上图可以看得出它们可以备份什么服务。**Backup Vault 是比较新的服务**。[["]](https://learn.microsoft.com/en-us/azure/backup/backup-vault-overview) 从官网很难看得出这两者的区别，只能死记硬背。值得一提的是，Azure 除了 SQL Server 以外，都有持续备份（Incremental backup）。[["]](https://learn.microsoft.com/en-us/azure/backup/backup-architecture)

### 2.1. Recovery Services vault

![Illustration that shows the Azure Backup job process for a virtual machine as described in the text.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240505185906.png)

- Recovery Services vault 是一个存储的仓库。而实际做备份和恢复操作的叫做  **Azure Backup Instant Restore capability。**
- 地里位置
  - Azure Backup reports 使用到的 Storage 必须和 Recovery Services vault 同一个地理位置。

  - Log Analytics workspaces 没有位置限制，不需要和 Recovery Services vault 在同一地里位置





### 2.1. 虚拟机备份策略

虚拟机的备份与恢复有若干种策略: 

- **vm snapshot**: 通过 **Recovery Services vault** 将虚拟机（包含内存和磁盘）做成 snapshot。这个过程在服务器关机时候也能执行。可恢复 VM，也可以只恢复文件。[["]](https://learn.microsoft.com/zh/azure/backup/backup-azure-vms-introduction) **适合生产环境**。[["]](https://learn.microsoft.com/zh/training/modules/configure-virtual-machine-backups/2-protect-data)

- **Azure managed disks - snapshot**: 通过 **Backup vault** 或 **Snapshot** 备份磁盘。[["]](https://learn.microsoft.com/zh/azure/backup/disk-backup-overview) **适合开发和测试环境**[["]](https://learn.microsoft.com/zh/training/modules/configure-virtual-machine-backups/2-protect-data)

  Backup vault 是增量备份的。[["]](https://learn.microsoft.com/zh/azure/backup/disk-backup-overview) **Snapshot 服务**可以选择全量备份，也可以选择增量备份。

  ![image-20240505194402405](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240505194409.png)

- **Azure unmanaged disks**: 看 [[here]]([Backup and disaster recovery for unmanaged disks on Azure VMs - Azure Virtual Machines | Microsoft Learn](https://learn.microsoft.com/en-us/azure/virtual-machines/page-blobs-backup-and-disaster-recovery))

- **Azure managed disks - image**: 通过制作 image 的形式做备份。

- **Azure Site Recovery**:  多地冗余，从非灾区将虚拟机复制到灾区。

  

基于 **Recovery Services vault** 的备份有三种[一致性](https://learn.microsoft.com/zh/azure/backup/backup-azure-vms-introduction)，也可以说是三种状态。分别是 **agent-based application-consistent**、**agent-based file-consistent backup**、**an agentless crash-consistent backup**。前两者需要在虚拟机里安装 agent，而后者则无需 agent。[["]](https://learn.microsoft.com/zh/azure/backup/backup-azure-vms-introduction) agent(Virtual Machine Agent) 默认安装在大部分的 Azure VM 里。[["]](https://learn.microsoft.com/en-us/training/modules/configure-virtual-machine-backups/5-backup-virtual-machines)

在虚拟机启动时候，会使用前两者进行备份，一旦虚拟机关机，则备份的一致性将会转换成 Crash Consistent。

![image-20240506010353293](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240506010356.png)

- **agent-based application-consistent** 使用 agent 将内存、IO操作都捕捉起来，保证数据不会有任何丢失。
  - **Windows VMs**: 可采用 [Microsoft Azure Recovery Services (MARS) agent](https://learn.microsoft.com/zh/azure/backup/backup-azure-vms-introduction) 回复文件到某一个磁盘上。
  - **Linux VMs**: 参考 [[here]](https://learn.microsoft.com/zh/azure/backup/backup-azure-vms-introduction)

- ...







## 3. Data Service

![image-20240424132626001](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240424132628.png)

数据同步有几种方式

- [Data factory](Azure%20Data%20Factory): 综合性比较好，各种迁移工具都有。可重复执行。

- blob object replication: 只能从 A 复制到 B account，不能在同一个 account 里复制。

  - [Change feed](https://learn.microsoft.com/en-us/azure/storage/blobs/storage-blob-change-feed) 和 [Blob versioning](https://learn.microsoft.com/en-us/azure/storage/blobs/versioning-overview) 都要启动。

  - snapshots 不能被复制。

- AzCopy: 本地命令工具

- Azure Import/Export: 数据装进硬盘里，发送到 Azure 的数据中心或者发过来发送到客户手中。

  

### 3.1. Azure Import/Export

Azure Import/Export:

- Import: 数据装进硬盘里，发送到 Azure 的数据中心。(支持服务: Azure Blob storage、Azure Files) [[”]](https://learn.microsoft.com/en-us/azure/import-export/storage-import-export-service) 

  所需配置文件：

  - a dataset CSV file: 文件信息
  - a driveset CSV file: 驱动信息
- Export: 从数据中心将数据取出来，存到硬盘发送到客户手上。



### 3.2. Azcopy

**AzCopy** 是命令行工具，可从数据源下载到本地，或者从本地上传。 [[”]](https://learn.microsoft.com/en-us/azure/storage/common/storage-use-azcopy-v10#download-azcopy)

- Azure Blob Storage、Azure File
- Azure Table 只支持旧版本 ll **[AzCopy version 7.3](https://aka.ms/downloadazcopynet)** ，新本不支持

| 命令行      | 说明                               |
| ----------- | ---------------------------------- |
| azcopy make | Creates a container or file share. |

**QA-1: AzCopy 连接 Azure Blob Storage、Azure File 通过什么方式验证？**

A: Microsoft Entra ID 、a Shared Access Signature (SAS) token
