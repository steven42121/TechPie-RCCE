# RCCE 上游同步记录（2026-11-26）

## 基线和实施边界

- RCCE 当前发布：26h2-112321，commit 38b303e10a49073cb5fdc126cecc71b1b0f9a3e4。
- 本轮重新 fetch 的 origin/master：2f40fea487d55b902276631d739e6295719a7b51。
- 上次同步采用的上游基点 5ba9c44 至本轮上游共 40 个 first-parent 合入节点。按实际实现比对，已存在的 #200、#194、#197 等不重复带入。
- 在独立 codex/rcce-sync-20261126 工作区实施三方合并，并逐项解决 RCCE 扩展冲突；合并父节点保留上游来源，便于下轮按祖先关系去重。
- 主目录 macos/Flutter/GeneratedPluginRegistrant.swift 是已有的插件生成文件差异；docs/qoder-conversations/ 是用户导入的原始历史记录。两者均按既有本地工作完整保留，未覆盖、暂存、清理或纳入本次提交；9 份原始记录的 SHA256SUMS 全部通过。
- 上游开发工作区 codex/upstream-dev 保持原样；共享 refs 已重新 fetch。本次只推 RCCE。

## 逐项来源与取舍

| Commit | PR | 上游标题 | RCCE 处理 |
|---|---|---|---|
| 8075bdb | #201 | Merge pull request #201 from hysonger/feat/student-allowance-query | 移植：校内补助查询，保留 IDS 本地会话边界。 |
| 8408bf8 | #200 | Merge pull request #200 from zzxt-0112/支持自定义Deadline,并接入Apple提醒事项 | 已在 26h2-112321 移植；仅整合后续提醒事项修复，不重复实现。 |
| 7716f7e | #157 | Merge pull request #157 from reisa531/codex/pr135-module-build | 构建支持按差异整合；保留 RCCE CameraX 16KB 对齐、测试密钥及日期版本。 |
| d0a5560 | #197 | Merge pull request #197 from steven42121/codex/spending-alerts | 功能已存在；跟进共享去重账本与账号代次保护，保留 RCCE 默认开关。 |
| 8ea0b11 | #156 | Merge pull request #156 from reisa531/codex/pr135-module-proxy | 移植代理归一化、环境代理回退和 Linux 登录窗口生命周期修复。 |
| 25d94da | #194 | Merge pull request #194 from steven42121/codex/android-island | 灵动通知已存在；整合新增截止事项详情，保留 RCCE 原生 API 守卫。 |
| aaef6bd | #155 | Merge pull request #155 from reisa531/codex/pr135-module-ddl | 桌面组件已存在；移植 GNOME 层叠修复和自定义事项地点回退，保留 subtitle 格式及大小限制。 |
| f913f8d | #121 | Merge pull request #121 from Maotechh/codex/campus-card-merchant-aliases | 移植商户别名、交易显示和贡献反馈。 |
| ba185ad | #203 | Merge pull request #203 from Maotechh/feat/design-system-foundation | 移植设计 token 和统一基础组件；保留 RCCE 玻璃 ThemeExtension。 |
| 3d60a18 | #204 | Merge pull request #204 from Maotechh/feat/design-system-home | 移植首页卡片及自适应布局；继续共用 coursesOnDay。 |
| 7e0285f | #205 | Merge pull request #205 from Maotechh/feat/design-system-desktop-bento | 移植桌面 Bento 侧栏与独立页栈。 |
| d631bb6 | #206 | feat(pages): 统一课表、Deadlines 与我的页设计语言并新增日程视图 (PR 4) (#206) | 移植主页面、课表日程模式；保留窄屏横向联动与末节裁剪。 |
| 4365367 | #207 | feat(pages): 统一 8 个二级表单与设置子页设计语言并纳入护栏 (PR 5) (#207) | 移植二级设置／编辑表单样式。 |
| 9afe3fa | #208 | feat(accounts): 重构第三方账号与平台绑定中心并纳入护栏 (PR 6) (#208) | 移植第三方绑定中心；保留 IDS 迁移行为。 |
| 50da1af | #209 | feat(elrc): 重构课程录播页设计语言、课程搜索与课次列表并纳入护栏 (PR 7) (#209) | 移植录播课程搜索与课次列表。 |
| 88120a2 | #210 | feat(gym): 重构并模块化场馆预约页，开启全仓库设计系统默认护栏 (PR 8) (#210) | 移植体育预约模块和设计护栏；RCCE 页面也遵循护栏。 |
| fd78e87 | #152 | Merge pull request #152 from reisa531/codex/pr135-watch-feedback | 已正式合入，移植课表、离线授权、重连和 Lite 伴侣；保留统一穿戴列表及小米桥接。 |
| 7d2d4e5 | #213 | Offer the TechPie user group after feedback succeeds or fails (#213) | 移植反馈完成或失败后的用户群入口。 |
| f596123 | #212 | feat(linux): package AppImage, Flatpak and AUR releases (#212) | 移植 Linux AppImage／Flatpak 和 AUR 工具；RCCE Auto CI 增加两种产物。AUR 仍只接受上游 semver 稳定标签，不向 AUR 发布日期标签。 |
| 6dd7bb3 | #214 | Keep the off switch thumb visible across Material themes (#214) | 移植暗色／浅色主题开关可见性修复。 |
| f5f46de | #211 | fix(design-system): TechRed 全组件配色、交互优化、官网风 Deadlines 与品牌标志 (#211) | 移植 TechRed 配色、Deadlines 布局和品牌标志。 |
| 1407a5c | #217 | feat(motion): Telegram 级弹簧物理、跨阈值触感与 TgGlass 锚点浮层 (#217) | 移植弹簧动效、触觉和锚点弹层；保留 RCCE 四种玻璃配方。 |
| 5a4a602 | #218 | feat(ui): 实体校园卡、侧边栏角标、课程详情升级与 Huawei Watch 设计系统迁移 (#218) | 移植校园卡、课程详情、侧栏徽标和华为手表页面设计。 |
| be7fda1 | #219 | feat(ux): 精简账号信息、校园卡小眼睛、首页 3×2 快捷功能与全应用偏好记忆 (#219) | 移植余额隐藏、快捷入口与界面偏好持久化；保留 RCCE 个人信息遮蔽。 |
| e06fa47 | #220 | chore: bump version to 1.1.0+32 (#220) | 不采用上游 1.1.0+32；使用 RCCE 日期版本 26.2.112601+26112601。 |
| c512a9a | #224 | Explain watch peer lookup failures and document debug pairing (#224) | 移植配对失败解释与调试配对文档。 |
| 04a6f0f | #221 | fix(schedule): fetch school calendar locally via IDS to eliminate 401 (#221) | 移植 IDS 本地校历，避免 backend term_begin 的 401。 |
| 28e0ce8 | #225 | feat(schedule): OKLCH 12-color course palette, stable grouping, and unified accents (#225) | 移植 OKLCH 12 色、稳定分组及统一课程色彩。 |
| b971108 | #226 | chore(release): add 1.1.0 changelog notes (#226) | 上游 1.1.0 说明不充当 RCCE 说明；新增独立 RCCE 版本节。 |
| 247b322 | #227 | 修复主题模式遮罩问题;主题色按钮修改为主题模式相同样式 (#227) | 移植主题遮罩与配色选择样式修复。 |
| a72e681 | #229 | fix(auth): distinguish invalid_grant from transient SSO renewal errors (#229) | 移植 refresh-token 拒绝与临时错误分类，保留会话恢复。 |
| 5bd9fed | #250 | fix(home): make whole Campus Card surface open Payment Code (#250) | 移植整张校园卡进入付款码的语义。 |
| bda9da8 | #251 | fix(settings): overhaul settings hierarchy, desktop-only proxy, and default TechRed theme (#251) | 移植设置层级、桌面代理和默认 TechRed；保留 RCCE 专有入口。 |
| 5917290 | #216 | Create roadmap for project development phases (#216) | 移植 ROADMAP，仅文档。 |
| 06aa2f2 | 直接提交 | fix(ci): OHOS 镜像自带 tool stamp 与 gitcode 镜像，release 不再联网解析 | 移植 OHOS tool stamp、gitcode 离线镜像及镜像摘要配方；保留 RCCE 发布入口。 |
| 286ba1e | #252 | feat(home): add a searchable campus service hub and editable shortcuts (#252) | 移植可搜索服务中心与可编辑快捷入口。 |
| 558e545 | #257 | feat(schedule): support short course titles in the week grid (#257) | 移植课程短标题，按 IDS 账号隔离。 |
| 2e497ac | #256 | fix(diagnostics): keep credentials and personal data out of logs (#256) | 移植凭据、个人资料与原始响应日志脱敏。 |
| ba2ea4b | #273 | 修复切换主题色时的按钮闪烁 (#273) | 移植主题色按钮切换闪烁修复。 |
| 2f40fea | #274 | fix(iOS): 修复 App Store 更新与提醒事项同步Fix/apple updates and reminders (#274) | 移植提醒事项重新打开时的双向同步；App Store 更新入口不适用于 RCCE，继续独立 GitHub 渠道。 |

