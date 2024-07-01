plugins {
    alias(libs.plugins.android.application)
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\Dumidu\\Documents\\tmp\\patima.jks")
            storePassword = "phonecall"
            keyAlias = "patima"
            keyPassword = "phonecall"
        }
    }
    namespace = "com.onesandzeros.patima"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.onesandzeros.patima"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            resValue("string", "network_security_config", "network_security_config_debug")
            buildConfigField("String", "API_URL", "\"${project.property("devApiUrl")}\"")
        }
        release {
            resValue("string", "network_security_config", "network_security_config_release")
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
            buildConfigField("String", "API_URL", "\"${project.property("prodApiUrl")}\"")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    implementation("org.osmdroid:osmdroid-android:6.1.11")


    val cameraxVersion = "1.4.0-alpha03"
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    implementation("androidx.camera:camera-view:${cameraxVersion}")

    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")

//    implementation("de.hdodenhof:circleimageview:3.1.0")

//    implementation("com.github.bumptech.glide:glide:4.12.0")
//    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
//    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.facebook.fresco:fresco:2.5.0")

//    Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

//    Animation
    implementation("com.airbnb.android:lottie:3.4.0")
}