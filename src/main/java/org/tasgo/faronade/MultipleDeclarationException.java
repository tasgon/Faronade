package org.tasgo.faronade;

/**
 * Created by Tasgo on 1/17/2016.
 */
public class MultipleDeclarationException extends Exception {
    public MultipleDeclarationException(String method) {
        super(String.format("The method %s is defined more than once and parameters have not been explicitly stated!", method));
    }
}
