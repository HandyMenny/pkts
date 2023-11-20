import com.diffplug.spotless.LineEnding

plugins {
    id("io.pkts.java-conventions")
    id("com.diffplug.spotless") version "6.22.0"
}

dependencies {
    api(project(":pkts-buffers"))
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.mockito:mockito-all:1.8.5")
}

description = "Core Pkts"

repositories { mavenCentral() }

spotless {
    format("misc") {
        target("*.md", ".gitignore", "**/*.csv")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }

    java {
        importOrder()
        removeUnusedImports()
        googleJavaFormat().aosp().reflowLongStrings()
        formatAnnotations()
    }

    kotlin { ktfmt().kotlinlangStyle() }

    kotlinGradle { ktfmt().kotlinlangStyle() }

    // Workaround: https://github.com/diffplug/spotless/issues/1644
    lineEndings = LineEnding.UNIX
}
