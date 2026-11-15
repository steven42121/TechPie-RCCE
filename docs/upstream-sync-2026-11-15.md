# RCCE 上游功能同步（2026-11-15）

对齐上游 `HeZeBang/TechPie-flutter` 的 `master`：
`aaefb6ada9299c0cf4b167fe9fe89b0a69405543`（2026-11-13，PR #189）。
本次为功能移植，保留 RCCE 的版本号、发行工作流和独立扩展。

## 本次补入

- IDS 软件 Passkey 注册、验证码/密码复核、本地登录、续期与解绑；设置及关联账号页提供旧账号迁移入口。
- EAMS、Blackboard/eLearning、eGate 签到、校园网页与 OA 场馆预约接入本地 IDS 会话。旧 `cpdaily` / `egate` 记录继续可读，使用校园功能前需要重新绑定 IDS。
- Passkey 纳入现有主密码加密同步，处理旧备份迁移、并发绑定与解绑、同步删除记录；续期不会被误判为账号切换。
- 首次使用及每次登录的隐私同意入口；RCCE 外屏在同意前也显示隐私页面，同意后直接恢复外屏界面。
- 密码输入持续遮蔽；软键盘出现时不再额外保留底栏空白。
- HarmonyOS 平台实现作为直接依赖注册，固定原有解析版本；下载遇到镜像哈希不一致时改写锁文件中的源地址并使用 pub.dev 重试，同时检查依赖版本未变。

## RCCE 兼容处理

- 复用现有 iOS `techpie/platform` 通道，增加 User-Agent 所需设备信息，保留 `iosMajorVersion` 与原生导航组件。
- 手表账号同步改用 IDS 学号，旧会话 UUID 和会话轮换不会影响手表身份。
- 保留 Linux 小组件开关、快照大小限制，以及启用但无缓存时发布空快照的行为；考试副标题继续显示地点。
- 保留工作区已有 Apple 小组件接入；调整相关测试使其覆盖隐私入口及 RCCE 的空快照语义。
- 已有的反馈表单、永久账号注销、课表排版及回到本周修复继续保留。

未引入上游的商店专属 AppGallery 发布流程及版本号变更。上游 iOS 首帧延迟修复针对另一套底栏实现，RCCE 的底栏不延迟首帧，因此保留现有实现。

## 校验

- `flutter analyze --no-pub`：无问题。
- `flutter test --no-pub --reporter expanded`：842 个测试通过。
- `flutter build linux --release --no-pub`：成功生成 `build/linux/x64/release/bundle/techpie`。
- HarmonyOS 依赖解析脚本通过 Bash 语法检查，四个插件已出现在根依赖图；原有锁定依赖版本保持不变。

IDS / OA 协议由模拟服务覆盖；真实校园账号绑定、Apple 签名与设备行为仍需对应环境验证。
