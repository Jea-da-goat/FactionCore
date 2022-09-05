package com.itndev.FactionCore.SocketConnection.IO;

import com.itndev.FactionCore.SocketConnection.Server.ConnectionThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ResponseList {

    private static ResponseList instance = null;

    private ResponseList() {
        instance = this;
    }

    public static ResponseList get() {
        if(instance == null) {
            instance = new ResponseList();
        }
        return instance;
    }

    public void removeOldConnections() {
        Threads.forEach(thread -> {
            if(thread.isClosed()) {
                Threads.remove(thread);
            }
        });
    }

    private ArrayList<ConnectionThread> Threads = new ArrayList<>();

    public void add(ConnectionThread serverThread) {
        Threads.add(serverThread);
    }

    public void remove(ConnectionThread serverThread) {
        Threads.remove(serverThread);
    }

    public synchronized void response(HashMap<Integer, String> map) {
        Threads.forEach(serverThread ->
                    new Thread(() -> {
                        try {
                            serverThread.send(map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start()
                );
    }

    public void closeAll() {
        response(new HashMap<Integer, String>());
        Threads.forEach(serverThread -> {
            try {
                serverThread.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
