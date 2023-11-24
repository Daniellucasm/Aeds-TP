import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static Scanner entrada = new Scanner(System.in);

    public static void leitorBase(String arquivo, List<Players> jogadores) throws Exception {
        // Inicializando um ID sequencial e o Leitor de arquivo
        int id = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo)));
        br.readLine(); // pulando a primeira linha que contem informações que não irá utilizar
        String linha = br.readLine();
        while (linha != null) {
            // Quebrando a linha pela virgula e guardando em variaveis para adicionar a
            // Lista de Players
            String line[] = linha.split(",");

            String selecao = line[0];
            int numeroCamisa = Integer.parseInt(line[1]);

            // PROCURA SE EXISTE MELHORES FORMAS DE FAZER
            char posicao[] = new char[2];
            posicao = line[2].toCharArray();

            String nomePopular = line[3];
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date aniversario = formatter.parse(line[4]);

            String nomeCamisa = line[5];
            String clube = line[6];
            Double height = Double.parseDouble(line[7]);
            Double weight = Double.parseDouble(line[8]);

            // adicionando jogadores a uma Lista de Players
            jogadores.add(new Players(id, selecao, numeroCamisa, posicao, nomePopular, aniversario,
                    nomeCamisa, clube, height, weight));
            linha = br.readLine();
            id++;
        }
        br.close();
        // testePlayers(jogadores);
        escreverBase(jogadores, "jogadores.txt");
    }

    // METODO USADO PARA TESTAR ALGUNS PROBLEMAS QUE ESTAVAM ACONTECENDO
    public static void testePlayers(List<Players> jogadores) {
        for (Players teste : jogadores) {
            System.out.println(teste);
        }
    }

    public static void escreverBase(List<Players> jogadores, String arquivo) throws Exception {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(arquivo));
        byte[] ba;
        for (int n = 0; n < jogadores.size(); n++) {
            ba = jogadores.get(n).toByteArray();
            dos.writeInt(ba.length);
            dos.write(ba);
        }
        dos.close();
    }

    public static void leituraID(List<Players> jogadores) throws Exception {
        Players j_temp = new Players();
        DataInputStream dis = new DataInputStream(new FileInputStream("jogadores.txt"));
        System.out.println("Qual o ID do registro?");
        int idBusca = entrada.nextInt();
        int len = dis.readInt();
        while (len != 0) {
            byte ba[] = new byte[len];
            dis.read(ba);
            j_temp.fromByteArray(ba);
            if (j_temp.getId() == idBusca) {
                System.out.println(j_temp.toString());
                break;
            } else {
                len = dis.readInt();
            }
        }
        dis.close();
    }

    /** LEMBRAR **/
    public static void atualizarRegistro(List<Players> jogadores) throws Exception {
        System.out.println("Qual o ID do registro que deseja atualizar?");
        int idBusca = entrada.nextInt();
        entrada.nextLine();
        System.out.println("Digite a seleção: ");
        jogadores.get(idBusca).setSelecao(entrada.nextLine());
        System.out.println("Digite o numero da camisa: ");
        jogadores.get(idBusca).setNumeroCamisa(Integer.parseInt(entrada.nextLine()));
        System.out.println("Digite o nomePopular: ");
        jogadores.get(idBusca).setNomePopular(entrada.nextLine());
        System.out.println("Digite a data no formato dd.MM.yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        jogadores.get(idBusca).setAniversario(formatter.parse(entrada.nextLine()));
        System.out.println("Digite o nome da Camisa");
        jogadores.get(idBusca).setNomeCamisa(entrada.nextLine());
        System.out.println("Digite o clube novo");
        jogadores.get(idBusca).setClube(entrada.nextLine());
        System.out.println("Digite a altura");
        jogadores.get(idBusca).setHeight(entrada.nextDouble());
        System.out.println("Digite o peso");
        jogadores.get(idBusca).setWeight(entrada.nextDouble());
        escreverBase(jogadores, "jogadores.txt");

    }

    /** LEMBRAR **/
    public static void deletarRegistro(List<Players> jogadores) throws Exception {
        System.out.println("Qual o ID do registro que deseja deletar?");
        int idBusca = entrada.nextInt();
        for (int i = 0; i < jogadores.size(); i++) {
            Players p = jogadores.get(i);
            if (p.getId() == idBusca) {
                jogadores.remove(p);
                break;
            }
        }
        escreverBase(jogadores, "jogadores.txt");
    }

    public static void mergeSort(List<Players> temp) {
        if (temp.size() <= 1) {
            return;
        }

        int mid = temp.size() / 2;
        List<Players> left = new ArrayList<Players>(temp.subList(0, mid));
        List<Players> right = new ArrayList<Players>(temp.subList(mid, temp.size()));

        mergeSort(left);
        mergeSort(right);

        merge(temp, left, right);
    }

    public static void merge(List<Players> temp, List<Players> left, List<Players> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getSelecao().compareTo(right.get(j).getSelecao()) < 0) {
                temp.set(k, left.get(i));
                i++;
            } else {
                temp.set(k, right.get(j));
                j++;
            }
            k++;
        }

        while (i < left.size()) {
            temp.set(k, left.get(i));
            i++;
            k++;
        }

        while (j < right.size()) {
            temp.set(k, right.get(j));
            j++;
            k++;
        }
    }

    // Metodo que ira realizar a intercalação balanceada comum
    // Recebe int m (quantidade de registros) e int c (quantidade de caminhos)
    public static void distribuicao(List<Players> jogadores) throws Exception {
        // Como padrão será 2 caminhos
        DataOutputStream dos1 = new DataOutputStream(new FileOutputStream("arq1.txt"));
        DataOutputStream dos2 = new DataOutputStream(new FileOutputStream("arq2.txt"));

        int i = 0;
        int comp = 1;
        while (jogadores.size() > i) {
            List<Players> temp = new ArrayList<Players>();
            for (int x = 0; x < 80; x++) {
                if (i < jogadores.size())
                    temp.add(jogadores.get(i));
                i++;
            }
            mergeSort(temp);
            if (comp == 1) {
                for (int x = 0; x < temp.size(); x++) {
                    byte[] ba;
                    for (int n = 0; n < temp.size(); n++) {
                        ba = temp.get(n).toByteArray();
                        dos1.writeInt(ba.length);
                        dos1.write(ba);
                    }
                }
                comp = 2;
            } else {
                for (int x = 0; x < temp.size(); x++) {
                    byte[] ba;
                    for (int n = 0; n < temp.size(); n++) {
                        ba = temp.get(n).toByteArray();
                        dos2.writeInt(ba.length);
                        dos2.write(ba);
                    }
                }
                comp = 1;
            }
        }
        dos1.close();
        dos2.close();
        intercalacao(4, jogadores.size());
    }

    public static void intercalacao(int m, int tam) throws Exception {
        List<Players> j1 = new ArrayList<Players>();
        List<Players> j2 = new ArrayList<Players>();
        DataInputStream dis1 = new DataInputStream(new FileInputStream("arq1.txt"));
        DataInputStream dis2 = new DataInputStream(new FileInputStream("arq2.txt"));
        Players jTemp = new Players();
        byte ba[];
        int len;

        boolean acabou = false;
        try {
            len = dis1.readInt();
            while (!acabou) {
                ba = new byte[len];
                dis1.read(ba);
                jTemp.fromByteArray(ba);
                j1.add(jTemp);
                len = dis1.readInt();
            }
        } catch (EOFException e){
                acabou = true;
        }
        acabou = false;
        try {
            len = dis2.readInt();
            while (len != 0) {
                ba = new byte[len];
                dis2.read(ba);
                jTemp.fromByteArray(ba);
                j2.add(jTemp);
                len = dis2.readInt();
            }
        } catch (EOFException e){
                acabou = true;
        }

        dis1.close();
        dis2.close();
        int x1 = 0;
        int x2 = 0;
        int arq = 1;
        DataOutputStream dos1 = new DataOutputStream(new FileOutputStream("arq3.txt"));
        DataOutputStream dos2 = new DataOutputStream(new FileOutputStream("arq4.txt"));
        while (x1 < j1.size() || x2 < j2.size()) {
            if (j1.get(x1).getSelecao().compareTo(j2.get(x2).getSelecao()) <= 0 && arq % 2 != 0 && j1.get(x1) != null && j2.get(x2) != null) {
                ba = j1.get(x1).toByteArray();
                dos1.writeInt(ba.length);
                dos1.write(ba);
                x1++;
            } else if (j1.get(x1).getSelecao().compareTo(j2.get(x2).getSelecao()) > 0 && arq % 2 != 0 && j1.get(x1) != null && j2.get(x2) != null) {
                ba = j2.get(x2).toByteArray();
                dos1.writeInt(ba.length);
                dos1.write(ba);
                x2++;
            } else if (j1.get(x1).getSelecao().compareTo(j2.get(x2).getSelecao()) <= 0 && arq % 2 == 0 && j1.get(x1) != null && j2.get(x2) != null) {
                ba = j1.get(x1).toByteArray();
                dos2.writeInt(ba.length);
                dos2.write(ba);
                x1++;
            } else {
                ba = j2.get(x2).toByteArray();
                dos2.writeInt(ba.length);
                dos2.write(ba);
                x2++;
            }

            if (x1 > arq * 80 && x2 > arq * 80) {
                arq++;
            }
        }

        if (m > tam) {
            intercalacao(m * 2, tam);
        } else {
            escreverBase(j1, "arq1.txt");
        }
    }

    public static void main(String[] args) throws Exception {
        // Guardando em uma variavel o local da base de dados
        String arquivo = "./src/base.csv";
        List<Players> jogadores = new ArrayList<Players>(); // inicializando Lista de Jogadores
        int n = 1;

        while (n != 0) {
            // MENU OPÇÔES
            System.out.println("======================= FIFA WORLD CUP 2018 ========================");
            System.out.println("--------------------- Escolha a opção desejada ---------------------");
            System.out.println("----------------- (1) Realizar carga da base de dados --------------");
            System.out.println("--------------------- (2) Ler um registro (id) ---------------------");
            System.out.println("--------------------- (3) Atualizar um registro --------------------");
            System.out.println("--------------------- (4) Deletar um registro ----------------------");
            System.out.println("--------------------- (5) ORDENAÇÃO EXTERNA ------------------------");
            System.out.println("--------------------- (6) Compressão de dados ----------------------");
            System.out.println("--------------------- (7) Casamento de Padrões ----------------------");
            System.out.println("----------------------------- (0) SAIR -----------------------------");
            System.out.println("====================================================================");
            n = entrada.nextInt();
            entrada.nextLine();

            switch (n) {
                case 1:
                    leitorBase(arquivo, jogadores);
                    break;
                case 2:
                    if (jogadores != null) {
                        leituraID(jogadores);
                    }
                    break;
                case 3:
                    if (jogadores != null) {
                        atualizarRegistro(jogadores);
                    }
                    break;
                case 4:
                    if (jogadores != null) {
                        deletarRegistro(jogadores);
                    }
                    break;
                case 5:
                    System.out.println("(1) Intercalação Balanceada comum");
                    // TODO: a ser implementado
                    // System.out.println("(2) Intercalação Balanceada com blocos de tamanho variável");
                    // System.out.println("(3) Intercalação Balanceada com seleção por substituição");
                    n = entrada.nextInt();
                    if (1 == n) {
                        distribuicao(jogadores);
                    } 
                    // else if (2 == n) {
                    // } else {}
                    break;
                case 6:
                    System.out.println("(1) Huffman");
                    System.out.println("(2) LZW");
                    n = entrada.nextInt();
                    if (1 == n) {
                        Huffman huff = new Huffman();
                        huff.executar(jogadores);
                    } else if (2 == n) {
                        LZW lzw = new LZW();
                        lzw.compress(jogadores.toString());
                        lzw.decompress();
                    } else {

                    }
                    break;
                case 7:
                    System.out.println("(1) KMP");
                    System.out.println("(2) Rabin-Karp");
                    n = entrada.nextInt();
                    entrada.nextLine();
                    if(1 == n){
                        KMP.executar(jogadores);
                    } else if (2 == n){
                        RabinKarp.executar(jogadores);
                    }
            }
        }

        entrada.close();
    }
}
