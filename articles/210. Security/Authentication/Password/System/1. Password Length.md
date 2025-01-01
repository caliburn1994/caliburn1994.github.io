## 1. 破解速度

而密码破解的速度取决于：

- 计算的速度 => 当前只考虑显卡（GPU）性能，不考虑量子计算机。若量子计算机遍及后，则需要考虑。

- 密码哈希计算的复杂度

- 密码的长度和组合。元素越多，组合很多：

  - 小写字母 lower case letters = 26
  - 大小字母 upper case letters = 26
  - 数字 digits = 10
  - 特殊符号 punctuations & special characters = 33

  关于组合如何计算，可以参考：[How many possible combinations in 8 character password? - Stack Exchange](https://math.stackexchange.com/questions/739874/how-many-possible-combinations-in-8-character-password) 

下图是 [Password tables comparing MD5 hashes cracked by an RTX 3090 GPU.](https://www.hivesystems.io/blog/are-your-passwords-in-the-green?utm_source=tabletext)

![](https://raw.githubusercontent.com/caliburn1994/caliburn1994.github.io/master/images/202301272045941.png)



## 2. 密码规范

首先密码至少要包含大写、小写英文字母、特殊符号。这是不需要讨论。

密码强度其实和账号的重要性相关。如果账号重要，破解者则会消耗更多算力去破解，而防守方则应该加强防御。有两个维度加强防御

- 加长密码长度。
- 要求管理员等重要人员定期更换密码。

考虑未来GPU的发展，以及其他因素。长度则最理想是11位以上，11位消耗300年。如果密码是10位，则要考虑半年一换。



## 3. 其他网站

### GitHub

 [Creating a strong password - GitHub Docs](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-strong-password#:~:text=Secure your account on GitHub,with any combination of characters) ：

> You must choose or generate a password for your account on GitHub.com that is at least:
>
> - Eight characters long, if it includes a number and a lowercase letter, or
> - 15 characters long with any combination of characters

