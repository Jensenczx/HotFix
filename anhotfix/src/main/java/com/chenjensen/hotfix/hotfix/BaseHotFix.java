/*
 * This source code is licensed under the MIT-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.chenjensen.hotfix.hotfix;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * HotFix Base Class Define some base method for searching
 * field, method and expand the array.HTHotFix
 */
public class BaseHotFix {


    /**
     *
     * @param instance
     * @param name
     * @return
     * @throws NoSuchFieldException
     */
    public static Field findField(Object instance, String name) throws NoSuchFieldException{
        for(Class<?> clazz = instance.getClass(); clazz != null; clazz.getSuperclass()) {

            Field field = clazz.getDeclaredField(name);

            if(!field.isAccessible()) {
                field.setAccessible(true);
            }

            return field;
        }
        throw new NoSuchFieldException("Field:" + name + "not found" + "in" + instance.getClass());
    }


    /**
     *
     * @param instance
     * @param name
     * @param parameterTypes
     * @return
     * @throws NoSuchMethodException
     */
    public static Method findMethod(Object instance, String name, Class<?> ... parameterTypes)
            throws NoSuchMethodException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {

            Method method = clazz.getDeclaredMethod(name, parameterTypes);

            if(!method.isAccessible()) {
                method.setAccessible(true);
            }

            return method;
        }

        throw new NoSuchMethodException("Method" + name + "with parameters" + Arrays.asList(parameterTypes)
                + "not found in" + instance.getClass());
    }

    /**
     *
     * @param instance
     * @param filedName
     * @param extraElements
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void expandFieldArray(Object instance, String filedName, Object[] extraElements) throws
            NoSuchFieldException, IllegalAccessException, IllegalArgumentException{
        Field field = findField(instance, filedName);

        Object[] original = (Object[]) field.get(instance);
        Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(),
                original.length + extraElements.length);

        System.arraycopy(extraElements, 0, combined, 0, extraElements.length);
        System.arraycopy(original, 0, combined, extraElements.length, original.length);

        field.set(instance, combined);
    }
}
