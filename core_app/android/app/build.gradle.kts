import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.artifacts.result.UnresolvedDependencyResult
import java.io.File

plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.example.gradle_showcases"
    compileSdk = 35
    ndkVersion = "27.0.12077973"
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
                    buildConfig = true
                }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.example.gradle_showcases"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
        multiDexEnabled = true

    }

    buildTypes {
        release {
            // TODO: Add your own signing config for the release build.
            // Signing with the debug keys for now, so `flutter run --release` works.
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

android.applicationVariants.all {
  // Define your build/flavor name e.g., debug/profile/release or premiumDebug/freemiumDebug
    val variantName = name //Taken from active build

    val capitalized = variantName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

// Taskname is dynamic, based on build/flavor
    val taskName = "list${capitalized}Dependencies"

    val configName = "${variantName}RuntimeClasspath" //Use RuntimeClasspath for full scan of app or change to see results for other configs

//Register task with dynamic name you just created

    tasks.register(taskName) {
        group = "Reporting"
        description = "Lists dependencies for the $variantName build variant"
//Check if the configuration you're trying to access is valid. Just to be super sure
        doLast {
            val config = configurations.findByName(configName)
            if (config == null) {
                println("Configuration '$configName' not found.")
                return@doLast
            }
//Prepare your file and build directory
            val outputFile = File(buildDir, "dependency-report-$variantName.txt")
            outputFile.parentFile.mkdirs()

///The dependencies at this stage may not be fully resolved, prepare a bucket to catch them (realistically should not happen but who knows) 
            val unresolved = mutableListOf<String>()

            outputFile.bufferedWriter().use { writer ->
                writer.appendLine("Dependencies for configuration: $configName")

//In all deps if they're resolved, write them to file
                config.incoming.resolutionResult.allDependencies.forEach { dep ->
                    when (dep) {
                        is ResolvedDependencyResult -> {
                            val selected = dep.selected
                            val group = selected.moduleVersion?.group ?: "unknown-group"
                            val name = selected.moduleVersion?.name
                            val version = selected.moduleVersion?.version ?: "unspecified"
                            writer.appendLine(" - $group:$name:$version")
                        }

//Catch unresolved deps and make note of it in the file
                        is UnresolvedDependencyResult -> {
                            val attempted = dep.attempted
                            unresolved.add(attempted.displayName)
                            writer.appendLine(" - [UNRESOLVED] ${attempted.displayName}")
                        }
                    }
                }

                if (unresolved.isNotEmpty()) {
                    writer.appendLine("\n[!] WARNING: Some dependencies could not be resolved:")
                    unresolved.forEach { writer.appendLine(" - $it") }
                }
            }
//Notify that process is finished
            println("Dependency report for $variantName written to: $outputFile")
        }
    }

    // Hook into variant build
    assembleProvider.configure {
        dependsOn(taskName)
    }
}

//Lists all available build configurations. Use by running $ ./gradlew :app:listConfigurations
tasks.register("listConfigurations") {
    doLast {
        configurations.forEach {
            println(it.name)
        }
    }
}

flutter {
    source = "../.."
}
