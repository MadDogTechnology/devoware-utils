package org.devoware

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.bundling.Compression
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.bundling.Tar

class HomonculusPlugin implements Plugin<Project> {

  void apply (Project project) {
    project.configure(project) {
      apply plugin: 'java'
      apply plugin: 'eclipse'
      
      dependencies {
        runtime 'org.devoware:homonculus-bootstrap:1.0'
      
        compile 'org.slf4j:log4j-over-slf4j:1.7.21',
            'org.slf4j:jcl-over-slf4j:1.7.21',
            'ch.qos.logback:logback-core:1.1.7',
            'ch.qos.logback:logback-classic:1.1.7',
            'com.google.guava:guava:19.0',
            'com.fasterxml.jackson.core:jackson-core:2.8.1',
            'com.fasterxml.jackson.core:jackson-databind:2.8.1',
            'com.fasterxml.jackson.core:jackson-annotations:2.8.1',
            'com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.8.1',
            'org.apache.commons:commons-lang3:3.4',
            'javax.validation:validation-api:1.1.0.Final',
            'org.hibernate:hibernate-validator:5.2.4.Final',
            'commons-beanutils:commons-beanutils-core:1.8.3',
            'org.glassfish:javax.el:3.0.0',
            'com.google.dagger:dagger:2.6',
            'org.devoware:homonculus-core:1.0',
            'org.devoware:homonculus-config:1.0',
            'org.devoware:homonculus-validators:1.0',
            'io.dropwizard.metrics:metrics-core:3.1.0',
            'io.dropwizard.metrics:metrics-healthchecks:3.1.0',
            'io.dropwizard.metrics:metrics-jvm:3.1.0'
      
        compileOnly 'com.google.code.findbugs:annotations:3.0.1',
            'com.google.dagger:dagger-compiler:2.6'
      
      
        testCompile 'junit:junit:4.12',
            'org.mockito:mockito-core:1.10.19',
            'org.hamcrest:hamcrest-all:1.3'
      }
      
    }
    
    project.extensions.create('homonculus', HomonculusExtension)
    
    project.task('deleteLibraries', type: Delete) {
      delete project.file('lib').listFiles(), project.file('logs')
    }

    project.task('copyLibraries', type: Copy) {
      doFirst{
        project.file('logs').mkdirs()
      }
      from project.configurations.compile, project.configurations.runtime
      into 'lib'
    }

    project.eclipse.project {
      buildCommand 'edu.umd.cs.findbugs.plugin.eclipse.findbugsBuilder'
      natures 'edu.umd.cs.findbugs.plugin.eclipse.findbugsNature'
    }

    project.tasks.cleanEclipseJdt {
      doFirst {
        delete project.file('.factorypath'),
            project.file("bin/${project.homonculus.appName} - Start.launch"),
            project.file("bin/${project.homonculus.appName} - Stop.launch")
      }
    }


    project.eclipse {
      jdt.file.withProperties { it['org.eclipse.jdt.core.compiler.processAnnotations'] = 'enabled' }
      classpath {
        defaultOutputDir = project.file('classes')
        downloadSources = true
      }
    }

    project.eclipse.classpath.file {
      whenMerged { classpath ->
        def sourcePaths = []
        classpath.entries.each {entry ->
          if (entry.hasProperty('sourcePath') && entry.sourcePath.hasProperty('path')) {
            sourcePaths.add(entry.sourcePath.path)
          }
        }
        def sourcePathsXml = ""
        sourcePaths.each {path -> sourcePathsXml += """&lt;container memento=&quot;&amp;lt;?xml version=&amp;quot;1.0&amp;quot; encoding=&amp;quot;UTF-8&amp;quot; standalone=&amp;quot;no&amp;quot;?&amp;gt;&amp;#10;&amp;lt;archive detectRoot=&amp;quot;true&amp;quot; path=&amp;quot;${path}&amp;quot;/&amp;gt;&amp;#10;&quot; typeId=&quot;org.eclipse.debug.core.containerType.externalArchive&quot;/&gt;&#10;""" }

        def launchConfig1 =
            project.file('bin/MIS Scheduler - Start.launch')

        launchConfig1.text = """\
               <?xml version="1.0" encoding="UTF-8" standalone="no"?>
               <launchConfiguration type="org.eclipse.jdt.launching.localJavaApplication">
               <listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_PATHS">
               <listEntry value="/mis-scheduler"/>
               </listAttribute>
               <listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_TYPES">
               <listEntry value="4"/>
               </listAttribute>
               <booleanAttribute key="org.eclipse.jdt.launching.ATTR_USE_START_ON_FIRST_THREAD" value="true"/>
               <stringAttribute key="org.eclipse.debug.core.source_locator_id" value="org.eclipse.jdt.launching.sourceLocator.JavaSourceLookupDirector"/>
               <stringAttribute key="org.eclipse.debug.core.source_locator_memento" value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#10;&lt;sourceLookupDirector&gt;&#10;&lt;sourceContainers duplicates=&quot;false&quot;&gt;&#10;${sourcePathsXml}&lt;container memento=&quot;&amp;lt;?xml version=&amp;quot;1.0&amp;quot; encoding=&amp;quot;UTF-8&amp;quot; standalone=&amp;quot;no&amp;quot;?&amp;gt;&amp;#10;&amp;lt;javaProject name=&amp;quot;mis-scheduler&amp;quot;/&amp;gt;&amp;#10;&quot; typeId=&quot;org.eclipse.jdt.launching.sourceContainer.javaProject&quot;/&gt;&#10;&lt;container memento=&quot;&amp;lt;?xml version=&amp;quot;1.0&amp;quot; encoding=&amp;quot;UTF-8&amp;quot; standalone=&amp;quot;no&amp;quot;?&amp;gt;&amp;#10;&amp;lt;default/&amp;gt;&amp;#10;&quot; typeId=&quot;org.eclipse.debug.core.containerType.default&quot;/&gt;&#10;&lt;/sourceContainers&gt;&#10;&lt;/sourceLookupDirector&gt;&#10;"/>
               <listAttribute key="org.eclipse.jdt.launching.CLASSPATH">
               <listEntry value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#10;&lt;runtimeClasspathEntry containerPath=&quot;org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.8/&quot; javaProject=&quot;mis-scheduler&quot; path=&quot;1&quot; type=&quot;4&quot;/&gt;&#10;"/>
               <listEntry value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#10;&lt;runtimeClasspathEntry internalArchive=&quot;/mis-scheduler/lib/homonculus-bootstrap-1.0.jar&quot; path=&quot;3&quot; type=&quot;2&quot;/&gt;&#10;"/>
               </listAttribute>
               <booleanAttribute key="org.eclipse.jdt.launching.DEFAULT_CLASSPATH" value="false"/>
               <stringAttribute key="org.eclipse.jdt.launching.MAIN_TYPE" value="org.devoware.homonculus.bootstrap.Bootstrap"/>
               <stringAttribute key="org.eclipse.jdt.launching.PROGRAM_ARGUMENTS" value="start mis-scheduler.yml"/>
               <stringAttribute key="org.eclipse.jdt.launching.PROJECT_ATTR" value="mis-scheduler"/>
               <stringAttribute key="org.eclipse.jdt.launching.VM_ARGUMENTS" value="-Dbootstrap.class=${project.homonculus.appClass} -Duser.timezone=GMT -Dlog.dir=../logs -Dlog.extra.appender=FILE"/>
               <stringAttribute key="org.eclipse.jdt.launching.WORKING_DIRECTORY" value="\${workspace_loc:mis-scheduler/bin}"/>
               </launchConfiguration>
          """.stripIndent()

        def launchConfig2 =
            project.file('bin/MIS Scheduler - Stop.launch')

        launchConfig2.text = """\
               <?xml version="1.0" encoding="UTF-8" standalone="no"?>
               <launchConfiguration type="org.eclipse.jdt.launching.localJavaApplication">
               <listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_PATHS">
               <listEntry value="/mis-scheduler"/>
               </listAttribute>
               <listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_TYPES">
               <listEntry value="4"/>
               </listAttribute>
               <booleanAttribute key="org.eclipse.jdt.launching.ATTR_USE_START_ON_FIRST_THREAD" value="true"/>
               <stringAttribute key="org.eclipse.debug.core.source_locator_id" value="org.eclipse.jdt.launching.sourceLocator.JavaSourceLookupDirector"/>
               <stringAttribute key="org.eclipse.debug.core.source_locator_memento" value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#10;&lt;sourceLookupDirector&gt;&#10;&lt;sourceContainers duplicates=&quot;false&quot;&gt;&#10;${sourcePathsXml}&lt;container memento=&quot;&amp;lt;?xml version=&amp;quot;1.0&amp;quot; encoding=&amp;quot;UTF-8&amp;quot; standalone=&amp;quot;no&amp;quot;?&amp;gt;&amp;#10;&amp;lt;javaProject name=&amp;quot;mis-scheduler&amp;quot;/&amp;gt;&amp;#10;&quot; typeId=&quot;org.eclipse.jdt.launching.sourceContainer.javaProject&quot;/&gt;&#10;&lt;container memento=&quot;&amp;lt;?xml version=&amp;quot;1.0&amp;quot; encoding=&amp;quot;UTF-8&amp;quot; standalone=&amp;quot;no&amp;quot;?&amp;gt;&amp;#10;&amp;lt;default/&amp;gt;&amp;#10;&quot; typeId=&quot;org.eclipse.debug.core.containerType.default&quot;/&gt;&#10;&lt;/sourceContainers&gt;&#10;&lt;/sourceLookupDirector&gt;&#10;"/>
               <listAttribute key="org.eclipse.jdt.launching.CLASSPATH">
               <listEntry value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#10;&lt;runtimeClasspathEntry containerPath=&quot;org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.8/&quot; javaProject=&quot;mis-scheduler&quot; path=&quot;1&quot; type=&quot;4&quot;/&gt;&#10;"/>
               <listEntry value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#10;&lt;runtimeClasspathEntry internalArchive=&quot;/mis-scheduler/lib/homonculus-bootstrap-1.0.jar&quot; path=&quot;3&quot; type=&quot;2&quot;/&gt;&#10;"/>
               </listAttribute>
               <booleanAttribute key="org.eclipse.jdt.launching.DEFAULT_CLASSPATH" value="false"/>
               <stringAttribute key="org.eclipse.jdt.launching.MAIN_TYPE" value="org.devoware.homonculus.bootstrap.Bootstrap"/>
               <stringAttribute key="org.eclipse.jdt.launching.PROGRAM_ARGUMENTS" value="stop"/>
               <stringAttribute key="org.eclipse.jdt.launching.PROJECT_ATTR" value="mis-scheduler"/>
               <stringAttribute key="org.eclipse.jdt.launching.VM_ARGUMENTS" value="-Dbootstrap.class=${project.homonculus.appClass} -Duser.timezone=GMT -Dlog.dir=../logs -Dlog.extra.appender=FILE"/>
               <stringAttribute key="org.eclipse.jdt.launching.WORKING_DIRECTORY" value="\${workspace_loc:mis-scheduler/bin}"/>
               </launchConfiguration>
          """.stripIndent()
      }
    }

    project.tasks.eclipseJdt {
      doFirst {
        def aptPrefs =
            project.file('.settings/org.eclipse.jdt.apt.core.prefs')
        aptPrefs.parentFile.mkdirs()

        aptPrefs.text = """\
          eclipse.preferences.version=1
          org.eclipse.jdt.apt.aptEnabled=true
          org.eclipse.jdt.apt.genSrcDir=.apt_generated
          org.eclipse.jdt.apt.reconcileEnabled=true
          """.stripIndent()

        project.file('.factorypath').withWriter {
          new groovy.xml.MarkupBuilder(it).'factorypath' {
            FileCollection libs = (project.configurations.compile + project.configurations.compileOnly).filter {lib ->
              lib.name.startsWith('dagger') ||
                  lib.name.startsWith('guava') ||
                  lib.name.startsWith('javax.inject')
            }
            libs.each {lib ->
              factorypathentry(
                  kind: 'EXTJAR',
                  id: lib.absolutePath,
                  enabled: true,
                  runInBatchMode: false
                  )
            }
          }
        }
      }
    }

    project.task('createPackage', dependsOn: project.build) {
      doLast {
        File dist = mkdir("${project.buildDir}/dist")
        File tmp = mkdir("${project.buildDir}/tmp/dist/${project.name}-${project.version}")
        tasks.withType(Jar).each { archiveTask ->
          copy {
            from archiveTask.archivePath
            into project.file("${tmp}/lib")
          }
        }
        copy {
          from configurations.compile, configurations.runtime
          into project.file("${tmp}/lib")
        }
        copy {
          from 'bin'
          into file("${tmp}/bin")
          exclude { details ->
            details.file.name.endsWith('.launch')
          }
        }
        copy {
          from 'config'
          into project.file("${tmp}/config")
        }
        mkdir("${tmp}/logs")
      }
    }

    project.task('archive', dependsOn: project.createPackage, type: Tar) {
      File dist = project.file("${project.buildDir}/dist")
      File tmp = project.file("${project.buildDir}/tmp/dist")

      baseName = "${project.name}-${project.version}"
      destinationDir = dist
      from tmp
      compression = Compression.GZIP
    }

    project.tasks.clean.dependsOn(project.deleteLibraries)
    project.tasks.cleanEclipse.dependsOn(project.deleteLibraries)
    project.tasks.build.dependsOn(project.copyLibraries)
    project.tasks.eclipse.dependsOn(project.copyLibraries)
  }
}
