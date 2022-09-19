---
Last Modified: 2022-08-18
---



a



## Azure Active Directory

**Azure Active Directory**（简称：Azure AD）是隶属 [Microsoft Entra](https://aka.ms/MicrosoftEntra)，提供了以下功能[^4]

- 单点登录 single sign-on
- 多要素认证 multifactor authentication
- 条件访问 conditional access

以达到抵御网络安全攻击。



### A1. tenant

**Azure AD tenant** 当我们注册了微软云服务时，就会获得一个 Azure AD service instance，这样的一个 instance 就叫做一个 tenant。通常一个 tenant 代表一个组织，因此 tenant 也是独一无二的。[^5]

- **tenant** 中文意思为借主，此处可以理解为，我们要借微软云服务的资源，因此我们是借主。



### A2. Active Directory

**Active Directory** 实质上是一个 directory 服务，而 directory 服务实质上是一个 NoSQL 数据库。该服务可以存储各种各样的信息，常见包含：用户信息、域名信息、证书相关信息等等。Azure AD 则存储的是用户相关的信息，用户登录 Azure AD 时， Azure AD 会进行校验。如果校验成功，则会跳转到对应的资源上。

通过 Azure AD，让我们能登录 Azure Cloud。如果稍加配置，还可以让 Azure用户 登录其他网站。这里分享两个网站作为扩展阅读：

- [Tutorial: Sign in users and call the Microsoft Graph API from a React single-page app (SPA) using auth code flow](https://docs.microsoft.com/en-us/azure/active-directory/develop/tutorial-v2-react)
- [Authentication with Azure AD and React](https://adrianhynes.medium.com/authentication-with-azure-ad-and-react-ced9a829e083)

回到 AD 的主题上。在 Windows 2000 的时候，微软引入了 Active Directory Domain Services 管理本地（on-premises）架构里的用户。而 Azure AD 则是 Active Directory Domain Services 云化，变成一种服务。Azure 称之为 Identity as a Service (IDaaS) 。[^6]



## Licenses

TODO

- [ ] Azure AD Premium P1 vs. P2





## Security

### S.1 Conditional Access 条件访问

**Conditional Access** 的前提条件有两个：

1. 更改套餐（license） [Azure Active Directory Premium](https://azure.microsoft.com/en-us/pricing/details/active-directory/)
2. 关闭 **[Security defaults](https://docs.microsoft.com/en-us/microsoft-365/business-premium/m365bp-conditional-access?view=o365-worldwide#security-defaults)** 

注意⚠️：Conditional Access 是一个付费功能，通过该功能可以自由的配置 policy。由于我们关闭 Security defaults，所以需要单独创建对应的 policy：[^3]

- [Require MFA for administrators](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-admin-mfa)
- [Require MFA for Azure management](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-azure-management)
- [Block legacy authentication](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-block-legacy)
- [Require MFA for all users](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-all-users-mfa)

**IP限制功能**：通过提前在 **[Named locations]** 定义一些国家、IP地址，并创建期待的 **[Conditional Access policy]**，我们可以禁止一些国家或者IP地址访问Azure（或者禁止访问Azure）。





### S.2 Security defaults

[Security defaults](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/concept-fundamentals-security-defaults) 默认是启动的，它可以保护绝大多数的安全。

它包含了：[^1][^2]

- 要求注册 MFA
- 要求 admins 开启 MFA
- 根据实际情况， MFA 会增加警告次数。如下载整个文件夹
- 禁止无法使用 MFA 的 legacy authentication clients
  - 非 [modern authentication](https://docs.microsoft.com/en-us/microsoft-365/enterprise/hybrid-modern-auth-overview?view=o365-worldwide#what-is-modern-authentication) 的
  - older mail protocols such as IMAP, SMTP, or POP3

所以当我们要关闭 **Security defaults** 时候，必须做的是手动创建 policy 去cover上述所有场景。



## 参考 References

[^1]: [Security defaults and Conditional Access - Microsoft Docs](https://docs.microsoft.com/en-us/microsoft-365/business-premium/m365bp-conditional-access?view=o365-worldwide)
[^2]: [Security defaults in Azure AD - Active Directory - Microsoft Docs](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/concept-fundamentals-security-defaults)
[^3]: [Security defaults in Azure AD - Active Directory - Microsoft Docs](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/concept-fundamentals-security-defaults#conditional-access)
[^4]: [Azure Active Directory](https://azure.microsoft.com/en-us/services/active-directory/)
[^5]: [Create an Azure Active Directory tenant to use with Power BI](https://learn.microsoft.com/en-us/power-bi/developer/embedded/create-an-azure-active-directory-tenant#:~:text=An Azure AD tenant is,from other Azure AD tenants.)
[^6]: [Compare Active Directory to Azure Active Directory](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/active-directory-compare-azure-ad-to-ad)