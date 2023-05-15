plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.9.0")
    implementation("io.kotest:kotest-assertions:4.0.7")
    implementation(kotlin("stdlib-jdk8"))

}

tasks {
    sourceSets {
        main {
            java.srcDirs("src/main")
        }
        test {
            java.srcDirs("src/test")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}

//java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(17))
//    }
//}
//kotlin {
//    jvmToolchain {
//        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
//    }
//}