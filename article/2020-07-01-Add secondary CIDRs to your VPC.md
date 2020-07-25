---
layout: post
title: ADVANCED VPC NETWORKING WITH EKS（摘要译文与详解）
date: 2020-06-01 00:00:02
tags: 鸦鸦的维基
comments: 1
excerpt: Add secondary CIDRs to your VPC（译文与详解）
typora-root-url: ..
---

本文是摘要翻译[Advanced VPC Networking with EKS :: Amazon EKS Workshop](https://www.eksworkshop.com/beginner/160_advanced-networking/)一文内容。

> You can expand your VPC network by adding additional CIDR ranges. This capability can be used if you are running out of IP ranges within your existing VPC or if you have consumed all available RFC 1918 CIDR ranges within your corporate network. EKS supports additional IPv4 CIDR blocks in the 100.64.0.0/10 and 198.19.0.0/16 ranges. You can review this announcement from our [what’s new blog](https://aws.amazon.com/about-aws/whats-new/2018/10/amazon-eks-now-supports-additional-vpc-cidr-blocks/)
>
> In this tutorial, we will walk you through the configuration that is needed so that you can launch your Pod networking on top of secondary CIDRs

> 通过添加额外的CIDR片段，来可以扩展VPC网络。通常在现有的VPC网络使用完IP片段时，或者公司网络使用完所有有效的RFC 1918 CIDR片段时，你可以通过上述方式扩展孔亮。EKS支持额外的`100.64.0.0/10`和`198.19.0.0/16` IPv4 CIDR块。相关公告可以翻阅[what’s new blog](https://aws.amazon.com/about-aws/whats-new/2018/10/amazon-eks-now-supports-additional-vpc-cidr-blocks/)。
>

> 通过该教程。你将经历配置以及启动在新增加的次要的CIDR上启动Pod网络。
>

## 相关概念

### CIDR

<u>子网络化</u><sup>subnet</sup>的标识有两种：<sup>[[参考]](https://www.digitalocean.com/community/tutorials/understanding-ip-addresses-subnets-and-cidr-notation-for-networking#:~:text=and%20more%20subnetworks.-,CIDR%20Notation,the%20routing%20or%20networking%20portion.)</sup>

1. 传统子网络化<sup>traditional subnetting</sup>：同时该网络也是有类别网络<sup>classful network</sup>，分别A、B、C、D、E五类，不同类有不同作用。<sup>[[wiki]](https://en.wikipedia.org/wiki/Classful_network#Classful_addressing_definition)</sup>

```
IP address:192.168.0.15
netmask:255.255.255.0
```

2. CIDR<sup>Classless Inter-Domain Routing</sup>是一种标记法，没有将网络进行分类，所以是无类别。

```
192.168.0.15/24
```

### VPC 

VPC<sup>Virtual Private Cloud</sup>，虚拟私人云。从谷歌云或者亚马逊云这些真实的云网络中，我们将获得一个虚拟的云网络，就像虚拟机与物理机的关系。在这个虚拟机的云中，我们可以配置机器/服务的IP地址的范围、配置路由表等等。

## 操作前提PREREQUISITES

### 为VPC添加第二块CIDR

> You can use below commands to add 100.64.0.0/16 to your EKS cluster VPC. Please note to change the Values parameter to EKS cluster name if you used different name than eksctl-eksworkshop

> 通过以下命令，你可以添加`100.64.0.0/16 `至EKS集群VPC上。（请注意命令中的变量值）

```shell
#获得VPC的ID
VPC_ID=$(aws ec2 describe-vpcs --filters Name=tag:Name,Values=eksctl-eksworkshop* | jq -r '.Vpcs[].VpcId')

# 将100.64.0.0/16该段添加至于上述的VPC中
aws ec2 associate-vpc-cidr-block --vpc-id $VPC_ID --cidr-block 100.64.0.0/16
```

> Next step is to create subnets. Before we do this step, let’s check how many subnets we are consuming. You can run this command to see EC2 instance and AZ details

> 接着创建<u>子网络</u><sup>subnet</sup>。在这之前，我们要检查一下我们用了哪些<u>子网络</u><sup>subnet</sup>。你可以通过以下命令查看到EC实例和<u>有效地带</u>（AZ<sup>Availability Zone</sup>）详情。

```shell
aws ec2 describe-instances --filters "Name=tag-key,Values=eks:cluster-name" "Name=tag-value,Values=eksworkshop*" --query 'Reservations[*].Instances[*].[PrivateDnsName,Tags[?Key==`eks:nodegroup-name`].Value|[0],Placement.AvailabilityZone,PrivateIpAddress,PublicIpAddress]' --output table 
```

操作结果可知，译者使用了三个<u>有效地带</u>（AZ<sup>Availability Zone</sup>）：`ap-northeast-1a`、`ap-northeast-1c`、`ap-northeast-1d`，当初译者[创建集群](https://www.eksworkshop.com/030_eksctl/launcheks/)时使用地域是`AWS_REGION=ap-northeast-1`。

`ap-northeast-1`意味着Asia Pacific (Tokyo)区域<sup>Region</sup>，而`ap-northeast-1a`、`ap-northeast-1c`、`ap-northeast-1d`意味着：在东京该**区域**的三个放置亚马逊服务器的**地带**<sup>。[[aws]](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Concepts.RegionsAndAvailabilityZones.html)</sup>

![image-20200724232333390](/assets/blog_res/image-20200724232222314.png)

> I have 3 instances and using 3 subnets in my environment. For simplicity, we will use the same AZ’s and create 3 secondary CIDR subnets but you can certainly customize according to your networking requirements. Remember to change the AZ names according to your environment

> 在我的（EKS）环境下，我拥有3个示例和3个子网络。为了简洁，我们将使用上述相同的AZ（名字），并创建3个次要的CIDR子网络，但你可以更具你自身的网络需求进行适当地调整。记住要修改AZ地名称

```shell
# AZ1 AZ2 AZ3可自行修改
export AZ1=us-east-2a
export AZ2=us-east-2b
export AZ3=us-east-2c
CGNAT_SNET1=$(aws ec2 create-subnet --cidr-block 100.64.0.0/19 --vpc-id $VPC_ID --availability-zone $AZ1 | jq -r .Subnet.SubnetId)
CGNAT_SNET2=$(aws ec2 create-subnet --cidr-block 100.64.32.0/19 --vpc-id $VPC_ID --availability-zone $AZ2 | jq -r .Subnet.SubnetId)
CGNAT_SNET3=$(aws ec2 create-subnet --cidr-block 100.64.64.0/19 --vpc-id $VPC_ID --availability-zone $AZ3 | jq -r .Subnet.SubnetId)
```

创建后，在AWS VPC我们可以看到，可以这样查看结果：

```shell
# aws ec2 help | grep subnet 查看与subnet相关的命令
$ aws ec2 describe-subnets | grep -E 'AvailabilityZone|100.64.'
			...
            "AvailabilityZone": "ap-northeast-1d",
            "AvailabilityZoneId": "apne1-az2",
            "CidrBlock": "100.64.64.0/19",
			...
            "AvailabilityZone": "ap-northeast-1c",
            "AvailabilityZoneId": "apne1-az1",
            "CidrBlock": "100.64.32.0/19",
            "AvailabilityZone": "ap-northeast-1a",
            "AvailabilityZoneId": "apne1-az4",
            "CidrBlock": "100.64.0.0/19",
```

上述结果，我们看到对应的AZ下有`100.64.*`的CidrBlock<sup>CIDR Block</sup>。

> Next step is to add Kubernetes tags on newer Subnets. You can check these tags by querying your current subnets

> 接下来我们需要为新的<u>子网络</u>的Kubernetes标签，（为此我们需要查看现存的子网络的标签，并复制到新的<u>子网络</u>），你可以通过下面的命令查看到这些标签。

```shell
# 查看192.168.0.0/19的cidr-block的标签(tag)
$ aws ec2 describe-subnets --filters Name=cidr-block,Values=192.168.0.0/19 --output text


TAGS    aws:cloudformation:logical-id   SubnetPublicUSEAST2C
TAGS    kubernetes.io/role/elb  1
TAGS    eksctl.cluster.k8s.io/v1alpha1/cluster-name     eksworkshop-eksctl
TAGS    Name    eksctl-eksworkshop-eksctl-cluster/SubnetPublicUSEAST2C
TAGS    aws:cloudformation:stack-name   eksctl-eksworkshop-eksctl-cluster
TAGS    kubernetes.io/cluster/eksworkshop-eksctl        shared
TAGS    aws:cloudformation:stack-id     arn:aws:cloudformation:us-east-2:012345678901:stack/eksctl-eksworkshop-eksctl-cluster/8da51fc0-2b5e-11e9-b535-022c6f51bf82
```

> Here are the commands to add tags to both the subnets

> 就像这些命令那样，添加标签到新的<u>子网络</u>里。

需要注意的是，上述一共有六个标签，而下文添加时只添加了三个标签：

1. `eksctl.cluster.k8s.io/v1alpha1/cluster-name`

2. `kubernetes.io/cluster/eksworkshop-eksctl`

3. `kubernetes.io/role/elb`：elb（Elastic Load Balancing）

②与③是用于load balancer和ingress的自动发现<sup>[[aws]](https://aws.amazon.com/premiumsupport/knowledge-center/eks-vpc-subnet-discovery/)</sup>，①的作用暂时不知道**TODO**。

```shell
# 
aws ec2 create-tags --resources $CGNAT_SNET1 --tags Key=eksctl.cluster.k8s.io/v1alpha1/cluster-name,Value=eksworkshop-eksctl
aws ec2 create-tags --resources $CGNAT_SNET1 --tags Key=kubernetes.io/cluster/eksworkshop-eksctl,Value=shared
aws ec2 create-tags --resources $CGNAT_SNET1 --tags Key=kubernetes.io/role/elb,Value=1
#
aws ec2 create-tags --resources $CGNAT_SNET2 --tags Key=eksctl.cluster.k8s.io/v1alpha1/cluster-name,Value=eksworkshop-eksctl
aws ec2 create-tags --resources $CGNAT_SNET2 --tags Key=kubernetes.io/cluster/eksworkshop-eksctl,Value=shared
aws ec2 create-tags --resources $CGNAT_SNET2 --tags Key=kubernetes.io/role/elb,Value=1
#
aws ec2 create-tags --resources $CGNAT_SNET3 --tags Key=eksctl.cluster.k8s.io/v1alpha1/cluster-name,Value=eksworkshop-eksctl
aws ec2 create-tags --resources $CGNAT_SNET3 --tags Key=kubernetes.io/cluster/eksworkshop-eksctl,Value=shared
aws ec2 create-tags --resources $CGNAT_SNET3 --tags Key=kubernetes.io/role/elb,Value=1
```

> As next step, we need to associate three new subnets into a route table. Again for simplicity, we chose to add new subnets to the Public route table that has connectivity to Internet Gateway

> 下一步，我们需要将三个<u>子网络</u>与<u>路由表</u>进行连接。为了更加简单易懂，我们选择添加这些新的<u>子网络</u>至<u>公开路由表</u>，该<u>公开路由表</u>已经连接上<u>因特网网关</u>。

<u>因特网网关</u><sup>Internet Gateway</sup>将允许VPC与因特网进行交互。

```shell
# 获得[子网络]192.168.0.0的路由表ID
SNET1=$(aws ec2 describe-subnets --filters Name=cidr-block,Values=192.168.0.0/19 | jq -r '.Subnets[].SubnetId')
RTASSOC_ID=$(aws ec2 describe-route-tables --filters Name=association.subnet-id,Values=$SNET1 | jq -r '.RouteTables[].RouteTableId')
# 为新的[子网络]连接[路由表]
aws ec2 associate-route-table --route-table-id $RTASSOC_ID --subnet-id $CGNAT_SNET1
aws ec2 associate-route-table --route-table-id $RTASSOC_ID --subnet-id $CGNAT_SNET2
aws ec2 associate-route-table --route-table-id $RTASSOC_ID --subnet-id $CGNAT_SNET3
```

