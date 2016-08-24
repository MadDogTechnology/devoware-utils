package org.devoware.homonculus.bootstrap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.devoware.homonculus.bootstrap.Bootstrap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class BootstrapTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  private File srcFolder;
  private File classesFolder;
  private File libFolder;
  private File configFolder;

  @Before
  public void setup() throws IOException {
    srcFolder = temp.newFolder("src");
    classesFolder = temp.newFolder("classes");
    libFolder = temp.newFolder("lib");
    configFolder = temp.newFolder("config");

    File sourceFile = generateSourceCode();
    compileSourceCode(sourceFile);
    System.setProperty(Bootstrap.APPLICATION_HOME, temp.getRoot().getAbsolutePath());
    System.setProperty(Bootstrap.APPLICATION_CLASS, "org.devoware.bootstrap.test.MockApplication");
  }

  @Test
  public void test_application_start_loaded_from_classes_directory()
      throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    String[] args = new String[] {"start", "hello-world.yml"};
    MethodCalls calls = executeBootstrap(args);
    assertThat(calls.initCalled, equalTo(true));
    assertThat(calls.startCalled, equalTo(true));
    assertThat(calls.stopCalled, equalTo(false));
    assertThat(calls.args[0], equalTo("start"));
    assertThat(calls.args[1], equalTo("hello-world.yml"));
  }

  @Test
  public void test_application_stop_loaded_from_classes_directory()
      throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    String[] args = new String[] {"stop"};
    MethodCalls calls = executeBootstrap(args);
    assertThat(calls.initCalled, equalTo(false));
    assertThat(calls.startCalled, equalTo(false));
    assertThat(calls.stopCalled, equalTo(true));
  }

  @Test
  public void test_default_command_is_start()
      throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    String[] args = new String[] {};
    MethodCalls calls = executeBootstrap(args);
    assertThat(calls.initCalled, equalTo(true));
    assertThat(calls.startCalled, equalTo(true));
    assertThat(calls.stopCalled, equalTo(false));
  }

  @Test
  public void test_default_home_directory_set()
      throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    System.clearProperty(Bootstrap.APPLICATION_HOME);
    String[] args = new String[] {"start"};
    Bootstrap.main(args);
    File expectedHome = new File(System.getProperty("user.dir") + "/..");
    File actualHome = new File(System.getProperty(Bootstrap.APPLICATION_HOME));
    assertThat(actualHome.getCanonicalPath(), equalTo(expectedHome.getCanonicalPath()));
  }

  @Test
  public void test_application_start_loaded_from_config_directory()
      throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    copyDirectory(classesFolder, configFolder);
    cleanClassesFolder(); // We don't want the class loaded from the classes folder!
    String[] args = new String[] {"start"};
    MethodCalls calls = executeBootstrap(args);
    assertThat(calls.initCalled, equalTo(true));
    assertThat(calls.startCalled, equalTo(true));
    assertThat(calls.stopCalled, equalTo(false));
  }

  @Test
  public void test_application_stop_loaded_from_config_directory()
      throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    copyDirectory(classesFolder, configFolder);
    cleanClassesFolder(); // We don't want the class loaded from the classes folder!
    String[] args = new String[] {"stop"};
    MethodCalls calls = executeBootstrap(args);
    assertThat(calls.initCalled, equalTo(false));
    assertThat(calls.startCalled, equalTo(false));
    assertThat(calls.stopCalled, equalTo(true));
  }

  @Test
  public void test_application_start_loaded_from_lib_directory_as_jar()
      throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    generateJar();
    cleanClassesFolder(); // We don't want the class loaded from the classes folder!
    String[] args = new String[] {"start"};
    MethodCalls calls = executeBootstrap(args);
    assertThat(calls.initCalled, equalTo(true));
    assertThat(calls.startCalled, equalTo(true));
    assertThat(calls.stopCalled, equalTo(false));
  }

  @Test
  public void test_application_stop_loaded_from_lib_directory_as_jar()
      throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    generateJar();
    cleanClassesFolder(); // We don't want the class loaded from the classes folder!
    String[] args = new String[] {"stop"};
    MethodCalls calls = executeBootstrap(args);
    assertThat(calls.initCalled, equalTo(false));
    assertThat(calls.startCalled, equalTo(false));
    assertThat(calls.stopCalled, equalTo(true));
  }

  private void cleanClassesFolder() throws IOException {
    for (File f : classesFolder.listFiles()) {
      if (f.isFile()) {
        if (!f.delete()) {
          throw new IOException(
              "A problem occurred while attempting to delete file " + f.getAbsolutePath());
        }
        continue;
      }
      Path directory = f.toPath();
      Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          Files.delete(file);
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
          Files.delete(dir);
          return FileVisitResult.CONTINUE;
        }
      });
    }
  }

  private void generateJar() throws IOException, FileNotFoundException {
    Manifest manifest = new Manifest();
    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    JarOutputStream target =
        new JarOutputStream(new FileOutputStream(new File(libFolder, "output.jar")), manifest);
    addToJar(classesFolder, target);
    target.close();
  }

  private void addToJar(File source, JarOutputStream target) throws IOException {
    String classesPath = classesFolder.getPath().replace("\\", "/");
    if (source.isDirectory()) {
      String name = source.getPath().replace("\\", "/").substring(classesPath.length());
      if (!name.isEmpty()) {
        name = name.substring(1); // remove the forward slash from the beginning of the name.
        if (!name.endsWith("/"))
          name += "/";
        JarEntry entry = new JarEntry(name);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
        target.closeEntry();
      }
      for (File nestedFile : source.listFiles())
        addToJar(nestedFile, target);
      return;
    }

    JarEntry entry =
        new JarEntry(source.getPath().replace("\\", "/").substring(classesPath.length() + 1));
    entry.setTime(source.lastModified());
    target.putNextEntry(entry);

    try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(source))) {

      byte[] buffer = new byte[1024];
      while (true) {
        int count = in.read(buffer);
        if (count == -1)
          break;
        target.write(buffer, 0, count);
      }
      target.closeEntry();
    }
  }

  private MethodCalls executeBootstrap(String[] args)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Bootstrap.main(args);
    Bootstrap bootstrap = Bootstrap.getInstance();
    assertNotNull(bootstrap);
    Object application = bootstrap.getApplication();
    assertThat(application.getClass().getName(),
        equalTo("org.devoware.bootstrap.test.MockApplication"));
    MethodCalls calls = determineMethodCalls(application);
    return calls;
  }

  private MethodCalls determineMethodCalls(Object application)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    MethodCalls calls = new MethodCalls();
    Method method = application.getClass().getMethod("isInitCalled", (Class[]) null);
    calls.initCalled = (boolean) method.invoke(application, (Object[]) null);
    method = application.getClass().getMethod("isStartCalled", (Class[]) null);
    calls.startCalled = (boolean) method.invoke(application, (Object[]) null);
    method = application.getClass().getMethod("isStopCalled", (Class[]) null);
    calls.stopCalled = (boolean) method.invoke(application, (Object[]) null);
    method = application.getClass().getMethod("getArgs", (Class[]) null);
    calls.args = (String []) method.invoke(application, (Object[]) null);
    return calls;
  }

  private void compileSourceCode(File sourceFile) throws IOException {
    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    final StandardJavaFileManager manager =
        compiler.getStandardFileManager(diagnostics, null, null);
    final Iterable<? extends JavaFileObject> sources =
        manager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));

    final CompilationTask task = compiler.getTask(null, manager, diagnostics,
        Arrays.asList(new String[] {"-d", classesFolder.getAbsolutePath()}), null, sources);
    if (!task.call()) {
      throw new IOException(
          "An error occurred while attempting to compile the MockApplication class");
    }
  }

  /**
   * Load the MockApplication.src file and write its contents to the temporary src folder so that it
   * can be compiled.
   * 
   * @throws IOException
   */
  private File generateSourceCode() throws IOException {
    File packageFolder = createPackageDirectory();
    String classSource = getClassSource();
    return createSourceFile(packageFolder, classSource);
  }

  private File createPackageDirectory() throws IOException {
    File packageFolder = new File(srcFolder, "org/devoware/bootstrap/test");
    if (!packageFolder.mkdirs()) {
      throw new IOException(
          "Could not create the package directory " + packageFolder.getAbsolutePath());
    }
    return packageFolder;
  }

  private File createSourceFile(File packageFolder, String classSource) throws IOException {
    File sourceFile = new File(packageFolder, "MockApplication.java");
    System.out.println(sourceFile.getAbsolutePath());
    try (PrintWriter out = new PrintWriter(new FileWriter(sourceFile))) {
      out.print(classSource);
    }
    return sourceFile;
  }

  private String getClassSource() throws IOException {
    String classSource;
    try (
        InputStream in = BootstrapTest.class.getClassLoader()
            .getResourceAsStream("org/devoware/bootstrap/MockApplication.src");
        Scanner s = new Scanner(in)) {
      s.useDelimiter("\\A");
      classSource = s.hasNext() ? s.next() : "";
      s.close();
    }
    return classSource;
  }

  private void copy(File sourceLocation, File targetLocation) throws IOException {
    if (sourceLocation.isDirectory()) {
      copyDirectory(sourceLocation, targetLocation);
    } else {
      copyFile(sourceLocation, targetLocation);
    }
  }

  private void copyDirectory(File source, File target) throws IOException {
    if (!target.exists()) {
      target.mkdir();
    }

    for (String f : source.list()) {
      copy(new File(source, f), new File(target, f));
    }
  }

  private void copyFile(File source, File target) throws IOException {
    try (InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(target)) {
      byte[] buf = new byte[1024];
      int length;
      while ((length = in.read(buf)) > 0) {
        out.write(buf, 0, length);
      }
    }
  }


  private static class MethodCalls {
    private boolean initCalled;
    private boolean startCalled;
    private boolean stopCalled;
    private String [] args;

  }
}
