plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val seleniumJavaVersion = "4.14.1"
val seleniumJupiterVersion = "5.0.1"
val webdrivermanagerVersion = "5.6.3"
val junitJupiterVersion = "5.9.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
	implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumJavaVersion")
	testImplementation("io.github.bonigarcia:selenium-jupiter:$seleniumJupiterVersion")
	testImplementation("io.github.bonigarcia:webdrivermanager:$webdrivermanagerVersion")
	testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}

tasks.register<Test>("unitTest") {
	description = "Runs unit tests."
	group = "verification"

	filter {
		excludeTestsMatching("*FunctionalTest")
	}
}

tasks.register<Test>("functionalTest") {
	description = "Runs functional tests."
	group = "verification"

	filter {
		includeTestsMatching("*FunctionalTest")
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

tasks.test{
	filter{
		excludeTestsMatching("*FunctionalTest")
	}

	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	classDirectories.setFrom(files(classDirectories.files.map {
		fileTree(it) { exclude("**/*Application**") }
	}))
	dependsOn(tasks.test) // tests are required to run before generating the report
	reports {
		xml.required.set(false)
		csv.required.set(false)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
}
