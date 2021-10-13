---
layout: post 
title: Azure Storage - 1.介绍
tags: Cloud
comments: 1 
excerpt: Aazure Storage 简介
typora-root-url: ..\..\..\..
typora-copy-images-to: ..\..\..\..\assets\blog_res\azure\azure storage
---





当我们谈及 Azure Storage 时，是在谈及 Azure 的几乎所有的存储方案。Azure Storage 是一个平台 (platform) 。[^1]

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

## Q&A

1. Azure Files vs Azure Blobs vs  Azure Disks

   答:  在绝大数时候，都是时候 Azure Blobs。

   当需要使用 [Server Message Block (SMB) protocol](https://docs.microsoft.com/en-us/windows/win32/fileio/microsoft-smb-protocol-and-cifs-protocol-overview) or [Network File System (NFS) protocol](https://en.wikipedia.org/wiki/Network_File_System) 等协议时，可以使用 Azure Files 。当需要目录时，如：操作系统需要一个文件系统才能进行运行时，使用Azure Disks。

## 参考 References

[^1]:  [Introduction to the core Azure Storage services - Microsoft Docs](https://docs.microsoft.com/en-us/azure/storage/common/storage-introduction)
[^2]: [Azure Cloud Storage Solutions and Services | Microsoft Azure](https://azure.microsoft.com/en-us/product-categories/storage/)