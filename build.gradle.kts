import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element

plugins {
    id("java")
    application
    id("org.openjfx.javafxplugin") version("0.1.0")
    checkstyle
    id("com.github.spotbugs") version "6.0.25"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-suite")

    // https://mvnrepository.com/artifact/org.easymock/easymock
    testImplementation("org.easymock:easymock:5.4.0")

    // cucumber
    testImplementation(platform("io.cucumber:cucumber-bom:7.20.1"))
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


javafx {
    version = "17.0.10" // Highly stable version that fully supports Java 11
    modules = listOf("javafx.controls") // Pulls in critical UI components like Button, Scene, and Stage
}

tasks.compileJava {
    options.release = 21
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("ui.Main") // 👈 Points Gradle to your main class
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = false
        html.required = true
        html.stylesheet = resources.text.fromFile("config/xsl/checkstyle-noframes-severity-sorted.xsl")
    }
}

checkstyle{
    isIgnoreFailures = false
    toolVersion = "14.4.0"
    configFile = file("${project.projectDir}/config/checkstyle/checkstyle.xml")
}

spotbugs {
    ignoreFailures = false
    showStackTraces = true
    showProgress = true
    effort = Effort.DEFAULT
    reportLevel = Confidence.DEFAULT
    reportsDir = file("spotbugs")
    maxHeapSize = "1g"
    extraArgs = listOf("-nested:false")
    excludeFilter = file("config/spotbugs/exclude.xml")
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs.html")
        setStylesheet("fancy-hist.xsl")
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required = true   // needed by complexityBadge
        xml.outputLocation = layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml")
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
    }
}

val complexityBadge by tasks.registering {
    group = "verification"
    description = "Writes a Shields.io badge for missed complexity across all production code"
    dependsOn(tasks.jacocoTestReport)

    val reportFile = layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml")
    val outFile = layout.buildDirectory.file("badges/complexity.json")
    inputs.file(reportFile)
    outputs.file(outFile)

    doLast {
        val factory = DocumentBuilderFactory.newInstance().apply {
            isValidating = false
            setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        }
        val report = factory.newDocumentBuilder().parse(reportFile.get().asFile).documentElement
        // Only use the report-level total: nested counters would double-count classes.
        val counter = (0 until report.childNodes.length)
            .map { report.childNodes.item(it) }
            .filterIsInstance<Element>()
            .singleOrNull {
                it.tagName == "counter" && it.getAttribute("type") == "COMPLEXITY"
            } ?: throw GradleException("No project COMPLEXITY counter found in JaCoCo report")

        val missed = counter.getAttribute("missed").toInt()
        val total = missed + counter.getAttribute("covered").toInt()
        val color = when {
            missed == 0 -> "brightgreen"
            missed.toLong() * 4 <= total -> "yellow"
            missed.toLong() * 2 <= total -> "orange"
            else -> "red"
        }
        val json = """{"schemaVersion":1,"label":"missed complexity","message":"$missed / $total","color":"$color"}"""
        outFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText(json)
        }
        println("Badge: $json")
    }
}

val mutantsKilledBadge by tasks.registering {
    group = "verification"
    description = "Writes a Shields.io badge for mutants killed in domain code"
    dependsOn(tasks.pitest)

    val reportFile = layout.buildDirectory.file("reports/pitest/mutations.xml")
    val outFile = layout.buildDirectory.file("badges/mutants-killed.json")
    inputs.file(reportFile)
    outputs.file(outFile)

    doLast {
        val factory = DocumentBuilderFactory.newInstance().apply {
            isValidating = false
            setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        }
        val doc = factory.newDocumentBuilder().parse(reportFile.get().asFile)
        val mutations = doc.getElementsByTagName("mutation")
        val total = mutations.length
        val killed = (0 until total).count {
            (mutations.item(it) as Element).getAttribute("status") == "KILLED"
        }
        val color = when {
            total == 0 -> "lightgrey"
            killed == total -> "brightgreen"
            killed.toLong() * 4 >= total.toLong() * 3 -> "yellow"
            killed.toLong() * 2 >= total -> "orange"
            else -> "red"
        }
        val message = if (total == 0) "no mutants" else "$killed / $total"
        val json = """{"schemaVersion":1,"label":"mutants killed","message":"$message","color":"$color"}"""
        outFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText(json)
        }
        println("Badge: $json")
    }
}

tasks.build {
    dependsOn("pitest")
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    finalizedBy(tasks.pitest)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

pitest {
    jvmPath.set(
        javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(21))
        }.map { it.executablePath }
    )
    targetClasses = setOf("catan.game_table.domain.*")
    targetTests = setOf("catan.game_table.domain.*")
    junit5PluginVersion = "1.2.1"
    pitestVersion = "1.15.0" //not needed when a default PIT version should be used

    threads = 4
    outputFormats = setOf("HTML", "XML")
    timestampedReports = false
    testSourceSets.set(listOf(sourceSets.test.get()))
    mainSourceSets.set(listOf(sourceSets.main.get()))
    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true) //useful with bigger projects on Windows
    fileExtensionsToFilter.addAll("xml")
    exportLineCoverage = true
}