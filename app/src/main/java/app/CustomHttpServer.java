package app;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomHttpServer {
    private static HttpServer server;

    public static int OpenServer(String fileID) throws IOException {
        int port = 8000;
        if (isPortInUse(port)) {
            System.out.println("Port 8000 is already in use. Attempting to stop the existing server...");
            stopServer();
        }
        server = HttpServer.create(new InetSocketAddress(port), 0);


        server.createContext("/", new FileHandler("src/main/resource/public/index.html", "text/html"));
        server.createContext("/assets/data.json", new FileHandler("src/main/resource/data/"+fileID+"/data.json", "application/json"));
        server.createContext("/assets/external.json", new FileHandler("src/main/resource/data/"+fileID+"/external.json", "application/json"));

        String assetsPath = "src/main/resource/public/assets";
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(assetsPath));
            for (Path path : directoryStream) {
                if (Files.isRegularFile(path)) {
                    String fileName = path.toString().replace("src/main/resource/public", "");
                    server.createContext(fileName,
                            new FileHandler(path.toString(), getContentType(fileName)));
                }
            }
            directoryStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.setExecutor(null);

        server.start();

        System.out.println("Server started on port 8000");
        return port;
    }
    
    public static String getContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        HashMap<String, String> map = new HashMap<>();
        map.put("css", "text/css");
        map.put("js", "application/javascript");
        map.put("json", "application/json");
        map.put("eot", "application/vnd.ms-fontobject");
        map.put("ttf", "font/ttf");
        map.put("woff", "font/woff");
        map.put("woff2", "font/woff2");
        return map.get(extension);
    }

    private static boolean isPortInUse(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    public static void stopServer() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped.");
        }
    }

    static class FileHandler implements HttpHandler {
        private final String filePath;
        private final String contentType;

        public FileHandler(String filePath, String contentType) {
            this.filePath = filePath;
            this.contentType = contentType;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, fileBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(fileBytes);
            os.close();
        }
    }
}
