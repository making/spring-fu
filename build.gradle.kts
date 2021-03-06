import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.jetbrains.kotlin.jvm") version "1.3.31" apply false
	id("org.springframework.boot") version "2.2.0.BUILD-SNAPSHOT" apply false
	id("org.jetbrains.dokka") version "0.9.18" apply false
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	id("maven-publish")
}

allprojects {
	apply {
		plugin("maven-publish")
		plugin("io.spring.dependency-management")
	}

	version = "0.1.BUILD-SNAPSHOT"
	group = "org.springframework.fu"

	dependencyManagement {
		val bootVersion: String by project
		val coroutinesVersion: String by project
		val springDataR2dbcVersion: String by project
		val r2dbcVersion: String by project
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:$bootVersion")
			mavenBom("io.r2dbc:r2dbc-bom:$r2dbcVersion")
		}
		dependencies {
			dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
			dependency("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
			dependency("org.springframework.data:spring-data-r2dbc:$springDataR2dbcVersion")
		}
	}

	publishing {
		repositories {
			val repoUsername: String? by project
			val repoPassword: String? by project
			maven {
				if (repoUsername != null && repoPassword != null) {
					credentials {
						username = repoUsername
						password = repoPassword
					}
					url = uri(
							if (version.toString().endsWith(".BUILD-SNAPSHOT")) "https://repo.spring.io/libs-snapshot-local/"
							else "https://repo.spring.io/libs-milestone-local/"
					)

				} else {
					url = uri("$buildDir/repo")
				}
			}
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			jvmTarget = "1.8"
			freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
		}
	}

	repositories {
		mavenCentral()
		maven("https://repo.spring.io/milestone")
		maven("https://repo.spring.io/snapshot")
		jcenter()
	}
}

publishing {
	publications {
		create<MavenPublication>("kofu-coroutines-mongodb") {
			groupId = "org.springframework.fu"
			artifactId = "spring-fu-samples-kofu-coroutines-mongodb"
			artifact(task<Zip>("kofuCoroutinesMongodbSample") {
				from("samples/kofu-coroutines-mongodb") {
					exclude("build", ".gradle", ".idea", "out", "*.iml")
				}
				destinationDir = file("$buildDir/dist")
				into("kofu-coroutines-mongodb")
				setExecutablePermissions()
			})
		}

		create<MavenPublication>("kofu-coroutines-r2dbc") {
			groupId = "org.springframework.fu"
			artifactId = "spring-fu-samples-kofu-coroutines-r2dbc"
			artifact(task<Zip>("kofuCoroutinesR2dbcSample") {
				from("samples/kofu-coroutines-r2dbc") {
					exclude("build", ".gradle", ".idea", "out", "*.iml")
				}
				destinationDir = file("$buildDir/dist")
				into("kofu-coroutines-r2dbc")
				setExecutablePermissions()
			})
		}

		create<MavenPublication>("kofu-reactive-minimal") {
			groupId = "org.springframework.fu"
			artifactId = "spring-fu-samples-kofu-reactive-minimal"
			artifact(task<Zip>("kofuReactiveMinimalSampleZip") {
				from("samples/kofu-reactive-minimal") {
					exclude("build", "com.sample.applicationkt", ".gradle", ".idea", "out", "*.iml")
				}
				destinationDir = file("$buildDir/dist")
				into("kofu-reactive-minimal")
				setExecutablePermissions()
			})
		}

		create<MavenPublication>("kofu-reactive-mongodb") {
			groupId = "org.springframework.fu"
			artifactId = "spring-fu-samples-kofu-reactive-mongodb"
			artifact(task<Zip>("kofuReactiveMongodbSampleZip") {
				from("samples/kofu-reactive-mongodb") {
					exclude("build", ".gradle", ".idea", "out", "*.iml")
				}
				destinationDir = file("$buildDir/dist")
				into("kofu-reactive-mongodb")
				setExecutablePermissions()
			})
		}

		create<MavenPublication>("kofu-reactive-r2dbc") {
			groupId = "org.springframework.fu"
			artifactId = "spring-fu-samples-kofu-reactive-r2dbc"
			artifact(task<Zip>("kofuReactiveR2dbcSampleZip") {
				from("samples/kofu-reactive-r2dbc") {
					exclude("build", ".gradle", ".idea", "out", "*.iml")
				}
				destinationDir = file("$buildDir/dist")
				into("kofu-reactive-r2dbc")
				setExecutablePermissions()
			})
		}
	}
}

fun CopySpec.setExecutablePermissions() {
	filesMatching("gradlew") { mode = 0b111101101 }
	filesMatching("gradlew.bat") { mode = 0b110100100 }
}
