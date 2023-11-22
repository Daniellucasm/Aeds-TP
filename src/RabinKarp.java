import java.util.*;

public abstract class RabinKarp {
    /**
     * Constante para quantidade de caracteres da tabela ASCII, que corresponde
     * ao número símbolos do alfabeto utilizado em questão
     */
    private final static int asc = 256;

    /**
     * Método para preparar a base de dados, coletar o padrão e iniciar
     * a busca
     * 
     * @param jogadores: lista de objetos jodadores que representam a base de dados
     */
    public static void executar(List<Players> jogadores) {
        final String base = jogadores.toString();

        Scanner entrada = new Scanner(System.in);
        System.out.print("Entre com o padrão que deseja buscar: ");
        String padrao = entrada.nextLine();

        buscar(padrao, base, asc);
    }

    /**
     * Método para realizar o casamento de padrões utilizando Rabin-Karp
     * 
     * @param padrao: padrão a ser identificado na busca
     * @param base:   todo o texto contido na base de dados da aplicação
     * @param modulo: valor para compor o hash mudular. Por conveniência,
     *                é utilizado o mesmo número de caracteres do alfabeto em
     *                questão
     */
    private static void buscar(String padrao, String base, int modulo) {
        int tamPadrao = padrao.length();
        int tamBase = base.length();
        int hashPadrao = 0;
        int hashJanela = 0;
        int auxHash = 1; // variável auxiliar para gerar e atribuir novos hashes
        int comparacoes = 1;
        int padroesEncontrados = 0;

        for (int i = 0; i < tamPadrao - 1; i++) {
            auxHash = (auxHash * asc) % modulo;
        }

        for (int i = 0; i < tamPadrao; i++) {
            hashPadrao = (asc * hashPadrao + padrao.charAt(i)) % modulo;
            hashJanela = (asc * hashJanela + base.charAt(i)) % modulo;
        }

        // Deslizar com o padrão pelo texto de uma em uma posição
        for (int i = 0; i <= tamBase - tamPadrao; i++, comparacoes++) {
            int j;
            /**
             * Comparar os hashes da janela deslizante e do padrão.
             * Se forem equivalentes, os caracteres serão checados.
             */
            if (hashPadrao == hashJanela) {
                for (j = 0; j < tamPadrao; j++) {
                    if (base.charAt(i + j) != padrao.charAt(j)) {
                        break;
                    }
                }

                if (j == tamPadrao) {
                    padroesEncontrados++;
                    System.out.println(
                            "Padrão encontrado na posição " + i + ". Número de comparações: " + comparacoes);
                }
            }

            // Cálculo para determinar o hash da próxima janela de texto
            if (i < tamBase - tamPadrao) {
                hashJanela = (asc * (hashJanela - base.charAt(i) * auxHash)
                        + base.charAt(i + tamPadrao))
                        % modulo;

                // Tratamento para impedir que o hashBase contenha valores negativos
                if (hashJanela < 0) {
                    hashJanela = (hashJanela + modulo);
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
}
