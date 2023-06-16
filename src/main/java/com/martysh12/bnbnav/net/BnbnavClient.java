package com.martysh12.bnbnav.net;


import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.martysh12.bnbnav.BnbnavMod;
import org.newsclub.net.unix.AFInputStream;
import org.newsclub.net.unix.AFOutputStream;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.IOException;
import java.nio.file.Path;

public class BnbnavClient {
    private final AFUNIXSocket sock;
    private final AFOutputStream out;
    private final AFInputStream in;

    private final NewlineDelimitingJsonRpcClient rpcClient;

    public BnbnavClient() throws IOException {
        this.sock = AFUNIXSocket.newInstance();
        this.sock.connect(AFUNIXSocketAddress.of(Path.of(System.getenv("XDG_RUNTIME_DIR"), "bnbnav")));
        this.out = this.sock.getOutputStream();
        this.in = this.sock.getInputStream();
        this.rpcClient = new NewlineDelimitingJsonRpcClient();

    }
    public String ping() throws Throwable {
        return this.rpcClient.invokeAndReadResponse("Ping", null, String.class, this.out, this.in);
    }
}
