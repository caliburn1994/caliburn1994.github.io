## 1. 概述

将在这里记载具有共同性的内容。





## 2. 区域

参考：

Region、Availability Zone 等概念

Region >> Availability Zone >> Data Center >>  数据副本

### 2.1. Region

每一个 Region 至少一个 Availability Zone（AZ 可用区）。

**例：日本有两个 Region。东日本和西日本。[[”\]](https://www.azurespeed.com/Information/AzureRegions)**

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240501150612.png" alt="image-20240501150609731" width="600" style="float: left;" />

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240501150700.png" alt="image-20240501150657112" width="900" style="float: left;"/>

### 2.2. Availability Zone

Availability Zone（AZ）叫做可用区，该区域特性：高可用

- 网络：高性能低延迟。
- 数据：有一个或多数据中心（**data center**），保证数据不丢。
- 灾备：部分服务支持 AZ 为单位的灾备，也就是当一个 AZ 的服务宕机时，其他 AZ 会接管对应的工作。

**例： 东日本有 3 个 AZ。分布在 Tokyo, Saitama。[["]](https://www.azurespeed.com/Information/AzureAvailabilityZones)**



### 2.3. Data Center & Copy

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240501144848.png" alt="image-20240501144836739" width="300" style="float: left;"/>

### 2.4. AWS Local Zone

AWS Local Zones 是 AWS Region 的一种扩展，其地位和 AZ 差不多，用于减少延迟。

原理：把 AWS 计算、存储、数据库和其他某些初级服务放置在更靠近大量人口聚居的位置，或者靠近行业和 IT 中心的位置。

[Regions and Zones - Amazon Elastic Compute Cloud](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html)



## 3. 冗余

类型：（按照可用性排序）

**Locally redundant storage (LRS)： **

- 在同一个 data center 里进行三次冗余。
- 数据是同步的。

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240501144848.png" alt="image-20240501144836739" width="300" style="float: left;"/>

**Zone-redundant storage (ZRS)**：

- 在同一个region里，多个 Availability Zone 冗余。

- 数据是同步的。

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240501145045.png" alt="image-20240501145042973" width="350" style="float: left;"/>

**Geo-redundant storage (GRS) or Read-access geo-redundant storage (RA-GRS)**

- 分布在 2 个region的 2个 LRS。
- region 之间是异步的。一旦一个数据中心出问题，就需要通过 failover 进行转移。服务可能短暂的中断。

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240501145507.png" alt="image-20240501145340662" width="600" style="float: left;"/>



**Geo-zone-redundant storage (GZRS) or Read-access geo-zone-redundant storage (RA-GZRS)**

- 分布在 2个 region，primary region 是 ZRS，secondary region 是 LRS。

- 主 region 的其中一个数据中心出问题，可以使用其他 AZ 的数据中心。除非极端情况，否则不需要 failover。服务不会出现短暂中断。

<img src="https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/20240501145713.png" alt="image-20240501145710629" width="800" style="float: left;" />



