---
Last Modified: 2022-10-04
---





## TODO

- [ ] [Protect your admin accounts](https://learn.microsoft.com/en-us/microsoft-365/business-premium/m365bp-protect-admin-accounts?view=o365-worldwide).
- [ ] [Protect against malware and other threats](https://learn.microsoft.com/en-us/microsoft-365/business-premium/m365bp-increase-protection?view=o365-worldwide).



## 概述 Overview

一般而言，使用 Azure Active Directory 去登录，首先需要加强的是验证方式。默认情况下使用的是 Security defaults，这将会强制管理员去开启 MFA，为了更好的安全，可以强制所有人开启 MFA。MFA 可以免除几乎所有的攻击。

当需要设置登录限制，如：国家限制、IP地址限制，那么可以开启 Conditional Access。但需注意 Conditional Access 是收费的。

延申阅读：[Bump up security](https://learn.microsoft.com/en-us/microsoft-365/business-premium/m365bp-security-overview?view=o365-worldwide)



## Security defaults

[Security defaults](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/concept-fundamentals-security-defaults) 默认是启动的，它可以保护绝大多数的安全。

它包含了：[^1][^2]

- 要求注册 MFA
- 要求 admins 开启 MFA
- 根据实际情况， MFA 会增加警告次数。如下载整个文件夹
- 禁止无法使用 MFA 的 legacy authentication clients
  - 非 [modern authentication](https://docs.microsoft.com/en-us/microsoft-365/enterprise/hybrid-modern-auth-overview?view=o365-worldwide#what-is-modern-authentication) 的
  - older mail protocols such as IMAP, SMTP, or POP3

所以当我们要关闭 **Security defaults** 时候，必须做的是手动创建 policy 去cover上述所有场景。





## 条件访问 Conditional Access

### C1 条件访问的概要 Overview

**Conditional Access** 的前提条件有两个：

1. 更改套餐（license） [Azure Active Directory Premium](https://azure.microsoft.com/en-us/pricing/details/active-directory/)
2. 关闭 **[Security defaults](https://docs.microsoft.com/en-us/microsoft-365/business-premium/m365bp-conditional-access?view=o365-worldwide#security-defaults)** 

注意⚠️：Conditional Access 是一个付费功能，通过该功能可以自由的配置 policy。由于我们关闭 Security defaults，所以需要单独创建对应的 policy：[^3]

- [Require MFA for administrators](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-admin-mfa)
- [Require MFA for Azure management](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-azure-management)
- [Block legacy authentication](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-block-legacy)
- [Require MFA for all users](https://docs.microsoft.com/en-us/azure/active-directory/conditional-access/howto-conditional-access-policy-all-users-mfa)

**IP限制功能**：通过提前在 **[Named locations]** 定义一些国家、IP地址，并创建期待的 **[Conditional Access policy]**，我们可以禁止一些国家或者IP地址访问Azure（或者禁止访问Azure）。



### C2 注意事项

由于我们设置策略时，主要想要**覆盖所有人**，使所有人都在策略之下。测试和配置可如下：

1. 测试阶段阶段
   1. 针对 canary订阅（subscription）、测试订阅制作一个 Group。
   2. 针对上述的 Group，开启策略。策略的命名规则如 [此处](https://learn.microsoft.com/en-us/azure/active-directory/conditional-access/plan-conditional-access#set-naming-standards-for-your-policies) 所推荐。
   3. 立即生效否。确认包含 Azure portal、Azure CLI 的所有终端都正确启动
2. 持续一段时间，并观察报告。
3. 当上述测试通过时，在正式进行部署
   1. 甚至紧急账号，排除该账号 。[^4] 防止策略不生效时，整个 Azure AD组织 都被锁掉。

关于如何减轻部署测试所带来的影响，可参考 [Create a resilient access control management strategy with Azure Active Directory](https://learn.microsoft.com/en-us/azure/active-directory/authentication/concept-resilient-controls#administrator-lockout-contingency)



### C3 紧急账号 Emergency Access Accounts

紧急账号用于防止 Azure AD被锁，但由于它是一个例外，所以我们需要额外的限制。

- **特殊情况才使用**。就像 Linux Root 用户那样，一般而言我们不会使用 Root 用户去做任何事情，我们只会使用它进行授权而已，并且我们是禁止密码登录的。而 Azure 的紧急用户而言，同样我们需要禁止紧急用户的滥用。

- 当登录紧急账号时，需要对特定的一群人有**提示信息**。正如刚刚所说，紧急账号只有特殊情况才使用，这种特殊情况并不常见，为了减少滥用或尽快发现违规操作，需要设置警告提示。
- Conditional Access 不应该博阿寒紧急账号。如上文所说，紧急账号是一个例外。
- **验证方式应多样**。当开启 MFA 时，为了防止一个设备失效后，导致紧急账号不可使用，应该绑定多个验证方式。如：手机验证和 app 应用。当手机被换掉时，手机验证仍旧能生效，当处于无手机网络的地方时，能用 app 进行验证。（此处可斟酌是否代开）
- **多个紧急账号**。同上，为了防止一个紧急账号失效而无法使用，应该包含两个或以上的账号存在。

延申阅读：[Manage emergency access accounts in Azure AD](https://learn.microsoft.com/en-us/azure/active-directory/roles/security-emergency-access)



## 参考 References

[^1]: [Security defaults and Conditional Access - Microsoft Docs](https://docs.microsoft.com/en-us/microsoft-365/business-premium/m365bp-conditional-access?view=o365-worldwide)
[^2]: [Security defaults in Azure AD - Active Directory - Microsoft Docs](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/concept-fundamentals-security-defaults)
[^3]: [Security defaults in Azure AD - Active Directory - Microsoft Docs](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/concept-fundamentals-security-defaults#conditional-access)
[^4]: [Plan a Conditional Access deployment - Azure - Microsoft Learn](https://learn.microsoft.com/en-us/azure/active-directory/conditional-access/plan-conditional-access)