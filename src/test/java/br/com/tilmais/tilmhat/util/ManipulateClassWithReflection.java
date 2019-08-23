package br.com.tilmais.tilmhat.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManipulateClassWithReflection {

    public static List<Method> getAllGettersPublic(Class<?> classToHandler) {
        return Arrays.stream(classToHandler.getDeclaredMethods())
                .filter(method -> method.getModifiers() == Modifier.PUBLIC)
                .collect(Collectors.toList());
    }

    public static List<Object> getAllValuesFromMethods(List<Method> methods, Object objectWithMethodToBeInvoked) {
        List<Object> objectList = new ArrayList<>();

        methods.forEach(method -> {
            try {
                final Object object = method.invoke(objectWithMethodToBeInvoked);

                if (object instanceof Optional) {
                    if (!((Optional) object).isPresent()) objectList.add(null);
                }
                objectList.add(object);
            } catch (IllegalAccessException | InvocationTargetException e) {
                objectList.add(null);
            }
        });

        return objectList;
    }
}
