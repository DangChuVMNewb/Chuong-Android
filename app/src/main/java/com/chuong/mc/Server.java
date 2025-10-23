package com.chuong.mc;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import fi.iki.elonen.NanoHTTPD;

public class Server extends NanoHTTPD {

    private final Context context;

    public Server(Context context) {
        super(9365);
        this.context = context;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        // Nếu truy cập "/", tự động load index.html
        if (uri.equals("/") || uri.isEmpty()) {
            uri = "/index.html";
        }

        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open(uri.substring(1)); // bỏ dấu "/"
            String mime = getMimeTypeForFile(uri);
            return newChunkedResponse(Response.Status.OK, mime, is);
        } catch (IOException e) {
            // Nếu file không tồn tại
            String notFound = "<h3>404 Not Found</h3><p>" + uri + "</p>";
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", notFound);
        }
    }

    public void startServer() {
        try {
            start(SOCKET_READ_TIMEOUT, false);
        } catch (IOException ignored) {}
    }
}