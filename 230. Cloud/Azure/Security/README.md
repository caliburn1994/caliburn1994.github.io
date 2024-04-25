

## Microsoft Entra

[Microsoft Entra ID](https://aka.ms/MicrosoftEntra) 是基于云的身份和访问管理对策 (a cloud-based identity and access management solution)。

它提供了以下功能以用于抵御网络安全攻击。[["]](https://www.microsoft.com/en-us/security/business/identity-access/microsoft-entra-id)

- 单点登录 single sign-on
- 多要素认证 multifactor authentication
- 条件访问 conditional access
- ...



## 概念

### 1. Tenant

**Azure AD tenant** 当我们注册了微软云服务时，就会获得一个 Azure AD service instance，这样的一个 instance 就叫做一个 tenant。通常一个 tenant 代表一个组织，因此 tenant 也是独一无二的。

- **tenant** 中文意思为租客，此处可以理解为，我们要借微软云服务的资源，因此我们是租客。



### 2. Subscription

Azure 把 Subscription 翻译为订阅。但，如果我们仔细去看 [字典](https://dictionary.cambridge.org/dictionary/english/subscription) 的解释便可以理解 Subscription 是加入或使用某个组织的服务，所需要缴纳的定期费用。在中文语境下常见的有：**订阅费**、月度会员费、年度会员费、月度报纸费。

Azure 也是将其描述为订阅费。[["]](https://learn.microsoft.com/en-us/microsoft-365/enterprise/subscriptions-licenses-accounts-and-tenants-for-microsoft-cloud-offerings?view=o365-worldwide)



### 3. Management Group

 management group 可以对若干个 Subscription 资源进行规范。是通过 [Azure Policy](https://learn.microsoft.com/en-us/azure/governance/policy/overview) 进行规范的。同时可以也会集中化 Subscription 部分功能，如：费用查看。

每一个 tenant 都有一个 top-level management group 叫 做 **root management group**、在这下面，可以分更多的 management group。更多详情查看 [官网](https://learn.microsoft.com/en-us/azure/governance/management-groups/overview)



![](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/202209241812584.png)





### 4. Group

Microsoft Entra ID 有两种类型的 group：**Security groups** 和 **Microsoft 365 groups**。

- **Security groups**：通过 Security groups 可以为 group 内的对象授权。

  - 对象可是设备、资源、用户、group。

- **Microsoft 365 Groups**： 在这个 group 下的所有用户可以共享邮箱、文件等微软服务。

  在 outlook 创建的 group 就是 **Microsoft 365 Group**，但 365 group 无法成为另外一个 group 的 member。由于无法嵌套 group，365 group 只适合使用在微软相关服务。



![image-20240417155136728](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/image-20240417155136728.png)

### 

### **4.1. Membership types**

用户组 (Group) 中有三种 **Membership types**，是在创建 group 的时候选择的。 [[”]](https://learn.microsoft.com/en-us/entra/fundamentals/concept-learn-about-groups)

- **Assigned**: 只能手动添加用户到用户组。
- Dynamic：需要 P1 或 P2 license
  - **Dynamic user**: 根据自身属性，自动分配用户到群组。
  - **Dynamic device**: 根据设备属性，自动用户到群组。


Assigned 类型的 group 有两种方式添加用户 [["]](https://learn.microsoft.com/en-us/training/modules/create-users-and-groups-in-azure-active-directory/4-manage-app-resource-access-azure-ad-groups)

- **Direct assignment**: 直接添加个别用户
- **Group assignment**: 添加群组，该群组下的所有用户都会被添加

Dynamic 类型的 group 添加用户的方式叫做 **Rule-based assignment**。



### **5. Administrative Unit（管理单元）**

**目的：** Administrative Unit 的主要目的是为了在较大的 Azure AD 组织中**提供更灵活和精细的权限管理**和策略应用。

每一个 Group 可以是一个部门，而在 Group 里可以直接添加 Administrative Unit。让 Administrative Unit 可以管理这些 Group。

![image-20240417154523017](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/image-20240417154523017.png)

### 6. User

**Microsoft Entra administrator roles** 可以对用户进行操作，包含：创建编辑用户、重置密码。这与 Azure role 是不同的，Azure role 是现存的用户授权，让他们访问 Azure 资源。

用户又分为:

- Member users: 组织成员。可进行个人资料编辑
- Guest users: 外来人员。
- Administrator: 管理员。可以操作所有人的资料





### 7. License

License 可以是描述软件的，也可以描述的是人的。Azure 中提到的 License 一般是指人的，因为软件的 License 费用一般都内嵌到服务里。[["]](https://learn.microsoft.com/en-us/microsoft-365/enterprise/subscriptions-licenses-accounts-and-tenants-for-microsoft-cloud-offerings?view=o365-worldwide)

参考: [Azure AD Premium P1 vs. P2: Which is right for you?](https://www.techtarget.com/searchwindowsserver/tip/Azure-AD-Premium-P1-vs-P2-Which-is-right-for-you)

![](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/202212190020471.png)



## 探讨

### 1. 开发产品环境分离 Separate Dev and Production 

开发产品环境分离，有两种方案：

- 使用 subscription 去分离
- 使用 tenant 去分离

两种方案，各有优缺点吧，**Azure 推荐使用 subscription 去分离**。

![Diagram showing the configuration of DevTest and DevOps for a PaaS application.](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/dev-test-paas.png)

原因：[["]](https://learn.microsoft.com/en-us/azure/cloud-adoption-framework/ready/enterprise-scale/testing-approach)

- 增加费用：
  - Azure AD licensing 费用。

- 复杂度和偏差：
  - RBAC
  - 各种配置：使用多 tenant，则很多配置需要配置两份。由于要配置两份，有时候会有一些偏差。
  - Azure 服务： 很多 Azure 服务也不支持跨 tenant







### 2. Azure vs Microsoft Entra

**Microsoft Entra** 和 **Azure** 是两个不同的概念。[["]](https://learn.microsoft.com/en-us/entra/identity/role-based-access-control/custom-overview)

- **Microsoft Entra** 的 RBAC 针对的是 Microsoft Entra 资源，如：users, groups,  applications using the Microsoft Graph API
- **Azure roles** 主要用于控制对 Azure 资源的访问权限，如 Azure VM









## 推荐网站

- [Tip 272 - Azure Security Best Practices | Azure Tips and Tricks (microsoft.github.io)](https://microsoft.github.io/AzureTipsAndTricks/blog/blog/tip272.html)

