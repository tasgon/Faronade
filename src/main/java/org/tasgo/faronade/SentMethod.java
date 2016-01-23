package org.tasgo.faronade;

/**
 * Created by Tasgo on 1/17/2016.
 */
public class SentMethod {
    public String name;
    public Object[] parameters;

    public SentMethod(String method, Object... objs) {
        this.name = method;
        this.parameters = objs;
    }

    public SentMethod(String method) {
        this.name = method;
        this.parameters = null;
    }
}