## RCCE 兼容检查

- 保留 BandBridge/BandReminders、小米 Vela 通信、ScreenFormFactorBridge、GlanceGate、KeepAliveService、后台岛屿刷新、统一穿戴列表以及小爱课表 ICS/文本分享。
- 保留 classic/frosted/liquid/fullLiquid 四模式，向新设计主题同时附加 TpColors 和 GlassTheme；桌面使用新侧栏，窄屏周课表头与网格一起滚动。
- 保留个人信息默认遮蔽、日期版本比较、RCCE 更新端点、Android API 24 下限与厂商 SDK 守卫、CameraX 16KB 对齐及稳定测试签名。
- 保留 Linux/Apple 小组件显式共享开关、subtitle 字段、256KB/512 条上限，新增自定义事项地点回退，不导出备注；保留 RCCE 桌面紧急色和自适应宽度。
- 消费提醒保留既有默认值及显式开关，迁移到原生共享账本；首次快照只建立基线，校验隐私同意、账号归属和请求代次，限制单批通知数量。
- 上游 App Store 应用 ID 属于上游分发，RCCE 不切换到该更新源；iOS 仍使用 RCCE GitHub Release。Apple 提醒事项修复已移植。
- 不自动上传应用商店或 AUR。AppImage/Flatpak 是额外 Linux 发布产物，原 tar.gz、Windows zip 与 Android 通用 APK 保留。

