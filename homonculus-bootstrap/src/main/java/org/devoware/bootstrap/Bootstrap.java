package org.devoware.bootstrap;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Bootstrap {

  public static final String APPLICATION_CLASS = "bootstrap.class";
  public static final String APPLICATION_HOME = "bootstrap.home";
  
  private static Bootstrap INSTANCE;
  private Object application;
  private ClassLoader applicationClassLoader;
  private String appHome = "..";


  public static void main(String[] args) {
    Bootstrap bootstrap = new Bootstrap();
    INSTANCE = bootstrap;
    bootstrap.execute(args);
  }

  public static Bootstrap getInstance() {
    return INSTANCE;
  }


  public Bootstrap() {}

  public void execute(String[] args) {
    System.out.println("Starting the bootstrap sequence.");
    try {
      String command = "start";
      if (args.length > 0) {
        command = args[0];
      }
      if (command.equals("start")) {
        try {
          init(args);
        } catch (Throwable t) {
          t.printStackTrace();
          return;
        }
        start(args);
      } else if (command.equals("stop")) {
        stop(args);
      } else {
        System.err.println("ERROR: Unrecognized command '" + command + "'.");
        return;
      }
      System.out.println("The bootstrap sequence completed successfully.");
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public void init(String [] args) throws Exception {
    init(args, true);
  }

  @SuppressWarnings("rawtypes")
  public void init(String [] args, boolean callEngineInit) throws Exception {
    applicationClassLoader = getClassLoader();
    String appClassName = System.getProperty(APPLICATION_CLASS);
    if (appClassName == null) {
      System.err.println(String.format("ERROR: You must specify a value for the '%s' JVM property.",
          APPLICATION_CLASS));
    }
    System.out.printf("Using %s: %s%n", APPLICATION_CLASS, appClassName);
    Thread.currentThread().setContextClassLoader(applicationClassLoader);
    Class appClass = applicationClassLoader.loadClass(appClassName);
    application = appClass.newInstance();
    if (callEngineInit) {
      Method method = application.getClass().getMethod("appInit", new Class [] { String[].class } );
      method.invoke(application, new Object [] { args });
    }
  }


  public void start(String [] args) throws Exception {
    if (application == null) {
      init(args);
    }

    Method method = application.getClass().getMethod("appStart", (Class[]) null);
    method.invoke(application, (Object[]) null);
  }


  public void stop(String [] args) throws Exception {
    if (application == null) {
      init(args, false);
    }

    Method method = application.getClass().getMethod("appStop", (Class[]) null);
    method.invoke(application, (Object[]) null);
  }


  public ClassLoader getClassLoader() throws Exception {
    String baseDir = System.getProperty(APPLICATION_HOME);
    if (baseDir != null) {
      appHome = baseDir;
    } else {
      System.setProperty(APPLICATION_HOME, new File(appHome).getCanonicalPath());
    }

    System.out.printf("Using %s: %s%n", APPLICATION_HOME, appHome);
    if (applicationClassLoader == null) {
      applicationClassLoader = createClassLoader();
    }
    return applicationClassLoader;
  }

  public Object getApplication() {
    return application;
  }


  private ClassLoader createClassLoader() throws Exception {
    List<File> classpathFiles = new ArrayList<File>();
    addJarFiles(classpathFiles);
    addConfigFiles(classpathFiles);
    addClassesFiles(classpathFiles);

    URL[] urls = convertFilesToUrls(classpathFiles);
    ClassLoader classLoader = new URLClassLoader(urls);
    return classLoader;
  }


  private void addClassesFiles(List<File> classpathFiles) {
    File classesDirectory = new File(appHome + "/classes");
    System.out.println(
        "Searching for files within the '" + classesDirectory.getAbsolutePath() + "' directory...");
    if (classesDirectory.exists() && classesDirectory.isDirectory()) {
      classpathFiles.add(classesDirectory);
      System.out.println("The 'classes' directory was found.");
    } else {
      System.out.println("The 'classes' directory was not found.");
    }
  }


  private void addConfigFiles(List<File> classpathFiles) {
    File configDirectory = new File(appHome + "/config");
    System.out.println("Searching for configuration files within the '"
        + configDirectory.getAbsolutePath() + "' directory...");
    if (configDirectory.exists() && configDirectory.isDirectory()) {
      classpathFiles.add(configDirectory);
      System.out.println("The 'config' directory was found.");
    } else {
      System.out.println("The 'config' directory was not found.");
    }
  }


  private void addJarFiles(List<File> classpathFiles) {
    File libDirectory = new File(appHome + "/lib");
    System.out.println(
        "Searching for JAR files within the '" + libDirectory.getAbsolutePath() + "' directory...");
    if (libDirectory.exists() && libDirectory.isDirectory()) {
      System.out.println("The 'lib' directory was found.");
      addJarsFromDir(classpathFiles, libDirectory);
    } else {
      System.out.println("The 'lib' directory was not found.");
    }
  }


  private void addJarsFromDir(List<File> classpathFiles, File directory) {
    File[] jarFiles = directory.listFiles(new FilenameFilter() {

      @Override
      public boolean accept(File file, String name) {
        try {
          String extension = name.substring(name.lastIndexOf(".") + 1);
          if ("jar".equals(extension)) {
            return true;
          }
        } catch (IndexOutOfBoundsException ex) {
        }
        return false;
      }
    });
    for (File jarFile : jarFiles) {
      System.out.println("    Including file " + jarFile.getName() + " in the classpath.");
      classpathFiles.add(jarFile);
    }
  }


  private URL[] convertFilesToUrls(List<File> classpathFiles) {
    URL[] urls = new URL[classpathFiles.size()];
    for (int i = 0; i < urls.length; i++) {
      urls[i] = convertFileToUrl(classpathFiles.get(i));
    }
    return urls;
  }


  private URL convertFileToUrl(File file) {
    URL url;
    try {
      url = file.toURI().toURL();
    } catch (MalformedURLException e) {
      // Rethrow this as an unchecked exception, since it should really never happen!
      throw new RuntimeException(e);
    }
    return url;
  }

}
