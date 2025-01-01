

## 1. 用于下载的仓库

通过配置 pom.xml 的`<repository>` ，就可以指定依赖包仓库来源

### 1.1. 公开仓库：

```xml
<repositories>
    <repository>
        <id>central</id>
        <name>Maven Central Repository</name>
        <url>https://repo.maven.apache.org/maven2</url>
    </repository>
    <repository>
        <id>central_1</id>
        <name>Maven Central Repository</name>
        <url>https://repo1.maven.org/maven2</url>
    </repository>
</repositories>
```

### 1.2. 私有 GitLab 仓库配置

使用某一个 Group 下面的依赖包，pom.xml 添加 Group 的地址：

- pom.xml 配置
  
    ```xml
    <repositories>
        <repository>
            <id>gitlab-maven</id>
            <url>https://${gitlab地址}/api/v4/groups/${group-id}/-/packages/maven</url>
        </repository>
    </repositories>
    ```
    

setting.xml 需要配置对应的认证凭证

- CI pipeline 专用 settings.xml
  
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <settings xmlns="http://maven.apache.org/SETTINGS/1.2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
        <servers>
            <server>
                <id>gitlab-maven</id>
                <configuration>
                    <httpHeaders>
                        <property>
                            <name>Job-Token</name>
                            <value>${CI_JOB_TOKEN}</value>
                        </property>
                    </httpHeaders>
                </configuration>
            </server>
        </servers>
    </settings>
    ```
    
- 本地开发使用的 settings.xml
  
    ```xml
    <settings>
        <servers>
          <server>
            <id>gitlab-maven</id>
            <configuration>
              <httpHeaders>
                <property>
                  <name>Private-Token</name>
                  <value>${PERSONAL_ACCESS_TOEKN}</value>
                </property>
              </httpHeaders>
            </configuration>
          </server>
        </servers>
    </settings>
    ```
    

## 2. 用于发布的仓库

发布依赖包时，使用的是 pom.xml 文件里的 `<distributionManagement>` **。**

distributionManagement 由 distribution 和 management 两个单词组成，译为分发包管理。这里的 distribution 和 software distribution 是相同意思。

**上传到私有 GitLab 仓库配置**

- settings.xml
  
    ```xml
    <distributionManagement>
          <repository>
              <id>gitlab-maven</id>
            <url>https://${gitlab地址}/api/v4/groups/${group-id}/-/packages/maven</url>
          </repository>
          <snapshotRepository>
              <id>gitlab-maven</id>
            <url>https://${gitlab地址}/api/v4/groups/${group-id}/-/packages/maven</url>
          </snapshotRepository>
      </distributionManagement>
    ```
    
    **snapshotRepository：** 非必要，用于发布稳定版本（非快照版本）