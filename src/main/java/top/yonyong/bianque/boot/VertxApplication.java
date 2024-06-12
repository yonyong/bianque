package top.yonyong.bianque.boot;

import jdk.internal.org.objectweb.asm.ClassReader;
import lombok.extern.slf4j.Slf4j;
import top.yonyong.bianque.bianque.BianqueApplication;
import top.yonyong.bianque.boot.support.Ordered;
import top.yonyong.bianque.boot.support.VerticleManager;
import top.yonyong.bianque.boot.support.annotation.Order;
import top.yonyong.bianque.boot.support.annotation.Verticle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/5/2 16:55
 */
@Slf4j
public class VertxApplication {

  /**
   * candidate class who needs to be deployed to vertx
   */
  private static final Set<Class<?>> _CANDIDATES_SET = new HashSet<>();

  /**
   * sorted candidate class {@link VertxApplication#sortCandidates()}
   */
  private static TreeMap<Integer, List<Class<?>>> _SORTED_CANDIDATES_MAP;

  /**
   * scan all verticle {@link Verticle}
   *
   * @param tClass tClass
   */
  public static void run(Class<BianqueApplication> tClass) {
    if (!tClass.isAnnotationPresent(VertxBootApplication.class)) {
      log.error("The current class is not a class with annotation {@link top.yonyong.vertx.boot.VertxBootApplication}");
      return;
    }

    //scan package to choice target vertx candidates
    doScan(tClass);

    //sort candidates. Sort by Annotation{@see top.yonyong.vertx.boot.support.annotation.Order}
    sortCandidates();

    //deploy clazz verticle to vertx
    doRegister();
  }

  /**
   * scan package to choice target vertx candidates
   *
   * @param tClass main class
   */
  private static void doScan(Class<BianqueApplication> tClass) {
    final VertxBootApplication application = tClass.getAnnotation(VertxBootApplication.class);
    try {
      final String basePackage = application.basePackage().isEmpty() ? tClass.getPackage().getName() : application.basePackage();
      log.info("scan package:{}", basePackage);
      File file = new File(tClass.getProtectionDomain().getCodeSource().getLocation().getPath());
      log.info("source code path:{}", file.getPath());
      if (file.isDirectory()) {
        processDirectory(file, basePackage);
      } else if (file.getName().endsWith(".jar")) {
        processJar(file, basePackage);
      } else if (file.getName().endsWith(".class")) {
        processClass(file, basePackage);
      }
    } catch (Exception e) {
      log.error("start vertx application failed,e:", e);
    }
  }

  /**
   * process directory
   *
   * @param directory   directory
   * @param basePackage basePackage
   */
  private static void processDirectory(File directory, String basePackage) {
    File[] files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        processDirectory(file, basePackage);
      } else if (file.getName().endsWith(".class")) {
        processClass(file, basePackage);
      }
    }
  }

  /**
   * process jar from jarFile
   *
   * @param jar         jar
   * @param basePackage basePackage
   */
  private static void processJar(File jar, String basePackage) {
    String basePackagePath = basePackage.replace(".", "/");
    try (JarFile jarFile = new JarFile(jar)) {
      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (entry.getName().endsWith(".class")) {
          if (!entry.getName().contains(basePackagePath)) {
            continue;
          }
          if (entry.getName().contains("$")) {
            //匿名类过滤
            continue;
          }
          processClass(jarFile.getInputStream(entry), basePackage);
        }
      }
    } catch (IOException e) {
      log.error("process jar err:", e);
    }
  }

  /**
   * processClass from classFile
   *
   * @param classFile   classFile
   * @param basePackage basePackage
   */
  private static void processClass(File classFile, String basePackage) {
    String className = classFile.getPath()
      .replace(File.separator, ".")
      .replace(".class", "");
    processClass(className, basePackage);
  }

  /**
   * process class from classInputStream
   *
   * @param classInputStream classInputStream
   * @param basePackage      basePackage
   */
  private static void processClass(InputStream classInputStream, String basePackage) {
    try (InputStream is = classInputStream) {
      ClassReader reader = new ClassReader(is);
      String className = reader.getClassName();
      processClass(className.replace("/", "."), basePackage);
    } catch (IOException e) {
      //e.printStackTrace();
    }
  }

  /**
   * process class.This is the final process step.
   *
   * @param className   className
   * @param basePackage basePackage
   */
  private static void processClass(String className, String basePackage) {
    try {
      if (className.contains(basePackage)) {
        String finalClassName = className.substring(className.indexOf(basePackage));
        Class<?> clazz = Class.forName(finalClassName);
        if (clazz.isAnnotationPresent(Verticle.class)) {
          _CANDIDATES_SET.add(clazz);
        }
      }
    } catch (ClassNotFoundException e) {
      //e.printStackTrace();
    }
  }

  /**
   * sort candidates
   */
  private static void sortCandidates() {
    if (0 == _CANDIDATES_SET.size()) {
      throw new RuntimeException("start application fail : no candidate class found");
    }

    TreeMap<Integer, List<Class<?>>> collectMap = new TreeMap();
    _CANDIDATES_SET.forEach(clazz -> {
      int sort = clazz.isAnnotationPresent(Order.class) ? clazz.getAnnotation(Order.class).value() : Ordered.DEFAULT_VALUE;
      List<Class<?>> classes = collectMap.get(sort);
      if (null == classes || classes.isEmpty()) {
        classes = new ArrayList<>();
        collectMap.put(sort, classes);
      }
      classes.add(clazz);
    });
    //sort
    _SORTED_CANDIDATES_MAP = new TreeMap<>(collectMap);
  }

  /**
   * deploy clazz verticle to vertx
   */
  private static void doRegister() {
    _SORTED_CANDIDATES_MAP.values().forEach(value -> value.forEach(VerticleManager::deploy));
  }
}
