import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

val ecardWidgetResources = layout.buildDirectory.dir("generated/ecardWidgetResources")
val prepareEcardWidgetResources by tasks.registering(Copy::class) {
    from("../../assets/campus_card/images/widget-background.png") {
        rename { "ecard_widget_background.png" }
    }
    into(ecardWidgetResources.map { it.dir("drawable-nodpi") })
}

val releaseSigningProperties = Properties()
val releaseSigningPropertiesFile = rootProject.file("key.properties")

if (releaseSigningPropertiesFile.isFile) {
    releaseSigningPropertiesFile.inputStream().use(releaseSigningProperties::load)
}

fun releaseSigningValue(propertyName: String, environmentName: String): String? =
    releaseSigningProperties.getProperty(propertyName)?.takeIf(String::isNotEmpty)
        ?: System.getenv(environmentName)?.takeIf(String::isNotEmpty)

val releaseStoreFilePath = releaseSigningValue(
    "storeFile",
    "ANDROID_KEYSTORE_PATH",
)
val releaseStorePassword = releaseSigningValue(
    "storePassword",
    "ANDROID_KEYSTORE_PASSWORD",
)
val releaseKeyAlias = releaseSigningValue(
    "keyAlias",
    "ANDROID_KEY_ALIAS",
)
val releaseKeyPassword = releaseSigningValue(
    "keyPassword",
    "ANDROID_KEY_PASSWORD",
)
val releaseStoreType = releaseSigningValue(
    "storeType",
    "ANDROID_KEYSTORE_TYPE",
) ?: "PKCS12"
val releaseStoreFile = releaseStoreFilePath?.let { rootProject.file(it) }
val releaseSigningValueCount = listOf(
    releaseStoreFilePath,
    releaseStorePassword,
    releaseKeyAlias,
    releaseKeyPassword,
).count { !it.isNullOrBlank() }
val releaseSigningConfigured = releaseSigningValueCount == 4
val releaseTaskRequested = gradle.startParameter.taskNames.any {
    it.contains("release", ignoreCase = true)
}

require(releaseSigningValueCount == 0 || releaseSigningConfigured) {
    "Android release signing requires all ANDROID_KEYSTORE_* and ANDROID_KEY_* values."
}

if (releaseTaskRequested) {
    val missingValues = buildList {
        if (releaseStoreFilePath == null) add("storeFile / ANDROID_KEYSTORE_PATH")
        if (releaseStorePassword == null) {
            add("storePassword / ANDROID_KEYSTORE_PASSWORD")
        }
        if (releaseKeyAlias == null) add("keyAlias / ANDROID_KEY_ALIAS")
        if (releaseKeyPassword == null) {
            add("keyPassword / ANDROID_KEY_PASSWORD")
        }
    }

    require(missingValues.isEmpty()) {
        "Release signing is not configured. Missing: ${missingValues.joinToString()}. " +
            "Copy android/key.properties.example to android/key.properties or set the " +
            "ANDROID_* environment variables."
    }
    require(releaseStoreFile?.isFile == true) {
        "Release keystore does not exist: ${releaseStoreFile?.absolutePath}"
    }
}

android {
    namespace = "club.geekpie.techpie"
    // mobile_scanner compiles against SDK 36; the SDK's default lags behind it.
    compileSdk = 36
    ndkVersion = "28.2.13676358"
    sourceSets.getByName("main").res.srcDir(ecardWidgetResources)

    signingConfigs {
        if (releaseSigningConfigured) {
            create("release") {
                storeFile = releaseStoreFile
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
                storeType = releaseStoreType

                // Direct-distribution APKs must remain installable on old devices
                // while also getting whole-APK integrity and signing-key rotation.
                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    defaultConfig {
        applicationId = "club.geekpie.techpie"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        // The HyperOS focus-notification library (island feature) requires API 27;
        // Wear Engine 5.0.3.300 floors at API 23, so the island wins.
        minSdk = maxOf(flutter.minSdkVersion, 27)
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    buildTypes {
        release {
            signingConfigs.findByName("release")?.let { signingConfig = it }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    // Xiaomi HyperOS focus-notification / super-island template builder.
    implementation("com.xzakota.hyper.notification:focus-api:1.4")
    // The Android 16 live-update NotificationCompat APIs (promoted ongoing,
    // short critical text) are newer than the core version the Flutter plugins
    // resolve transitively. 1.18.0 is the newest that still builds with AGP 8.11.
    implementation("androidx.core:core:1.18.0")
    // The scheduled background refresh: periodic work, no resident service.
    implementation("androidx.work:work-runtime-ktx:2.9.1")
}

tasks.named("preBuild") { dependsOn(prepareEcardWidgetResources) }

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    // The plugins pin CameraX 1.3.x, whose camera-core ships
    // libimage_processing_util_jni.so built for 4 KB pages — Android's 16 KB
    // alignment check rejects the whole APK for that one library. The 1.4.x
    // line rebuilt it; the constraints move the whole camera family together
    // so core cannot float above its camera2/lifecycle siblings.
    constraints {
        implementation("androidx.camera:camera-core:1.4.2")
        implementation("androidx.camera:camera-camera2:1.4.2")
        implementation("androidx.camera:camera-lifecycle:1.4.2")
    }
}

flutter {
    source = "../.."
}

dependencies {
    // Wear Engine 5.0.3.300 drives the Huawei watch companion; agconnect-core
    // backs the HMS app id in the manifest. Work scheduling is covered by the
    // work-runtime-ktx line above (it pulls work-runtime in).
    implementation("com.huawei.agconnect:agconnect-core:1.5.2.300")
    implementation("com.huawei.hms:wearengine:5.0.3.300")
}

// Local AGConnect settings contain credentials and are not checked into Git.
if (file("agconnect-services.json").isFile) {
    apply(plugin = "com.huawei.agconnect")
}
