**RCCE 上游完整回补与 PR 拆分（2026-11-16）**

本次将上游 `master` 的 `aaefb6ada9299c0cf4b167fe9fe89b0a69405543` 正式合并进 RCCE 历史，并逐项核对 [2026-11-15 对比表](rcce-upstream-comparison-2026-11-15.md) 的 B01–B27。目标版本 **26h2-111614**，构建号 **26111614**。以下“已跟进”表示源码与集成完成，不等于已进行所有 Apple／厂商设备或商店验收。

| 上游 PR | 范围 | 基线与兼容 | 本地验证 |
| --- | --- | --- | --- |
| [#194](https://github.com/HeZeBang/TechPie-flutter/pull/194) | A01–A03：课程／DDL 灵动通知 | 独立基于上游 master，默认关闭；无保活或周期网络刷新；最低 Android API 24 | 静态分析、772 项 Flutter 测试、Android debug 构建通过 |
| [#195](https://github.com/HeZeBang/TechPie-flutter/pull/195) | 统一穿戴列表、小米连接／快照／提醒／离线码协议 | 保留上游 Apple Watch 在线能力、签名与发行配置；Vela 页面仍明确为诊断示例 | 静态分析、762 项 Flutter 测试、Android debug 构建通过 |
| [#196](https://github.com/HeZeBang/TechPie-flutter/pull/196) | A15–A19：外屏／背屏识别、布局、设置、路由和隐私 | 保留上游 iOS 标签栈；外屏是在线付款码，离线码在完整校园卡页 | 静态分析、792 项 Flutter 测试、Android debug 构建通过 |

三个 PR 均由 @steven42121 提交，互不依赖，应用 ID、上游版本、依赖锁、签名和发布流程未修改。2026-11-16 本次检查时三个 PR 均开放、可合并，上游 Analyze CI 全部通过。

**B 类逐项验收**

| 编号 | 原缺口／旧实现 | 当前结果 | 主要代码 |
| --- | --- | --- | --- |
| B01 | Apple Watch 在线付款码 | 已跟进；所列主要实现与上游快照一致。 | [WatchStore.swift](../ios/TechPieWatch/WatchStore.swift)、[WatchOnlineCodeClient.swift](../ios/WatchSupport/Sources/WatchSupport/WatchOnlineCodeClient.swift) |
| B02 | Apple Watch 在线／离线选择与回退 | 已跟进；所列主要实现与上游快照一致。 | [WatchPaymentPreferences.swift](../ios/WatchSupport/Sources/WatchSupport/WatchPaymentPreferences.swift)、[watch_settings_page.dart](../lib/pages/watch_settings_page.dart) |
| B03 | Apple Watch 消费结果检测 | 已跟进；所列主要实现与上游快照一致。 | [WatchPaymentResult.swift](../ios/WatchSupport/Sources/WatchSupport/WatchPaymentResult.swift)、[WatchPaymentSchedule.swift](../ios/WatchSupport/Sources/WatchSupport/WatchPaymentSchedule.swift) |
| B04 | Apple Watch 独立余额刷新与节能轮询 | 已跟进；所列主要实现与上游快照一致。 | [WatchConnectivityBridge.swift](../ios/Runner/WatchConnectivityBridge.swift)、[WatchOnlineAccount.swift](../ios/WatchSupport/Sources/WatchSupport/WatchOnlineAccount.swift)、[WatchEcardCipher.swift](../ios/WatchSupport/Sources/WatchSupport/WatchEcardCipher.swift) |
| B05 | iOS 每个标签独立导航栈 | 已跟进；所列主要实现与上游快照一致。 | [mobile_shell.dart](../lib/widgets/app_shell/mobile_shell.dart)、[app_shell_tab_bar.dart](../lib/widgets/app_shell/app_shell_tab_bar.dart) |
| B06 | iOS 再点当前标签 | 已接入上游回根页／滚至顶部；额外保留外屏对付款入口的消费。 | [app_shell.dart](../lib/widgets/app_shell/app_shell.dart)、[ios_tab_navigation_test.dart](../test/ios_tab_navigation_test.dart) |
| B07 | iOS 大标题滚动联动 | 已跟进；所列主要实现与上游快照一致。 | [adaptive_navigation_scaffold.dart](../lib/widgets/adaptive_navigation_scaffold.dart)、[ios_native_navigation_bar.dart](../lib/widgets/ios/ios_native_navigation_bar.dart) |
| B08 | iOS DDL 选择工具栏与恢复反馈 | 已接入选择工具栏、恢复数量提示与选择集清理；保留 RCCE 的未绑定／无缓存空状态。 | [assignments_page.dart](../lib/pages/assignments_page.dart)、[hidden_assignments_page.dart](../lib/pages/hidden_assignments_page.dart) |
| B09 | iOS 原生弹层、选项与分段控件 | 已跟进；所列主要实现与上游快照一致。 | [adaptive_select.dart](../lib/widgets/adaptive_select.dart)、[adaptive_switch.dart](../lib/widgets/adaptive_switch.dart)、[adaptive_segmented_control.dart](../lib/widgets/adaptive_segmented_control.dart)、[adaptive_multi_select.dart](../lib/widgets/adaptive_multi_select.dart) |
| B10 | iOS 日期／学期／节次选择细节 | 已跟进；所列主要实现与上游快照一致。 | [adaptive_date_picker.dart](../lib/widgets/adaptive_date_picker.dart)、[schedule_picker_sheet.dart](../lib/widgets/schedule_picker_sheet.dart)、[custom_course_editor_page.dart](../lib/pages/custom_course_editor_page.dart)、[custom_course_period_picker.dart](../lib/pages/custom_course_period_picker.dart) |
| B11 | iOS 关联账号详情 | 已跟进；所列主要实现与上游快照一致。 | [third_party_accounts_page.dart](../lib/pages/third_party_accounts_page.dart) |
| B12 | iOS 课程详情和校园网页外观 | 已跟进；所列主要实现与上游快照一致。 | [course_detail_panel.dart](../lib/widgets/course_detail_panel.dart)、[elrc_player_page.dart](../lib/pages/elrc_player_page.dart)、[debug_webview_page.dart](../lib/pages/debug_webview_page.dart)、[generic_webview_page.dart](../lib/pages/generic_webview_page.dart)、[webview_host.dart](../lib/pages/webview_host.dart) |
| B13 | iOS 全屏扫码／登录／播放器路由 | iOS 使用根导航器；Linux 原生代理登录分支保留。 | [scan_page.dart](../lib/widgets/scanner/scan_page.dart)、[uni_auth_service.dart](../lib/services/uni_auth_service.dart)、[elrc_recordings_page.dart](../lib/pages/elrc_recordings_page.dart) |
| B14 | 校园卡底部安全区与固定操作栏 | 已跟进；所列主要实现与上游快照一致。 | [shell.dart](../lib/features/campus_card/presentation/app/shell.dart)、[apple_wallet_components.dart](../lib/features/campus_card/presentation/widgets/apple_wallet_components.dart) |
| B15 | iOS 无障碍与本地化细节 | 已跟进；所列主要实现与上游快照一致。 | [campus_card_account_page.dart](../lib/pages/campus_card_account_page.dart)、[sf_symbol_glyphs.dart](../lib/widgets/ios/sf_symbol_glyphs.dart)、[ios_native_select.dart](../lib/widgets/ios/ios_native_select.dart) |
| B16 | iOS 触觉反馈不打断音乐 | 已跟进；所列主要实现与上游快照一致。 | [EcardFeedback.swift](../ios/Runner/EcardFeedback.swift)、[haptics.dart](../lib/utils/haptics.dart) |
| B17 | iOS 模拟器校园卡绑定提示 | 已跟进；所列主要实现与上游快照一致。 | [EcardBindTunnelPlugin.swift](../ios/Runner/EcardBindTunnelPlugin.swift) |
| B18 | 新版扫码插件及 iPhone 17 适配 | 扫码固定为上游 7.0.1 对应提交 76f6f44；pubspec 与上游差别仅为 RCCE 版本号。 | [pubspec.yaml](../pubspec.yaml)、[pubspec.lock](../pubspec.lock)、[scanner_viewport.dart](../lib/widgets/scanner/scanner_viewport.dart) |
| B19 | Apple 隐私清单打包 | 已跟进；所列主要实现与上游快照一致。 | [Podfile](../ios/Podfile)、[PrivacyInfo.xcprivacy](../ios/PrivacyManifests/package_info_plus/PrivacyInfo.xcprivacy) |
| B20 | Xcode 27 lipo 兼容脚本 | 已跟进；所列主要实现与上游快照一致。 | [lipo](../scripts/apple-tools/lipo)、[Runner.xcscheme](../ios/Runner.xcodeproj/xcshareddata/xcschemes/Runner.xcscheme)、[Runner.xcscheme](../macos/Runner.xcodeproj/xcshareddata/xcschemes/Runner.xcscheme) |
| B21 | macOS 构建基线／插件集成 | 已跟进；所列主要实现与上游快照一致。 | [project.pbxproj](../macos/Runner.xcodeproj/project.pbxproj)、[Podfile](../macos/Podfile) |
| B22 | Android 旧系统支持范围 | 恢复 flutter.minSdkVersion（构建结果 API 24）；HyperOS SDK 调用加 API 27 守卫，通知 channel、消费通知和前台服务启动加 API 26 守卫，私有 DNS 诊断加 API 28 守卫。 | [build.gradle.kts](../android/app/build.gradle.kts) |
| B23 | AppGallery 自动提审 | 工作流和脚本已跟进；本次发布不自动执行商店提审，仍需 AppGallery 凭据／签名配置。 | [ohos-appgallery.yml](../.github/workflows/ohos-appgallery.yml)、[agc-publish.mjs](../scripts/agc-publish.mjs)、[agc-status.mjs](../scripts/agc-status.mjs)、[agc-test-version.mjs](../scripts/agc-test-version.mjs) |
| B24 | 上游发布版本线防退保护 | 上游 --check／--require-version 和版本线防退检查已跟进，并扩展识别 RCCE 日期标签；Auto CI 拒绝旧日期版本回退，允许重建当前版本。 | [prepare-release.yml](../.github/workflows/prepare-release.yml)、[release-plan.mjs](../scripts/release-plan.mjs) |
| B25 | 误提交环境／签名文件的 CI 阻止规则 | 旧受跟踪环境备份已移除；加入 CI 阻止规则，唯一明确例外为 RCCE 已有的公开测试 keystore。未清写 Git 历史。 | [analyze.yml](../.github/workflows/analyze.yml)、[.gitignore](../.gitignore) |
| B26 | 校园卡个人资料请求精简 | 已移除 profile／头像重复请求、未接通资料页和旧仓库；保留消费通知 port、付款异常兜底及手环二维码组件。 | [app_providers.dart](../lib/features/campus_card/app/app_providers.dart)、[ecard_card_repository.dart](../lib/features/campus_card/data/repositories/ecard_card_repository.dart) |
| B27 | 付款入口从组件启动后的导航策略 | iOS 组件入口归位 Home 标签并打开付款；Android 外屏继续转到可见付款视图。 | [app_shell.dart](../lib/widgets/app_shell/app_shell.dart)、[campus_card_page.dart](../lib/pages/campus_card_page.dart) |

**保留的 RCCE 扩展与校验**

保留后台刷新、保活、消费通知、小米／华为伴侣、外屏、Android 玻璃主题、窄屏课表、小爱分享入口、Linux GNOME／KDE 截止日期组件、应用代理、反馈工单、RCCE 更新渠道与日期版本。新版 iOS 导航、原生控件与这些扩展共存。原生检查还修复了后台 Worker 创建／销毁 Flutter 引擎和通道时的线程归属，以及旧 Android API 守卫、可选相机硬件声明和日文通知资源。没有把尚未合入 master 的研究生课表、AI、课程直播等 P 类提案当成已发布功能。

本地验证：静态分析通过；866 项 Flutter 测试通过；35 项发布规则／穿戴／反馈 Node 测试通过；Android debug APK 和 Linux release 构建成功；Android 原生 lint 通过（无错误，仍有已有资源／依赖建议警告）。测试在依赖原仓库的固定提交源码上执行，下载连接中断时采用同一提交的源码归档；本地路径覆盖不进入 Git。Apple / macOS 侧源码和构建配置已跟进上游，但本机为 Linux，未执行 Xcode 签名或 Apple Watch 实机测试。AppGallery 提审及厂商穿戴授权仍需对应服务配置。

**对旧报告的更正**

A17 的外屏付款视图实际只使用在线码；离线付款保留在完整校园卡页面。旧报告中“外屏在线／离线码”的表述已更正。旧表 B 类描述记录的是 2026-11-15、26h2-111519 的状态，不能再用于判断本次版本是否缺少这些能力。
