---
layout: post
title: 1.Primary Key
date: 2021-02-01 00:00:02
tags: [database]
comments: 1
excerpt: 关于主键(Primary Key)的探讨。
typora-root-url: ..
---

## What

主键，英文Primary Key，用于表的唯一标识。<sup>[[psql]](https://www.postgresql.org/docs/8.1/ddl-constraints.html)</sup>

主键的实质是由以下组成的：

> Technically, a primary key constraint is simply a **combination** of a **unique constraint** and a **not-null constraint**. 

> Adding a **unique constraint** will automatically create a **unique B-tree index** on the column or group of columns listed in the constraint. A uniqueness restriction covering only some rows **cannot** be written as a unique constraint, but it is possible to enforce such a restriction by creating a unique **partial index**.

- 唯一约束（unique constraint）
- 非空约束（not-null constraint）
- B-tree index 或 [partial index](https://www.postgresql.org/docs/current/indexes-partial.html)

其他参考：https://dev.mysql.com/doc/refman/8.0/en/primary-key-optimization.html

## Necessary

> When you define a `PRIMARY KEY` on your table, `InnoDB` uses it as the clustered index. Define a primary key for each table that you create. If there is no logical unique and non-null column or set of columns, add a new [auto-increment](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_auto_increment) column, whose values are filled in automatically.

[MySQL官网](https://dev.mysql.com/doc/refman/8.0/en/innodb-index-types.html) 中写到，如果不创建主键（`PRIMARY KEY` ），又没有唯一部位空的一个列，那么将自动创建一个自增加的列，用作 clustered 索引。所以我们**应该创建主键**。

## How

主键的实现可以从**数值生成地点**、**数值生成方式**两个角度出发。

根据主键数值的**生成地点**的不同，可以分为 由**应用程序** 和 由**数据库** 生成。如果数值是由数据库生成的话，那么数值的递增是**不会有冲突的**（不可能同一时间有相同的主键数值），但会提高耦合度。

根据数值**生成方式**，**递增**和**无序**。递增的方式适合于B-Tree结构的索引，因为插入新的数值所导致B-Tree结构大幅度变动几率低。而无序适合于hash index，但hash index需要表单为内存模式<sup>[[mysql]](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_hash_index)</sup>。

由数据库生成：

- 单体数据库的 自增长主键——[`auto-increment`](#auto-increment)。
- 分布式数据库的 自增长主键——[`UUID_SHORT()`](https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid-short)。
- 全局主键——[UUID](https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid)

由应用程序生成：

- 分布式数据库主键
  - 利用算法生成的无冲突的全局主键（无序）
  - 自增长主键（递增）

### 数据库生成

#### auto-increment

由于数据库一般使用的是B-TREE，所以检索时会将范围连续的数据**一并检索**，所以字段如果过长，将会势必会减少每一次读取的数据数据量，即会增加从磁盘的读取次数，造成读取的时间。所以**字段长度要合理**。

MySQL中的数据长度分为以下：<sup>[[MySQL]](https://dev.mysql.com/doc/refman/8.0/en/integer-types.html)</sup>

![image-20210217114118363](/../../assets/blog_res/image-20210217114118363.png)

#### UUID（数据库层）

MySQL的 `UUID()` 是使用了 RFC 4122 中定义的 <u>版本一的UUID</u>，而版本一是根据 时间戳(timestamp) 和 MAC地址 生成的，是**递增的**，而**不是随机的**，因此适用于B-Tree。<u>版本一的UIID</u>是<u>根据 60-bit的时间戳</u> + <u>48-bit MAC地址</u> 生成的，生成的字符串为128bit，36个字符。

```
dae38ac4-734b-11eb-9439-0242ac130002
```

即便是插入的数据是递增的，但是由数据长度过长，所以并RFC所规定的UUID并不适合当B-Tree进行检索。

对于`UUID()`，我们可以使用 [`UUID_TO_BIN`](https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid-to-bin) 将字符串转换城二进制数据，从而节省存储空间，转换后的数据将为<u>16字节</u>。

版本一的UUID 可以的解析度为100纳秒，也就是说当达到 <u>1000万</u>每秒的频率才有可能产生重复的ID。<sup>[StackOverflow](https://stackoverflow.com/a/6963990/4883754)</sup>

参考：[mysql文档](https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid)、[UUID的原理](https://www.sohamkamani.com/uuid-versions-explained/)、[在线UUID生成器](https://www.uuidgenerator.net/version1)

#### UUID_SHORT

[`UUID_SHORT()`](https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid-short) 是以 <u>64-bit</u> 的 <u>无符号整数</u>(unsigned integer) 的形式进行存储，即储存空间为8个字节，是 `UUID()` 的一半。根据 [`server_id`](https://dev.mysql.com/doc/refman/8.0/en/replication-options.html#sysvar_server_id)(1字节)、服务器启动时间(server_startup_time_in_seconds)、自增数(incremented_variable) 三个部分而生成的。[`UUID_SHORT()`](https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid-short) 每秒最大可以达到<u>1600万</u>的频率。

### 应用程序生成

#### UUID（应用层）

UUID 如果使用版本一，那么和上述的UUID一致。如果使用其他版本，那么可能导致巨大差异。比如，版本四的UUID 是乱序的，那么不适合B-Tree的插入操作。

#### 自增长主键

应用层的自增式UUID 和 MySQL的 [`UUID_SHORT()`](https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid-short)  相似，无论从组成结构，还是从存储大小。如

- [sony](https://github.com/sony)/**[sonyflake](https://github.com/sony/sonyflake)** （39+8+16=63-bit）
- [Snowflake](https://blog.twitter.com/engineering/en_us/a/2010/announcing-snowflake.html)

与[`UUID_SHORT()`](https://dev.mysql.com/doc/refman/8.0/en/miscellaneous-functions.html#function_uuid-short) 的区别在于——生成主键值的部件是否是数据库。如果不是的话，耦合性将会降低。Twitter 官方说，**因为 Cassandra(NoSQL) 没有内置方法生成 unique ids**，所以创造了[Snowflake](https://blog.twitter.com/engineering/en_us/a/2010/announcing-snowflake.html)，而snowflake需要得特性：<sup>[Snowflake](https://blog.twitter.com/engineering/en_us/a/2010/announcing-snowflake.html)</sup>

- 一千万每秒的生成量
- 有序
- 存储大小尽可能小

其他参考网站：

- [Leaf——美团点评分布式ID生成系统](https://tech.meituan.com/2017/04/21/mt-leaf.html)

## Others

### clustered index是什么？

MySQL有若干个存储引擎，其中一个存储引擎叫 [InnoDB](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_innodb)，它使用了的是 B-tree。<sup>[[mysql]](https://dev.mysql.com/doc/refman/8.0/en/innodb-physical-structure.html)</sup>

> With the exception of spatial indexes, `InnoDB` indexes are [B-tree](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_b_tree) data structures. Spatial indexes use [R-trees](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_r_tree),

B-tree 使用的物理索引是 clustered index，而不是hashed index。B-tree 有以下特性：<sup>[[wik]i](https://zh.wikipedia.org/wiki/B%E6%A0%91)</sup>

> 在计算机科学中，B树（英语：B-tree）是一种自平衡的树，能够保持数据有序。这种数据结构能够让查找数据、顺序访问、插入数据及删除的动作，都在对数时间内完成。

### 能使用字符串作为主键吗?

答：不应该使用字符串作为主键。因为字符串翻译成字节，将是随机数字，不利于 B-tree 的插入。如果是memory式的引擎的话，可以使用字符串作为主键，而索引应该使用 hash index（类似NoSQL的用法）。