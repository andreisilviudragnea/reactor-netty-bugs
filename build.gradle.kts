plugins {
    java
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile("io.projectreactor", "reactor-core", "3.3.0.RELEASE")
    compile("org.springframework:spring-webflux:5.2.0.RELEASE")
    compile("io.projectreactor.netty:reactor-netty:0.9.0.RELEASE")
    testCompile("org.testng", "testng", "7.0.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    test {
        useTestNG()
    }
}
