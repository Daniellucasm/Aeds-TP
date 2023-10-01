import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void leitorBase(String arquivo, List<Players> jogadores) throws Exception{
        //Inicializando um ID sequencial e o Leitor de arquivo
        int id = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo)));
        br.readLine(); //pulando a primeira linha que contem informações que não irá utilizar
        String linha = br.readLine();
        while(linha != null){
            //Quebrando a linha pela virgula e guardando em variaveis para adicionar a Lista de Players
            String line[] = linha.split(",");

            String selecao = line[0];
            int numeroCamisa = Integer.parseInt(line[1]);

            //PROCURA SE EXISTE MELHORES FORMAS DE FAZER
            char posicao[] = new char[2];
            posicao = line[2].toCharArray();  
            
            String nomePopular = line[3];
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date aniversario = formatter.parse(line[4]);

            String nomeCamisa = line[5];
            String clube = line[6];
            Double height = Double.parseDouble(line[7]);
            Double weight = Double.parseDouble(line[8]);

            //adicionando jogadores a uma Lista de Players
            jogadores.add(new Players(id, selecao, numeroCamisa, posicao, nomePopular, aniversario, 
                                     nomeCamisa, clube, height, weight));
            linha = br.readLine();
            id++;
        }
        br.close();
        //testePlayers(jogadores);
        escreverBase(jogadores);
    }

    //METODO USADO PARA TESTAR ALGUNS PROBLEMAS QUE ESTAVAM ACONTECENDO
    public static void testePlayers(List<Players> jogadores) {
        for(Players teste : jogadores){
            System.out.println(teste);
        }
    }

    public static void escreverBase(List<Players> jogadores) throws Exception{
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("jogadores.txt"));
        byte[] ba;
        for(int n = 0; n < jogadores.size(); n++){
            ba = jogadores.get(n).toByteArray();
            dos.writeInt(ba.length);
            dos.write(ba);
        }
        dos.close();
    }

    public static void leituraID(List<Players> jogadores) throws Exception{
        Players j_temp= new Players();
        DataInputStream dis = new DataInputStream(new FileInputStream("jogadores.txt"));
        Scanner entrada = new Scanner(System.in);
        System.out.println("Qual o ID do registro?");
        int idBusca = entrada.nextInt();
        int len = dis.readInt();
        while(len != 0){
            byte ba[] = new byte[len];
            dis.read(ba);
            j_temp.fromByteArray(ba);
            if(j_temp.getId() == idBusca){
                System.out.println(j_temp.toString());
                break;
            } else {
                len = dis.readInt();
            }
        }
        dis.close();
        entrada.close();
        
    }

    /** LEMBRAR **/
    public static void atualizarRegistro(List<Players> jogadores) throws Exception{
        Scanner entrada = new Scanner(System.in);
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
        escreverBase(jogadores);

        entrada.close();
    }

    /** LEMBRAR **/
    public static void deletarRegistro(List<Players> jogadores) throws Exception{
        Scanner entrada = new Scanner(System.in);
        System.out.println("Qual o ID do registro que deseja deletar?");
        int idBusca = entrada.nextInt();
        for(int i = 0; i < jogadores.size(); i++){
            Players p = jogadores.get(i);
            if(p.getId() == idBusca){
                jogadores.remove(p);
                break;
            }
        }
        escreverBase(jogadores);

        entrada.close();
    }

    public static void main(String[] args) throws Exception {
        //Guardando em uma variavel o local da base de dados
        String arquivo = "C:\\Users\\Pichau\\Desktop\\TP1\\src\\base.csv";
        Scanner entrada = new Scanner(System.in);
        List<Players> jogadores = new ArrayList<Players>(); //inicializando Lista de Jogadores
        int n;
        do{
            //MENU OPÇÔES
            System.out.println("======================= FIFA WORLD CUP 2018 ========================");
            System.out.println("--------------------- Escolha a opção desejada ---------------------");
            System.out.println("----------------- (1) Realizar carga da base de dados --------------");
            System.out.println("--------------------- (2) Ler um registro (id) ---------------------");
            System.out.println("--------------------- (3) Atualizar um registro --------------------");
            System.out.println("--------------------- (4) Deletar um registro ----------------------");
            System.out.println("--------------------- (5) ORDENAÇÃO EXTERNA ------------------------");
            System.out.println("--------------------- (6) INDEXAÇÃO --------------------------------");
            System.out.println("----------------------------- (0) SAIR -----------------------------");
            System.out.println("====================================================================");
            n = entrada.nextInt();
            switch (n) {
                case 1:
                    leitorBase(arquivo, jogadores);
                    break;
                case 2:
                    if(jogadores != null){
                        leituraID(jogadores);
                    }
                    break;
                case 3:
                    if(jogadores != null){
                        atualizarRegistro(jogadores);
                    }
                    break;
                case 4:
                    if(jogadores != null){
                        deletarRegistro(jogadores);
                    }
                    break;
                case 5:
                    System.out.println("(1) Intercalação Balanceada comum");
                    System.out.println("(2) Intercalação Balanceada com blocos de tamanho variável");
                    System.out.println("(3) Intercalação Balanceada com seleção por substituição");
                    int opcao = entrada.nextInt();
                    if(1 == opcao){

                    } else if( 2 == opcao){
                        
                    } else {

                    }
                    break;
                case 6:
                    System.out.println("(1) Árvore B");
                    System.out.println("(2) Hashing Estendido");
                    System.out.println("(3) Lista invertida");
                    opcao = entrada.nextInt();
                    if(1 == opcao){

                    } else if( 2 == opcao){
                        
                    } else {

                    }
                    break;
            }
        }while(n != 0);
        
        entrada.close();
    }
}
