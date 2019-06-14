package com.master.homework.common;

@FunctionalInterface
public interface Handler<E, T> {

  void handle(E event, T body);
}
