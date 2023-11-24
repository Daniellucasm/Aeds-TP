import java.util.*;

public abstract class KMP {

    /**
     * Método para preparar a base de dados, coletar o padrão e iniciar
     * a busca
     * 
     * @param jogadores: lista de objetos jodadores que representam a base de dados
     */
    public static void executar(List<Players> jogadores) {
        final String base = jogadores.toString();

        System.out.print("\nEntre com o padrão que deseja buscar: ");
        String padrao = Main.entrada.nextLine();

        buscar(padrao, base);
    }

    /**
     * Método para realizar o casamento de padrões utilizando KMP
     * 
     * @param padrao: padrão a ser identificado na busca
     * @param base:   todo o texto contido na base de dados da aplicação
     */
    private static void buscar(String padrao, String base) {
        int tamPadrao = padrao.length();
        int tamBase = base.length();
        int[] lps = gerarVetorLPS(padrao);
        int i = 0; // index da iteração sobre a base
        int j = 0; // index da iteração sobre o padrão
        int comparacoes = 0;
        int padroesEncontrados = 0;

        while (i < tamBase) {
            comparacoes++;

            if (padrao.charAt(j) == base.charAt(i)) {
                i++;
                j++;
            }

            /**
             * Se j for igual ao tamanho do padrão, logo todos os caracteres do intervalo da
             * base analisados correspondem aos do padrão. Caso contrário, passamos para a
             * próxima posição válida no vetor LPS
             */
            if (j == tamPadrao) {
                padroesEncontrados++;
                System.out
                        .println("Padrão encontrado na posição " + (i - j) + ". Número de comparações: " + comparacoes);
                j = lps[j - 1];
            } else if (i < tamBase && padrao.charAt(j) != base.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        if (padroesEncontrados == 0) {
            System.out.println("\nNenhum padrão foi encontrado. Total de comparações: " + comparacoes + "\n");
        } else {
            System.out.println(
                    "\nUm total de " + padroesEncontrados + " padrões foram encontrados. Total de comparações: "
                            + comparacoes + "\n");
        }
    }

    /**
     * Método responsável por calcular a matriz de prefixo-sufixo
     * 
     * @param padrao: padrão a ser identificado na busca
     * @return um vertor de inteiros contendo a matriz transposta linearmente
     */
    private static int[] gerarVetorLPS(String padrao) {
        int tamPadrao = padrao.length();
        int[] lps = new int[tamPadrao];
        int j = 0;
        int i = 1;
        lps[0] = 0;

        while (i < tamPadrao) {
            // Caso os caracteres sejam iguais, incrementamos j e atribuimos esse valor a j
            if (padrao.charAt(i) == padrao.charAt(j)) {
                j++;
                lps[i] = j;
                i++;
            } else {
                /**
                 * Caso não haja correspondência, o algoritmo ajusta j baseado nos valores já
                 * processados na matriz
                 */
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
