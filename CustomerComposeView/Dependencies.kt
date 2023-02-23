package com.xiaomi.fitness.plugin

object Versions {
    const val kotlin = "1.7.20"
    const val kotlinCoroutineTest = "1.5.2"
    const val kotlinCoroutinesCore = "1.6.4"
    const val kotlinCoroutinesAndroid = "1.6.4"
    const val junit = "4.13.1"
    const val junitExt = "1.1.2"
    const val espressoCore = "3.3.0"
    const val coreKtx = "1.8.0"
    const val appcompat = "1.5.1"
    const val fragment = "1.5.4"
    const val constraintlayout = "2.0.4"
    const val swiperefreshlayout = "1.1.0"
    const val cardview = "1.0.0"
    const val recyclerview = "1.2.1"
    const val viewpager2="1.0.0"
    const val paging = "3.0.0-rc01"
    const val pagingktx = "3.0.0"
    const val work = "2.7.1"
    const val hilt = "2.44"
    const val retrofit = "2.9.0"
    const val okhttpLogging = "4.9.1"
    const val okhttp = "4.9.1"
    const val room = "2.3.0"
    const val timber = "4.7.1"
    const val coil = "2.2.2"
    const val ktx = "2.5.1"
    const val arouter = "1.5.1"
    const val protobuf = "3.1.0"
    const val gms = "17.0.0"
    const val camera2 = "1.1.0-alpha08"
    const val cameraView = "1.0.0-alpha28"
    const val wx="5.5.8"
    const val miuixSdk = "2.0.0-alpha"
}

object Mi {
    const val account = "com.xiaomi.account:passportsdk-client-ui:${Versions.account}"
}

object Kt {
    const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutinesCore}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutinesAndroid}"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
}

object Test {
    const val junit = "junit:junit:${Versions.junit}"
    const val androidTestJunit = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutineTest}"
    const val ktJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
}

object AndroidX {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragment}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val swiperefreshlayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewpager2}"
    const val cardview = "androidx.cardview:cardview:${Versions.cardview}"
    const val pagingRuntime = "androidx.paging:paging-runtime:${Versions.paging}"
    const val pagingRxJava2 = "androidx.paging:paging-rxjava2:${Versions.paging}"
    const val workRuntime = "androidx.work:work-runtime:${Versions.work}"
    const val workRuntimeKtx = "androidx.work:work-runtime:${Versions.work}"
    const val workTesting = "androidx.work:work-testing:${Versions.work}"
    const val annota = "androidx.annotation:annotation:1.2.0"
    const val pagingKtx = "androidx.paging:paging-runtime-ktx:${Versions.pagingktx}"
}

object Lifecycle {
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.ktx}"
    const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.ktx}"
    const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ktx}"
    const val lifecycleJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.ktx}"
}

object Hilt {
    const val plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val daggerRuntime = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val daggerCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val test = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
}

object Retrofit {
    const val runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val scalars = "com.squareup.retrofit2:converter-scalars:${Versions.retrofit}"
    const val mock = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"
    const val logging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val rxjavaAdapter =  "com.squareup.retrofit2:adapter-rxjava2:${Versions.rxjavaAdapter}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttp_url_connection = "com.squareup.okhttp3:okhttp-urlconnection:${Versions.okhttp}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.room}"
    const val compiler = "androidx.room:room-compiler:${Versions.room}"
    const val ktx = "androidx.room:room-ktx:${Versions.room}"
    const val testing = "androidx.room:room-testing:${Versions.room}"
}

object Reactivex {
    const val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
    const val rxandroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}"
}

object ARouter {
    const val api = "com.alibaba:arouter-api:${Versions.arouter}"
    const val compiler = "com.alibaba:arouter-compiler:${Versions.arouter}"
}

object Google {
    const val protobufNano = "com.google.protobuf.nano:protobuf-javanano:${Versions.protobuf}"
    const val wearable = "com.google.android.gms:play-services-wearable:${Versions.gms}"
    const val material = "com.google.android.material:material:1.3.0"
    const val autoService = "com.google.auto.service:auto-service:1.0"
    const val serviceAnnotation = "com.google.auto.service:auto-service-annotations:1.0"
    const val gson = "com.google.code.gson:gson:2.8.6"
    const val flexbox = "com.google.android:flexbox:2.0.1"
}

object Camera2 {
    const val cameraCore = "androidx.camera:camera-core:${Versions.camera2}"
    const val camera2 = "androidx.camera:camera-camera2:${Versions.camera2}"
    const val cameraLifeCycle = "androidx.camera:camera-lifecycle:${Versions.camera2}"
    const val cameraView = "androidx.camera:camera-view:${Versions.cameraView}"
}

object WeiChat {
//    const val sdk = "com.tencent.mm.opensdk:wechat-sdk-android-without-mta:${Versions.wx}"
    const val sdk = "com.tencent.mm.opensdk:wechat-sdk-android:+"
}

object EventBus {
    const val sdk = "org.greenrobot:eventbus:${Versions.eventBus}"
}