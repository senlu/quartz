package com.quartz.quartz.utils;

import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 * @author
 * @description
 * @data2019/12/20
 */
public class MethodUtil
{
    /**
     * 调用对象方法
     * 通过反射机制，根据对象的方法名、参数个数获取方法对象。然后将字符串参数按照参数类型进行转型。
     * 然后调用对象方法。当前仅仅支持包装类型和int、long两种基本类型。该方法不支持重载函数，即同
     * 一个函数名称，对应相同数量的不同类型的参数。该方法会自动选择第一个方法进行调用。
     *
     * @param obj        被调用对象
     * @param clazz      被调用对象类型
     * @param methodName 被调用方法名称
     * @param parameters 传递参数数组
     * @throws InvocationTargetException 方法调用失败，抛出该异常。该异常实际是FastMethod.invoke()抛出异常，而这一层不做处理。
     * @throws NoSuchMethodException     如果该类型中，没有指定的方法名并且对应指定参数数量的方法；指定方法名和参数的方法被重载过。
     *                                   则抛出该异常。
     */
    @SuppressWarnings("unchecked")
    public static Object invokeMethod(Object obj, Class clazz,
        String methodName, String[] parameters)
        throws InvocationTargetException, NoSuchMethodException
    {
        Method[] methods = clazz.getMethods();

        Method targetMethod = null;
        // 便利方法，获取一个方法参数数量与传递的参数数量一样的Method
        for (Method method : methods)
        {
            // 如果参数数量对应相同，则开始转换类型，并且调用方法
            if (method.getName().equals(methodName)
                && method.getParameterTypes().length == parameters.length)
            {
                if (null == targetMethod)
                {
                    targetMethod = method;
                }
                else
                {
                    throw new NoSuchMethodException(
                        clazz + "." + methodName + "() with "
                            + parameters.length
                            + " parameters  must be a unique method.");
                }
            }
        }

        // 判断目标方法是否存在，如果不存在，则抛出异常
        if (null == targetMethod)
        {
            throw new NoSuchMethodException(
                clazz + "." + methodName + "() with " + parameters.length
                    + " parameters.");
        }

        // 获取对象方法的参数类型数组
        Class[] parameterTypes = targetMethod.getParameterTypes();
        Object[] parameterObjects = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++)
        {
            if (null != parameterTypes[i].getComponentType())
            {
                StringTokenizer st = new StringTokenizer(parameters[i], ",");
                String[] p = new String[st.countTokens()];
                int index = 0;
                while (st.hasMoreTokens())
                {
                    p[index++] = st.nextToken();
                }
                if (parameterTypes[i].getComponentType()
                    .getName()
                    .equals("int"))
                {
                    parameterObjects[i] = warpParameter_int(p);
                }
                else if (parameterTypes[i].getComponentType()
                    .getName()
                    .equals("long"))
                {
                    parameterObjects[i] = warpParameter_long(p);
                }
                else
                {
                    parameterObjects[i] =
                        warpParameterObject(parameterTypes[i].getComponentType(),
                            p);
                }
            }
            else
            {
                parameterObjects[i] =
                    warpParameterObject(parameterTypes[i], parameters[i]);
            }

        }

        return invokeFastMethod(obj, targetMethod, parameterObjects);
    }

    /**
     * 包装参数
     *
     * @param clazz
     * @param s
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Object warpParameterObject(Class clazz, String s)
    {
        final String clazzName = clazz.getName();

        // 首先判断基础数据类型
        if (clazzName.equals("int"))
        {
            return Integer.parseInt(s);
        }
        else if (clazzName.equals("long"))
        {
            return Long.parseLong(s);
        }
        else if (clazz.equals(Integer.class))
        {
            return Integer.valueOf(s);
        }
        else if (clazz.equals(Long.class))
        {
            return Long.valueOf(s);
        }
        else
        {
            return s;
        }
    }

    /**
     * 包装参数
     *
     * @param clazz
     * @param s
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] warpParameterObject(Class<T> clazz, String[] s)
    {
        T[] ret = (T[])java.lang.reflect.Array.newInstance(clazz, s.length);
        for (int i = 0; i < s.length; i++)
        {
            ret[i] = (T)warpParameterObject(clazz, s[i]);
        }
        return ret;
    }

    private static int[] warpParameter_int(String[] s)
    {
        int[] ret = new int[s.length];
        for (int i = 0; i < s.length; i++)
        {
            ret[i] = Integer.parseInt(s[i]);
        }
        return ret;
    }

    private static long[] warpParameter_long(String[] s)
    {
        long[] ret = new long[s.length];
        for (int i = 0; i < s.length; i++)
        {
            ret[i] = Integer.parseInt(s[i]);
        }
        return ret;
    }

    /**
     * 基于cglib的FastMethod来实现反射调用
     *
     * @param obj        对象
     * @param method     方法对象
     * @param parameters 参数
     * @throws InvocationTargetException
     */
    private static Object invokeFastMethod(Object obj, Method method,
        Object[] parameters)
        throws InvocationTargetException
    {
        FastClass fc = FastClass.create(obj.getClass());
        FastMethod fm = fc.getMethod(method);
        return fm.invoke(obj, parameters);
    }
}
