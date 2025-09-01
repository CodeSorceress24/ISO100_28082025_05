import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PingThread implements Runnable {
    private String servidor;
    private String nomeServidor;
    
    public PingThread(String servidor, String nomeServidor) {
        this.servidor = servidor;
        this.nomeServidor = nomeServidor;
    }
    
    @Override
    public void run() {
        System.out.println("Iniciando ping para " + nomeServidor + " (" + servidor + ")");
        
        List<Double> tempos = new ArrayList<>();
        
        try {
            // Comando ping para Linux: 10 iterações com IPv4
            String comando = "ping -4 -c 10 " + servidor;
            Process process = Runtime.getRuntime().exec(comando);
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Processar linhas que contêm tempo de resposta
                if (linha.contains("time=")) {
                    // Extrair o tempo da linha (ex: "time=12.345 ms")
                    int inicio = linha.indexOf("time=");
                    int fim = linha.indexOf(" ms");
                    
                    if (inicio != -1 && fim != -1) {
                        String tempoStr = linha.substring(inicio + 5, fim);
                        try {
                            double tempo = Double.parseDouble(tempoStr);
                            tempos.add(tempo);
                            
                            System.out.println(nomeServidor + " - Iteração " + 
                                             tempos.size() + ": " + tempo + " ms");
                            
                        } catch (NumberFormatException e) {
                            // Ignorar linhas com formato inválido
                        }
                    }
                }
            }
            
            // Calcula tempo médio
            if (!tempos.isEmpty()) {
                double soma = 0;
                for (double tempo : tempos) {
                    soma += tempo;
                }
                double media = soma / tempos.size();
                
                System.out.println("\n " + nomeServidor + " - Tempo médio: " + 
                                 String.format("%.2f", media) + " ms");
                System.out.println("-----------------------------------");
            }
            
            process.waitFor();
            
        } catch (IOException | InterruptedException e) {
            System.out.println(" Erro ao executar ping para " + nomeServidor + ": " + e.getMessage());
        }
    }
}