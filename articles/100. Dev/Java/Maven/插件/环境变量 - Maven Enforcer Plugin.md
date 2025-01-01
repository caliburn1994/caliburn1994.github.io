

**Enforcer plugin provides** 用于控制某些 environmental constraints，如：Maven、JDK

## 配置

[基础配置](https://maven.apache.org/enforcer/maven-enforcer-plugin/plugin-info.html)：

```xml
<project>
  ...
  <build>
    <!-- To define the plugin version in your parent POM -->
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-enforcer-plugin</artifactId>
          <version>3.0.0-M3</version>
        </plugin>
        ...
      </plugins>
    </pluginManagement>
    <!-- To use the plugin goals in your POM or parent POM -->
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <version>3.0.0-M3</version>
      </plugin>
      ...
    </plugins>
  </build>
  ...
</project>
```

### JDK&Maven版本检测

```xml
<executions>
    <execution>
        <goals>
            <goal>enforce</goal>
        </goals>
        <configuration>
            <rules>
                <requireMavenVersion>
                    <version>3.1.0</version>
                </requireMavenVersion>
                <requireJavaVersion>
                    <version>1.8</version>
                </requireJavaVersion>
            </rules>
        </configuration>
    </execution>
</executions>
```





## maven 2

使用以下xml检查版本

```xml
  <prerequisites>
    <maven>2.0.6</maven>
  </prerequisites>
```

