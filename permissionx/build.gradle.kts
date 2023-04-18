plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

android {
    namespace = "com.lws.permissionx"
    compileSdk = 33

    defaultConfig {
        minSdk = 19
        consumerProguardFile("consumer-rules.pro")
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
}


publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.liu-wanshun"
            artifactId = "permissionx"
            version = "1.0"
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
