---
layout: post
title: ADVANCED VPC NETWORKING WITH EKS（摘要译文与详解）
date: 2020-06-01 00:00:02
tags: 鸦鸦的维基
comments: 1
excerpt: Add secondary CIDRs to your VPC（译文与详解）
typora-root-url: ..
---

本文是摘要翻译[Advanced VPC Networking with EKS :: Amazon EKS Workshop](https://www.eksworkshop.com/beginner/160_advanced-networking/)一文内容。但代码和解释并不会完全与教程相同，仅供参考。

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

### CNI

容器网络接口（CNI，Container Network Interface），AWS的CNI插件帮助Pod在Pod内以及Pod外的IP地址相同<sup>[[aws]](https://docs.aws.amazon.com/eks/latest/userguide/pod-networking.html)</sup>。有点像我们使用桥接。

![                 EKS networking             ](https://docs.aws.amazon.com/eks/latest/userguide/images/networking.png)

### CRD

定制资源定义（custom resource definition CRD）



## 操作前提(PREREQUISITES)

> Before we configure EKS, we need to enable secondary CIDR blocks in your VPC and make sure they have proper tags and route table configurations

> 在配置EKS之前，我们必须在你的VPC网路中，启动次要的CIDR块，并确认他们有适当的标签和路由表配置。

### Add secondary CIDRs to your VPC

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
export AZ1=ap-northeast-1a
export AZ2=ap-northeast-1c
export AZ3=ap-northeast-1d
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

## 配置CNI(CONFIGURE CNI)

### 检查版本

Before we start making changes to VPC CNI, let’s make sure we are using latest CNI version

Run this command to find CNI version

> 在我们修VPC CNI之前，先确认一下CNI的版本
>
> 运行该命令查看CNI版本

```shell
$ kubectl describe daemonset aws-node --namespace kube-system | grep Image | cut -d "/" -f 2
amazon-k8s-cni:1.6.1
```

> Upgrade to the latest v1.6 config if you have an older version:

> 更新版本：

```sh
kubectl apply -f https://raw.githubusercontent.com/aws/amazon-vpc-cni-k8s/master/config/v1.6/aws-k8s-cni.yaml
```

> Wait until all the pods are recycled. You can check the status of pods by using this command

> 等待所有的Pods被回收。然后通过以下命令查看Pods的状态

```shell
kubectl get pods -n kube-system -w
```

### Configure Custom networking

> Edit aws-node configmap and add AWS_VPC_K8S_CNI_CUSTOM_NETWORK_CFG environment variable to the node container spec and set it to true
>
> Note: You only need to set one environment variable in the CNI daemonset configuration:

> 修改aws-node的configmap ，将AWS_VPC_K8S_CNI_CUSTOM_NETWORK_CFG 环境变量添加到节点容器的spec 并设置为true
>
> 注：你仅需设置一个环境变量到CNI的daemonset的配置文件即可

```shell
# ds=daemonset 
kubectl set env ds aws-node -n kube-system\
AWS_VPC_K8S_CNI_CUSTOM_NETWORK_CFG=true
```

![image-20200726030440960](/assets/blog_res/image-20200726030440960.png)

```shell
$ kubectl describe daemonset aws-node -n kube-system \
| grep -A5 Environment

    Environment:
      AWS_VPC_K8S_CNI_LOGLEVEL:            DEBUG
      AWS_VPC_K8S_CNI_VETHPREFIX:          eni
      AWS_VPC_ENI_MTU:                     9001
      MY_NODE_NAME:                         (v1:spec.nodeName)
      AWS_VPC_K8S_CNI_CUSTOM_NETWORK_CFG:  true
```

> Terminate worker nodes so that Autoscaling launches newer nodes that come bootstrapped with custom network config
>
> Warning: Use caution before you run the next command because it terminates all worker nodes including running pods in your workshop

> 我们将中止所有工作节点，以便自动扩缩，自动扩缩时会启动配置了自定义的网络设置的新节点。
>
> 警告：在使用下一条命令时前需谨慎，因为该命令会中止所有工作节点，也会中止workshop下的所有运行中的Pods。

```shell
INSTANCE_IDS=(`aws ec2 describe-instances --query 'Reservations[*].Instances[*].InstanceId' --filters "Name=tag-key,Values=eks:cluster-name" "Name=tag-value,Values=eksworkshop*" --output text` )
for i in "${INSTANCE_IDS[@]}"
do
	echo "Terminating EC2 instance $i ..."
	aws ec2 terminate-instances --instance-ids $i
done
```



## 创建CRDS(CREATE CRDS)

### Create custom resources for ENIConfig CRD

> As next step, we will add custom resources to ENIConfig custom resource definition (CRD). CRDs are extensions of Kubernetes API that stores collection of API objects of certain kind. In this case, we will store VPC Subnet and SecurityGroup configuration information in these CRDs so that Worker nodes can access them to configure VPC CNI plugin.
>
> You should have ENIConfig CRD already installed with latest CNI version (1.3+). You can check if its installed by running this command.

> 接下来，我们需要添加<u>定制资源</u>到<u>ENIConfig定制资源定义</u>（CRD）里。CRDs是Kubernetes API的扩展功能，它存储着某个类型（kind）的API对象的集合体。在本例中，我们将存储VPC子网络到这些CRDs的SecurityGroup配置信息中，以便工作节点们可以访问它们，并用它们去配置VPC CNI插件。
>
> 最新CNI版本(1.3+)，ENIConfig CRD已经被安装了。可使用下面的命令进行检查。

```sh 
#crd=customresourcedefinitions
kubectl get crd
```

> You should see a response similar to this

> 得到类似的结果

```
NAME                               CREATED AT
eniconfigs.crd.k8s.amazonaws.com   2019-03-07T20:06:48Z
```

> If you don’t have ENIConfig installed, you can install it by using this command

> 如果没安装ENIConfig，可用下面语句进行安装

```shell
kubectl apply -f https://raw.githubusercontent.com/aws/amazon-vpc-cni-k8s/master/config/v1.3/aws-k8s-cni.yaml
```

> Create custom resources for each subnet by replacing **Subnet** and **SecurityGroup IDs**. Since we created three secondary subnets, we need create three custom resources.
>
> Here is the template for custom resource. Notice the values for Subnet ID and SecurityGroup ID needs to be replaced with appropriate values

> 可通过代替个<u>子网络</u><sup>subnet</sup>和SecurityGroup IDs的方式，为每一个<u>子网络</u><sup>subnet</sup>创建<u>定制资源</u><sup>custom resources</sup>。我们创建次要的<u>子网络</u><sup>subnet</sup>后，我们需要创建三个<u>定制资源</u><sup>custom resources</sup>。
>
> 这是<u>定制资源</u><sup>custom resources</sup>的模版。注意需要替换<u>子网络ID</u><sup>subnet ID</sup>和SecurityGroup ID成对应的值。

```
apiVersion: crd.k8s.amazonaws.com/v1alpha1
kind: ENIConfig
metadata:
 name: group1-pod-netconfig
spec:
 subnet: $SUBNETID1
 securityGroups:
 - $SECURITYGROUPID1
 - $SECURITYGROUPID2
```

> Check the AZs and Subnet IDs for these subnets. Make note of AZ info as you will need this when you apply annotation to Worker nodes using custom network config

> 查看这些<u>子网络</u><sup>subnet</sup>的AZ和Subnet ID。注意AZ信息，当使用<u>定制网络配置</u><sup>custom network config</sup>注入注解<sup>annotation </sup>到工作节点时，你需要使用到上述信息。

```shell
aws ec2 describe-subnets  --filters "Name=cidr-block,Values=100.64.*" --query 'Subnets[*].[CidrBlock,SubnetId,AvailabilityZone]' --output table
```

```
-------------------------------------------------------------------
|                         DescribeSubnets                         |
+----------------+----------------------------+-------------------+
|  100.64.64.0/19|  subnet-05d5650a34feabd49  |  ap-northeast-1d  |
|  100.64.32.0/19|  subnet-04ddb02797eef2c55  |  ap-northeast-1c  |
|  100.64.0.0/19 |  subnet-027979de16683f06d  |  ap-northeast-1a  |
+----------------+----------------------------+-------------------+
```

> Check your Worker Node SecurityGroup

> 查看工作节点SecurityGroup

```shell
INSTANCE_IDS=(`aws ec2 describe-instances --query 'Reservations[*].Instances[*].InstanceId' --filters "Name=tag-key,Values=eks:cluster-name" "Name=tag-value,Values=eksworkshop*" --output text`)
for i in "${INSTANCE_IDS[@]}"
do
  echo "SecurityGroup for EC2 instance $i ..."
  aws ec2 describe-instances --instance-ids $i | jq -r '.Reservations[].Instances[].SecurityGroups[].GroupId'
done  
```

```
SecurityGroup for EC2 instance i-0c81388618e5df124 ...
sg-05f11134e34884dbc
sg-06a6e6e43d455f32f
SecurityGroup for EC2 instance i-099fd5497018f71ee ...
sg-05f11134e34884dbc
sg-06a6e6e43d455f32f
SecurityGroup for EC2 instance i-08f622f7601d94cfe ...
sg-05f11134e34884dbc
sg-06a6e6e43d455f32f
```

> Create custom resource **group1-pod-netconfig.yaml** for first subnet (100.64.0.0/19). Replace the SubnetId and SecuritGroupIds with the values from above. Here is how it looks with the configuration values for my environment
>
> Note: We are using same SecurityGroup for pods as your Worker Nodes but you can change these and use custom SecurityGroups for your Pod Networking

> 为第一个<u>子网络</u><sup>subnet</sup>创建<u>定制资源</u><sup>custom resources</sup>**group1-pod-netconfig.yaml**。使用上面的值代替SubnetId和SecuritGroupIds。下面是笔者的环境的样本。
>
> 注意：虽然我们在Pod和工作节点使用的SecurityGroup是相同，但是在Pod网络时，你需要使用定制的SecurityGroups。

```
apiVersion: crd.k8s.amazonaws.com/v1alpha1
kind: ENIConfig
metadata:
 name: group1-pod-netconfig
spec:
 subnet: subnet-0a364a23ee91b6f9b
 securityGroups:
 - sg-05f11134e34884dbc
 - sg-06a6e6e43d455f32f
```

> Create custom resource **group2-pod-netconfig.yaml** for second subnet (100.64.32.0/19). Replace the SubnetId and SecuritGroupIds as above.

> 为第二个子网络(100.64.32.0/19)创建定制资源**group2-pod-netconfig.yaml** 。记得改掉SubnetId 和SecuritGroupIds

> Similarly, create custom resource **group3-pod-netconfig.yaml** for third subnet (100.64.64.0/19). Replace the SubnetId and SecuritGroupIds as above.

> 为第二个子网络(100.64.64.0/19)创建定制资源**group3-pod-netconfig.yaml** 。记得改掉SubnetId 和SecuritGroupIds

> Check the instance details using this command as you will need AZ info when you apply annotation to Worker nodes using custom network config

> 当使用<u>定制网络配置</u><sup>custom network config</sup>注入注解<sup>annotation </sup>到工作节点时，你需要使用到AZ的信息，你可以使用下面的命令去检索实例信息。

```shell
aws ec2 describe-instances --filters "Name=tag-key,Values=eks:cluster-name" "Name=tag-value,Values=eksworkshop*" --query 'Reservations[*].Instances[*].[PrivateDnsName,Tags[?Key==`eks:nodegroup-name`].Value|[0],Placement.AvailabilityZone,PrivateIpAddress,PublicIpAddress]' --output table 
```

```
--------------------------------------------------------------------------------------------------------------------------
|                                                    DescribeInstances                                                   |
+----------------------------------------------------+------------+------------------+-----------------+-----------------+
|  ip-192-168-71-224.ap-northeast-1.compute.internal |  nodegroup |  ap-northeast-1c |  192.168.71.224 |  3.115.4.26     |
|  ip-192-168-63-192.ap-northeast-1.compute.internal |  nodegroup |  ap-northeast-1a |  192.168.63.192 |  18.179.196.63  |
|  ip-192-168-29-126.ap-northeast-1.compute.internal |  nodegroup |  ap-northeast-1d |  192.168.29.126 |  18.183.42.58   |
+----------------------------------------------------+------------+------------------+-----------------+-----------------+
```

> Apply the CRDs

> 应用这些CRD

```shell
kubectl apply -f group1-pod-netconfig.yaml
kubectl apply -f group2-pod-netconfig.yaml
kubectl apply -f group3-pod-netconfig.yaml
```

> As last step, we will annotate nodes with custom network configs.

> 最后一步，我们将通过自定义网络配置，给节点添加注解

> Warning: Be sure to annotate the instance with config that matches correct AZ. For ex, in my environment instance ip-192-168-33-135.us-east-2.compute.internal is in us-east-2b. So, I will apply **group1-pod-netconfig.yaml** to this instance. Similarly, I will apply **group2-pod-netconfig.yaml** to ip-192-168-71-211.us-east-2.compute.internal and **group3-pod-netconfig.yaml** to ip-192-168-9-228.us-east-2.compute.internal

> 注意：需小心地给实例进行注解，配置机器正确地AZ。举例，在我地环境里ip-192-168-33-135.us-east-2.compute.interna实例是在us-east-2b之下。因此，我需要应用 **group1-pod-netconfig.yaml** 到该实例。同样地，我需要应用**group2-pod-netconfig.yaml** 至 ip-192-168-71-211.us-east-2.compute.internal 和应用 **group3-pod-netconfig.yaml** 至 ip-192-168-9-228.us-east-2.compute.internal

```shell
kubectl annotate node <nodename>.<region>.compute.internal k8s.amazonaws.com/eniConfig=group1-pod-netconfig
```

> As an example, here is what I would run in my environment

> 举例，这里是在我环境下运行的命令。

```shell
kubectl annotate node ip-192-168-33-135.us-east-2.compute.internal k8s.amazonaws.com/eniConfig=group1-pod-netconfig
```























