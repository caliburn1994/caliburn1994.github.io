

# 1. 预设变量

GitLab 有很多[预设变量](https://docs.gitlab.com/ee/ci/variables/predefined_variables.html)，有以下作用。

- 可减少我们的重复工作。

- 当我们想修改预设 CI 时，常常通过覆盖这些环境变量，就能达到效果。如，[release-cli](https://gitlab.com/gitlab-org/release-cli/-/tree/master/docs) 你可以看到 server-url 使用的预设变量是 CI_SERVER_URL

  ```bash
  GLOBAL OPTIONS:
     --server-url value                 The base URL of the GitLab instance, including protocol and port, for example https://gitlab.example.com:8080 [$CI_SERVER_URL]
  ```

  

- 另外，CI 里很多变量都是会变化的，我们无法手动设置的，这种只能使用预设变量。如，每次提交 MR 时，CI_COMMIT_SHA 变量都是变化的。

- 预设变量也常常用于输出到控制台，增加可读性。

下表是部分的预设变量

|                | 说明 | 例子                    |
| -------------- | ---- | ----------------------- |
| CI_SERVER_FQDN | FQDN | gitlab.example.com:8080 |
| CI_SERVER_HOST |      | gitlab.example.com      |
| CI_SERVER_URL  |      | https://gitlab.com      |



# 2. Keywords

[关键词 (keywords) ](https://docs.gitlab.com/ee/ci/yaml/)分为三类，[Global(pipeline等级)](https://docs.gitlab.com/ee/ci/yaml/#global-keywords)、[Header](https://docs.gitlab.com/ee/ci/yaml/#header-keywords)、 [Job](https://docs.gitlab.com/ee/ci/yaml/#job-keywords):

## 2.1. Global Keywords

| Global Keyword | Description                                                  |
| :------------- | :----------------------------------------------------------- |
| default        | Custom default values for job keywords. <br />job keywords 的默认值 |
| include        | Import configuration from other YAML files.<br />外部 CI 文件依赖 |
| stages         | The names and order of the pipeline stages.<br />阶段。让 jobs 按顺序执行 |
| variables      | Define CI/CD variables for all job in the pipeline.<br />变量 |
| workflow       | Control what types of pipeline run.<br />控制 pipeline 的运行行为。如: 有新的 commit 推送，旧的 Jobs 会不会停止。在项目也可有相关设置。 |

## 2.2. Header Keyword

| Header Keyword | Description                                             |
| :------------- | :------------------------------------------------------ |
| spec           | Define specifications for external configuration files. |

在 include 关键词时使用，可给依赖的 YAML 文件输入参数。

## 2.3. Job Keyword

具体的 Job 关键词

| Job Keyword         | Description                                                  |
| :------------------ | :----------------------------------------------------------- |
| after_script        | Override a set of commands that are executed after job.      |
| allow_failure       | Allow job to fail. A failed job does not cause the pipeline to fail. |
| artifacts           | List of files and directories to attach to a job on success. |
| before_script       | Override a set of commands that are executed before job.     |
| cache               | List of files that should be cached between subsequent runs. |
| coverage            | Code coverage settings for a given job.                      |
| dast_configuration  | Use configuration from DAST profiles on a job level.         |
| dependencies        | Restrict which artifacts are passed to a specific job by providing a list of jobs to fetch artifacts from.<br /> job 之间建立以来，用于传输 artifacts |
| environment         | Name of an environment to which the job deploys.<br />和部署在一起用，提供了部署的可视化界面 |
| extends             | Configuration entries that this job inherits from.<br />job 继承 job |
| identity            | Authenticate with third party services using identity federation.<br />使于登录第三方服务。17.1为止只支持Google |
| image               | Use Docker images.                                           |
| inherit             | Select which global defaults all jobs inherit.<br />当前 job 自己选择继承什么 keyword |
| interruptible       | Defines if a job can be canceled when made redundant by a newer run.<br />新的commit进来，设置为 true 的 job 会自动被取消。<br />可设置在测试相关的 job。 |
| manual_confirmation | Define a custom confirmation message for a manual job.<br />为手动操作增加了一个提示框。可用于 deploy 相关的 job。 |
| needs               | Execute jobs earlier than the stage ordering.<br />不可与 dependencies 一起用。needs 可用于同一个stage。<br />让 job 提前执行。 |
| pages               | Upload the result of a job to use with GitLab Pages.<br />可用于后端开发 API 页面 |
| parallel            | How many instances of a job should be run in parallel.<br />并发执行某 job |
| release             | Instructs the runner to generate a [release](https://docs.gitlab.com/ee/user/project/releases/index.html) object. |
| resource_group      | Limit job concurrency.<br />上锁，可用于上锁 deploy to dev 和 release 脚本。 |
| retry               | When and how many times a job can be auto-retried in case of a failure. |
| rules               | List of conditions to evaluate and determine selected attributes of a job, and whether or not it’s created.<br />定期 job 被执行与否，以及相关内容 |
| script              | Shell script that is executed by a runner.                   |
| secrets             | The CI/CD secrets the job needs.<br />可在 CI 里更好地使用 Azure Key vault 等敏感信息存储器。 (可能要premium或以上才能使用)  |
| services            | Use Docker services images.<br />设置服务。如: 为测试而创建临时数据库，临时数据库就是服务 |
| stage               | Defines a job stage.                                         |
| tags                | List of tags that are used to select a runner.               |
| timeout             | Define a custom job-level timeout that takes precedence over the project-wide setting. |
| trigger             | Defines a downstream pipeline trigger.                       |
| variables           | Define job variables on a job level.                         |
| when                | When to run job.                                             |

`include:component`: 可以让我们使用第三方的 CICD 组件。减少重复造轮子，例: 

- [dependabot-standalone](https://gitlab.com/dependabot-gitlab/dependabot-standalone)

> [!WARNING]  
> self-managed GitLab 无法使用 GitLab 官网的 CICD 组件，只能 clone 官网内容到 self-managed GitLab 后才能使用。[["]](https://docs.gitlab.com/ee/ci/components/#use-a-gitlabcom-component-in-a-self-managed-instance)

``include:template``: GitLab 内置组件。







