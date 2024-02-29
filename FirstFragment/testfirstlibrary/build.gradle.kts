plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.example.testfirstlibrary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.testfirstlibrary"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

afterEvaluate {
    publishing {
        publications {
            // Creates two publications with different artifactIds
            create<MavenPublication>("full") {
                run{
                    groupId = "com.example"
                    artifactId = "testlibrary"
                    version = "1.0"
                    artifact("$buildDir/outputs/aar/${artifactId}-release.aar")
                }
//                from(components["full"])
            }
        }

//        repositories {
//            maven {
//                name = "GitHubPackages"
//                url = uri("https://maven.pkg.github.com/<GITHUB_USER_NAME>/<REPOSITORY_NAME>")
//                credentials {
//                    username = githubProperties.get("gpr.usr") as String? ?: System.getenv("GPR_USER")
//                    password = githubProperties.get("gpr.key") as String? ?: System.getenv("GPR_API_KEY")
//                }
//            }
//        }
    }
}