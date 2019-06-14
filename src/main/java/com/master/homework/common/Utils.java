package com.master.homework.common;

public class Utils {

  /**
   * 参数1是否为参数2的子类或接口实现
   *
   * @param parentCls
   * @return
   */
  public static boolean isInstanceof(Class<?> cls, Class<?> parentCls) {
    return parentCls.isAssignableFrom(cls);
  }

}
