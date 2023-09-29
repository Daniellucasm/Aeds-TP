import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        
    }

    public static void atualizarRegistro(List<Players> jogadores) throws Exception{

    }

    public static void main(String[] args) throws Exception {
        //Guardando em uma variavel o local da base de dados
        String arquivo = "C:\\Users\\Pichau\\Desktop\\TP1\\src\\base.csv";
        Scanner entrada = new Scanner(System.in);
        List<Players> jogadores = new ArrayList<Players>(); //inicializando Lista de Jogadores
        int n;
        do{
            //MENU OPÇÔES
            System.out.println("--------------------- Escolha a opção desejada ---------------------");
            System.out.println("----------------- (1) Realizar carga da base de dados --------------");
            System.out.println("--------------------- (2) Ler um registro (id) ---------------------");
            System.out.println("--------------------- (3) Atualizar um registro --------------------");
            System.out.println("----------------------------- (0) SAIR -----------------------------");
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
            }
        }while(n != 0);
        
    }
}
