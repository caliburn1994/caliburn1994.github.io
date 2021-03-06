---
layout: post
title: kubernetes-认证
date: 2020-08-02 00:00:02
tags: [AWS,kubernetes]
comments: 1
excerpt: kubernetes认证相关内容
typora-root-url: ..
---

## 访问集群

所谓的「访问集群」有两种：[访问集群中的服务](https://kubernetes.io/docs/tasks/access-application-cluster/access-cluster/#accessing-services-running-on-the-cluster)、[访问集群中的REST API](https://kubernetes.io/docs/tasks/access-application-cluster/access-cluster/#directly-accessing-the-rest-api)。本章节为了更为具体地讲述，选择后者作为示例，该示例实质上分为三个步骤：

1. 通过集群CA<sup>Certificate Authority</sup>签发的证书建立安全连接。证书的获取参考[该章节](#获得证书)
2. 通过JWT令牌验证身份。<sup>[[k8s]](https://kubernetes.io/docs/tasks/access-application-cluster/access-cluster/#accessing-the-api-from-a-pod)</sup>
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

### 证书

#### 获得证书

该证书有若干个地方可以获取得到，如：

- AWS EKS的面板

![image-20200802152806799](/../assets/blog_res/image-20200802152806799.png)

- kubeconfig文件

```shell
kubectl config view
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

#### 证书格式

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

```yaml
Certificate:
    Data:
        Version: 3 (0x2) # 版本号
        Serial Number: 0 (0x0) # 序列号
        Signature Algorithm: sha256WithRSAEncryption # 签名算法
        Issuer: CN = kubernetes # 颁发者
        Validity # 证书有效期
            Not Before: Jul 26 15:43:52 2020 GMT
            Not After : Jul 24 15:43:52 2030 GMT
        Subject: CN = kubernetes # 证书主体（使用证书的主体）
        Subject Public Key Info: # 公钥信息
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

从`Subject: CN = kubernetes`该字段，我们知道该证书是用于`kubernetes`，也就是我们使用公钥加密信息并发送给`kubernetes`。后面章节我们提到如何给集群创建用户时，将会提到两种创建用户的方式：

1. 通过上面的证书的签名，产生一个`Subject: CN=用户名`的新证书，并将该证书导入集群。通过上述一系列操作注册新用户。
2. 不导入新的证书，取而代之是创建

### JWT令牌

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
   # Signature（签名，防止数据篡改）
}
```

JWT令牌更详细的中文解析可参考[JSON Web Token 入门教程](https://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html)



## 创建用户

创建用户有两种方式：

- 通过Service Account的方式创建用户，需要在AWS创建信息，并在K8s中生成一些信息，最后将AWS的凭证输入到K8s中即可。
- 

### 通过Service Account创建用户

#### 背景资料

一个<u>策略</u><sup>Policy</sup>包含若干种权限，如：可读集群信息，可写集群信息等等。

一个<u>群</u><sup>Group</sup>拥有若干个<u>用户</u><sup>User</sup>，而我们将若干个Policy授予给一个Group，那么该Group下的所有User都会拥有这些Policy。当然，我也可以直接将Policy直接授予一个User。

而上一段描述的背景是所有的User都在同一个AWS账号中，而如果要跨越账号进行授权，那么我们需要使用到<u>角色</u><sup>Role</sup>。跨账号的例子有：大厂家提供了服务，然后客户要有相应权限才能使用该服务。

这时候，策略

#### 创建操作

我们以AWS EKS为示例，示例地址[此处](https://www.eksworkshop.com/beginner/091_iam-groups/intro/)。在AWS EKS中，我们新增加用户有以下步骤：

1. 创建Role（AWS） `开发者`
2. 创建Group，并让该Group成为Role（AWS） `产品开发团队`
3. 添加用户，并将该用户添加到Group中（AWS）`小明`
4. 创建一个K8s的Role，该Role将控制K8s用户对K8s内资源的操作权限（K8s）
5. 创建Subject（此处可以是用户），并将Role和Subject连接起来（K8s）
6. AWS的Role和K8s用户进行连接，这样就完成整个操作。

而上述的步骤不是完全固定的。K8s的用户可以与AWS的User、Group、Role任何一个进行连接。连接成功后，K8s的`developer`用户将与AWS的

- 小明（连接AWS的User的情况）
- 小明、小红ta们开发团队的每一个（连接AWS的Group的情况）
- 小明、小红ta们开发团队的每一个，以及隔壁团队和其他公司团队其他AWS账号的人（连接AWS的Role的情况）。

#### 配置用户

为了初始化`aws-cli`（即初始化`~/.aws`），需要将新创的AWS IAM用户的Key ID和Access Key等信息添加进来：

```shell
$ aws configure
AWS Access Key ID [None]: AKIAIOSFODNN7EXAMPLE
AWS Secret Access Key [None]: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Default region name [None]: region-code
Default output format [None]: json
```

初始化`~/.kube/`配置文件

```shell
# 手动切换角色
$ aws sts assume-role \
--role-arn arn:aws:iam::056844949861:role/k8sDev \
--role-session-name test-eks-role
{
    "Credentials": {
        "AccessKeyId": "ASIAQ2PBZDFSUGKRDRWV",
        "SecretAccessKey": "mMXb2op+gBP0HH99fMcukX6TrEAhoFHW+L9r2A6N",
        "SessionToken": "IQoJb3JpZ2luX2VjEPj//////////wEaDmFwLW5vcnRoZWFzdC0xIkcwRQIgNJzT0ksUbCpAvwSvf1R05BNlssyxQhrG1Pug7cTmioUCIQCefRSZ1aSFnSOb/jnW+FRzYR9ibM7afuooFiD13YrM3iqjAgjC//////////8BEAAaDDA1Njg0NDk0OTg2MSIMUDmCYDCO8dqki2hjKvcB8FVHYh3yzhJyN4STYCcdeZsRhnpWpm1nvORqGa0aHt3ZMNAjgF49SY7NFCEzO1HKY1NbAoTzJQ6hf0obk+q6wWMumwP2+AwWCkMqTYwXxW5tR6yVHZAmyY3Qmy387AkPyPXg83O75GSA2SF3WLMw5beq9uIjaaIjNiSH7ME5ka6lC3c2i8PgLrqJCsooV6biw9+AhFEv5tpAf6Y85f1GWopUSbmndX5183c3xlvxLORsqsE7TaKy7mIHciwiEjEaDGAD0KgIDhbLusJGgk74AYg+OCV8Gn2NTsaL+yLfl5hEAROZLGlRuwX801HhBCevZehSUuLXrTDjuav5BTqdAQfG99SoGBclCkElJliw6FDwItYiCa/DiO2+8MS984p9/JpFrk0jpqRp53BK1+Y+bGYudVl/lzSvKT/BuXFb1q+QmSXSqn63ZpR6VKcmh+k5H2ZNb31SeZ3vs3Py/AQ52RkSkMdRQGiR2N3RESz1apQhB4QY9P6QpFP7/VWAnefj5gcg6GLcYDR9Qrvflv2SLrQ7hEWMj99KgJCJpJ0=",
        "Expiration": "2020-08-05T17:22:59+00:00"
    },
    "AssumedRoleUser": {
        "AssumedRoleId": "AROAQ2PBZDFSUEPY7UGLB:test-eks-role",
        "Arn": "arn:aws:sts::056844949861:assumed-role/k8sDev/test-eks-role"
    }
}


$ export AWS_ACCESS_KEY_ID=ASIAQ2PBZDFSUGKRDRWV
$ export AWS_SECRET_ACCESS_KEY=mMXb2op+gBP0HH99fMcukX6TrEAhoFHW+L9r2A6N
$ export AWS_SESSION_TOKEN=IQoJb3JpZ2luX2VjEPj//////////wEaDmFwLW5vcnRoZWFzdC0xIkcwRQIgNJzT0ksUbCpAvwSvf1R05BNlssyxQhrG1Pug7cTmioUCIQCefRSZ1aSFnSOb/jnW+FRzYR9ibM7afuooFiD13YrM3iqjAgjC//////////8BEAAaDDA1Njg0NDk0OTg2MSIMUDmCYDCO8dqki2hjKvcB8FVHYh3yzhJyN4STYCcdeZsRhnpWpm1nvORqGa0aHt3ZMNAjgF49SY7NFCEzO1HKY1NbAoTzJQ6hf0obk+q6wWMumwP2+AwWCkMqTYwXxW5tR6yVHZAmyY3Qmy387AkPyPXg83O75GSA2SF3WLMw5beq9uIjaaIjNiSH7ME5ka6lC3c2i8PgLrqJCsooV6biw9+AhFEv5tpAf6Y85f1GWopUSbmndX5183c3xlvxLORsqsE7TaKy7mIHciwiEjEaDGAD0KgIDhbLusJGgk74AYg+OCV8Gn2NTsaL+yLfl5hEAROZLGlRuwX801HhBCevZehSUuLXrTDjuav5BTqdAQfG99SoGBclCkElJliw6FDwItYiCa/DiO2+8MS984p9/JpFrk0jpqRp53BK1+Y+bGYudVl/lzSvKT/BuXFb1q+QmSXSqn63ZpR6VKcmh+k5H2ZNb31SeZ3vs3Py/AQ52RkSkMdRQGiR2N3RESz1apQhB4QY9P6QpFP7/VWAnefj5gcg6GLcYDR9Qrvflv2SLrQ7hEWMj99KgJCJpJ0=

# 切换角色后拥有权限进行初始化
$ clustername=eksworkshop-eksctl
$ region=ap-northeast-1
$ roleArn=arn:aws:iam::056844949861:role/k8sDev
$ aws eks update-kubeconfig  --name ${clustername}  --region ${region} --role-arn ${roleArn}
Updated context arn:aws:eks:ap-northeast-1:056844949861:cluster/eksworkshop-eksctl in /home/vagrant/.kube/config
```

更详细的内容查看[此处](/aws-iam-%E5%88%9B%E5%BB%BA%E8%A7%92%E8%89%B2#k8s%E6%9C%AC%E5%9C%B0%E5%88%9D%E5%A7%8B%E5%8C%96%E9%85%8D%E7%BD%AE---%E8%B4%A6%E5%8F%B72)。

#### 观察

通过比较集群创建者以及新创用户的`~/.kube/config`，我们可以知道他们使用的`certificate-authority-data`是一模一样，即访问集群的证书一样。

或者，通过以下类似命令：

```shell
kubectl get secrets [default-token开头的] --namespace=<命名空间> -o yaml
```

对两个用户的JWT token和证书进行比较。我们得出两者的差异在于JWT token。



### 通过证书创建用户

**TODO**