Failsafe 用于集成测试，Surefire用于单元测试。<sup>[[来源]](https://maven.apache.org/surefire/maven-failsafe-plugin/index.html)</sup>



## Failsafe

[Failsafe](https://maven.apache.org/surefire/maven-failsafe-plugin/index.html) 用于集成测试。

xml参考

```xml
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>3.0.0-M5</version>
            </plugin>
        </plugins>
    </build>
</project>
```

## Surefire

[Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/index.html) 用于单元测试

### 使用 Junit 测试

[xml配置](https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html)：

<script src="https://gist.github.com/caliburn1994/de07a40b41839db2b760be062773cf3d.js"></script>

官网[地址](https://maven.apache.org/surefire/maven-surefire-plugin/examples/fork-options-and-parallel-execution.html)：

- `forkCount ` 定义了用于测试的最大 JVM程序数。
- `reuseForks`重用程序数。但是重用程序数，可能会用到其他测试代码的全局变量。<sup>[[来源]](https://stackoverflow.com/questions/34684482/maven-surefire-when-are-we-forced-to-set-reuseforks-false)</sup>

- `<argLine>-Xmx1024m -XX:MaxPermSize=256m</argLine>` 内存。
- `useUnlimitedThreads` 线程不限制