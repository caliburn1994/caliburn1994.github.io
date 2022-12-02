---
layout: post 
title: Azure - Data Plane & Control Plane
tags: Cloud
comments: 1 
excerpt: 
typora-root-url: ..\..\..\..
typora-copy-images-to: ..\..\..\..\assets\blog_res\azure
---

Azure operations can be divided into two categories - control plane(控制平面) and data plane(数据平面).[^1]

## Control plane

You use the control plane to manage resources in your subscription. [^1] 

**Q: How to know which operation use the control plane?** 

All requests for control plane operations are sent to the *Azure Resource Manager* URL. [^1]

**Q: What is the relationship between Azure Resource Manager?**

*Azure Resource Manager is a control plane.* Another control plane is Azure Service Manager which is the old control plane.[^8]

## Data Plane

 You use the data plane to use capabilities *exposed by* your instance of a resource type.[^1] Data plane operations are operations sent to your instance of a service, such as `https://myaccount.blob.core.windows.net/`.[^5] 

## REST API & SDK

To discover which operations use the Azure Resource Manager URL, see the [Azure REST API](https://docs.microsoft.com/en-us/rest/api/azure/).[^1]

Let's take Cosmos DB as an example. There are two API in  [Azure REST API](https://docs.microsoft.com/en-us/rest/api/azure/): 

- [Cosmos DB REST API](https://docs.microsoft.com/en-us/rest/api/cosmos-db/)
- [Cosmos DB Resource Provider REST API](https://docs.microsoft.com/en-us/rest/api/cosmos-db-resource-provider/)

![image-20211031235413747](/assets/blog_res/azure/image-20211031235413747.png)As shown in the image above, you can create a Database or a Container by either Cosmos API or Resource Provider API. [^3][^4] 



**Q:  Can I use Resource Provider API to put data into Cosmos DB?**

No. Because there is a note in [Collections](https://docs.microsoft.com/en-us/rest/api/cosmos-db/collections) :

> If you require complete feature support for all Azure Cosmos DB resources, we recommend using the [Cosmos DB Resource Provider](https://docs.microsoft.com/en-us/rest/api/cosmos-db-resource-provider/).

But there isn't a note in [Documents](https://docs.microsoft.com/en-us/rest/api/cosmos-db/documents) . So Resource Provider API doesn't support insert data.

Another example is that you can set [Lock](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/lock-resources?tabs=json) to Cosmos DB Account and Database, but it doesn't work for data because of Lock just working for Resource Provider API.

**Q:  which API does tools use?** 

![Resource Manager request model](/assets/blog_res/azure/consistent-management-layer.png)



From the [image](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/overview), we can know that all the tools use the control plane.

We can use Azure Portal, Azure PowerShell and Azure CLI use Cosmos API to insert data[^6], so they use both Cosmos DB REST API and Cosmos DB Resource Provider REST API.

The SDK code about Azure Resource Manager is placed  the `management` group.[^7] Here is the [sample code](https://github.com/Azure/azure-sdk-for-js/issues/9980) to create a Cosmos DB Account. The Azure SDK also can use the data plane.

**Terraform** is the only tool that just use the control plane but not use the data plane.[^2]

## See also

- [Azure SDK - GitHub](https://github.com/Azure/azure-sdk)

## References

[^1]: https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/control-plane-and-data-plane
[^2]: [Docs overview | hashicorp/azurerm  Terraform Registry](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs)
[^3]: [Sql Resources - REST API (Azure Cosmos DB Resource Provider](https://docs.microsoft.com/en-us/rest/api/cosmos-db-resource-provider/2021-04-01-preview/sql-resources)
[^4]: [Azure Cosmos DB REST API Reference  Microsoft Docs](https://docs.microsoft.com/en-us/rest/api/cosmos-db/)
[^5]: [Lock resources to prevent changes - Azure Resource Manager](https://docs.microsoft.com/en-us/azure/azure-resource-manager/management/lock-resources)
[^6]: [What is Azure PowerShell?](https://docs.microsoft.com/en-us/powershell/azure/what-is-azure-powershell)
[^7]: [General Guidelines: API Design  Azure SDKs](https://azure.github.io/azure-sdk/general_design.html)
[^8]: [Frequently asked questions about classic to Azure Resource Manager migration](https://docs.microsoft.com/en-us/azure/virtual-machines/migration-classic-resource-manager-faq)

