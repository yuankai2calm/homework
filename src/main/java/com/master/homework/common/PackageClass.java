package com.master.homework.common;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageClass {

  /**
   * 读取包类所有类，获取class对象，并根据指定的条件过滤
   * @param pname
   * @return
   */
  public static Set<Class<?>> getPackegeClasses(String pname) {
    Set<Class<?>> classes = new HashSet<>();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    String packageDirName = pname.replace(".", "/");

    try {
      Enumeration<URL> dirs = cl.getResources(packageDirName);
      while (dirs.hasMoreElements()) {
        URL url = dirs.nextElement();
        String protocol = url.getProtocol();
        if("file".equals(protocol)) {
            findByFile(cl, pname, URLDecoder.decode(url.getFile(), "utf-8"), classes);
        } else if("jar".equals(protocol)) {
          findInJar(cl, pname, packageDirName, url, classes);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }


    return classes;
  }

  /**
   * 从文件获取java类
   * @param cl
   * @param packageName
   * @param filePath
   * @param classes
   */
  private static void findByFile(ClassLoader cl, String packageName, String filePath, Set<Class<?>> classes) {
    File dir = new File(filePath);
    if(!dir.exists() || !dir.isDirectory()) {
      return;
    }

    File[] dirFiles = dir.listFiles(file -> file.isDirectory() || file.getAbsolutePath().endsWith(".class"));

    for (File file: dirFiles) {
      if(file.isDirectory()) {
        findByFile(cl, packageName + "." + file.getName(), file.getAbsolutePath(), classes);
      } else {
        String className = packageName + "."
          + file.getName().substring(0, file.getName().length() - 6);
        Class<?> clazz = null;
        try {
          clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
        classes.add(clazz);
      }
    }
  }

  /**
   * 读取jar中的java类
   * @param cl
   * @param pname
   * @param packageDirName
   * @param url
   * @param classes
   */
  private static void findInJar(ClassLoader cl, String pname, String packageDirName, URL url, Set<Class<?>> classes) {
    try {
      JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
      Enumeration<JarEntry> entries = jar.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (entry.isDirectory())
          continue;

        String name = entry.getName();

        if (name.charAt(0) == '/')
          name = name.substring(0);

        if (name.startsWith(packageDirName) && name.contains("/") && name.endsWith(".class")) {
          name = name.substring(0, name.length() - 6).replace('/', '.');
          try {
            Class<?> clazz = cl.loadClass(name);

            classes.add(clazz);
          } catch (Throwable e) {
            System.out.println("无法直接加载的类：" + name);
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
