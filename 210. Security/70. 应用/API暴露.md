# API暴露

## 问题

API不存在或者验证失败，状态码应该一致吗？

## 思考

API的状态码应当是描述性的，使得开发者和客户端能够明确地了解请求的结果。当API不存在和验证失败时，虽然这两种情况都是错误状态，但它们的含义和解决方式是不同的，所以它们应当有不同的状态码。我们来具体分析：

1. **API不存在**:
   - **HTTP 状态码**: 404 Not Found
   - **解释**: 这表示客户端尝试访问的URL不存在。
   - **原因**: 客户端可能输入了错误的路径或API已被移除。
2. **验证失败**:
   - **HTTP 状态码**: 401 Unauthorized 或 403 Forbidden，具体取决于情况。
   - 解释:
     - **401 Unauthorized**: 客户端尚未进行身份验证，或者提供的验证信息是无效的。客户端应重新进行身份验证。
     - **403 Forbidden**: 客户端的身份验证是有效的，但它没有权限访问所请求的资源。
   - **原因**: 客户端可能提供了无效的认证令牌、证书或API密钥。

### 那么，为什么需要区分这两种状态码呢？

1. **清晰性**: 不同的状态码为开发者提供了清晰的指示，使他们能够迅速地确定问题所在，并进行适当的修复。

2. **安全性**: 从安全的角度来看，有些开发者选择将“不存在的API”和“无权限”的响应设置为相同的状态码（如都返回403 Forbidden），以避免给潜在的攻击者提供过多信息。但这通常是在特定的上下文和更严格的安全要求中使用的策略。

   如果没有公开 API 文档的情况下，API 可能需要隐藏。但如果已经公开了，也没必要统一使用 403 或 401 进行遮掩。

3. **用户体验**: 不同的错误状态可以使前端应用更准确地为用户提供有用的错误消息和解决建议。

4. **调试**: 当开发或维护API时，具有描述性的状态码可以更快地帮助开发者定位和解决问题。

综上所述，通常建议为不同的错误情况使用不同的HTTP状态码，以便提供清晰、有描述性的响应。然而，在某些特定的安全上下文中，为了不向潜在的攻击者泄露过多信息，可能会选择使用相同的状态码。