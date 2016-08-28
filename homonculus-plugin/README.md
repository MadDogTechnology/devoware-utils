# homonculus-plugin
A gradle plugin which encapsulates a lot of the boilerplate code needed to perform various build-related tasks for an application built with the **homonculus** framework.

To use the **homonculus-plugin** within your application, add the following lines to your ```build.gradle``` file, setting the appropriate values for the ```appName``` and ```appClass``` properties:

```groovy
buildscript {
    repositories {
        url "https://github.com/cpdevoto/maven-repository/raw/master/"
    }
    dependencies {
        classpath group: 'org.devoware', name: 'homonculus-plugin',
				  version: '1.0'
    }
}
apply plugin: 'com.doradosystems.homonculus'

homonculus {
  appName = 'MIS Scheduler'
  appClass = 'com.doradosystems.mis.scheduler.MisSchedulerApplication'
}
```
