plugins {
    id("com.android.library")
    id("kotlin-android")
    `maven-publish`
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 19
        targetSdk = 32
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
            groupId = "com.gitee.liuwanshun"
            artifactId = "permissionx"
            version = "1.0"
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
