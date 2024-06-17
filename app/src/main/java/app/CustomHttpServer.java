package app;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.net.ServerSocket;
import java.nio.file.Paths;

public class CustomHttpServer {
    private static HttpServer server;
    public static int OpenServer(String fileID) throws IOException {
        int port = 8000;
        if (isPortInUse(port)) {
            System.out.println("Port 8000 is already in use. Attempting to stop the existing server...");
            stopServer();
        }
        // 創建HttpServer，綁定到本地端口8000
        server = HttpServer.create(new InetSocketAddress(port), 0);
        // 設置處理請求的處理程序
        server.createContext("/", new FileHandler("src/main/resource/public/index.html", "text/html"));
        server.createContext("/assets/index.js", new FileHandler("src/main/resource/public/assets/index.js", "application/javascript"));
        server.createContext("/assets/index.css", new FileHandler("src/main/resource/public/assets/index.css", "text/css"));
        server.createContext("/assets/data.json", new FileHandler("src/main/resource/public/assets/"+fileID+".json", "application/json"));
        server.createContext("/assets/external.json", new FileHandler("src/main/resource/public/assets/external.json", "application/json"));
        // 設置默認的執行器
        server.setExecutor(null);

        // 啟動伺服器
        server.start();

        System.out.println("Server started on port 8000");
        return port;
    }
    private static boolean isPortInUse(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // 如果能打開一個ServerSocket，則表示端口未被占用
            return false;
        } catch (IOException e) {
            // 如果不能打開，則表示端口已被占用
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
