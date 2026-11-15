**RCCE／上游逐文件核对目录（2026-11-15）**

对应 [完整功能对比表](rcce-upstream-comparison-2026-11-15.md)。这里覆盖功能快照 `8d1b926d02be495cc9a384e8c20540ae647023b1` 相对上游 `aaefb6a` 的全部 **422 个差异条目**。A／B／C／D／P 编号可在完整表中查询行为、PR 作者、状态和限制。

`A` 表示 RCCE 相对上游新增文件，`D` 表示只有上游有，`M` 为不同，`R100` 为内容完全一致的移动。文件新增不等于功能新增；删除也可能是替代实现。计数方向始终是“上游 → RCCE”。仅版本号、此次更新日志和比较报告的后续写入不属于这份应用功能快照。

环境备份不读取内容；二进制按路径／用途核对；锁文件按依赖解析处理；测试、文档、资源单列用途。清单保证差异路径全部有归属，不声称每个硬件分支已通过实机测试。

| 状态 | 文件 | 文本变化 | 文件性质 | 对应功能表及来源索引 |
| --- | --- | --- | --- | --- |
| A | `.envrc.release-backup-20260919-042730` | +19 / −0 | 旧环境备份；只记路径存在，不读取内容 | B25 误提交环境／签名文件的 CI 阻止规则 |
| A | [.gitattributes](../.gitattributes) | +3 / −0 | 文档／仓库规则 | A39 Windows 资源路径与架构测试 |
| M | [.github/workflows/analyze.yml](../.github/workflows/analyze.yml) | +8 / −21 | 构建／发布／开发工具 | A40 穿戴与反馈额外回归检查；B25 误提交环境／签名文件的 CI 阻止规则 |
| M | [.github/workflows/android-release.yml](../.github/workflows/android-release.yml) | +110 / −0 | 构建／发布／开发工具 | A38 Android 相机依赖与 16KB 检查 |
| A | [.github/workflows/auto-ci.yml](../.github/workflows/auto-ci.yml) | +517 / −0 | 构建／发布／开发工具 | A37 RCCE 自动构建发布 |
| M | [.github/workflows/linux-release.yml](../.github/workflows/linux-release.yml) | +12 / −1 | 构建／发布／开发工具 | A31 Linux 小组件安装与唤起；A37 RCCE 自动构建发布 |
| D | [.github/workflows/ohos-appgallery.yml](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/.github/workflows/ohos-appgallery.yml) | +0 / −203 | 构建／发布／开发工具 | B23 AppGallery 自动提审 |
| M | [.github/workflows/prepare-release.yml](../.github/workflows/prepare-release.yml) | +0 / −15 | 构建／发布／开发工具 | B24 上游发布版本线防退保护 |
| M | [.github/workflows/release.yml](../.github/workflows/release.yml) | +10 / −34 | 构建／发布／开发工具 | A40 穿戴与反馈额外回归检查；B23 AppGallery 自动提审；B24 上游发布版本线防退保护 |
| M | [.github/workflows/windows-release.yml](../.github/workflows/windows-release.yml) | +1 / −1 | 构建／发布／开发工具 | A37 RCCE 自动构建发布；A39 Windows 资源路径与架构测试 |
| M | [.gitignore](../.gitignore) | +24 / −5 | 文档／仓库规则 | B25 误提交环境／签名文件的 CI 阻止规则 |
| M | [CHANGELOG.md](../CHANGELOG.md) | +0 / −75 | 文档／仓库规则 | C01 IDS 软件 Passkey 绑定／登录／续期／解绑；C05 应用内反馈与图片；C06 永久账号注销；C07 DDL 滚动、回本周与文字溢出修复 |
| M | [CLAUDE.md](../CLAUDE.md) | +14 / −74 | 文档／仓库规则 | C01 IDS 软件 Passkey 绑定／登录／续期／解绑；C02 IDS 下游校园会话；C03 Passkey 加密同步与旧账号迁移 |
| M | [README.md](../README.md) | +4 / −9 | 文档／仓库规则 | A12 华为手表连接和账号同步；A29 GNOME 截止日期组件；A36 独立更新渠道 |
| M | [android/app/build.gradle.kts](../android/app/build.gradle.kts) | +47 / −2 | 实现代码／原生桥／构建集成 | A38 Android 相机依赖与 16KB 检查；B22 Android 旧系统支持范围 |
| A | [android/app/libs/xms-wearable-lib_1.4_release.aar](../android/app/libs/xms-wearable-lib_1.4_release.aar) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A08 小米手环连接桥 |
| M | [android/app/src/main/AndroidManifest.xml](../android/app/src/main/AndroidManifest.xml) | +41 / −0 | 资源／声明／配置；随关联功能核对 | A01 Android 课程／DDL 灵动通知；A04 后台刷新；A05 可选前台保活；A06 校园卡消费通知；A08 小米手环连接桥；A12 华为手表连接和账号同步 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/BandBridge.kt](../android/app/src/main/kotlin/club/geekpie/techpie/BandBridge.kt) | +501 / −0 | 实现代码／原生桥／构建集成 | A08 小米手环连接桥 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/BandReminders.kt](../android/app/src/main/kotlin/club/geekpie/techpie/BandReminders.kt) | +509 / −0 | 实现代码／原生桥／构建集成 | A10 小米手环课程提醒 |
| M | [android/app/src/main/kotlin/club/geekpie/techpie/MainActivity.kt](../android/app/src/main/kotlin/club/geekpie/techpie/MainActivity.kt) | +131 / −2 | 实现代码／原生桥／构建集成 | A01 Android 课程／DDL 灵动通知；A04 后台刷新；A05 可选前台保活；A08 小米手环连接桥；A12 华为手表连接和账号同步；A15 折叠外屏／背屏识别 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/ScreenFormFactorBridge.kt](../android/app/src/main/kotlin/club/geekpie/techpie/ScreenFormFactorBridge.kt) | +91 / −0 | 实现代码／原生桥／构建集成 | A15 折叠外屏／背屏识别 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/background/BackgroundScheduler.kt](../android/app/src/main/kotlin/club/geekpie/techpie/background/BackgroundScheduler.kt) | +33 / −0 | 实现代码／原生桥／构建集成 | A04 后台刷新 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/background/RefreshWorker.kt](../android/app/src/main/kotlin/club/geekpie/techpie/background/RefreshWorker.kt) | +71 / −0 | 实现代码／原生桥／构建集成 | A04 后台刷新 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/island/FocusSequenceStore.kt](../android/app/src/main/kotlin/club/geekpie/techpie/island/FocusSequenceStore.kt) | +41 / −0 | 实现代码／原生桥／构建集成 | A03 灵动通知时间切换与恢复 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/island/IslandAlarms.kt](../android/app/src/main/kotlin/club/geekpie/techpie/island/IslandAlarms.kt) | +275 / −0 | 实现代码／原生桥／构建集成 | A03 灵动通知时间切换与恢复 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/island/IslandBackend.kt](../android/app/src/main/kotlin/club/geekpie/techpie/island/IslandBackend.kt) | +21 / −0 | 实现代码／原生桥／构建集成 | A02 HyperOS／Android 16 通知适配 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/island/IslandBridge.kt](../android/app/src/main/kotlin/club/geekpie/techpie/island/IslandBridge.kt) | +121 / −0 | 实现代码／原生桥／构建集成 | A01 Android 课程／DDL 灵动通知；A02 HyperOS／Android 16 通知适配；A03 灵动通知时间切换与恢复 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/island/IslandCapabilityProbe.kt](../android/app/src/main/kotlin/club/geekpie/techpie/island/IslandCapabilityProbe.kt) | +87 / −0 | 实现代码／原生桥／构建集成 | A01 Android 课程／DDL 灵动通知；A02 HyperOS／Android 16 通知适配；A03 灵动通知时间切换与恢复 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/island/IslandFrame.kt](../android/app/src/main/kotlin/club/geekpie/techpie/island/IslandFrame.kt) | +53 / −0 | 实现代码／原生桥／构建集成 | A01 Android 课程／DDL 灵动通知；A02 HyperOS／Android 16 通知适配；A03 灵动通知时间切换与恢复 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/island/IslandNotificationFactory.kt](../android/app/src/main/kotlin/club/geekpie/techpie/island/IslandNotificationFactory.kt) | +215 / −0 | 实现代码／原生桥／构建集成 | A01 Android 课程／DDL 灵动通知；A02 HyperOS／Android 16 通知适配；A03 灵动通知时间切换与恢复 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/keepalive/KeepAliveService.kt](../android/app/src/main/kotlin/club/geekpie/techpie/keepalive/KeepAliveService.kt) | +137 / −0 | 实现代码／原生桥／构建集成 | A05 可选前台保活 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/notification/SpendingNotificationBridge.kt](../android/app/src/main/kotlin/club/geekpie/techpie/notification/SpendingNotificationBridge.kt) | +134 / −0 | 实现代码／原生桥／构建集成 | A06 校园卡消费通知 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/wear/WearEngineBridge.kt](../android/app/src/main/kotlin/club/geekpie/techpie/wear/WearEngineBridge.kt) | +218 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步 |
| A | [android/app/src/main/kotlin/club/geekpie/techpie/wear/WearReminders.kt](../android/app/src/main/kotlin/club/geekpie/techpie/wear/WearReminders.kt) | +155 / −0 | 实现代码／原生桥／构建集成 | A14 华为手表提醒旧方案 |
| A | [android/app/src/main/res/drawable/ic_island_notification.xml](../android/app/src/main/res/drawable/ic_island_notification.xml) | +12 / −0 | 资源／声明／配置；随关联功能核对 | A01 Android 课程／DDL 灵动通知；A05 可选前台保活；A06 校园卡消费通知 |
| A | [android/app/src/main/res/drawable/ic_spending_notification.xml](../android/app/src/main/res/drawable/ic_spending_notification.xml) | +9 / −0 | 资源／声明／配置；随关联功能核对 | A01 Android 课程／DDL 灵动通知；A05 可选前台保活；A06 校园卡消费通知 |
| A | [android/app/src/main/res/values-zh/alert_strings.xml](../android/app/src/main/res/values-zh/alert_strings.xml) | +5 / −0 | 资源／声明／配置；随关联功能核对 | A01 Android 课程／DDL 灵动通知；A05 可选前台保活；A06 校园卡消费通知 |
| A | [android/app/src/main/res/values-zh/island_strings.xml](../android/app/src/main/res/values-zh/island_strings.xml) | +5 / −0 | 资源／声明／配置；随关联功能核对 | A01 Android 课程／DDL 灵动通知；A05 可选前台保活；A06 校园卡消费通知 |
| A | [android/app/src/main/res/values-zh/keepalive_strings.xml](../android/app/src/main/res/values-zh/keepalive_strings.xml) | +7 / −0 | 资源／声明／配置；随关联功能核对 | A01 Android 课程／DDL 灵动通知；A05 可选前台保活；A06 校园卡消费通知 |
| A | [android/app/src/main/res/values/alert_strings.xml](../android/app/src/main/res/values/alert_strings.xml) | +5 / −0 | 资源／声明／配置；随关联功能核对 | A01 Android 课程／DDL 灵动通知；A05 可选前台保活；A06 校园卡消费通知 |
| A | [android/app/src/main/res/values/island_strings.xml](../android/app/src/main/res/values/island_strings.xml) | +5 / −0 | 资源／声明／配置；随关联功能核对 | A01 Android 课程／DDL 灵动通知；A05 可选前台保活；A06 校园卡消费通知 |
| A | [android/app/src/main/res/values/keepalive_strings.xml](../android/app/src/main/res/values/keepalive_strings.xml) | +7 / −0 | 资源／声明／配置；随关联功能核对 | A01 Android 课程／DDL 灵动通知；A05 可选前台保活；A06 校园卡消费通知 |
| M | [android/build.gradle.kts](../android/build.gradle.kts) | +24 / −0 | 实现代码／原生桥／构建集成 | A38 Android 相机依赖与 16KB 检查 |
| M | [assets/campus_card/images/card-full.svg](../assets/campus_card/images/card-full.svg) | +1 / −0 | 资源／声明／配置；随关联功能核对 | C14 手机在线／离线校园码、账单、安全设置；D06 旧端口、主题和扫描封装 |
| M | [assets/campus_card/images/card-top.svg](../assets/campus_card/images/card-top.svg) | +1 / −0 | 资源／声明／配置；随关联功能核对 | C14 手机在线／离线校园码、账单、安全设置；D06 旧端口、主题和扫描封装 |
| A | [assets/icon/appgallery-216.png](../assets/icon/appgallery-216.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | B23 AppGallery 自动提审 |
| A | [ci/techpie-test-keystore.p12](../ci/techpie-test-keystore.p12) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A37 RCCE 自动构建发布 |
| D | [docs/deadline-widgets.md](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/docs/deadline-widgets.md) | +0 / −100 | 文档／仓库规则 | C08 iOS／macOS 截止日期小组件；A32 小组件数据共享选择 |
| A | [docs/upstream-sync-2026-11-15.md](../docs/upstream-sync-2026-11-15.md) | +33 / −0 | 文档／仓库规则 | C08 iOS／macOS 截止日期小组件；A32 小组件数据共享选择 |
| A | [harmonyos_watch/.gitignore](../harmonyos_watch/.gitignore) | +9 / −0 | 文档／仓库规则 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/BUILDING.md](../harmonyos_watch/BUILDING.md) | +34 / −0 | 文档／仓库规则 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/build-profile.template.json5](../harmonyos_watch/build-profile.template.json5) | +26 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/build.ps1](../harmonyos_watch/build.ps1) | +49 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/build-profile.json5](../harmonyos_watch/entry/build-profile.json5) | +5 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/hvigorfile.ts](../harmonyos_watch/entry/hvigorfile.ts) | +39 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/oh-package.json5](../harmonyos_watch/entry/oh-package.json5) | +6 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/config.json](../harmonyos_watch/entry/src/main/config.json) | +26 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/js/MainAbility/app.js](../harmonyos_watch/entry/src/main/js/MainAbility/app.js) | +4 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/js/MainAbility/common/LICENSE.wearengine](../harmonyos_watch/entry/src/main/js/MainAbility/common/LICENSE.wearengine) | +202 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/js/MainAbility/common/wear-config.js](../harmonyos_watch/entry/src/main/js/MainAbility/common/wear-config.js) | +6 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/js/MainAbility/common/wear-connection.js](../harmonyos_watch/entry/src/main/js/MainAbility/common/wear-connection.js) | +303 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/js/MainAbility/common/wear-link.js](../harmonyos_watch/entry/src/main/js/MainAbility/common/wear-link.js) | +78 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/js/MainAbility/common/wearengine.js](../harmonyos_watch/entry/src/main/js/MainAbility/common/wearengine.js) | +565 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/js/MainAbility/pages/index/index.css](../harmonyos_watch/entry/src/main/js/MainAbility/pages/index/index.css) | +22 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/js/MainAbility/pages/index/index.hml](../harmonyos_watch/entry/src/main/js/MainAbility/pages/index/index.hml) | +75 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/js/MainAbility/pages/index/index.js](../harmonyos_watch/entry/src/main/js/MainAbility/pages/index/index.js) | +127 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码 |
| A | [harmonyos_watch/entry/src/main/resources/base/element/string.json](../harmonyos_watch/entry/src/main/resources/base/element/string.json) | +6 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/resources/base/media/icon.png](../harmonyos_watch/entry/src/main/resources/base/media/icon.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/entry/src/main/resources/base/media/icon_small.png](../harmonyos_watch/entry/src/main/resources/base/media/icon_small.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/hvigor/hvigor-config.json5](../harmonyos_watch/hvigor/hvigor-config.json5) | +4 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/hvigorfile.ts](../harmonyos_watch/hvigorfile.ts) | +6 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/oh-package.json5](../harmonyos_watch/oh-package.json5) | +7 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/.gitignore](../harmonyos_watch/ohos/.gitignore) | +21 / −0 | 文档／仓库规则 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/AppScope/app.template.json5](../harmonyos_watch/ohos/AppScope/app.template.json5) | +20 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/AppScope/resources/base/element/string.json](../harmonyos_watch/ohos/AppScope/resources/base/element/string.json) | +8 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/AppScope/resources/base/media/background.png](../harmonyos_watch/ohos/AppScope/resources/base/media/background.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/AppScope/resources/base/media/foreground.png](../harmonyos_watch/ohos/AppScope/resources/base/media/foreground.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/AppScope/resources/base/media/layered_image.json](../harmonyos_watch/ohos/AppScope/resources/base/media/layered_image.json) | +7 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/build-profile.template.json5](../harmonyos_watch/ohos/build-profile.template.json5) | +56 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/.gitignore](../harmonyos_watch/ohos/entry/.gitignore) | +7 / −0 | 文档／仓库规则 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/hvigorfile.ts](../harmonyos_watch/ohos/entry/hvigorfile.ts) | +7 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/libs/arm64-v8a/libecardbind_reader.so](../harmonyos_watch/ohos/entry/libs/arm64-v8a/libecardbind_reader.so) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/oh-package.json5](../harmonyos_watch/ohos/entry/oh-package.json5) | +14 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/cpp/CMakeLists.txt](../harmonyos_watch/ohos/entry/src/main/cpp/CMakeLists.txt) | +45 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/cpp/ecardbind_reader.cpp](../harmonyos_watch/ohos/entry/src/main/cpp/ecardbind_reader.cpp) | +246 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/cpp/types/libecardbind_reader/index.d.ts](../harmonyos_watch/ohos/entry/src/main/cpp/types/libecardbind_reader/index.d.ts) | +14 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/cpp/types/libecardbind_reader/oh-package.json5](../harmonyos_watch/ohos/entry/src/main/cpp/types/libecardbind_reader/oh-package.json5) | +6 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/campuscard/CampusCardPlatformPlugin.ets](../harmonyos_watch/ohos/entry/src/main/ets/campuscard/CampusCardPlatformPlugin.ets) | +67 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindDns.ets](../harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindDns.ets) | +121 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindPackets.ets](../harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindPackets.ets) | +143 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindPlugin.ets](../harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindPlugin.ets) | +230 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindState.ets](../harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindState.ets) | +129 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindVpnAbility.ets](../harmonyos_watch/ohos/entry/src/main/ets/ecardbind/EcardBindVpnAbility.ets) | +471 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/ecardshortcut/EcardShortcutPlugin.ets](../harmonyos_watch/ohos/entry/src/main/ets/ecardshortcut/EcardShortcutPlugin.ets) | +75 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/entryability/EntryAbility.ets](../harmonyos_watch/ohos/entry/src/main/ets/entryability/EntryAbility.ets) | +35 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/pages/Index.ets](../harmonyos_watch/ohos/entry/src/main/ets/pages/Index.ets) | +24 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/ets/platform/SystemFeedbackPlugin.ets](../harmonyos_watch/ohos/entry/src/main/ets/platform/SystemFeedbackPlugin.ets) | +735 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/module.json5](../harmonyos_watch/ohos/entry/src/main/module.json5) | +70 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/base/element/color.json](../harmonyos_watch/ohos/entry/src/main/resources/base/element/color.json) | +8 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/base/element/string.json](../harmonyos_watch/ohos/entry/src/main/resources/base/element/string.json) | +24 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/base/media/ic_ecard_shortcut.png](../harmonyos_watch/ohos/entry/src/main/resources/base/media/ic_ecard_shortcut.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/base/media/icon.png](../harmonyos_watch/ohos/entry/src/main/resources/base/media/icon.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/base/profile/main_pages.json](../harmonyos_watch/ohos/entry/src/main/resources/base/profile/main_pages.json) | +5 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/base/profile/shortcuts_config.json](../harmonyos_watch/ohos/entry/src/main/resources/base/profile/shortcuts_config.json) | +19 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/en_US/element/string.json](../harmonyos_watch/ohos/entry/src/main/resources/en_US/element/string.json) | +24 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/rawfile/buildinfo.json5](../harmonyos_watch/ohos/entry/src/main/resources/rawfile/buildinfo.json5) | +8 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/rawfile/framesconfig.json](../harmonyos_watch/ohos/entry/src/main/resources/rawfile/framesconfig.json) | +37 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/main/resources/zh_CN/element/string.json](../harmonyos_watch/ohos/entry/src/main/resources/zh_CN/element/string.json) | +24 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/ets/test/Ability.test.ets](../harmonyos_watch/ohos/entry/src/ohosTest/ets/test/Ability.test.ets) | +35 / −0 | 回归测试／夹具；不是独立功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/ets/test/List.test.ets](../harmonyos_watch/ohos/entry/src/ohosTest/ets/test/List.test.ets) | +5 / −0 | 回归测试／夹具；不是独立功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/ets/testability/TestAbility.ets](../harmonyos_watch/ohos/entry/src/ohosTest/ets/testability/TestAbility.ets) | +48 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/ets/testability/pages/Index.ets](../harmonyos_watch/ohos/entry/src/ohosTest/ets/testability/pages/Index.ets) | +36 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/ets/testrunner/OpenHarmonyTestRunner.ts](../harmonyos_watch/ohos/entry/src/ohosTest/ets/testrunner/OpenHarmonyTestRunner.ts) | +50 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/module.json5](../harmonyos_watch/ohos/entry/src/ohosTest/module.json5) | +37 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/resources/base/element/color.json](../harmonyos_watch/ohos/entry/src/ohosTest/resources/base/element/color.json) | +8 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/resources/base/element/string.json](../harmonyos_watch/ohos/entry/src/ohosTest/resources/base/element/string.json) | +16 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/resources/base/media/icon.png](../harmonyos_watch/ohos/entry/src/ohosTest/resources/base/media/icon.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/entry/src/ohosTest/resources/base/profile/test_pages.json](../harmonyos_watch/ohos/entry/src/ohosTest/resources/base/profile/test_pages.json) | +5 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/hvigor/hvigor-config.json5](../harmonyos_watch/ohos/hvigor/hvigor-config.json5) | +6 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/hvigorconfig.ts](../harmonyos_watch/ohos/hvigorconfig.ts) | +4 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/hvigorfile.ts](../harmonyos_watch/ohos/hvigorfile.ts) | +17 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/oh-package.json5](../harmonyos_watch/ohos/oh-package.json5) | +15 / −0 | 资源／声明／配置；随关联功能核对 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/scripts/build-ecardbind-reader.sh](../harmonyos_watch/ohos/scripts/build-ecardbind-reader.sh) | +55 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/ohos/scripts/generate-build-profile.mjs](../harmonyos_watch/ohos/scripts/generate-build-profile.mjs) | +142 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/scripts/lite-profile.cjs](../harmonyos_watch/scripts/lite-profile.cjs) | +69 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/scripts/lite-profile.test.cjs](../harmonyos_watch/scripts/lite-profile.test.cjs) | +59 / −0 | 回归测试／夹具；不是独立功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/scripts/minify-lite.cjs](../harmonyos_watch/scripts/minify-lite.cjs) | +30 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/scripts/signing-config.test.cjs](../harmonyos_watch/scripts/signing-config.test.cjs) | +48 / −0 | 回归测试／夹具；不是独立功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/scripts/validate-signing-config.cjs](../harmonyos_watch/scripts/validate-signing-config.cjs) | +57 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [harmonyos_watch/scripts/wear-connection.test.mjs](../harmonyos_watch/scripts/wear-connection.test.mjs) | +200 / −0 | 回归测试／夹具；不是独立功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| M | [ios/Podfile](../ios/Podfile) | +0 / −5 | 实现代码／原生桥／构建集成 | B19 Apple 隐私清单打包 |
| M | [ios/Podfile.lock](../ios/Podfile.lock) | +87 / −35 | 依赖锁／解析结果；不是独立功能 | B18 新版扫码插件及 iPhone 17 适配；B19 Apple 隐私清单打包 |
| D | [ios/PrivacyManifests/package_info_plus/PrivacyInfo.xcprivacy](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/PrivacyManifests/package_info_plus/PrivacyInfo.xcprivacy) | +0 / −14 | 实现代码／原生桥／构建集成 | B19 Apple 隐私清单打包 |
| M | [ios/Runner.xcodeproj/project.pbxproj](../ios/Runner.xcodeproj/project.pbxproj) | +45 / −7 | 实现代码／原生桥／构建集成 | B05 iOS 每个标签独立导航栈；B18 新版扫码插件及 iPhone 17 适配；B20 Xcode 27 lipo 兼容脚本；C08 iOS／macOS 截止日期小组件 |
| M | [ios/Runner.xcodeproj/xcshareddata/xcschemes/Runner.xcscheme](../ios/Runner.xcodeproj/xcshareddata/xcschemes/Runner.xcscheme) | +1 / −1 | 实现代码／原生桥／构建集成 | B20 Xcode 27 lipo 兼容脚本 |
| M | [ios/Runner.xcodeproj/xcshareddata/xcschemes/TechPieWatch.xcscheme](../ios/Runner.xcodeproj/xcshareddata/xcschemes/TechPieWatch.xcscheme) | +2 / −0 | 实现代码／原生桥／构建集成 | B05 iOS 每个标签独立导航栈；B18 新版扫码插件及 iPhone 17 适配；B20 Xcode 27 lipo 兼容脚本；C08 iOS／macOS 截止日期小组件 |
| M | [ios/Runner/AppDelegate.swift](../ios/Runner/AppDelegate.swift) | +3 / −21 | 实现代码／原生桥／构建集成 | B04 Apple Watch 独立余额刷新与节能轮询；C01 IDS 软件 Passkey 绑定／登录／续期／解绑；C08 iOS／macOS 截止日期小组件 |
| M | [ios/Runner/EcardBindTunnelPlugin.swift](../ios/Runner/EcardBindTunnelPlugin.swift) | +0 / −9 | 实现代码／原生桥／构建集成 | B17 iOS 模拟器校园卡绑定提示 |
| M | [ios/Runner/EcardFeedback.swift](../ios/Runner/EcardFeedback.swift) | +14 / −55 | 实现代码／原生桥／构建集成 | B16 iOS 触觉反馈不打断音乐 |
| M | [ios/Runner/Info.plist](../ios/Runner/Info.plist) | +16 / −16 | 资源／声明／配置；随关联功能核对 | B15 iOS 无障碍与本地化细节；C08 iOS／macOS 截止日期小组件 |
| R100 | [ios/Runner/NativeGlass/IcsFilePresenterPlugin.swift](../ios/Runner/NativeGlass/IcsFilePresenterPlugin.swift)（上游原路径 `ios/Runner/Platform/IcsFilePresenterPlugin.swift`） | +0 / −0 | 完全相同内容的路径移动 | B05 iOS 每个标签独立导航栈；B09 iOS 原生弹层、选项与分段控件 |
| A | [ios/Runner/NativeGlass/NativeGlassPresenterPlugin.swift](../ios/Runner/NativeGlass/NativeGlassPresenterPlugin.swift) | +351 / −0 | 实现代码／原生桥／构建集成 | B05 iOS 每个标签独立导航栈；B09 iOS 原生弹层、选项与分段控件 |
| A | [ios/Runner/NativeGlass/NativeGlassRegistry.swift](../ios/Runner/NativeGlass/NativeGlassRegistry.swift) | +45 / −0 | 实现代码／原生桥／构建集成 | B05 iOS 每个标签独立导航栈；B09 iOS 原生弹层、选项与分段控件 |
| A | [ios/Runner/NativeGlass/NativeGlassTabBarView.swift](../ios/Runner/NativeGlass/NativeGlassTabBarView.swift) | +251 / −0 | 实现代码／原生桥／构建集成 | B05 iOS 每个标签独立导航栈；B09 iOS 原生弹层、选项与分段控件 |
| A | [ios/Runner/NativeGlass/NativeNavigationBar.swift](../ios/Runner/NativeGlass/NativeNavigationBar.swift) | +349 / −0 | 实现代码／原生桥／构建集成 | B05 iOS 每个标签独立导航栈；B09 iOS 原生弹层、选项与分段控件 |
| A | [ios/Runner/NativeGlass/NativeSelect.swift](../ios/Runner/NativeGlass/NativeSelect.swift) | +122 / −0 | 实现代码／原生桥／构建集成 | B05 iOS 每个标签独立导航栈；B09 iOS 原生弹层、选项与分段控件 |
| M | [ios/Runner/Runner.entitlements](../ios/Runner/Runner.entitlements) | +4 / −2 | 资源／声明／配置；随关联功能核对 | C08 iOS／macOS 截止日期小组件 |
| M | [ios/Runner/WatchConnectivityBridge.swift](../ios/Runner/WatchConnectivityBridge.swift) | +2 / −7 | 实现代码／原生桥／构建集成 | B04 Apple Watch 独立余额刷新与节能轮询 |
| M | [ios/TechPieWatch/Info.plist](../ios/TechPieWatch/Info.plist) | +1 / −1 | 资源／声明／配置；随关联功能核对 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| M | [ios/TechPieWatch/README.md](../ios/TechPieWatch/README.md) | +10 / −12 | 文档／仓库规则 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| M | [ios/TechPieWatch/TechPieWatchApp.swift](../ios/TechPieWatch/TechPieWatchApp.swift) | +43 / −194 | 实现代码／原生桥／构建集成 | C15 Apple Watch 离线付款码 |
| M | [ios/TechPieWatch/WatchStore.swift](../ios/TechPieWatch/WatchStore.swift) | +15 / −334 | 实现代码／原生桥／构建集成 | B01 Apple Watch 在线付款码 |
| M | [ios/WatchSupport/Sources/CWatchCrypto/WatchCrypto.c](../ios/WatchSupport/Sources/CWatchCrypto/WatchCrypto.c) | +0 / −11 | 实现代码／原生桥／构建集成 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| M | [ios/WatchSupport/Sources/CWatchCrypto/include/WatchCrypto.h](../ios/WatchSupport/Sources/CWatchCrypto/include/WatchCrypto.h) | +0 / −3 | 实现代码／原生桥／构建集成 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| M | [ios/WatchSupport/Sources/WatchSupport/WatchCodeDisplayState.swift](../ios/WatchSupport/Sources/WatchSupport/WatchCodeDisplayState.swift) | +9 / −50 | 实现代码／原生桥／构建集成 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| D | [ios/WatchSupport/Sources/WatchSupport/WatchEcardCipher.swift](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/WatchSupport/Sources/WatchSupport/WatchEcardCipher.swift) | +0 / −63 | 实现代码／原生桥／构建集成 | B04 Apple Watch 独立余额刷新与节能轮询 |
| D | [ios/WatchSupport/Sources/WatchSupport/WatchOnlineAccount.swift](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/WatchSupport/Sources/WatchSupport/WatchOnlineAccount.swift) | +0 / −58 | 实现代码／原生桥／构建集成 | B04 Apple Watch 独立余额刷新与节能轮询 |
| D | [ios/WatchSupport/Sources/WatchSupport/WatchOnlineCodeClient.swift](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/WatchSupport/Sources/WatchSupport/WatchOnlineCodeClient.swift) | +0 / −331 | 实现代码／原生桥／构建集成 | B01 Apple Watch 在线付款码 |
| D | [ios/WatchSupport/Sources/WatchSupport/WatchPaymentPreferences.swift](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/WatchSupport/Sources/WatchSupport/WatchPaymentPreferences.swift) | +0 / −20 | 实现代码／原生桥／构建集成 | B02 Apple Watch 在线／离线选择与回退 |
| D | [ios/WatchSupport/Sources/WatchSupport/WatchPaymentResult.swift](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/WatchSupport/Sources/WatchSupport/WatchPaymentResult.swift) | +0 / −59 | 实现代码／原生桥／构建集成 | B03 Apple Watch 消费结果检测 |
| D | [ios/WatchSupport/Sources/WatchSupport/WatchPaymentSchedule.swift](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/WatchSupport/Sources/WatchSupport/WatchPaymentSchedule.swift) | +0 / −34 | 实现代码／原生桥／构建集成 | B03 Apple Watch 消费结果检测 |
| M | [ios/WatchSupport/Sources/WatchSupport/WatchSnapshot.swift](../ios/WatchSupport/Sources/WatchSupport/WatchSnapshot.swift) | +1 / −7 | 实现代码／原生桥／构建集成 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| M | [ios/WatchSupport/Tests/WatchSupportTests/WatchCodeDisplayStateTests.swift](../ios/WatchSupport/Tests/WatchSupportTests/WatchCodeDisplayStateTests.swift) | +0 / −92 | 回归测试／夹具；不是独立功能 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| D | [ios/WatchSupport/Tests/WatchSupportTests/WatchOnlineCodeTests.swift](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/WatchSupport/Tests/WatchSupportTests/WatchOnlineCodeTests.swift) | +0 / −548 | 回归测试／夹具；不是独立功能 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| D | [ios/WatchSupport/Tests/WatchSupportTests/WatchPaymentPreferencesTests.swift](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/WatchSupport/Tests/WatchSupportTests/WatchPaymentPreferencesTests.swift) | +0 / −38 | 回归测试／夹具；不是独立功能 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| D | [ios/WatchSupport/Tests/WatchSupportTests/WatchPaymentTests.swift](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/ios/WatchSupport/Tests/WatchSupportTests/WatchPaymentTests.swift) | +0 / −115 | 回归测试／夹具；不是独立功能 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| A | [lib/background_refresh.dart](../lib/background_refresh.dart) | +86 / −0 | 实现代码／原生桥／构建集成 | A04 后台刷新 |
| M | [lib/features/campus_card/app/app_providers.dart](../lib/features/campus_card/app/app_providers.dart) | +68 / −45 | 实现代码／原生桥／构建集成 | B26 校园卡个人资料请求精简 |
| M | [lib/features/campus_card/application/payment_code_controller.dart](../lib/features/campus_card/application/payment_code_controller.dart) | +13 / −0 | 实现代码／原生桥／构建集成 | D07 支付异常兜底 |
| M | [lib/features/campus_card/core/config/app_environment.dart](../lib/features/campus_card/core/config/app_environment.dart) | +44 / −0 | 实现代码／原生桥／构建集成 | D05 充值接口和初始化脚手架 |
| M | [lib/features/campus_card/core/config/payment_code_preferences.dart](../lib/features/campus_card/core/config/payment_code_preferences.dart) | +10 / −0 | 实现代码／原生桥／构建集成 | A17 外屏付款与实时状态 |
| M | [lib/features/campus_card/data/api/decrypted_http_trace.dart](../lib/features/campus_card/data/api/decrypted_http_trace.dart) | +2 / −2 | 实现代码／原生桥／构建集成 | B26 校园卡个人资料请求精简；D03 旧校园卡个人资料页 |
| M | [lib/features/campus_card/data/api/ecard_api_client.dart](../lib/features/campus_card/data/api/ecard_api_client.dart) | +34 / −3 | 实现代码／原生桥／构建集成 | B26 校园卡个人资料请求精简；D03 旧校园卡个人资料页 |
| M | [lib/features/campus_card/data/auth/auth_ports.dart](../lib/features/campus_card/data/auth/auth_ports.dart) | +37 / −0 | 实现代码／原生桥／构建集成 | D06 旧端口、主题和扫描封装 |
| M | [lib/features/campus_card/data/crypto/sm2_offline_crypto.dart](../lib/features/campus_card/data/crypto/sm2_offline_crypto.dart) | +8 / −0 | 实现代码／原生桥／构建集成 | A11 小米手环离线码／凭证协议 |
| M | [lib/features/campus_card/data/mock/demo_repositories.dart](../lib/features/campus_card/data/mock/demo_repositories.dart) | +138 / −0 | 实现代码／原生桥／构建集成 | D04 月度账单统计仓库；D05 充值接口和初始化脚手架 |
| M | [lib/features/campus_card/data/mock/demo_scenarios.dart](../lib/features/campus_card/data/mock/demo_scenarios.dart) | +1 / −0 | 实现代码／原生桥／构建集成 | D04 月度账单统计仓库；D05 充值接口和初始化脚手架 |
| A | [lib/features/campus_card/data/repositories/disabled_ports.dart](../lib/features/campus_card/data/repositories/disabled_ports.dart) | +117 / −0 | 实现代码／原生桥／构建集成 | D06 旧端口、主题和扫描封装 |
| A | [lib/features/campus_card/data/repositories/ecard_bill_summary_repository.dart](../lib/features/campus_card/data/repositories/ecard_bill_summary_repository.dart) | +146 / −0 | 实现代码／原生桥／构建集成 | D04 月度账单统计仓库 |
| M | [lib/features/campus_card/data/repositories/ecard_card_repository.dart](../lib/features/campus_card/data/repositories/ecard_card_repository.dart) | +60 / −0 | 实现代码／原生桥／构建集成 | B26 校园卡个人资料请求精简 |
| A | [lib/features/campus_card/data/repositories/ecard_recharge_repository.dart](../lib/features/campus_card/data/repositories/ecard_recharge_repository.dart) | +102 / −0 | 实现代码／原生桥／构建集成 | D05 充值接口和初始化脚手架 |
| M | [lib/features/campus_card/domain/models/bill_models.dart](../lib/features/campus_card/domain/models/bill_models.dart) | +64 / −0 | 实现代码／原生桥／构建集成 | D04 月度账单统计仓库 |
| A | [lib/features/campus_card/domain/models/profile_models.dart](../lib/features/campus_card/domain/models/profile_models.dart) | +13 / −0 | 实现代码／原生桥／构建集成 | B26 校园卡个人资料请求精简；D04 月度账单统计仓库；D05 充值接口和初始化脚手架 |
| M | [lib/features/campus_card/domain/models/security_models.dart](../lib/features/campus_card/domain/models/security_models.dart) | +58 / −0 | 实现代码／原生桥／构建集成 | B26 校园卡个人资料请求精简；D04 月度账单统计仓库；D05 充值接口和初始化脚手架 |
| M | [lib/features/campus_card/domain/ports/bill_ports.dart](../lib/features/campus_card/domain/ports/bill_ports.dart) | +12 / −0 | 实现代码／原生桥／构建集成 | B26 校园卡个人资料请求精简；D04 月度账单统计仓库；D05 充值接口和初始化脚手架 |
| M | [lib/features/campus_card/domain/ports/card_ports.dart](../lib/features/campus_card/domain/ports/card_ports.dart) | +2 / −0 | 实现代码／原生桥／构建集成 | B26 校园卡个人资料请求精简；D04 月度账单统计仓库；D05 充值接口和初始化脚手架 |
| M | [lib/features/campus_card/domain/ports/security_ports.dart](../lib/features/campus_card/domain/ports/security_ports.dart) | +14 / −0 | 实现代码／原生桥／构建集成 | B26 校园卡个人资料请求精简；D04 月度账单统计仓库；D05 充值接口和初始化脚手架 |
| M | [lib/features/campus_card/presentation/app/app.dart](../lib/features/campus_card/presentation/app/app.dart) | +0 / −3 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；D03 旧校园卡个人资料页 |
| M | [lib/features/campus_card/presentation/app/shell.dart](../lib/features/campus_card/presentation/app/shell.dart) | +2 / −17 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏 |
| A | [lib/features/campus_card/presentation/scanner/scanner_geometry.dart](../lib/features/campus_card/presentation/scanner/scanner_geometry.dart) | +2 / −0 | 实现代码／原生桥／构建集成 | D06 旧端口、主题和扫描封装 |
| A | [lib/features/campus_card/presentation/scanner/scanner_viewport.dart](../lib/features/campus_card/presentation/scanner/scanner_viewport.dart) | +2 / −0 | 实现代码／原生桥／构建集成 | B18 新版扫码插件及 iPhone 17 适配；D06 旧端口、主题和扫描封装 |
| M | [lib/features/campus_card/presentation/screens/bill_transaction_screen.dart](../lib/features/campus_card/presentation/screens/bill_transaction_screen.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/bind_card_screen.dart](../lib/features/campus_card/presentation/screens/bind_card_screen.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/card_manage_screen.dart](../lib/features/campus_card/presentation/screens/card_manage_screen.dart) | +10 / −7 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/login_screen.dart](../lib/features/campus_card/presentation/screens/login_screen.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| A | [lib/features/campus_card/presentation/screens/me_screen.dart](../lib/features/campus_card/presentation/screens/me_screen.dart) | +125 / −0 | 实现代码／原生桥／构建集成 | D03 旧校园卡个人资料页 |
| M | [lib/features/campus_card/presentation/screens/offline_screen.dart](../lib/features/campus_card/presentation/screens/offline_screen.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/payment_code_page.dart](../lib/features/campus_card/presentation/screens/payment_code_page.dart) | +65 / −236 | 实现代码／原生桥／构建集成 | C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/security_hub_screen.dart](../lib/features/campus_card/presentation/screens/security_hub_screen.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/security_limit_screen.dart](../lib/features/campus_card/presentation/screens/security_limit_screen.dart) | +4 / −5 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/security_password_screen.dart](../lib/features/campus_card/presentation/screens/security_password_screen.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/session_restore_screen.dart](../lib/features/campus_card/presentation/screens/session_restore_screen.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/settings_screen.dart](../lib/features/campus_card/presentation/screens/settings_screen.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [lib/features/campus_card/presentation/screens/widget_setup_screen.dart](../lib/features/campus_card/presentation/screens/widget_setup_screen.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| A | [lib/features/campus_card/presentation/theme/glass.dart](../lib/features/campus_card/presentation/theme/glass.dart) | +112 / −0 | 实现代码／原生桥／构建集成 | D06 旧端口、主题和扫描封装 |
| M | [lib/features/campus_card/presentation/theme/tokens.dart](../lib/features/campus_card/presentation/theme/tokens.dart) | +19 / −0 | 实现代码／原生桥／构建集成 | D06 旧端口、主题和扫描封装 |
| M | [lib/features/campus_card/presentation/widgets/apple_wallet_components.dart](../lib/features/campus_card/presentation/widgets/apple_wallet_components.dart) | +3 / −23 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏 |
| M | [lib/features/campus_card/presentation/widgets/gp_state.dart](../lib/features/campus_card/presentation/widgets/gp_state.dart) | +27 / −0 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；D06 旧端口、主题和扫描封装；D07 支付异常兜底 |
| A | [lib/features/campus_card/presentation/widgets/payment_code_qr.dart](../lib/features/campus_card/presentation/widgets/payment_code_qr.dart) | +174 / −0 | 实现代码／原生桥／构建集成 | B14 校园卡底部安全区与固定操作栏；D06 旧端口、主题和扫描封装；D07 支付异常兜底 |
| A | [lib/features/glance/application/glance_snapshot_builder.dart](../lib/features/glance/application/glance_snapshot_builder.dart) | +76 / −0 | 实现代码／原生桥／构建集成 | A16 外屏课程和截止日期概览 |
| A | [lib/features/glance/domain/glance_snapshot.dart](../lib/features/glance/domain/glance_snapshot.dart) | +77 / −0 | 实现代码／原生桥／构建集成 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| A | [lib/features/glance/presentation/cover_glance_layout.dart](../lib/features/glance/presentation/cover_glance_layout.dart) | +732 / −0 | 实现代码／原生桥／构建集成 | A16 外屏课程和截止日期概览 |
| A | [lib/features/glance/presentation/glance_formatters.dart](../lib/features/glance/presentation/glance_formatters.dart) | +41 / −0 | 实现代码／原生桥／构建集成 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| A | [lib/features/glance/presentation/glance_payment_card.dart](../lib/features/glance/presentation/glance_payment_card.dart) | +215 / −0 | 实现代码／原生桥／构建集成 | A17 外屏付款与实时状态 |
| A | [lib/features/glance/presentation/glance_payment_view.dart](../lib/features/glance/presentation/glance_payment_view.dart) | +296 / −0 | 实现代码／原生桥／构建集成 | A17 外屏付款与实时状态 |
| A | [lib/features/glance/presentation/glance_shell.dart](../lib/features/glance/presentation/glance_shell.dart) | +464 / −0 | 实现代码／原生桥／构建集成 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| A | [lib/features/glance/presentation/glance_widgets.dart](../lib/features/glance/presentation/glance_widgets.dart) | +164 / −0 | 实现代码／原生桥／构建集成 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| A | [lib/features/glance/presentation/live_glance_shell.dart](../lib/features/glance/presentation/live_glance_shell.dart) | +134 / −0 | 实现代码／原生桥／构建集成 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| A | [lib/features/glance/presentation/rear_glance_layout.dart](../lib/features/glance/presentation/rear_glance_layout.dart) | +269 / −0 | 实现代码／原生桥／构建集成 | A16 外屏课程和截止日期概览 |
| M | [lib/main.dart](../lib/main.dart) | +182 / −33 | 实现代码／原生桥／构建集成 | A21 RCCE 标识 |
| A | [lib/models/island_frame.dart](../lib/models/island_frame.dart) | +156 / −0 | 实现代码／原生桥／构建集成 | A01 Android 课程／DDL 灵动通知 |
| A | [lib/models/screen_form_factor.dart](../lib/models/screen_form_factor.dart) | +115 / −0 | 实现代码／原生桥／构建集成 | A15 折叠外屏／背屏识别 |
| A | [lib/models/wearable_device.dart](../lib/models/wearable_device.dart) | +60 / −0 | 实现代码／原生桥／构建集成 | A07 统一穿戴设备管理 |
| M | [lib/pages/assignments_page.dart](../lib/pages/assignments_page.dart) | +25 / −5 | 实现代码／原生桥／构建集成 | A25 DDL 空状态和本地覆盖保存顺序；B08 iOS DDL 选择工具栏与恢复反馈；C07 DDL 滚动、回本周与文字溢出修复 |
| A | [lib/pages/band_settings_page.dart](../lib/pages/band_settings_page.dart) | +362 / −0 | 实现代码／原生桥／构建集成 | A08 小米手环连接桥 |
| M | [lib/pages/campus_card_account_page.dart](../lib/pages/campus_card_account_page.dart) | +0 / −1 | 实现代码／原生桥／构建集成 | B15 iOS 无障碍与本地化细节 |
| M | [lib/pages/campus_card_page.dart](../lib/pages/campus_card_page.dart) | +2 / −0 | 实现代码／原生桥／构建集成 | B27 付款入口从组件启动后的导航策略 |
| M | [lib/pages/custom_course_editor_page.dart](../lib/pages/custom_course_editor_page.dart) | +2 / −6 | 实现代码／原生桥／构建集成 | B10 iOS 日期／学期／节次选择细节 |
| M | [lib/pages/custom_course_period_picker.dart](../lib/pages/custom_course_period_picker.dart) | +0 / −3 | 实现代码／原生桥／构建集成 | B10 iOS 日期／学期／节次选择细节 |
| M | [lib/pages/debug_webview_page.dart](../lib/pages/debug_webview_page.dart) | +4 / −16 | 实现代码／原生桥／构建集成 | B12 iOS 课程详情和校园网页外观 |
| M | [lib/pages/elrc_player_page.dart](../lib/pages/elrc_player_page.dart) | +4 / −18 | 实现代码／原生桥／构建集成 | B12 iOS 课程详情和校园网页外观；C12 移动端课程录播 |
| M | [lib/pages/elrc_recordings_page.dart](../lib/pages/elrc_recordings_page.dart) | +20 / −60 | 实现代码／原生桥／构建集成 | B13 iOS 全屏扫码／登录／播放器路由；C12 移动端课程录播 |
| M | [lib/pages/generic_webview_page.dart](../lib/pages/generic_webview_page.dart) | +6 / −10 | 实现代码／原生桥／构建集成 | B12 iOS 课程详情和校园网页外观 |
| A | [lib/pages/glance_preview_page.dart](../lib/pages/glance_preview_page.dart) | +144 / −0 | 实现代码／原生桥／构建集成 | A18 外屏内容设置和预览 |
| A | [lib/pages/glance_settings_page.dart](../lib/pages/glance_settings_page.dart) | +250 / −0 | 实现代码／原生桥／构建集成 | A18 外屏内容设置和预览 |
| M | [lib/pages/hidden_assignments_page.dart](../lib/pages/hidden_assignments_page.dart) | +39 / −253 | 实现代码／原生桥／构建集成 | B08 iOS DDL 选择工具栏与恢复反馈 |
| M | [lib/pages/home_page.dart](../lib/pages/home_page.dart) | +8 / −42 | 实现代码／原生桥／构建集成 | A16 外屏课程和截止日期概览；A20 Android 四种玻璃主题；A26 跨组件课程时间线；B07 iOS 大标题滚动联动 |
| A | [lib/pages/huawei_watch_page.dart](../lib/pages/huawei_watch_page.dart) | +482 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| M | [lib/pages/oa_gym_page.dart](../lib/pages/oa_gym_page.dart) | +13 / −9 | 实现代码／原生桥／构建集成 | C13 OA 场馆预约和个人信息模糊 |
| M | [lib/pages/schedule_page.dart](../lib/pages/schedule_page.dart) | +318 / −137 | 实现代码／原生桥／构建集成 | A22 窄屏课表可读性；A23 课表紧凑布局；A24 小爱课程表分享入口；C07 DDL 滚动、回本周与文字溢出修复；C10 普通课表、自定义课程／考试、ICS 导出 |
| M | [lib/pages/settings_page.dart](../lib/pages/settings_page.dart) | +289 / −30 | 实现代码／原生桥／构建集成 | A27 应用代理；A32 小组件数据共享选择；A34 RCCE 工单入口 |
| M | [lib/pages/third_party_accounts_page.dart](../lib/pages/third_party_accounts_page.dart) | +1 / −142 | 实现代码／原生桥／构建集成 | B11 iOS 关联账号详情 |
| M | [lib/pages/uni_auth_webview_page.dart](../lib/pages/uni_auth_webview_page.dart) | +6 / −18 | 实现代码／原生桥／构建集成 | B12 iOS 课程详情和校园网页外观；A28 Linux 原生登录窗口 |
| M | [lib/pages/watch_settings_page.dart](../lib/pages/watch_settings_page.dart) | +3 / −16 | 实现代码／原生桥／构建集成 | B02 Apple Watch 在线／离线选择与回退 |
| A | [lib/pages/wearables_page.dart](../lib/pages/wearables_page.dart) | +223 / −0 | 实现代码／原生桥／构建集成 | A07 统一穿戴设备管理 |
| M | [lib/pages/webview_host.dart](../lib/pages/webview_host.dart) | +0 / −20 | 实现代码／原生桥／构建集成 | B12 iOS 课程详情和校园网页外观 |
| M | [lib/services/assignment_service.dart](../lib/services/assignment_service.dart) | +8 / −7 | 实现代码／原生桥／构建集成 | A25 DDL 空状态和本地覆盖保存顺序；C11 DDL 多来源、完成／隐藏及 lateDue 处理 |
| M | [lib/services/auth_service.dart](../lib/services/auth_service.dart) | +3 / −4 | 实现代码／原生桥／构建集成 | C06 永久账号注销 |
| A | [lib/services/background_refresher.dart](../lib/services/background_refresher.dart) | +19 / −0 | 实现代码／原生桥／构建集成 | A04 后台刷新 |
| A | [lib/services/band_service.dart](../lib/services/band_service.dart) | +481 / −0 | 实现代码／原生桥／构建集成 | A08 小米手环连接桥；A09 小米手环课表同步协议 |
| M | [lib/services/campus_card_service.dart](../lib/services/campus_card_service.dart) | +82 / −5 | 实现代码／原生桥／构建集成 | A11 小米手环离线码／凭证协议 |
| M | [lib/services/deadline_widget_service.dart](../lib/services/deadline_widget_service.dart) | +54 / −28 | 实现代码／原生桥／构建集成 | A32 小组件数据共享选择；A33 小组件快照语义；C16 桌面刷新、更新弹层和考试地点 |
| M | [lib/services/feedback_service.dart](../lib/services/feedback_service.dart) | +15 / −6 | 实现代码／原生桥／构建集成 | A35 反馈请求兼容性；C05 应用内反馈与图片 |
| A | [lib/services/glance_settings_service.dart](../lib/services/glance_settings_service.dart) | +65 / −0 | 实现代码／原生桥／构建集成 | A18 外屏内容设置和预览 |
| M | [lib/services/http_client.dart](../lib/services/http_client.dart) | +18 / −0 | 实现代码／原生桥／构建集成 | A27 应用代理 |
| A | [lib/services/island_service.dart](../lib/services/island_service.dart) | +284 / −0 | 实现代码／原生桥／构建集成 | A01 Android 课程／DDL 灵动通知 |
| A | [lib/services/keep_alive_service.dart](../lib/services/keep_alive_service.dart) | +41 / −0 | 实现代码／原生桥／构建集成 | A05 可选前台保活 |
| A | [lib/services/schedule_timeline_service.dart](../lib/services/schedule_timeline_service.dart) | +60 / −0 | 实现代码／原生桥／构建集成 | A09 小米手环课表同步协议；A26 跨组件课程时间线 |
| A | [lib/services/screen_form_factor_service.dart](../lib/services/screen_form_factor_service.dart) | +118 / −0 | 实现代码／原生桥／构建集成 | A15 折叠外屏／背屏识别 |
| M | [lib/services/service_provider.dart](../lib/services/service_provider.dart) | +29 / −1 | 实现代码／原生桥／构建集成 | A01 Android 课程／DDL 灵动通知；A04 后台刷新；A05 可选前台保活；A07 统一穿戴设备管理；A15 折叠外屏／背屏识别；A27 应用代理 |
| A | [lib/services/spending_alert_service.dart](../lib/services/spending_alert_service.dart) | +110 / −0 | 实现代码／原生桥／构建集成 | A06 校园卡消费通知 |
| M | [lib/services/storage_service.dart](../lib/services/storage_service.dart) | +117 / −0 | 实现代码／原生桥／构建集成 | C03 Passkey 加密同步与旧账号迁移 |
| M | [lib/services/theme_service.dart](../lib/services/theme_service.dart) | +110 / −2 | 实现代码／原生桥／构建集成 | A20 Android 四种玻璃主题 |
| M | [lib/services/uni_auth_service.dart](../lib/services/uni_auth_service.dart) | +24 / −5 | 实现代码／原生桥／构建集成 | A27 应用代理；A28 Linux 原生登录窗口；B13 iOS 全屏扫码／登录／播放器路由 |
| M | [lib/services/update_service.dart](../lib/services/update_service.dart) | +80 / −38 | 实现代码／原生桥／构建集成 | A36 独立更新渠道 |
| M | [lib/services/watch_sync_service.dart](../lib/services/watch_sync_service.dart) | +5 / −25 | 实现代码／原生桥／构建集成 | C15 Apple Watch 离线付款码 |
| A | [lib/services/wear/android_wear_transport.dart](../lib/services/wear/android_wear_transport.dart) | +99 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [lib/services/wear/techpie_wear_data_source.dart](../lib/services/wear/techpie_wear_data_source.dart) | +107 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码；A14 华为手表提醒旧方案 |
| A | [lib/services/wear/wear_engine_service.dart](../lib/services/wear/wear_engine_service.dart) | +893 / −0 | 实现代码／原生桥／构建集成 | A12 华为手表连接和账号同步 |
| A | [lib/services/wear/wear_protocol.dart](../lib/services/wear/wear_protocol.dart) | +205 / −0 | 实现代码／原生桥／构建集成 | A13 华为手表 DDL 与短时校园码 |
| A | [lib/services/wear/wear_qr_matrix.dart](../lib/services/wear/wear_qr_matrix.dart) | +35 / −0 | 实现代码／原生桥／构建集成 | A13 华为手表 DDL 与短时校园码 |
| M | [lib/utils/glass.dart](../lib/utils/glass.dart) | +42 / −5 | 实现代码／原生桥／构建集成 | A20 Android 四种玻璃主题 |
| M | [lib/utils/haptics.dart](../lib/utils/haptics.dart) | +7 / −6 | 实现代码／原生桥／构建集成 | B16 iOS 触觉反馈不打断音乐 |
| M | [lib/utils/platform.dart](../lib/utils/platform.dart) | +12 / −2 | 实现代码／原生桥／构建集成 | B09 iOS 原生弹层、选项与分段控件；C01 IDS 软件 Passkey 绑定／登录／续期／解绑 |
| M | [lib/utils/product_version.dart](../lib/utils/product_version.dart) | +71 / −1 | 实现代码／原生桥／构建集成 | A21 RCCE 标识；A36 独立更新渠道 |
| A | [lib/utils/today_courses.dart](../lib/utils/today_courses.dart) | +45 / −0 | 实现代码／原生桥／构建集成 | A26 跨组件课程时间线 |
| M | [lib/widgets/adaptive_alert_dialog.dart](../lib/widgets/adaptive_alert_dialog.dart) | +62 / −15 | 实现代码／原生桥／构建集成 | B09 iOS 原生弹层、选项与分段控件 |
| M | [lib/widgets/adaptive_date_picker.dart](../lib/widgets/adaptive_date_picker.dart) | +39 / −15 | 实现代码／原生桥／构建集成 | B10 iOS 日期／学期／节次选择细节 |
| M | [lib/widgets/adaptive_multi_select.dart](../lib/widgets/adaptive_multi_select.dart) | +3 / −52 | 实现代码／原生桥／构建集成 | B09 iOS 原生弹层、选项与分段控件 |
| D | [lib/widgets/adaptive_navigation_scaffold.dart](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/lib/widgets/adaptive_navigation_scaffold.dart) | +0 / −113 | 实现代码／原生桥／构建集成 | B07 iOS 大标题滚动联动 |
| M | [lib/widgets/adaptive_segmented_control.dart](../lib/widgets/adaptive_segmented_control.dart) | +20 / −7 | 实现代码／原生桥／构建集成 | B09 iOS 原生弹层、选项与分段控件 |
| M | [lib/widgets/adaptive_select.dart](../lib/widgets/adaptive_select.dart) | +40 / −12 | 实现代码／原生桥／构建集成 | B09 iOS 原生弹层、选项与分段控件 |
| M | [lib/widgets/adaptive_switch.dart](../lib/widgets/adaptive_switch.dart) | +9 / −8 | 实现代码／原生桥／构建集成 | B09 iOS 原生弹层、选项与分段控件 |
| A | [lib/widgets/adaptive_time_picker.dart](../lib/widgets/adaptive_time_picker.dart) | +68 / −0 | 实现代码／原生桥／构建集成 | D06 旧端口、主题和扫描封装 |
| A | [lib/widgets/app_shell/app_entry_notifier.dart](../lib/widgets/app_shell/app_entry_notifier.dart) | +11 / −0 | 实现代码／原生桥／构建集成 | A19 折叠切换保留状态、快捷付款路由 |
| M | [lib/widgets/app_shell/app_shell.dart](../lib/widgets/app_shell/app_shell.dart) | +17 / −119 | 实现代码／原生桥／构建集成 | B06 iOS 再点当前标签；B27 付款入口从组件启动后的导航策略 |
| D | [lib/widgets/app_shell/app_shell_tab_bar.dart](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/lib/widgets/app_shell/app_shell_tab_bar.dart) | +0 / −84 | 实现代码／原生桥／构建集成 | B05 iOS 每个标签独立导航栈 |
| M | [lib/widgets/app_shell/mobile_shell.dart](../lib/widgets/app_shell/mobile_shell.dart) | +19 / −78 | 实现代码／原生桥／构建集成 | B05 iOS 每个标签独立导航栈 |
| M | [lib/widgets/app_shell/tg_bottom_nav_bar.dart](../lib/widgets/app_shell/tg_bottom_nav_bar.dart) | +39 / −115 | 实现代码／原生桥／构建集成 | A20 Android 四种玻璃主题 |
| M | [lib/widgets/course_detail_panel.dart](../lib/widgets/course_detail_panel.dart) | +7 / −18 | 实现代码／原生桥／构建集成 | B12 iOS 课程详情和校园网页外观 |
| A | [lib/widgets/feedback_entry.dart](../lib/widgets/feedback_entry.dart) | +60 / −0 | 实现代码／原生桥／构建集成 | A34 RCCE 工单入口 |
| A | [lib/widgets/glance/glance_active_scope.dart](../lib/widgets/glance/glance_active_scope.dart) | +27 / −0 | 实现代码／原生桥／构建集成 | A15 折叠外屏／背屏识别；A19 折叠切换保留状态、快捷付款路由 |
| A | [lib/widgets/glance/glance_gate.dart](../lib/widgets/glance/glance_gate.dart) | +148 / −0 | 实现代码／原生桥／构建集成 | A19 折叠切换保留状态、快捷付款路由 |
| M | [lib/widgets/ios/ios_native_navigation_bar.dart](../lib/widgets/ios/ios_native_navigation_bar.dart) | +127 / −448 | 实现代码／原生桥／构建集成 | B07 iOS 大标题滚动联动 |
| M | [lib/widgets/ios/ios_native_select.dart](../lib/widgets/ios/ios_native_select.dart) | +89 / −35 | 实现代码／原生桥／构建集成 | B15 iOS 无障碍与本地化细节 |
| M | [lib/widgets/ios/ios_native_tab_bar.dart](../lib/widgets/ios/ios_native_tab_bar.dart) | +88 / −169 | 实现代码／原生桥／构建集成 | C17 iOS 首帧延迟修复 |
| D | [lib/widgets/ios/sf_symbol_glyphs.dart](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/lib/widgets/ios/sf_symbol_glyphs.dart) | +0 / −53 | 实现代码／原生桥／构建集成 | B15 iOS 无障碍与本地化细节 |
| M | [lib/widgets/privacy_consent.dart](../lib/widgets/privacy_consent.dart) | +5 / −2 | 实现代码／原生桥／构建集成 | A19 折叠切换保留状态、快捷付款路由；C04 隐私同意与密码遮蔽 |
| M | [lib/widgets/scanner/scan_page.dart](../lib/widgets/scanner/scan_page.dart) | +1 / −3 | 实现代码／原生桥／构建集成 | B13 iOS 全屏扫码／登录／播放器路由 |
| M | [lib/widgets/scanner/scanner_viewport.dart](../lib/widgets/scanner/scanner_viewport.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | B18 新版扫码插件及 iPhone 17 适配 |
| M | [lib/widgets/schedule_picker_sheet.dart](../lib/widgets/schedule_picker_sheet.dart) | +30 / −198 | 实现代码／原生桥／构建集成 | B10 iOS 日期／学期／节次选择细节 |
| A | [lib/widgets/tg_glass_surface.dart](../lib/widgets/tg_glass_surface.dart) | +216 / −0 | 实现代码／原生桥／构建集成 | A20 Android 四种玻璃主题 |
| M | [lib/widgets/update_dialogs.dart](../lib/widgets/update_dialogs.dart) | +1 / −1 | 实现代码／原生桥／构建集成 | A36 独立更新渠道 |
| M | [linux/flutter/generated_plugins.cmake](../linux/flutter/generated_plugins.cmake) | +1 / −0 | 实现代码／原生桥／构建集成 | A28 Linux 原生登录窗口；A30 KDE Plasma 截止日期组件 |
| M | [linux/runner/CMakeLists.txt](../linux/runner/CMakeLists.txt) | +6 / −0 | 实现代码／原生桥／构建集成 | A28 Linux 原生登录窗口；A31 Linux 小组件安装与唤起；A33 小组件快照语义 |
| A | [linux/runner/deadline_snapshot_store.cc](../linux/runner/deadline_snapshot_store.cc) | +92 / −0 | 实现代码／原生桥／构建集成 | A33 小组件快照语义 |
| A | [linux/runner/deadline_snapshot_store.h](../linux/runner/deadline_snapshot_store.h) | +10 / −0 | 实现代码／原生桥／构建集成 | A33 小组件快照语义 |
| M | [linux/runner/my_application.cc](../linux/runner/my_application.cc) | +75 / −1 | 实现代码／原生桥／构建集成 | A31 Linux 小组件安装与唤起 |
| A | [linux/runner/uni_auth_window.cc](../linux/runner/uni_auth_window.cc) | +151 / −0 | 实现代码／原生桥／构建集成 | A28 Linux 原生登录窗口 |
| A | [linux/runner/uni_auth_window.h](../linux/runner/uni_auth_window.h) | +24 / −0 | 实现代码／原生桥／构建集成 | A28 Linux 原生登录窗口 |
| A | [linux/widgets/CMakeLists.txt](../linux/widgets/CMakeLists.txt) | +11 / −0 | 实现代码／原生桥／构建集成 | A31 Linux 小组件安装与唤起 |
| A | [linux/widgets/common/snapshot.mjs](../linux/widgets/common/snapshot.mjs) | +48 / −0 | 实现代码／原生桥／构建集成 | A33 小组件快照语义 |
| A | [linux/widgets/gnome/desktopCard.js](../linux/widgets/gnome/desktopCard.js) | +164 / −0 | 实现代码／原生桥／构建集成 | A29 GNOME 截止日期组件 |
| A | [linux/widgets/gnome/extension.js](../linux/widgets/gnome/extension.js) | +177 / −0 | 实现代码／原生桥／构建集成 | A29 GNOME 截止日期组件 |
| A | [linux/widgets/gnome/metadata.json](../linux/widgets/gnome/metadata.json) | +9 / −0 | 资源／声明／配置；随关联功能核对 | A29 GNOME 截止日期组件；A30 KDE Plasma 截止日期组件；A31 Linux 小组件安装与唤起；A33 小组件快照语义 |
| A | [linux/widgets/gnome/reader.mjs](../linux/widgets/gnome/reader.mjs) | +39 / −0 | 实现代码／原生桥／构建集成 | A29 GNOME 截止日期组件 |
| A | [linux/widgets/gnome/schemas/org.gnome.shell.extensions.techpie-deadlines.gschema.xml](../linux/widgets/gnome/schemas/org.gnome.shell.extensions.techpie-deadlines.gschema.xml) | +18 / −0 | 资源／声明／配置；随关联功能核对 | A29 GNOME 截止日期组件；A30 KDE Plasma 截止日期组件；A31 Linux 小组件安装与唤起；A33 小组件快照语义 |
| A | [linux/widgets/gnome/stylesheet.css](../linux/widgets/gnome/stylesheet.css) | +22 / −0 | 资源／声明／配置；随关联功能核对 | A29 GNOME 截止日期组件；A30 KDE Plasma 截止日期组件；A31 Linux 小组件安装与唤起；A33 小组件快照语义 |
| A | [linux/widgets/install.py](../linux/widgets/install.py) | +119 / −0 | 实现代码／原生桥／构建集成 | A31 Linux 小组件安装与唤起 |
| A | [linux/widgets/kde/backend/deadline_model.cpp](../linux/widgets/kde/backend/deadline_model.cpp) | +83 / −0 | 实现代码／原生桥／构建集成 | A30 KDE Plasma 截止日期组件 |
| A | [linux/widgets/kde/backend/deadline_model.h](../linux/widgets/kde/backend/deadline_model.h) | +32 / −0 | 实现代码／原生桥／构建集成 | A29 GNOME 截止日期组件；A30 KDE Plasma 截止日期组件；A31 Linux 小组件安装与唤起；A33 小组件快照语义 |
| A | [linux/widgets/kde/backend/plugin.cpp](../linux/widgets/kde/backend/plugin.cpp) | +13 / −0 | 实现代码／原生桥／构建集成 | A29 GNOME 截止日期组件；A30 KDE Plasma 截止日期组件；A31 Linux 小组件安装与唤起；A33 小组件快照语义 |
| A | [linux/widgets/kde/contents/ui/backend/qmldir](../linux/widgets/kde/contents/ui/backend/qmldir) | +1 / −0 | 实现代码／原生桥／构建集成 | A29 GNOME 截止日期组件；A30 KDE Plasma 截止日期组件；A31 Linux 小组件安装与唤起；A33 小组件快照语义 |
| A | [linux/widgets/kde/contents/ui/main.qml](../linux/widgets/kde/contents/ui/main.qml) | +128 / −0 | 实现代码／原生桥／构建集成 | A30 KDE Plasma 截止日期组件 |
| A | [linux/widgets/kde/metadata.json](../linux/widgets/kde/metadata.json) | +14 / −0 | 资源／声明／配置；随关联功能核对 | A29 GNOME 截止日期组件；A30 KDE Plasma 截止日期组件；A31 Linux 小组件安装与唤起；A33 小组件快照语义 |
| M | [macos/Flutter/GeneratedPluginRegistrant.swift](../macos/Flutter/GeneratedPluginRegistrant.swift) | +0 / −4 | 实现代码／原生桥／构建集成 | B21 macOS 构建基线／插件集成 |
| M | [macos/Podfile](../macos/Podfile) | +1 / −8 | 实现代码／原生桥／构建集成 | B21 macOS 构建基线／插件集成 |
| M | [macos/Podfile.lock](../macos/Podfile.lock) | +11 / −35 | 依赖锁／解析结果；不是独立功能 | B18 新版扫码插件及 iPhone 17 适配；B21 macOS 构建基线／插件集成 |
| M | [macos/Runner.xcodeproj/project.pbxproj](../macos/Runner.xcodeproj/project.pbxproj) | +8 / −26 | 实现代码／原生桥／构建集成 | B21 macOS 构建基线／插件集成 |
| M | [macos/Runner.xcodeproj/xcshareddata/xcschemes/Runner.xcscheme](../macos/Runner.xcodeproj/xcshareddata/xcschemes/Runner.xcscheme) | +0 / −18 | 实现代码／原生桥／构建集成 | B20 Xcode 27 lipo 兼容脚本 |
| M | [macos/Runner/DebugProfile.entitlements](../macos/Runner/DebugProfile.entitlements) | +4 / −2 | 资源／声明／配置；随关联功能核对 | C08 iOS／macOS 截止日期小组件；B21 macOS 构建基线／插件集成 |
| M | [macos/Runner/Info.plist](../macos/Runner/Info.plist) | +15 / −6 | 资源／声明／配置；随关联功能核对 | C08 iOS／macOS 截止日期小组件；B21 macOS 构建基线／插件集成 |
| M | [macos/Runner/MainFlutterWindow.swift](../macos/Runner/MainFlutterWindow.swift) | +3 / −1 | 实现代码／原生桥／构建集成 | C08 iOS／macOS 截止日期小组件；B21 macOS 构建基线／插件集成 |
| M | [macos/Runner/Release.entitlements](../macos/Runner/Release.entitlements) | +4 / −2 | 资源／声明／配置；随关联功能核对 | C08 iOS／macOS 截止日期小组件；B21 macOS 构建基线／插件集成 |
| M | [ohos/scripts/generate-build-profile.mjs](../ohos/scripts/generate-build-profile.mjs) | +2 / −2 | 实现代码／原生桥／构建集成 | C09 OHOS 直接插件依赖和镜像重试 |
| M | [pubspec.lock](../pubspec.lock) | +177 / −106 | 依赖锁／解析结果；不是独立功能 | B18 新版扫码插件及 iPhone 17 适配 |
| M | [pubspec.yaml](../pubspec.yaml) | +16 / −26 | 实现代码／原生桥／构建集成 | B18 新版扫码插件及 iPhone 17 适配；C09 OHOS 直接插件依赖和镜像重试 |
| D | [scripts/agc-publish.mjs](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/scripts/agc-publish.mjs) | +0 / −394 | 构建／发布／开发工具 | B23 AppGallery 自动提审 |
| D | [scripts/agc-status.mjs](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/scripts/agc-status.mjs) | +0 / −145 | 构建／发布／开发工具 | B23 AppGallery 自动提审 |
| D | [scripts/agc-test-version.mjs](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/scripts/agc-test-version.mjs) | +0 / −291 | 构建／发布／开发工具 | B23 AppGallery 自动提审 |
| D | [scripts/apple-tools/lipo](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/scripts/apple-tools/lipo) | +0 / −15 | 构建／发布／开发工具 | B20 Xcode 27 lipo 兼容脚本 |
| M | [scripts/release-plan.mjs](../scripts/release-plan.mjs) | +7 / −206 | 构建／发布／开发工具 | B24 上游发布版本线防退保护 |
| A | [test/account_deletion_test.dart](../test/account_deletion_test.dart) | +109 / −0 | 回归测试／夹具；不是独立功能 | C06 永久账号注销 |
| M | [test/adaptive_alert_dialog_test.dart](../test/adaptive_alert_dialog_test.dart) | +58 / −48 | 回归测试／夹具；不是独立功能 | B09 iOS 原生弹层、选项与分段控件；B10 iOS 日期／学期／节次选择细节 |
| M | [test/adaptive_confirmation_button_test.dart](../test/adaptive_confirmation_button_test.dart) | +3 / −3 | 回归测试／夹具；不是独立功能 | B09 iOS 原生弹层、选项与分段控件；B10 iOS 日期／学期／节次选择细节 |
| D | [test/adaptive_navigation_scaffold_test.dart](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/test/adaptive_navigation_scaffold_test.dart) | +0 / −124 | 回归测试／夹具；不是独立功能 | B07 iOS 大标题滚动联动 |
| M | [test/adaptive_select_test.dart](../test/adaptive_select_test.dart) | +95 / −31 | 回归测试／夹具；不是独立功能 | B09 iOS 原生弹层、选项与分段控件；B10 iOS 日期／学期／节次选择细节 |
| M | [test/adaptive_selection_controls_test.dart](../test/adaptive_selection_controls_test.dart) | +8 / −11 | 回归测试／夹具；不是独立功能 | B09 iOS 原生弹层、选项与分段控件；B10 iOS 日期／学期／节次选择细节 |
| D | [test/app_shell_tab_bar_test.dart](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/test/app_shell_tab_bar_test.dart) | +0 / −147 | 回归测试／夹具；不是独立功能 | B05 iOS 每个标签独立导航栈；B08 iOS DDL 选择工具栏与恢复反馈 |
| M | [test/app_update_prompt_test.dart](../test/app_update_prompt_test.dart) | +7 / −5 | 回归测试／夹具；不是独立功能 | A36 独立更新渠道 |
| M | [test/assignment_service_test.dart](../test/assignment_service_test.dart) | +40 / −2 | 回归测试／夹具；不是独立功能 | A25 DDL 空状态和本地覆盖保存顺序；C11 DDL 多来源、完成／隐藏及 lateDue 处理 |
| M | [test/back_navigation_test.dart](../test/back_navigation_test.dart) | +19 / −6 | 回归测试／夹具；不是独立功能 | A19 折叠切换保留状态、快捷付款路由；B05 iOS 每个标签独立导航栈；B27 付款入口从组件启动后的导航策略 |
| A | [test/background_refresher_test.dart](../test/background_refresher_test.dart) | +56 / −0 | 回归测试／夹具；不是独立功能 | A04 后台刷新 |
| M | [test/campus_card/application/auth_offline_state_test.dart](../test/campus_card/application/auth_offline_state_test.dart) | +8 / −0 | 回归测试／夹具；不是独立功能 | D07 支付异常兜底；C14 手机在线／离线校园码、账单、安全设置 |
| M | [test/campus_card/application/payment_code_controller_test.dart](../test/campus_card/application/payment_code_controller_test.dart) | +8 / −18 | 回归测试／夹具；不是独立功能 | D07 支付异常兜底；C14 手机在线／离线校园码、账单、安全设置 |
| M | [test/campus_card/architecture/environment_isolation_test.dart](../test/campus_card/architecture/environment_isolation_test.dart) | +35 / −2 | 回归测试／夹具；不是独立功能 | A39 Windows 资源路径与架构测试 |
| M | [test/campus_card/architecture/platform_contract_test.dart](../test/campus_card/architecture/platform_contract_test.dart) | +1 / −1 | 回归测试／夹具；不是独立功能 | A39 Windows 资源路径与架构测试 |
| A | [test/campus_card/data/ecard_bill_summary_repository_test.dart](../test/campus_card/data/ecard_bill_summary_repository_test.dart) | +103 / −0 | 回归测试／夹具；不是独立功能 | D04 月度账单统计仓库 |
| M | [test/campus_card/data/ecard_card_repository_test.dart](../test/campus_card/data/ecard_card_repository_test.dart) | +12 / −0 | 回归测试／夹具；不是独立功能 | B26 校园卡个人资料请求精简 |
| M | [test/campus_card/data/ecard_cipher_test.dart](../test/campus_card/data/ecard_cipher_test.dart) | +1 / −1 | 回归测试／夹具；不是独立功能 | C14 手机在线／离线校园码、账单、安全设置 |
| A | [test/campus_card/data/ecard_recharge_repository_test.dart](../test/campus_card/data/ecard_recharge_repository_test.dart) | +78 / −0 | 回归测试／夹具；不是独立功能 | D05 充值接口和初始化脚手架 |
| A | [test/campus_card/data/vela_sm2_fixture_test.dart](../test/campus_card/data/vela_sm2_fixture_test.dart) | +35 / −0 | 回归测试／夹具；不是独立功能 | A11 小米手环离线码／凭证协议 |
| M | [test/campus_card/presentation/apple_wallet_components_test.dart](../test/campus_card/presentation/apple_wallet_components_test.dart) | +42 / −16 | 回归测试／夹具；不是独立功能 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [test/campus_card/presentation/ecard_settings_and_security_test.dart](../test/campus_card/presentation/ecard_settings_and_security_test.dart) | +12 / −0 | 回归测试／夹具；不是独立功能 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [test/campus_card/presentation/payment_activity_performance_test.dart](../test/campus_card/presentation/payment_activity_performance_test.dart) | +4 / −0 | 回归测试／夹具；不是独立功能 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [test/campus_card/presentation/scanner_geometry_test.dart](../test/campus_card/presentation/scanner_geometry_test.dart) | +1 / −1 | 回归测试／夹具；不是独立功能 | B13 iOS 全屏扫码／登录／播放器路由；B18 新版扫码插件及 iPhone 17 适配 |
| M | [test/campus_card/presentation/scanner_modal_test.dart](../test/campus_card/presentation/scanner_modal_test.dart) | +1 / −1 | 回归测试／夹具；不是独立功能 | B13 iOS 全屏扫码／登录／播放器路由；B18 新版扫码插件及 iPhone 17 适配 |
| M | [test/campus_card/presentation/startup_routing_test.dart](../test/campus_card/presentation/startup_routing_test.dart) | +7 / −0 | 回归测试／夹具；不是独立功能 | B14 校园卡底部安全区与固定操作栏；C14 手机在线／离线校园码、账单、安全设置 |
| M | [test/campus_card/support/fake_ecard_transport.dart](../test/campus_card/support/fake_ecard_transport.dart) | +21 / −0 | 回归测试／夹具；不是独立功能 | B26 校园卡个人资料请求精简；D04 月度账单统计仓库；D05 充值接口和初始化脚手架 |
| M | [test/campus_card_account_page_test.dart](../test/campus_card_account_page_test.dart) | +0 / −9 | 回归测试／夹具；不是独立功能 | B15 iOS 无障碍与本地化细节 |
| M | [test/deadline_widget_service_test.dart](../test/deadline_widget_service_test.dart) | +4 / −2 | 回归测试／夹具；不是独立功能 | A32 小组件数据共享选择；A33 小组件快照语义；C08 iOS／macOS 截止日期小组件 |
| M | [test/ecard_bind_service_test.dart](../test/ecard_bind_service_test.dart) | +0 / −18 | 回归测试／夹具；不是独立功能 | B17 iOS 模拟器校园卡绑定提示 |
| M | [test/ecard_entry_navigation_test.dart](../test/ecard_entry_navigation_test.dart) | +16 / −233 | 回归测试／夹具；不是独立功能 | A19 折叠切换保留状态、快捷付款路由；B27 付款入口从组件启动后的导航策略 |
| A | [test/feedback_entry_test.dart](../test/feedback_entry_test.dart) | +88 / −0 | 回归测试／夹具；不是独立功能 | A34 RCCE 工单入口；A35 反馈请求兼容性 |
| A | [test/feedback_service_test.dart](../test/feedback_service_test.dart) | +122 / −0 | 回归测试／夹具；不是独立功能 | A35 反馈请求兼容性 |
| A | [test/fixtures/vela-sm2.json](../test/fixtures/vela-sm2.json) | +61 / −0 | 回归测试／夹具；不是独立功能 | A11 小米手环离线码／凭证协议；D01 Vela 腕端完成度 |
| A | [test/glance/glance_gate_test.dart](../test/glance/glance_gate_test.dart) | +191 / −0 | 回归测试／夹具；不是独立功能 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| A | [test/glance/glance_payment_view_test.dart](../test/glance/glance_payment_view_test.dart) | +93 / −0 | 回归测试／夹具；不是独立功能 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| A | [test/glance/glance_shell_test.dart](../test/glance/glance_shell_test.dart) | +359 / −0 | 回归测试／夹具；不是独立功能 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| A | [test/glance/glance_snapshot_builder_test.dart](../test/glance/glance_snapshot_builder_test.dart) | +148 / −0 | 回归测试／夹具；不是独立功能 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| A | [test/glance/screen_form_factor_test.dart](../test/glance/screen_form_factor_test.dart) | +209 / −0 | 回归测试／夹具；不是独立功能 | A15 折叠外屏／背屏识别；A16 外屏课程和截止日期概览；A17 外屏付款与实时状态；A19 折叠切换保留状态、快捷付款路由 |
| M | [test/haptics_test.dart](../test/haptics_test.dart) | +9 / −11 | 回归测试／夹具；不是独立功能 | B16 iOS 触觉反馈不打断音乐 |
| D | [test/ios_first_frame_test.dart](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/test/ios_first_frame_test.dart) | +0 / −67 | 回归测试／夹具；不是独立功能 | C17 iOS 首帧延迟修复 |
| D | [test/ios_legacy_chrome_test.dart](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/test/ios_legacy_chrome_test.dart) | +0 / −127 | 回归测试／夹具；不是独立功能 | B09 iOS 原生弹层、选项与分段控件 |
| M | [test/ios_native_navigation_bar_test.dart](../test/ios_native_navigation_bar_test.dart) | +72 / −97 | 回归测试／夹具；不是独立功能 | B05 iOS 每个标签独立导航栈；B09 iOS 原生弹层、选项与分段控件 |
| M | [test/ios_native_tab_bar_test.dart](../test/ios_native_tab_bar_test.dart) | +22 / −66 | 回归测试／夹具；不是独立功能 | B05 iOS 每个标签独立导航栈；B09 iOS 原生弹层、选项与分段控件 |
| D | [test/ios_tab_navigation_test.dart](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/test/ios_tab_navigation_test.dart) | +0 / −272 | 回归测试／夹具；不是独立功能 | B06 iOS 再点当前标签 |
| A | [test/island_frame_test.dart](../test/island_frame_test.dart) | +188 / −0 | 回归测试／夹具；不是独立功能 | A01 Android 课程／DDL 灵动通知；A02 HyperOS／Android 16 通知适配；A03 灵动通知时间切换与恢复 |
| A | [test/keep_alive_service_test.dart](../test/keep_alive_service_test.dart) | +72 / −0 | 回归测试／夹具；不是独立功能 | A05 可选前台保活 |
| M | [test/product_version_test.dart](../test/product_version_test.dart) | +9 / −0 | 回归测试／夹具；不是独立功能 | A36 独立更新渠道 |
| D | [test/scan_page_navigator_test.dart](https://github.com/HeZeBang/TechPie-flutter/blob/aaefb6ada9299c0cf4b167fe9fe89b0a69405543/test/scan_page_navigator_test.dart) | +0 / −38 | 回归测试／夹具；不是独立功能 | B13 iOS 全屏扫码／登录／播放器路由 |
| M | [test/schedule_first_week_test.dart](../test/schedule_first_week_test.dart) | +6 / −7 | 回归测试／夹具；不是独立功能 | A22 窄屏课表可读性；A23 课表紧凑布局；C07 DDL 滚动、回本周与文字溢出修复 |
| A | [test/schedule_row_count_test.dart](../test/schedule_row_count_test.dart) | +34 / −0 | 回归测试／夹具；不是独立功能 | A23 课表紧凑布局 |
| A | [test/services/huawei_watch_page_test.dart](../test/services/huawei_watch_page_test.dart) | +126 / −0 | 回归测试／夹具；不是独立功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码 |
| A | [test/services/techpie_wear_data_source_test.dart](../test/services/techpie_wear_data_source_test.dart) | +89 / −0 | 回归测试／夹具；不是独立功能 | A12 华为手表连接和账号同步；C01 IDS 软件 Passkey 绑定／登录／续期／解绑 |
| A | [test/services/wear_engine_service_test.dart](../test/services/wear_engine_service_test.dart) | +552 / −0 | 回归测试／夹具；不是独立功能 | A12 华为手表连接和账号同步；A13 华为手表 DDL 与短时校园码 |
| A | [test/spending_alert_test.dart](../test/spending_alert_test.dart) | +117 / −0 | 回归测试／夹具；不是独立功能 | A06 校园卡消费通知 |
| A | [test/tg_glass_surface_test.dart](../test/tg_glass_surface_test.dart) | +69 / −0 | 回归测试／夹具；不是独立功能 | A20 Android 四种玻璃主题 |
| M | [test/theme_service_test.dart](../test/theme_service_test.dart) | +46 / −0 | 回归测试／夹具；不是独立功能 | A20 Android 四种玻璃主题 |
| M | [test/uni_auth_webview_test.dart](../test/uni_auth_webview_test.dart) | +15 / −32 | 回归测试／夹具；不是独立功能 | A28 Linux 原生登录窗口；B12 iOS 课程详情和校园网页外观 |
| M | [test/update_service_test.dart](../test/update_service_test.dart) | +116 / −28 | 回归测试／夹具；不是独立功能 | A36 独立更新渠道 |
| M | [test/watch_settings_page_test.dart](../test/watch_settings_page_test.dart) | +0 / −13 | 回归测试／夹具；不是独立功能 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| M | [test/watch_sync_service_test.dart](../test/watch_sync_service_test.dart) | +5 / −55 | 回归测试／夹具；不是独立功能 | B01 Apple Watch 在线付款码；B02 Apple Watch 在线／离线选择与回退；B03 Apple Watch 消费结果检测；B04 Apple Watch 独立余额刷新与节能轮询 |
| M | [test/widget_test.dart](../test/widget_test.dart) | +18 / −38 | 回归测试／夹具；不是独立功能 | A15 折叠外屏／背屏识别；A20 Android 四种玻璃主题；C01 IDS 软件 Passkey 绑定／登录／续期／解绑；C04 隐私同意与密码遮蔽 |
| M | [tool/ohos_local_smoke.dart](../tool/ohos_local_smoke.dart) | +3 / −0 | 构建／发布／开发工具 | C09 OHOS 直接插件依赖和镜像重试 |
| A | [vela_app/package-lock.json](../vela_app/package-lock.json) | +9482 / −0 | 依赖锁／解析结果；不是独立功能 | A08 小米手环连接桥；A09 小米手环课表同步协议；A11 小米手环离线码／凭证协议；D01 Vela 腕端完成度 |
| A | [vela_app/package.json](../vela_app/package.json) | +20 / −0 | 资源／声明／配置；随关联功能核对 | A08 小米手环连接桥；A09 小米手环课表同步协议；A11 小米手环离线码／凭证协议；D01 Vela 腕端完成度 |
| A | [vela_app/src/app.ux](../vela_app/src/app.ux) | +10 / −0 | 实现代码／原生桥／构建集成 | A08 小米手环连接桥；A09 小米手环课表同步协议；A11 小米手环离线码／凭证协议；D01 Vela 腕端完成度 |
| A | [vela_app/src/common/logo.png](../vela_app/src/common/logo.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | A08 小米手环连接桥；A09 小米手环课表同步协议；A11 小米手环离线码／凭证协议；D01 Vela 腕端完成度 |
| A | [vela_app/src/common/sm2.js](../vela_app/src/common/sm2.js) | +987 / −0 | 实现代码／原生桥／构建集成 | A11 小米手环离线码／凭证协议；D01 Vela 腕端完成度 |
| A | [vela_app/src/manifest.json](../vela_app/src/manifest.json) | +27 / −0 | 资源／声明／配置；随关联功能核对 | A08 小米手环连接桥；A09 小米手环课表同步协议；A11 小米手环离线码／凭证协议；D01 Vela 腕端完成度 |
| A | [vela_app/src/pages/index/index.ux](../vela_app/src/pages/index/index.ux) | +140 / −0 | 实现代码／原生桥／构建集成 | D01 Vela 腕端完成度 |
| A | [web/feedback/.gitignore](../web/feedback/.gitignore) | +5 / −0 | 文档／仓库规则 | D02 独立反馈网页；A40 穿戴与反馈额外回归检查 |
| A | [web/feedback/README.md](../web/feedback/README.md) | +9 / −0 | 文档／仓库规则 | D02 独立反馈网页 |
| A | [web/feedback/public/app.js](../web/feedback/public/app.js) | +93 / −0 | 实现代码／原生桥／构建集成 | D02 独立反馈网页 |
| A | [web/feedback/public/common.js](../web/feedback/public/common.js) | +145 / −0 | 实现代码／原生桥／构建集成 | D02 独立反馈网页；A40 穿戴与反馈额外回归检查 |
| A | [web/feedback/public/icon.png](../web/feedback/public/icon.png) | +- / −- | 二进制资源／SDK；不按文本行宣称功能 | D02 独立反馈网页；A40 穿戴与反馈额外回归检查 |
| A | [web/feedback/public/index.html](../web/feedback/public/index.html) | +239 / −0 | 实现代码／原生桥／构建集成 | D02 独立反馈网页；A40 穿戴与反馈额外回归检查 |
| A | [web/feedback/public/style.css](../web/feedback/public/style.css) | +404 / −0 | 资源／声明／配置；随关联功能核对 | D02 独立反馈网页；A40 穿戴与反馈额外回归检查 |
| A | [web/feedback/test/app.test.mjs](../web/feedback/test/app.test.mjs) | +96 / −0 | 回归测试／夹具；不是独立功能 | D02 独立反馈网页；A40 穿戴与反馈额外回归检查 |
| A | [web/feedback/test/common.test.mjs](../web/feedback/test/common.test.mjs) | +156 / −0 | 回归测试／夹具；不是独立功能 | D02 独立反馈网页；A40 穿戴与反馈额外回归检查 |
| M | [windows/flutter/generated_plugins.cmake](../windows/flutter/generated_plugins.cmake) | +1 / −0 | 实现代码／原生桥／构建集成 | A39 Windows 资源路径与架构测试 |

**覆盖核对**

总数：422；所有条目均映射至功能表，无未分类路径。

| 目录／根文件 | 条目数 |
| --- | --- |
| `.envrc.release-backup-20260919-042730` | 1 |
| `.gitattributes` | 1 |
| `.github` | 8 |
| `.gitignore` | 1 |
| `CHANGELOG.md` | 1 |
| `CLAUDE.md` | 1 |
| `README.md` | 1 |
| `android` | 29 |
| `assets` | 3 |
| `ci` | 1 |
| `docs` | 2 |
| `harmonyos_watch` | 81 |
| `ios` | 36 |
| `lib` | 137 |
| `linux` | 22 |
| `macos` | 9 |
| `ohos` | 1 |
| `pubspec.lock` | 1 |
| `pubspec.yaml` | 1 |
| `scripts` | 5 |
| `test` | 62 |
| `tool` | 1 |
| `vela_app` | 7 |
| `web` | 9 |
| `windows` | 1 |