## 发布与本地验证

- pubspec 唯一版本：26.2.112601+26112601；RCCE tag：26h2-112601。
- Flutter 静态分析：无问题。
- Flutter 全量回归：1,178 项通过，包含 RCCE 外屏、手环、日期版本及新增上游测试。
- Node 发布规则／穿戴／反馈：70 项通过；GNOME 桌面层叠：5 项通过。
- actionlint 1.7.7：工作流语法通过（本机没有 shellcheck，脚本 lint 由对应 CI 完成）。
- Android 通用 Release APK：构建通过；aapt 校验 versionName=26h2-112601、versionCode=26112601、minSdk=24；apksigner v2/v3 验签通过。
- Android 原生 SpendingLedger 单元测试：9 项通过。
- 最终后台入口修正：消费读取回调在隐私／身份校验之后执行；相关 17 项测试和静态分析通过，Android/Linux Release 均重新构建通过。
- Linux AppImage 本地打包通过；打包检查及 Flatpak 打包检查在 GitHub Actions 通过。
- Linux 主程序 Release：构建通过，已包含更新后的 WebKit 登录窗口。
- 本机没有 Qt6 开发包，KDE 组件编译由发布 CI 验证；本机没有 Xcode/Windows 工具链，Apple 编译和真机光学扫码、睡眠唤醒不声称已验证。
- 发布 Auto CI 增加发布前 analyze/test/Node 门禁以及原生账本单元测试；Linux 同步生成 AppImage/Flatpak。远端完成结果以对应发布和 Actions 为准。
