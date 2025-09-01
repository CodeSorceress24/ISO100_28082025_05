import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PingServidores {
    
    public static void main(String[] args) {
        // Verifica se o sistema operacional é Linux
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("linux")) {
            System.out.println("ERRO: Esta aplicação só funciona no Linux!");
            System.exit(1);
        }
        
        System.out.println("Iniciando teste de ping para 3 servidores...");
        System.out.println("=============================================\n");
        
        // Lista de servidores para testar
        String[] servidores = {
            "www.uol.com.br",
            "www.terra.com.br", 
            "www.google.com.br"
        };
        
        String[] nomes = {"UOL", "Terra", "Google"};
        
        // Lista para armazenar as threads
        List<Thread> threads = new ArrayList<>();
        
        // Cria e inicia uma thread para cada servidor
        for (int i = 0; i < servidores.length; i++) {
            PingThread pingThread = new PingThread(servidores[i], nomes[i]);
            Thread thread = new Thread(pingThread);
            threads.add(thread);
            thread.start();
            
            // Pequena pausa entre o início das threads
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Aguarda todas as threads terminarem
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\n Todos os testes de ping foram concluídos!");
    }
}