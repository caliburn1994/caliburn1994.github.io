---
typora-copy-images-to: ./images
---

![img](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/dev/images/image-202404212202.png)

# 1. 简介与重点

目的：避免使用过大权限进行操作

策略：

1. **Eligible（合格用户）**：在 Eligible 状态下**，**用户具有 activate 角色的资格。
1. **Active（活跃状态）**：在 Active 状态下，用户可以执行角色的权限。
1. **Activate（激活）**：激活角色权限。
1. …

参考：[What is Privileged Identity Management? - Microsoft Entra ID Governance | Microsoft Learn](https://learn.microsoft.com/en-us/entra/id-governance/privileged-identity-management/pim-configure#what-does-it-do)

# 2. 前提

确保租户具有所有用户（其身份或访问权限受到治理或其与身份治理功能交互）的 Microsoft Entra ID 治理或 Microsoft Azure AD Premium P2 许可证。