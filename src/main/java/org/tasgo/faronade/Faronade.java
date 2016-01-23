package org.tasgo.faronade;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class that handles communication between the two networked objects
 */
public class Faronade extends Thread {
    private boolean active = false;
    private Kryo kryo = new Kryo();
    private int port;
    private String address;
    private List<NetworkMethod> registeredMethods = new ArrayList<NetworkMethod>();
    private Socket socket;
    private Input socketInput;
    private Output socketOutput;

    public Faronade(int port) {
        this.port = port;
    }

    public void bind(String address) {
        this.address = address;
    }

    public void registerMethod(Object obj, @Nonnull String method, @Nullable Class<?>... params) throws NoSuchMethodException, MultipleDeclarationException {
        registeredMethods.add(new NetworkMethod(obj, method, params));
    }

    public Object invoke(Class<?> clazz, String method, @Nullable Object... args) throws ConnectionNotEstablishedException {
        if (!active)
            throw new ConnectionNotEstablishedException();

        kryo.writeObject(socketOutput, new SentMethod(method, args));
        return kryo.readObject(socketInput, clazz);
    }

    public void invoke(String method, @Nullable Object... args) {
        kryo.writeObject(socketOutput, new SentMethod(method, args));
    }

    public void run() {
        System.out.println("Faronade starting");
        active = true;

        try {
            if (address != null)
                socket = new Socket(address, port);
            else
                socket = new ServerSocket(port).accept();
            //socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //socketOutput = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            socketInput = new Input(socket.getInputStream());
            socketOutput = new Output(socket.getOutputStream());
            System.out.println("Streams established");

            while (active) {
                if (socketInput.available() > 0) {
                    SentMethod method = kryo.readObject(socketInput, SentMethod.class);
                    for (NetworkMethod networkMethod : registeredMethods) {
                        if (networkMethod.methodName.equals(method.name)) {
                            if (method.parameters == null)
                                networkMethod.call();
                            else
                                networkMethod.call(method.parameters);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void halt() {
        active = false;
    }

    public int getPort() {
        return port;
    }
}
