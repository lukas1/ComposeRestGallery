// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("kotlinVersion", "1.5.10")
        set("coroutinesVersion", "1.5.0")
        set("composeVersion", "1.0.0-beta08")
        set("hiltVersion", "2.37")
    }

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val hiltVersion: String by rootProject.extra
        classpath("com.android.tools.build:gradle:7.0.0-beta03")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}