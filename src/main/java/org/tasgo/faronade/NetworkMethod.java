package org.tasgo.faronade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Tasgo on 1/17/2016.
 */
public class NetworkMethod {
    public Object target;
    public String methodName;
    private Method method;

    public NetworkMethod(Object target, @Nonnull String methodName, @Nullable Class<?>... params) throws MultipleDeclarationException, NoSuchMethodException {
        this.target = target;
        this.methodName = methodName;

        if (params == null) {
            for (Method m : target.getClass().getDeclaredMethods()) {
                if (m.getName().equals(methodName)) {
                    if (method != null)
                        throw new MultipleDeclarationException(methodName);
                    else
                        method = m;
                }
            }
        }
        else
            method = target.getClass().getDeclaredMethod(methodName, params);
    }

    public Object call(Object[] params) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, params);
    }
}
