package com.martysh12.bnbnav.net;

import com.martysh12.bnbnav.BnbnavMod;
import org.newsclub.net.unix.AFInputStream;
import org.newsclub.net.unix.AFOutputStream;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.IOException;
import java.nio.file.Path;

public class BnbnavClient {
    private final Reconnector reconnector;
    private final NewlineDelimitingJsonRpcClient rpcClient;

    public BnbnavClient() throws IOException {
        this.reconnector = new Reconnector();
        Thread reconnectThread = new Thread(reconnector, "Reconnect thread");
        reconnectThread.start();

        this.rpcClient = new NewlineDelimitingJsonRpcClient();
    }

    public boolean isConnected() {
        return this.reconnector.isConnected();
    }

    private void ensureConnected() throws IOException {
        if (!this.isConnected()) {
            throw new IOException("Not connected!");
        }
    }

    private AFOutputStream getOutStream() throws IOException {
        return this.reconnector.sock.getOutputStream();
    }

    private AFInputStream getInStream() throws IOException {
        return this.reconnector.sock.getInputStream();
    }

    public String ping() throws Throwable {
        this.ensureConnected();
        return this.rpcClient.invokeAndReadResponse("Ping", null, String.class, this.getOutStream(), this.getInStream());
    }

    public String getLoggedInUsername() throws Throwable {
        this.ensureConnected();
        return this.rpcClient.invokeAndReadResponse("GetLoggedInUsername", null, String.class, this.getOutStream(), this.getInStream());
    }

    private static class Reconnector implements Runnable {
        private AFUNIXSocket sock;

        public Reconnector() throws IOException {
            this.sock = AFUNIXSocket.newInstance();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Reconnect if not connected;
                    if (!this.isConnected() || !this.sock.isBound()) {
                        if (!this.sock.isBound()) {
                            // Create a new instance if the socket is not bound
                            BnbnavMod.logger.info("Socket is not bound anymore, creating new instance");
                            this.sock = AFUNIXSocket.newInstance();
                        }

                        this.sock.connect(AFUNIXSocketAddress.of(Path.of(System.getenv("XDG_RUNTIME_DIR"), "bnbnav")));
                    }
                } catch (IOException ignored) {
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }

        public boolean isConnected() {
            return this.sock.isConnected();
        }
    }
}
