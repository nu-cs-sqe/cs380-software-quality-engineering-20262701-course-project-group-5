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
    testImplementation("org.easymock:easymock:3.1")

    // cucumber
    testImplementation(platform("io.cucumber:cucumber-bom:7.20.1"))
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}


javafx {
    version = "17.0.10" // Highly stable version that fully supports Java 11
    modules = listOf("javafx.controls") // Pulls in critical UI components like Button, Scene, and Stage
}

tasks.compileJava {
    options.release = 11
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
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
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
    targetClasses = setOf("Code.*") //by default "${project.group}.*"
    targetTests = setOf("Code.*")
    junit5PluginVersion = "1.2.1"
    pitestVersion = "1.15.0" //not needed when a default PIT version should be used

    threads = 4
    outputFormats = setOf("HTML")
    timestampedReports = false
    testSourceSets.set(listOf(sourceSets.test.get()))
    mainSourceSets.set(listOf(sourceSets.main.get()))
    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true) //useful with bigger projects on Windows
    fileExtensionsToFilter.addAll("xml")
    exportLineCoverage = true
}

// JVM internal class name: package path with "/", inner classes use "$"
val badgeClass = "Code/CheckOrders"
val badgeMethod = "checkSupport"
val badgeMethodDesc: String? = null   // e.g. "(I)Z" if the method is overloaded

val complexityBadge by tasks.registering {
    group = "verification"
    description = "Writes a shields.io JSON badge with the missed complexity of one method as grading aid"
    dependsOn(tasks.jacocoTestReport)

    val reportFile = layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml")
    val outFile = layout.buildDirectory.file("badges/complexity.json")
    inputs.file(reportFile)
    outputs.file(outFile)

    doLast {
        val factory = DocumentBuilderFactory.newInstance().apply {
            isValidating = false
            // JaCoCo's XML has a DOCTYPE pointing at report.dtd; don't try to fetch it
            setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        }
        val doc = factory.newDocumentBuilder().parse(reportFile.get().asFile)

        var missed = -1
        var covered = -1
        val classes = doc.getElementsByTagName("class")
        for (i in 0 until classes.length) {
            val cls = classes.item(i) as Element
            if (cls.getAttribute("name") != badgeClass) continue
            val methods = cls.getElementsByTagName("method")
            for (j in 0 until methods.length) {
                val m = methods.item(j) as Element
                if (m.getAttribute("name") != badgeMethod) continue
                if (badgeMethodDesc != null && m.getAttribute("desc") != badgeMethodDesc) continue
                val counters = m.getElementsByTagName("counter")
                for (k in 0 until counters.length) {
                    val c = counters.item(k) as Element
                    if (c.getAttribute("type") == "COMPLEXITY") {
                        missed = c.getAttribute("missed").toInt()
                        covered = c.getAttribute("covered").toInt()
                    }
                }
            }
        }
        if (missed < 0) throw GradleException("Method $badgeClass.$badgeMethod not found in JaCoCo report")

        val total = missed + covered
        val color = when {
            missed == 0 -> "brightgreen"
            missed * 4 <= total -> "yellow"
            missed * 2 <= total -> "orange"
            else -> "red"
        }
        val json = """{"schemaVersion":1,"label":"$badgeMethod missed complexity","message":"$missed / $total","color":"$color"}"""
        outFile.get().asFile.apply { parentFile.mkdirs(); writeText(json) }
        println("Badge: $json")
    }
}

