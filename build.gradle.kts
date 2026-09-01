// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains:annotations:23.0.0")
        }
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kandroid) apply false
    alias(libs.plugins.kcompose) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.kserialization) apply false
    alias(libs.plugins.ksp) apply false
}