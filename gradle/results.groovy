/**
 * This Gradle script is responsible for parsing the JMH results file and update the README with the latest results
 */

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "org.ajoberstar:gradle-git:0.10.0"
    }
}

task parseResults(dependsOn:'jmh') {
    description = "Parses the JMH results file and generates an Asciidoctor results file from it"

    ext.adocFile = file("${project.buildDir}/reports/jmh/results.adoc")

    inputs.file jmh.resultsFile
    outputs.file adocFile

    doLast {
        def lines = jmh.resultsFile as String[]
        def sorted = lines[1..-1].collect {
            def (benchmark, mode, threads, samples, score, error, unit, size) = it.split(',')*.replace(/"/,'')
            score = score as double
            error = error as double
            size = size as int
            benchmark = benchmark[1+benchmark.lastIndexOf('.')..-1]
            [benchmark, score, error, unit, size]
        } groupBy {
            it[-1] // group by size
        } collectEntries { size, elements ->
            [size, elements.sort { -(it[1]-it[2]) } ] // sort by score desc
        }
        // generate adoc format
        def sb = new StringBuilder()
        sorted.each { len, elements ->

        sb << """[cols="1,3,2,2", options="header"]
.Table Benchmark results for string of length *${len}* on ${System.getProperty('java.runtime.version')}
|===
|Rank |Method name |Score | Error (99.9%)
"""
            elements.eachWithIndex { entry, i ->
                def (benchmark, score, error, unit, sz) = entry
                sb << """|${i+1}
|$benchmark
|$score $unit
|±$error $unit

"""
            }
            sb << '|===\n\n'
        }
        adocFile.withWriter('utf-8') { it<<sb }
    }
}

task updateReadme(dependsOn:parseResults) {
    description = "Automatically updates the README.adoc file with the latest results"

    ext.gitRepo = 'https://github.com/melix/lecharny-challenge.git'
    ext.repoPath = file("$project.buildDir/git")
    ext.readmeFile = file("$repoPath/README.adoc")

    inputs.file(readmeFile)
    inputs.property('gitRepo', gitRepo)
    inputs.property('repoPath', repoPath)
    outputs.file(readmeFile)

    onlyIf {
        JavaVersion.current().java7Compatible && System.getenv('TRAVIS_BRANCH') == 'master'
    }

    doLast {

        if (repoPath.exists()) {
            repoPath.deleteDir()
        }

        // workaround for Java 6

        def credentials = Class.forName('org.ajoberstar.grgit.Credentials').newInstance(username:System.getenv('GITHUB_TOKEN'),password:'')

        // first clone the repo
        def grgit = Class.forName('org.ajoberstar.grgit.Grgit').clone(credentials: credentials, dir: repoPath, uri: gitRepo)

        def readme = readmeFile as String[]
        def startMarker = "start::jdk${JavaVersion.current().majorVersion}"
        def endMarker = "end::jdk${JavaVersion.current().majorVersion}"
        boolean inside = false
        readmeFile.withWriter('utf-8') { wrt ->
            readme.each { line ->
                if (line =~ endMarker) {
                    inside = false
                }
                if (!inside) {
                    wrt << "${line}\n"
                }
                if (line=~startMarker) {
                    inside = true
                    wrt << parseResults.adocFile.getText('utf-8')
                }
            }
        }

        grgit.commit(message: "[ci skip] Updating benchmark results for JDK ${JavaVersion.current()}", all: true)
        grgit.push()
    }
}