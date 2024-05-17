
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript { dependencies{
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
} }




plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
//    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10" apply false

}
