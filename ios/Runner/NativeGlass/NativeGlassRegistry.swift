import Flutter
import UIKit

enum NativeGlassRegistry {
  static func registerAll(with registrar: FlutterPluginRegistrar) {
    let messenger = registrar.messenger()
    let platformChannel = FlutterMethodChannel(
      name: "techpie/platform",
      binaryMessenger: messenger
    )

    platformChannel.setMethodCallHandler { call, result in
      switch call.method {
      case "iosMajorVersion":
        let major = ProcessInfo.processInfo.operatingSystemVersion.majorVersion
        result(major)
      case "getUserAgentInfo":
        result([
          "device": UIDevice.current.model,
          "system": UIDevice.current.systemName,
          "systemVersion": UIDevice.current.systemVersion,
          "scale": UIScreen.main.scale,
        ])
      default:
        result(FlutterMethodNotImplemented)
      }
    }

    registrar.register(
      NativeGlassTabBarFactory(messenger: messenger),
      withId: NativeGlassTabBarPlatformView.viewType
    )
    registrar.register(
      NativeNavigationBarFactory(messenger: messenger),
      withId: NativeNavigationBarPlatformView.viewType
    )
    registrar.register(
      NativeSelectFactory(messenger: messenger),
      withId: NativeSelectPlatformView.viewType
    )

    NativeGlassPresenterPlugin.register(with: registrar)
    IcsFilePresenterPlugin.register(with: registrar)
  }
}
