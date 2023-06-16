package com.martysh12.bnbnav.net;

import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.googlecode.jsonrpc4j.RequestIDGenerator;
import com.martysh12.bnbnav.BnbnavMod;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class NewlineDelimitingJsonRpcClient extends JsonRpcClient {
    RequestIDGenerator idGenerator;
    int currentId = 0;

    public NewlineDelimitingJsonRpcClient() {
        this.idGenerator = () -> {
            this.currentId++;
            return String.valueOf(this.currentId);
        };

        this.setRequestIDGenerator(this.idGenerator);
    }

    @Override
    public <T> T invokeAndReadResponse(String methodName, Object argument, Class<T> clazz, OutputStream output, InputStream input) throws Throwable {
        super.invoke(methodName, argument, output);
        String id = String.valueOf(this.currentId);

        output.write("\r\n".getBytes());
        output.flush();

        return super.readResponse(clazz, input, id);
    }
}
