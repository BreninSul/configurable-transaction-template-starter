import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "2.2.0"
    val springBootVersion = "4.1.0"
    id("java")
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.4"
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlin.kapt") version kotlinVersion
}
val springBootVersion = "4.1.0"
val springTrxVersion = "7.0.0"
val kotlinVersion = "2.2.0"
val javaVersion = JavaVersion.VERSION_21

group = "io.github.breninsul"
version = "2.0.0"

java {
    sourceCompatibility = javaVersion
}

java {
    withJavadocJar()
    withSourcesJar()
}
repositories {
    mavenCentral()
}
tasks.compileJava {
    dependsOn.add(tasks.processResources)
}
tasks.compileKotlin {
    dependsOn.add(tasks.processResources)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework:spring-tx:$springTrxVersion")
    // Boot 4 split jdbc/transaction autoconfigure into dedicated modules
    implementation("org.springframework.boot:spring-boot-jdbc:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-transaction:$springBootVersion")
    kapt("org.springframework.boot:spring-boot-autoconfigure-processor")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")
    testImplementation("org.postgresql:postgresql:42.7.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testImplementation("org.apache.curator:curator-test:5.6.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

signing {
    val signingKey: String? = (findProperty("signingKey") as String?) ?: System.getenv("SIGNING_KEY")
    val signingPassword: String? = (findProperty("signingPassword") as String?) ?: System.getenv("SIGNING_PASSWORD")
    if (!signingKey.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
    } else {
        useGpgCmd()
    }
}

centralPortal {
    pom {
        packaging = "jar"
        name.set("BreninSul Spring Boot Configurable TransactionTemplate Starter")
        val repositoryName = "configurable-transaction-template-starter"
        url.set("https://github.com/BreninSul/$repositoryName")
        description.set("Starter for Configurable TransactionTemplate. Allows to execute any trx with different isolation,propagation,timeout,readOnly mode")
        licenses {
            license {
                name.set("MIT License")
                url.set("http://opensource.org/licenses/MIT")
            }
        }
        scm {
            connection.set("scm:https://github.com/BreninSul/$repositoryName.git")
            developerConnection.set("scm:git@github.com:BreninSul/$repositoryName.git")
            url.set("https://github.com/BreninSul/$repositoryName")
        }
        developers {
            developer {
                id.set("BreninSul")
                name.set("BreninSul")
                email.set("brenimnsul@gmail.com")
                url.set("breninsul.github.io")
            }
        }
    }
}

tasks.jar {
    enabled = true
    archiveClassifier.set("")
}

// This is a library starter, not an executable application: disable bootJar.
tasks.named("bootJar") {
    enabled = false
}
