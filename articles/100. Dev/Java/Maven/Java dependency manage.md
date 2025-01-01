## 使用技巧

### 减少 package 数量

由于非核心的 package 常常依赖核心的 package。所以我们查找 package 时**需要去查看依赖的 package 有哪些**，尽可能**减少 package 的数量**。

### 从现存 package 分析

当添加新的 package 时，可以考虑查看现存的 package 和将要添加的 package 的依赖是否有同名，有同名的话，可以根据现存同名的 package 查找将要添加 package 的版本。

也可以根据 package 的**时间**进行下载。

如：

```
package2下载什么版本好呢？
分析：
package1(已经依赖的) --依赖--> package2(版本 2.2)

结论：
查找2.2版本的package2
```

