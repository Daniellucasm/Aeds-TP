import java.util.*;

public class KMP {
    List<Players> jogadores;

    /**
     * Método para iniciar o KMP
     */
    public void iniciaKMP(){
        //armazenando toda a base em uma String
        String text = jogadores.toString();

        Scanner entrada = new Scanner(System.in);
        System.out.println("Entre com o padrão que deseja buscar:");
        String busca = entrada.nextLine();
        
        KMPSearch(text, busca);
    }

    public static void KMPSearch(String text, String pattern) {
        //inicializando variaveis com o tamanho da base e padrão buscado
        int M = pattern.length();
        int N = text.length();

        int[] lps = computeLPSArray(pattern);
        int i = 0;
        int j = 0;
        int comparacoes = 0;//Variável de contagem de comparações

        while (i < N) {
            comparacoes++;
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == M) {
                System.out.println("Padrão encontrado na posição " + (i - j) + ". Número de comparações: " + comparacoes);
                j = lps[j - 1];
            } else if (i < N && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
    }

    /*
     * Método responsável por calcular a matriz de prefixo-sufixo
     */
    public static int[] computeLPSArray(String pattern) {
        int len = pattern.length();
        int[] lps = new int[len];
        int j = 0;
        int i = 1;
        lps[0] = 0;

        while (i < len) {
            //caso os caracteres forem iguais incrementamos j e atribuimos esse valor a j.
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
                lps[i] = j;
                i++;
            } else {
                //caso não encontrado o algoritmo ajusta j baseado nos valores já computados na matriz.
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}
