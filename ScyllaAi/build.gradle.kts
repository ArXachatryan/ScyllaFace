plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    id("maven-publish")
}

android {
    namespace = "ai.face.liva"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

publishing {
    publications {
        create<MavenPublication>("bar") {
            groupId = "com.scylla"
            artifactId = "scyllaai"
            version = "4.0.2"
            artifact("$buildDir/outputs/aar/ScyllaAi-release.aar")

            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")

                // Manually add your navigation dependencies
                val navDependencies = listOf(
                    Triple("androidx.navigation", "navigation-compose", "2.9.0"),
                    Triple("androidx.navigation", "navigation-runtime", "2.9.0"),
                    Triple("androidx.navigation", "navigation-runtime-ktx", "2.9.0"),
                    Triple("androidx.navigation", "navigation-common", "2.9.0"),
                    Triple("androidx.navigation", "navigation-common-ktx", "2.9.0"),
                    Triple("com.airbnb.android", "lottie-compose", "4.12.0")
                )

                navDependencies.forEach { (groupId, artifactId, version) ->
                    val dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", groupId)
                    dependencyNode.appendNode("artifactId", artifactId)
                    dependencyNode.appendNode("version", version)
                }
            }
        }
    }
    repositories {
        maven {
            name = "ScyllaFace"
            url = uri("https://maven.pkg.github.com/ArXachatryan/ScyllaFace")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("bar") {
//            groupId = "com.scylla"
//            artifactId = "scyllaai"
//            version = "1.0.4"
//            artifact("$buildDir/outputs/aar/ScyllaAi-release.aar")
//        }
//    }
//
//    repositories {
//        maven {
//            name = "ScyllaFace"
//            url = uri("https://maven.pkg.github.com/ArXachatryan/ScyllaFace")
//            credentials {
//                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
//                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
//            }
//        }
//    }
//}

//publishing {
//    publications {
//        create<MavenPublication>("bar") {
//            groupId = "com.scylla"
//            artifactId = "scyllaai"
//            version = "1.0.3"
//            artifact("$buildDir/outputs/aar/ScyllaAi-release.aar")
//
//            // Add these for better dependency resolution
//            pom {
//                name.set("ScyllaAi")
//                description.set("Scylla AI Library")
//                url.set("https://github.com/ArXachatryan/ScyllaFace")
//                licenses {
//                    license {
//                        name.set("The Apache License, Version 2.0")
//                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//                developers {
//                    developer {
//                        id.set("ArXachatryan")
//                        name.set("Your Name")
//                        email.set("your.email@example.com")
//                    }
//                }
//                scm {
//                    connection.set("scm:git:git://github.com/ArXachatryan/ScyllaFace.git")
//                    developerConnection.set("scm:git:ssh://github.com:ArXachatryan/ScyllaFace.git")
//                    url.set("https://github.com/ArXachatryan/ScyllaFace/tree/main")
//                }
//            }
//        }
//    }
//
//    repositories {
//        maven {
//            name = "ScyllaFace"
//            url = uri("https://maven.pkg.github.com/ArXachatryan/ScyllaFace")
//            credentials {
//                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
//                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
//            }
//        }
//    }
//}

dependencies {

    implementation(libs.retrofit2)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.logging.interceptor)
//    api(libs.androidx.navigation.compose)
    api("androidx.navigation:navigation-compose:2.9.0")
    api("androidx.navigation:navigation-runtime:2.9.0")
    api("androidx.navigation:navigation-runtime-ktx:2.9.0")
    api("androidx.navigation:navigation-common:2.9.0")
    api("androidx.navigation:navigation-common-ktx:2.9.0")

    implementation(libs.androidx.activity.compose.v180)
    implementation(libs.accompanist.permissions)
    api(libs.lottie.compose)

    implementation(libs.koin.androidx.compose)
    api(libs.androidx.camera.camera2)
    api(libs.androidx.camera.lifecycle)
    api(libs.androidx.camera.view)
    api(libs.face.detection)

//    api("com.google.mlkit:face-detection:16.1.7")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}