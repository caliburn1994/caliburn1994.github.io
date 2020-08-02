---
layout: post
title: kubernetes-认证
date: 2020-08-02 00:00:02
tags: [kubernetes]
comments: 1
excerpt:
typora-root-url: ..
---



## 访问集群-概要

所谓的「访问集群」实质上分为三个步骤：

1. 通过集群CA<sup>Certificate Authority</sup>签发的证书建立连接。证书的获取参考[该章节](#获得证书)
2. 通过令牌验证身份
3. 访问REST API

我们通过curl来演示上述操作：

```shell
# 获取Service Account所在的secret名
DefaultTokenName=`kubectl get serviceaccounts  default  -o jsonpath="{.secrets[*]['name']}"`

# 从该secret种获取ca证书，并存储为./ca_cert.pem
kubectl get secrets ${DefaultTokenName} -o=jsonpath='{.data.ca\.crt}' | base64 --decode > ./ca_cert.pem

# 获得JWT令牌
Token=`kubectl get secrets ${DefaultTokenName} -o=jsonpath='{.data.token}' | base64 --decode`

# 使用上述两者对kubernetes REST API进行访问
curl \
--header "Authorization: Bearer ${Token}" \
--cacert ./ca_cert.pem \
-X GET <REST API网址>/api
```



## 证书

### 获得证书



该证书有若干个地方可以获取得到，如：

- AWS EKS的面板

![image-20200802152806799](/../assets/blog_res/image-20200802152806799.png)

- kubeconfig文件

```shell
cat ~/.kube/config
```

<div class="kyakya_collap" value="config文件内容："></div>

```yaml
apiVersion: v1
kind: Config
preferences: {}

clusters:
- cluster: # 集群的kubernetes API地址和凭证
    certificate-authority-data: # EKS的<Certificate authority>
    server: https://2C1A77626A2087EBA1D1123EA9398DAF.gr7.ap-northeast-1.eks.amazonaws.com
    # EKS的<API server endpoint>
  name: eksworkshop-eksctl.ap-northeast-1.eksctl.io
contexts:
- context:
    cluster: eksworkshop-eksctl.ap-northeast-1.eksctl.io
    user: kyakya@eksworkshop-eksctl.ap-northeast-1.eksctl.io
  name: kyakya@eksworkshop-eksctl.ap-northeast-1.eksctl.io
current-context: kyakya@eksworkshop-eksctl.ap-northeast-1.eksctl.io
users:
- name: kyakya@eksworkshop-eksctl.ap-northeast-1.eksctl.io
  user:
    exec:
      apiVersion: client.authentication.k8s.io/v1alpha1
      args:
      - token
      - -i
      - eksworkshop-eksctl
      command: aws-iam-authenticator
      env:
      - name: AWS_STS_REGIONAL_ENDPOINTS
        value: regional
      - name: AWS_DEFAULT_REGION
        value: ap-northeast-1
```

`certificate-authority-data`下就是Base64编码的pem格式证书。通过以下命令可以解码Base64

```shell
echo <certificate-authority-data字段的数据> | base64 --decode
```

- 通过默认的Service Account

```shell
# 获取Service Account所在的secret名
DefaultTokenName=`kubectl get serviceaccounts  default  -o jsonpath="{.secrets[*]['name']}"`
# 从该secret种获取ca证书，并存储为./ca_cert.pem
kubectl get secrets ${DefaultTokenName} -o=jsonpath='{.data.ca\.crt}' | base64 --decode > ./ca_cert.pem
```

### 证书格式

上一章节我们提到在哪里获取证书。而需注意，一般会以Base64格式存储在配置文件，因为PEM拥有回车以及空格，而单独格式存储在文件时，并没必要以Base64格式存储。

在获取证书后，我们将其保存为`ca_cert.pem`文件，本章节将更为详细地介绍该证书。

```shell
$ cat ./ca_cert.pem
-----BEGIN CERTIFICATE-----
MIICyDCCAbCgAwIBAgIBADANBgkqhkiG9w0BAQsFADAVMRMwEQYDVQQDEwprdWJl
cm5ldGVzMB4XDTIwMDcyNjE1NDM1MloXDTMwMDcyNDE1NDM1MlowFTETMBEGA1UE
AxMKa3ViZXJuZXRlczCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMFg
GIdj7++HyXHjlYkFTcJdLtnd+h4ZtwIPEQ8gv7Dd9eexG0qzHhz3oVl3nqLAzVO4
slOIXYGixvQmk5oMrB28U6ovGVR39YOQ/yBp48JQt4+KhCoxCxUR0+Mty6DIe6+A
p1w3xvxE2SryF5EHpsHkCeLeRMGvfz5JuktpoZv3QnYFSTP0HhBCfe/KPZ7rjZB1
yvMCr/jugsUhrv2jxOmqnZ6XtQqlsj1fpSLzejHpJBPPjBVcRVK8Mf2SLm8cvogC
+6uVj3yNE+uy1BgH4xcCWrbb6XYUCApPXHI7nD1Thurnq2LWjuGI2k6WMWPIMyWW
GjQaMIRVeHqKtvE2dyUCAwEAAaMjMCEwDgYDVR0PAQH/BAQDAgKkMA8GA1UdEwEB
/wQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEBAJXD9c8PocS0OfWJDEHixImAK/Xz
Y6MMPL3ox4mW97M8DSoMyXQHDnDiZWjr4utCDcG4s9Lwc1zMRON2hTZgObeMaHl1
Vt2OmQSG/ooIS1F+42BkVoK6D7ndIPbaOCnL9h5MQjES41ZbWFFa9CAf2de8ZRVd
Q3coUPlWHKcBlbzNTcHZfu0IIfxsGbbdtH/FAx2CNTVA5X2TY4sxaGqZXkGNxQEs
Tqu+W72wUXt758/RBZcOf4uP/XpSjgeyjWobgNlualcCpR0vnVpOVkGwGF12Ax9X
Hcc/NCdDuVoituvE2DRTOITiHnns7XakB+z2Hr7kuMCyTP6maZa2XcG7mIo=
-----END CERTIFICATE-----
```

由于格式为 `BEGIN CERTIFICATE` 开头，`END CERTIFICATE` 结尾，我们可以知道其为[PEM](https://en.wikipedia.org/wiki/Privacy-Enhanced_Mail)格式。PEM格式一般用于存储、发送证书和密钥。

```shell
# 查看证书格式内容
openssl x509 -in ca_cert.pem -text
```

<div class="kyakya_collap" value="证书格式内容："></div>

```
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number: 0 (0x0)
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN = kubernetes
        Validity
            Not Before: Jul 26 15:43:52 2020 GMT
            Not After : Jul 24 15:43:52 2030 GMT
        Subject: CN = kubernetes
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                RSA Public-Key: (2048 bit)
                Modulus:
                    00:c1:60:18:87:63:ef:ef:87:c9:71:e3:95:89:05:
                    4d:c2:5d:2e:d9:dd:fa:1e:19:b7:02:0f:11:0f:20:
                    bf:b0:dd:f5:e7:b1:1b:4a:b3:1e:1c:f7:a1:59:77:
                    9e:a2:c0:cd:53:b8:b2:53:88:5d:81:a2:c6:f4:26:
                    93:9a:0c:ac:1d:bc:53:aa:2f:19:54:77:f5:83:90:
                    ff:20:69:e3:c2:50:b7:8f:8a:84:2a:31:0b:15:11:
                    d3:e3:2d:cb:a0:c8:7b:af:80:a7:5c:37:c6:fc:44:
                    d9:2a:f2:17:91:07:a6:c1:e4:09:e2:de:44:c1:af:
                    7f:3e:49:ba:4b:69:a1:9b:f7:42:76:05:49:33:f4:
                    1e:10:42:7d:ef:ca:3d:9e:eb:8d:90:75:ca:f3:02:
                    af:f8:ee:82:c5:21:ae:fd:a3:c4:e9:aa:9d:9e:97:
                    b5:0a:a5:b2:3d:5f:a5:22:f3:7a:31:e9:24:13:cf:
                    8c:15:5c:45:52:bc:31:fd:92:2e:6f:1c:be:88:02:
                    fb:ab:95:8f:7c:8d:13:eb:b2:d4:18:07:e3:17:02:
                    5a:b6:db:e9:76:14:08:0a:4f:5c:72:3b:9c:3d:53:
                    86:ea:e7:ab:62:d6:8e:e1:88:da:4e:96:31:63:c8:
                    33:25:96:1a:34:1a:30:84:55:78:7a:8a:b6:f1:36:
                    77:25
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Key Usage: critical
                Digital Signature, Key Encipherment, Certificate Sign
            X509v3 Basic Constraints: critical
                CA:TRUE
    Signature Algorithm: sha256WithRSAEncryption
         95:c3:f5:cf:0f:a1:c4:b4:39:f5:89:0c:41:e2:c4:89:80:2b:
         f5:f3:63:a3:0c:3c:bd:e8:c7:89:96:f7:b3:3c:0d:2a:0c:c9:
         74:07:0e:70:e2:65:68:eb:e2:eb:42:0d:c1:b8:b3:d2:f0:73:
         5c:cc:44:e3:76:85:36:60:39:b7:8c:68:79:75:56:dd:8e:99:
         04:86:fe:8a:08:4b:51:7e:e3:60:64:56:82:ba:0f:b9:dd:20:
         f6:da:38:29:cb:f6:1e:4c:42:31:12:e3:56:5b:58:51:5a:f4:
         20:1f:d9:d7:bc:65:15:5d:43:77:28:50:f9:56:1c:a7:01:95:
         bc:cd:4d:c1:d9:7e:ed:08:21:fc:6c:19:b6:dd:b4:7f:c5:03:
         1d:82:35:35:40:e5:7d:93:63:8b:31:68:6a:99:5e:41:8d:c5:
         01:2c:4e:ab:be:5b:bd:b0:51:7b:7b:e7:cf:d1:05:97:0e:7f:
         8b:8f:fd:7a:52:8e:07:b2:8d:6a:1b:80:d9:6e:6a:57:02:a5:
         1d:2f:9d:5a:4e:56:41:b0:18:5d:76:03:1f:57:1d:c7:3f:34:
         27:43:b9:5a:22:b6:eb:c4:d8:34:53:38:84:e2:1e:79:ec:ed:
         76:a4:07:ec:f6:1e:be:e4:b8:c0:b2:4c:fe:a6:69:96:b6:5d:
         c1:bb:98:8a
-----BEGIN CERTIFICATE-----
MIICyDCCAbCgAwIBAgIBADANBgkqhkiG9w0BAQsFADAVMRMwEQYDVQQDEwprdWJl
cm5ldGVzMB4XDTIwMDcyNjE1NDM1MloXDTMwMDcyNDE1NDM1MlowFTETMBEGA1UE
AxMKa3ViZXJuZXRlczCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMFg
GIdj7++HyXHjlYkFTcJdLtnd+h4ZtwIPEQ8gv7Dd9eexG0qzHhz3oVl3nqLAzVO4
slOIXYGixvQmk5oMrB28U6ovGVR39YOQ/yBp48JQt4+KhCoxCxUR0+Mty6DIe6+A
p1w3xvxE2SryF5EHpsHkCeLeRMGvfz5JuktpoZv3QnYFSTP0HhBCfe/KPZ7rjZB1
yvMCr/jugsUhrv2jxOmqnZ6XtQqlsj1fpSLzejHpJBPPjBVcRVK8Mf2SLm8cvogC
+6uVj3yNE+uy1BgH4xcCWrbb6XYUCApPXHI7nD1Thurnq2LWjuGI2k6WMWPIMyWW
GjQaMIRVeHqKtvE2dyUCAwEAAaMjMCEwDgYDVR0PAQH/BAQDAgKkMA8GA1UdEwEB
/wQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEBAJXD9c8PocS0OfWJDEHixImAK/Xz
Y6MMPL3ox4mW97M8DSoMyXQHDnDiZWjr4utCDcG4s9Lwc1zMRON2hTZgObeMaHl1
Vt2OmQSG/ooIS1F+42BkVoK6D7ndIPbaOCnL9h5MQjES41ZbWFFa9CAf2de8ZRVd
Q3coUPlWHKcBlbzNTcHZfu0IIfxsGbbdtH/FAx2CNTVA5X2TY4sxaGqZXkGNxQEs
Tqu+W72wUXt758/RBZcOf4uP/XpSjgeyjWobgNlualcCpR0vnVpOVkGwGF12Ax9X
Hcc/NCdDuVoituvE2DRTOITiHnns7XakB+z2Hr7kuMCyTP6maZa2XcG7mIo=
-----END CERTIFICATE-----
```

## JWT令牌

通过以下命令

```shell
# 获取Service Account所在的secret名
DefaultTokenName=`kubectl get serviceaccounts  default  -o jsonpath="{.secrets[*]['name']}"`

# 获得JWT令牌
Token=`kubectl get secrets ${DefaultTokenName} -o=jsonpath='{.data.token}' | base64 --decode`
```

我们获得了编码后的令牌：

```
eyJhbGciOiJSUzI1NiIsImtpZCI6InY5NmhaZUNOTnJ6Tm1mbmdXU2JuZkhZV0ZUMWg2TlNuamk2TDdoaGYtLTgifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImRlZmF1bHQtdG9rZW4tNnF6c3ciLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGVmYXVsdCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjI2OTZjODNkLWM2MTMtNDkyNi04ZTUzLWRkZDhlOGU2YmM5YiIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDpkZWZhdWx0OmRlZmF1bHQifQ.AtYCSzZXSMS2jnLL6G1DqbEEThJDl8PgFxz8FaXjcKVw50aVictWr88Xykgeni7ht63No_9mWQoDSCbUQXvRi1Q9rLdZL1QKj0v6fokxegnVbW1PlR_dJxQO9yq3AV1SbL02x6ERabEirZTETVUna56WVj8vUur_2rx4tg3SKETUI3oJdw8OoissB-jlJAUCjJQZrPvHAkuOD8oxRUFDHRrhI9uzCyq70f7Ayeto59Cxjw8ByG2N9zbLPX5PpLoy4cVDP1SeLIDuMiLJAQo5iz-kWbyggVKe4LT10BT0gCo5hEgJsqO79zuoN-QTerXhgBT5Q7MxFkiSmyKm7ChkXA
```

通过该[网址](https://jwt.io/)解码后：

```json
{
  # Header（头部）
  "alg": "RS256",
  "kid": "v96hZeCNNrzNmfngWSbnfHYWFT1h6NSnji6L7hhf--8"
}
{
   # Payload（负载）
  "iss": "kubernetes/serviceaccount",
  "kubernetes.io/serviceaccount/namespace": "default",
  "kubernetes.io/serviceaccount/secret.name": "default-token-6qzsw",
  "kubernetes.io/serviceaccount/service-account.name": "default",
  "kubernetes.io/serviceaccount/service-account.uid": "2696c83d-c613-4926-8e53-ddd8e8e6bc9b",
  "sub": "system:serviceaccount:default:default"
}
{
   # Signature（签名）
}
```

JWT令牌更详细的中文解析可参考[JSON Web Token 入门教程](https://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html)