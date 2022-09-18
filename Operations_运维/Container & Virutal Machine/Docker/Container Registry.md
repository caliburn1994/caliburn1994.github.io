```
Last Modified: 2022-09-18
```



## 登录 Login

1. 我们可以使用以下命令去登录 private registry

   ```bash
   docker login registry.example.com:5000 \ # 仓库名
   --username my_username \
   --password my_password # or --password-stdin
   ```

2. 登录之后，Docker 会去把登录凭证存储在[^1]

   - `$HOME/.docker/config.json` on Linux 
   - `%USERPROFILE%/.docker/config.json` on Windows

3. 存储凭证的方式：

   - keychain：更安全
   - Base64：将用户名和密码通过 Base64 编码，直接存储到 config.json 文件里

   	```json
   $ cat ~/.docker/config.json
   {
       "auths": {
           "registry.example.com:5000": {
               "auth": "(Base64 content from above)" <<== `echo -n "my_username:my_password" | base64` 
           }
       }
   }



## Case1: CICD **platform** 

### C1. GitLab

通过配置一个名为 DOCKER_AUTH_CONFIG 的 [CI/CD variable](https://docs.gitlab.com/ee/ci/variables/index.html) ，就可以直接在 GitLab 里使用私有仓库了。[^2]

DOCKER_AUTH_CONFIG 里需要填写 base64编码的登录凭证，根式如下：

```json
{
    "auths": {
        "registry.example.com:5000": {
            "auth": "(Base64 content from above)"
        }
    }
}
```

配置完毕后，就可以像下面那样使用私有镜像。

```yaml
#.gitlab-ci.yml
stages:
 - stage1


stage1:
	image: registry.example.com:5000/namespace/image:tag
	...
```



### C2. GitHub Action

GitHub Action　的做法比较直观：



## 参考 References

[^1]: [Privileged user requirement - Docker](https://docs.docker.com/engine/reference/commandline/login/#privileged-user-requirement)
[^2]: [Determine your DOCKER_AUTH_CONFIG data - ＧitLab](https://docs.gitlab.com/ee/ci/docker/using_docker_images.html#determine-your-docker_auth_config-data)

[^3]: [GitHub Actions: Private registry support for job and service containers](https://github.blog/changelog/2020-09-24-github-actions-private-registry-support-for-job-and-service-containers/)

 
