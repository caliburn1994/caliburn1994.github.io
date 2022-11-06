---
Last Modified: 2022-11-06
---



## 1. Key vault 和 Service principal 的联调

1. 创建一个 App Registration
   - 在 **[Overview]** 获取 
     - **Application (client) ID**（下文统称 **${clientId}**）
     - **Directory (tenant) ID**（下文统称**${tenantId}**）
   - 在 **[Certificates & secrets] > [Client secret]** 创建一个 Client secret。并保存该 value 数值，下文统称 **${clientSecret}**
2. 创建一个 key vault
   - 在 **[Access policies]** 里点击 **[create]**，创建一个 application 的 access policy
     - **[Permissions]** 选择所需要的权限
     - Principal 里输入 **${clientId}**





## 2. Maven包

追加以下内容到 pom.xml 文件

```xml
<dependency>
  <groupId>com.azure</groupId>
  <artifactId>azure-identity</artifactId>
</dependency>
<dependency>
  <groupId>com.azure</groupId>
  <artifactId>azure-security-keyvault-keys</artifactId>
</dependency>
```

```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.azure</groupId>
                <artifactId>azure-sdk-bom</artifactId>
                <version>1.2.3</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```





## 3. 如何连接 key vault

```java
        // key vault
        var keyVaultUrl = "https://xxx.vault.azure.net/";

        // service principal相关信息和secret
        var clientId = ${clientId};
        var clientSecret = ${clientSecret};
        var tenantId = ${tenantId};

        // 创建凭证
        var credential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();
        // 客户端
        var client = new KeyClientBuilder()
                .vaultUrl(keyVaultUrl)
                .credential(credential)
                .buildClient();
```

