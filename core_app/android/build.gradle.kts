import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.gradle.api.JavaVersion

allprojects {
    repositories {
        google()
        mavenCentral()
    }
 subprojects {
    afterEvaluate {
        val isAndroid = plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")

        if (isAndroid) {
            val android = extensions.findByName("android") as? BaseExtension ?: return@afterEvaluate

            android.apply {
                compileSdkVersion(35)

                defaultConfig {
                    minSdkVersion(24)
                }

                //ndkVersion = "26.1.10909125"

                buildFeatures.apply {
                    buildConfig = true
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                // Try to set namespace if possible (only works in AGP 7+)
                try {
                    val ns = android::class.java.getMethod("getNamespace").invoke(android) as? String
                    if (ns.isNullOrEmpty()) {
                        val setNs = android::class.java.getMethod("setNamespace", String::class.java)
                        setNs.invoke(android, project.group.toString())
                    }
                } catch (ignored: Throwable) {
                    // Not available on older plugins or causes reflection issues
                }
            }

            // Set Kotlin JVM target if available
            extensions.findByName("kotlinOptions")?.let {
                (it as? KotlinJvmOptions)?.jvmTarget = "11"
            }
        }
    }
}

    
}


val newBuildDir: Directory = rootProject.layout.buildDirectory.dir("../../build").get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
