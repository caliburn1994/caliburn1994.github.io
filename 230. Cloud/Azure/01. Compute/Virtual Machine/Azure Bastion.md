Azure Bastion 是一种托管服务，提供从 Azure 门户安全连接到 Azure 虚拟机 (VM) 的功能，而无需通过公共 IP 地址进行连接。它通过 Azure 门户直接提供基于浏览器的远程桌面协议 (RDP) 和安全外壳协议 (SSH) 连接，确保你的虚拟机不会暴露在公共互联网中，从而提高安全性。

为了保证 Bastion 能正常运行，必须在 size >= /27 的 subnets（如：10.1.1.0/27） 