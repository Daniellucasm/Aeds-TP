import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class OneTimePad {
    private final static String arquivo = "./otp_db.txt";

    /**
     * Método para preparar os dados necessários para realizar a criptografia OTP
     * 
     * @param jogadores: lista de objetos jodadores que representam a base de dados
     */
    public static void cifrar(List<Players> jogadores) throws Exception {
        System.out.print("\nInsira o ID do registro a ser criptografado: ");
        final int id = Main.entrada.nextInt();
        String registro = "";
        String[] auxRegistro = null;

        // Verifica se o arquivo de dados já existe e se o ID inserido já existe no
        // mesmo
        if (hasFile() && !lerRegistroOTP(id).isEmpty()) {
            System.out.println("\nRegistro já criptografado.\n");
            return;
        }

        for (Players jogador : jogadores) {
            if (jogador.getId() == id) {
                auxRegistro = jogador.toString().split(" ");
                break;
            }
        }

        if (auxRegistro == null) {
            System.out.println("\nID não encontrado.\n");
            return;
        }

        // Laço responsável por remover o ID da String do registro, pois este é
        // utilizado para identificar o registro no arquivo e não é tratado como uma
        // informação sensível a ser criptografada
        for (int i = 1; i < auxRegistro.length; i++) {
            registro += auxRegistro[i] + " ";
        }

        final String chave = randomString(registro.length());
        final String cifra = cifrar(registro.toCharArray(), chave.toCharArray());

        escreverRegistroOTP(id, chave, cifra);
        System.out.println("\nCriptografia realizada com sucesso.\n");
    }

    /**
     * Método para preparar os dados necessários para realizar a decriptografia OTP
     */
    public static void decifrar() throws Exception {
        System.out.print("\nInsira o ID do registro que deseja descriptografar: ");
        final int id = Main.entrada.nextInt();

        String chaveCifra = lerRegistroOTP(id);

        if (chaveCifra.isEmpty()) {
            System.out.println("\nID não encontrado.\n");
            return;
        }

        String chave = chaveCifra.substring(0, (chaveCifra.length() / 2) + 1);
        String cifra = chaveCifra.substring(chaveCifra.length() / 2);

        // Como o ID não é criptografa junto ao restante dos dados, precisamos
        // concatená-lo para completar o registro
        String registro = decifrar(cifra.toCharArray(), chave.toCharArray());
        registro = id + " " + registro;

        System.out.println("\nDecriptografia realizada com sucesso: " + registro + "\n");
    }

    /**
     * Método para realizar a criptografia utilizando OTP
     * 
     * @param registro: vetor de caracteres que formam o registro completo
     * @param chave:    vetor de caracteres que formam a chave completa do registro
     * @return uma String contendo a crifragem resultante
     */
    private static String cifrar(char[] registro, char[] chave) {
        StringBuilder sb = new StringBuilder(registro.length);

        // Laço responsável por iterar os pares de caracteres da chave e do registro, e
        // realizar a operação XOR para cada par. O resultado é concatenado como um
        // caractere a String resultante. O XOR nesse caso trabalha tendo como base os
        // valores da tabela ASCII correspondentes a cada caractere
        for (int i = 0; i < registro.length; i++) {
            // XOR efetuado entre os pares da chave e do registro, gerando a cifra
            // resultante
            sb.append((char) (chave[i] ^ registro[i]));
        }

        return sb.toString();
    }

    /**
     * Método para realizar a decriptografia utilizando OTP
     * 
     * @param cifra: vetor de caracteres que formam a cifra completa
     * @param chave: vetor de caracteres que formam a chave completa do registro
     * @return uma String contendo a decifragem resultante
     */
    private static String decifrar(char[] cifra, char[] chave) {
        StringBuilder sb = new StringBuilder(cifra.length);

        // Laço responsável por iterar os pares de caracteres da chave e do registro, e
        // realizar a operação XOR para cada par. O resultado é concatenado como um
        // caractere a String resultante. O XOR nesse caso trabalha tendo como base os
        // valores da tabela ASCII correspondentes a cada caractere
        for (int i = 0; i < cifra.length; i++) {
            // XOR efetuado entre os pares da chave e da crifra, gerando o texto resultante
            sb.append((char) (chave[i] ^ cifra[i]));
        }

        return sb.toString();
    }

    /**
     * Método para gerar uma String aleatória de tamanho qualquer
     * 
     * @param length: inteiro que define o tamanho da String a ser gerada
     * @return uma String aleatória de tamanho igual ao @param length
     */
    private static String randomString(int length) {
        int randomNum;
        final int min = 0; // caractere ASCII mínimno
        final int max = 127; // caractere ASCII máximo
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Usa ThreadLocalRandom como seed para geração dos valores aleatórios
            randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            sb.append((char) randomNum);
        }

        return sb.toString();
    }

    /**
     * Método para ler do arquivo OTP um registro pré-determinado pelo usuário
     * 
     * @param id: inteiro que define o registro a ser procurado
     * @return uma String contendo o registro referente ao @param id. Caso o
     *         registro não seja encontrado, uma String vazia será retornada
     */
    private static String lerRegistroOTP(int id) throws Exception {
        DataInputStream dis = new DataInputStream(new FileInputStream("otp_db.txt"));
        int auxId;
        int tamRegistro;
        boolean eof = false; // Variável de controle End of File
        String chaveCifra = "";

        // Os registros do arquivo possuem a seguinte estrutura: ID + tamanho do
        // registro + chave + cifra
        // Logo, primeiramente realizamos a leitura do ID para identificar se equivale
        // ao registro pretendido e depois lemos o tamanho do registro. Caso seja o ID
        // correto, lê-se o restante do registro e termina a operação. Caso n!ao seja, o
        // registro é pulado e a mesma verificação ocorre novamente. Este processo
        // também finaliza quando se alcança o final do arquivo
        while (!eof) {
            try {
                auxId = dis.readInt();
                tamRegistro = dis.readInt();
                byte ba[] = new byte[tamRegistro];
                dis.read(ba);

                if (auxId == id) {
                    chaveCifra = otpFromByteArray(ba);
                    eof = true;
                }
            } catch (Exception e) {
                eof = true;
            }
        }

        dis.close();
        return chaveCifra;
    }

    /**
     * Método para escrever no arquivo um novo registro de cifra
     * 
     * @param id: inteiro que define o registro a ser escrito no arquivo
     * @param chave: chave de uma cifra a ser escrita no arquivo
     * @param cifra: cifra a ser escrita no arquivo
     */
    private static void escreverRegistroOTP(int id, String chave, String cifra) throws Exception {
        boolean hasToAppendData = hasFile();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream byteDos = new DataOutputStream(baos);
        DataOutputStream fileDos = new DataOutputStream(new FileOutputStream("otp_db.txt", hasToAppendData));
        byte[] ba;

        byteDos.writeUTF(chave);
        byteDos.writeUTF(cifra);
        ba = baos.toByteArray();
        fileDos.writeInt(id);
        fileDos.writeInt(ba.length);
        fileDos.write(ba);

        baos.close();
        byteDos.close();
        fileDos.close();
    }

    /**
     * Método para recuperar uma chave e sua cifra de um vetor de bytes
     * 
     * @param ba: vetor de bytes referente a cifra + chave
     * @return uma String contendo a concatenação da chave + cifra
     */
    private static String otpFromByteArray(byte ba[]) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        String chaveCrifra = dis.readUTF();
        chaveCrifra += dis.readUTF();

        bais.close();
        dis.close();

        return chaveCrifra;
    }

    /**
     * Método para verificar se o arquivo OTP existe
     * 
     * @return um booleano informando se o arquivo OTP existe ou não
     */
    private static boolean hasFile() {
        File f = new File(arquivo);
        return f.exists() && !f.isDirectory();
    }
}
