---
Creation Date: 2021-10-18
Modified Date: 2022-08-14
---

## 简介 Introduction

当我们谈及 Azure Storage 时，是在谈及 Azure 的几乎所有的存储方案。Azure Storage 是一个平台（platform）。[^1]

Azure Storage 核心服务有：[^1]

- [Azure Blobs](https://docs.microsoft.com/en-us/azure/storage/blobs/storage-blobs-introduction): A massively scalable object store(对象存储) for text and binary data. Also includes support for big data analytics through Data Lake Storage Gen2.

  Azure Blobs 又叫做 Azure Blob storage

- [Azure Files](https://docs.microsoft.com/en-us/azure/storage/files/storage-files-introduction): Managed file shares for cloud or on-premises deployments.

- [Azure Queues](https://docs.microsoft.com/en-us/azure/storage/queues/storage-queues-introduction): A messaging store for reliable messaging between application components.

- [Azure Tables](https://docs.microsoft.com/en-us/azure/storage/tables/table-storage-overview): A NoSQL store for schemaless storage of structured data.

- [Azure Disks](https://docs.microsoft.com/en-us/azure/virtual-machines/managed-disks-overview): Block-level storage volumes for Azure VMs(虚拟机).

除此以外还有：[^2]

- [Azure NetApp Files](https://azure.microsoft.com/en-us/services/netapp/)
- [Data Box](https://azure.microsoft.com/en-us/services/databox/)
- [Microsoft Azure Confidential Ledger](https://azure.microsoft.com/en-us/services/azure-confidential-ledger/)
- [ Azure Data Lake Storage](https://azure.microsoft.com/en-us/services/storage/data-lake-storage/)



## FAQ

问：Azure Blobs、Azure Files、Azure Disks 选择哪一个使用。

答:  程序开发时，一般都使用 Azure Blobs，除非有什么特殊需求。

- Azure Files： 需要使用 [Server Message Block (SMB) protocol](https://docs.microsoft.com/en-us/windows/win32/fileio/microsoft-smb-protocol-and-cifs-protocol-overview) or [Network File System (NFS) protocol](https://en.wikipedia.org/wiki/Network_File_System) 等协议时使用。
- Azure Disks： 当需要目录时，如：操作系统需要一个文件系统才能进行运行时使用。



## 延伸阅读 See also

- [Azure updates](https://azure.microsoft.com/en-us/updates/?category=storage)： Azure Storage 最近更新内容
- [Azure Blob Storage documentation](https://azure.microsoft.com/en-us/updates/?category=storage)： Azure Blob Storage is Microsoft's object storage solution for the cloud. Blob storage is optimized for storing massive amounts of unstructured data.






[^1]:  [Introduction to the core Azure Storage services - Microsoft Docs](https://docs.microsoft.com/en-us/azure/storage/common/storage-introduction)
[^2]: [Azure Cloud Storage Solutions and Services | Microsoft Azure](https://azure.microsoft.com/en-us/product-categories/storage/)