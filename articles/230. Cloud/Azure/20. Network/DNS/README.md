## DNS record types

- A(Address Record): 将域名或 host 名映射到一个 IP 地址。
- AAAA (IPv6 Address Record) :类似于 A 记录，用于 IPv6 地址解析。
- CNAME(Canonical Name Record): 将域名指向另一个域名。
- MX(Mail Exchanger Record): 让邮件发送到指定的邮箱服务器。
- MX(Mail Exchange): text record。Azure 和 Microsoft 365 用于验证域名的拥有权。[["]](https://learn.microsoft.com/zh/training/modules/host-domain-azure-dns/2-what-is-azure-dns)
- NS (name server): 用于解析特定域名的权威 DNS 服务器。它定义了域名的 DNS 服务器。
- SOA (start of authority): 记录包含关于域名的授权信息，如域名的主 DNS 服务器、负责人电子邮件地址等。

## domain delegation

Changing the NS details is called *domain delegation*. When you delegate the domain, you must use all four name servers provided by Azure DNS.[["]](https://learn.microsoft.com/en-us/training/modules/host-domain-azure-dns/3-configure-azure-dns-host-domain)