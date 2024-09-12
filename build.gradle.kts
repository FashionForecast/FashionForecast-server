plugins {
    java
    id("org.springframework.boot") version "3.2.8"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val snippetsDir = file("build/generated-snippets")
// QClass 파일 생성 위치 지정
val generated: String = "src/main/generated"

dependencies {
    implementation("org.springframework.retry:spring-retry")
    implementation("com.opencsv:opencsv:5.5.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    implementation ("org.flywaydb:flyway-core")
    implementation ("org.flywaydb:flyway-mysql")

    implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")


    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("io.rest-assured:rest-assured:5.1.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// QClass 파일 생성 위치 지정
tasks.withType<JavaCompile>().configureEach {
    options.generatedSourceOutputDirectory.set(file(generated))
}


// 서브모듈 설정파일 복사
tasks.register<Copy>("copySecret") {
    from(file("./FashionForecast-server-submodule"))
    include("application*.yml")
    into("src/main/resources")
}

tasks.named<ProcessResources>("processResources") {
    dependsOn("copySecret")
}

tasks.withType<Test> {
    dependsOn("copySecret") // copySecret이 선행되어야함
    useJUnitPlatform() // 테스트 진행

    // Test 결과를 snippets 디렉터리에 출력
    outputs.dir(snippetsDir)
}

// AsciiDoc 생성 및 빌드 관련 Task
tasks {
    asciidoctor {
        dependsOn(test)  // test가 성공해야 asciidoctor가 실행됨

        attributes(mapOf(
                "snippets" to snippetsDir.absolutePath
        ))

        // 기존 Docs 삭제 (문서 최신화를 위해)
        doFirst {
            delete(file("src/main/resources/static/docs"))
        }

        // snippet 디렉터리 설정
        inputs.dir(snippetsDir)

        // AsciiDoc 파일 생성
        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
        }
    }

    build {
        // AsciiDoc 파일 생성이 완료되어야 build 진행
        dependsOn(asciidoctor)
    }
}

// Gradle clean 시 QClass 디렉토리 삭제
tasks.clean {
    delete(file(generated))
}
