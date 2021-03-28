---
layout: post
title: Spring-JPA Auditing
tags: [Java,Spring]
comments: 1
excerpt: Spring-JPA Auditing
typora-root-url: ...
---

Here is the [document](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing) about Auditing on official site.<br>这里是 Auditing 的官方文档。

## Definition

What is Auditing?<br>
Auditing 是什么？ 

> Spring Data provides sophisticated support to transparently **keep track of who created or changed an entity and when the change happened**. To benefit from that functionality, you have to equip your entity classes with auditing metadata that can be defined either using annotations or by implementing an interface. Additionally, auditing has to be enabled either through Annotation configuration or XML configuration to register the required infrastructure components. Please refer to the store-specific section for configuration samples.

## Usage

> Spring Data JPA ships with an entity listener that can be used to **trigger the capturing** of auditing information. **First, you must register the `AuditingEntityListener`** to be used for all entities in your persistence contexts inside your `orm.xml` file, as shown in the following example:

Ref: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/index.html#jpa.auditing

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MyEntity {

}
```

