package com.yesmind.stok.core.port.in.action;

import org.aspectj.lang.ProceedingJoinPoint;

public interface IActionAspect {

    Object trackSaveAction(ProceedingJoinPoint joinPoint) throws Throwable;
}