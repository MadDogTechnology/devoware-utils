# bootstrap
**bootstrap** is a small library which provides base functionality for creating Java stand-alone applications. In a particular, **bootstrap**
uses a custom classloader to load resources from known directories, and it also introduces a basic lifecycle model for implementing
applications with graceful shutdown semantics. 

## Project directory structure
All applications created with the **bootstrap** are presumed to have a common directory structure as shown below:

```
project-directory
  |__bin
  |__classes
  |__config
  |__lib
```

### bin
The working directory from which the application will be launched.  The **bootstrap** libraries uses paths that are relative to the 
**bin** directory in order to load resources.

### classes
A directory containing Java .class files organized into subdirectories reflecting the package structure of the application.  The 
**bootstrap** library uses a custom class loader to ensure that all files placed in this directory are included in the application
classpath.  In a production application, this directory will generally not be used, since all of the .class files for the application
will be stored in a JAR file that is placed in the **lib** directory.

### config
A directory containing application-specific configuration files. The**bootstrap** library uses a custom class loader to ensure that all
files placed in this directory are included in the application classpath.  This is where you would place your log4j.properties file or 
your logback.xml file, for instance.

### lib
A directory containing all of the JAR files that your application depends on, including the **bootstrap-1.0.jar** itself. The 
**bootstrap** library uses a custom class loader to ensure that all files placed in this directory are included in the application
classpath.

## Using bootstrap
To use the **bootstrap** library, create an application directory structure as described above, ensuring that the **bootstrap-1.0.jar** file
is in the **lib** directory, and also ensuring that your application classes are either stored in a JAR file within the **lib** directory, 
or placed in raw format within the **classes** directory.  Your application will need to include at least one class, referred to henceforth
as the *application class* with all of the following methods:

```
public final void appInit(String[] args) throws Exception
public final void appStart() throws Exception
public final void appStop() throws Exception
```

### appInit
This method will be called when the bootstrap application is launched with the **start** argument. The **args** parameter is the set of all command-line 
arguments passed to the JVM when the application was launched. Use this method to perform all initialization logic for your application.

### appStart
This method will be called right after the **appInit** method is invoked when the bootstrap application is launched with the **start** argument.
Use this method to kick off any enduring subcomponents of your application.  For instance, if you are using the Quartz engine or a scheduled
executor service in order to schedule recurring jobs, this is where you would invoke the start() method for those subcomponents.

### appStop
This method will be called when the bootstrap application is launched with the **stop** argument. Use this method to trigger the graceful
termination of your application.  In a typical implementation, the **appInit** method will create a server socket that listens for a termination signal
while the **appStop** method will create a client socket to transmit said termination signal.

## Starting a bootstrap application
Suppose you have created an application class named ```com.doradosystems.mis.scheduler.MisSchedulerApplication```. To start this application, 
navigate to the **bin** directory and execute the following command:

```
$JAVA_HOME/java -cp ../lib/bootstrap-1.0.jar -Dbootstrap.class=com.doradosystems.mis.scheduler.MisSchedulerApplication org.devoware.bootstrap.Bootstrap start
```

## Stopping a bootstrap application
To stop the application described above,navigate to the **bin** directory and execute the following command:

```
$JAVA_HOME/java -cp ../lib/bootstrap-1.0.jar -Dbootstrap.class=com.doradosystems.mis.scheduler.MisSchedulerApplication org.devoware.bootstrap.Bootstrap stop
```



