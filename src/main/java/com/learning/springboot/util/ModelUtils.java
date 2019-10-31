package com.learning.springboot.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ModelUtils {

    /**
     * 将源对象中需要传递的属性保留
     * 使用情境：mybatis逆向工程中对selectByExample返回的结果进行处理
     * @param source   源对象
     * @param attrs   目标传递的属性
     * @return
     */
    public static Object convert(Object source, String... attrs){

        if(attrs.length == 0){
            return source;
        }

        Class clazz = source.getClass();
        try {
            Constructor constructor = clazz.getConstructor();
            Object target = constructor.newInstance();

            for (String attr : attrs) {
                //调用源对象的目标getter方法
                String getterMethodName = toMethodName("get", attr);
                Method getterMethod = clazz.getMethod(getterMethodName);
                Object value = getterMethod.invoke(source);
                Class<?> returnType = getterMethod.getReturnType();

                //目标对象调用setter方法
                String setterMethodName = toMethodName("set", attr);
                Method setterMethod = clazz.getMethod(setterMethodName, returnType);
                setterMethod.invoke(target, value);
            }
            return target;
        }catch (Exception e){
            //此处抛出的异常未进行日志处理
            return source;
        }

    }

    private static String toMethodName(String method, String attr){
        String letter = attr.substring(0, 1).toUpperCase();
        String methodName = method +
                letter + attr.substring(1);
        return methodName;
    }

}
