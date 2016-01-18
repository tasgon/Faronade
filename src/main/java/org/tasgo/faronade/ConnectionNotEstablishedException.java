package org.tasgo.faronade;

/**
 * Created by Tasgo on 1/17/2016.
 */
public class ConnectionNotEstablishedException extends Exception {
    public ConnectionNotEstablishedException() {
        super("A connection to a different machine has not been established yet!");
    }
}
