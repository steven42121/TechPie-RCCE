# RCCE 上游同步记录（2026-11-23）

## 同步范围

- RCCE 发布基线：`fa0ef9b649a07aec9273ac8fa6754af328f07e01`（`26h2-111614`）。
- 上游 `master`：`5ba9c44c88f18d9c6e7d9700323052eacdef3183`，自共同祖先 `aaefb6ada9299c0cf4b167fe9fe89b0a69405543` 前进 13 个提交、22 个文件。
- 上游研究生课表 [PR #184](https://github.com/HeZeBang/TechPie-flutter/pull/184) 已于 2026-11-23 合入 `master`。
- 开放的 [PR #200](https://github.com/HeZeBang/TechPie-flutter/pull/200)（head `e1d8ea7e94c9d33c8087c3cac9a2a0d6e0c15c86`）提供自定义截止日期及 Apple 提醒事项双向同步。本次按用户要求移植；查询时 PR 为 Open、非 Draft，未报告 CI 检查。

## 功能与兼容处理

- 新增 IDS 身份识别、本科／研究生课表来源选择、研究生学期与教学方案切换、未排／停开课程展示及研究生课表导出。
- 研究生课表、考试、课程方案与自定义课程按身份／学期隔离；等待身份和 EAMS 会话就绪后再拉取考试；IDS 会话轮换使旧请求失效；刷新失败保留缓存并避免重试风暴。
- 自定义截止日期按登录账号或访客范围保存在本地，纳入 Deadlines、首页和组件；完成状态可双向同步到 Apple 提醒事项，支持添加、编辑、删除和去重。
- iOS / macOS Reminders 原生桥接独立接入；保留 RCCE 的 Deadline 组件启用开关、Linux 组件、安全快照尺寸限制、现有多平台穿戴／灵动通知／消费提醒和桌面代理功能。
- RCCE 今日课程及岛屿共用 `coursesOnDay`，扩展支持研究生日历、日期型课程和组件展示；保留窄屏周课表的联动横向滚动布局。
- 版本更新为 `26.2.112321+26112321`，Release 标签预计为 `26h2-112321`。

## 未纳入的开放提案

[PR #152](https://github.com/HeZeBang/TechPie-flutter/pull/152) 在 2026-11-23 仍是 Draft。其说明记录了 GT 6 Pro 在手机关闭测试中意外重置，待复测冷启动校园码续期、离线缓存读取和生产签名设备行为；该伴侣功能未纳入本次可发布范围。上游仍开放的 #194–#197 功能已在 `26h2-111614` 实现，本次不重复移植。

## 验证

- Node 发布规则测试：5 项通过。
- Flutter 静态分析：通过。
- Flutter 全量测试：972 项通过。
- Node 发布规则测试：5 项通过。
- Linux Release 构建：通过。
- Android Release 构建：通过；包内版本名 `26h2-112321`、版本码 `26112321`，APK 使用 RCCE CI 固定测试密钥并通过 apksigner 验签。
- iOS/macOS 原生桥接已核对 Runner 源文件、权限说明与通道注册；当前 Linux 环境没有 Xcode，Apple 平台编译待 GitHub CI 验证。
- 推送与 Release CI：待本轮最终验证。
