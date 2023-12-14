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

    private final static String file = "./otp_db.txt";

    public static void cifrar(List<Players> jogadores) throws Exception {
        System.out.print("\nInsira o ID do registro a ser criptografado: ");
        final int id = Main.entrada.nextInt();
        String registro = "";
        String[] auxRegistro = null;

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

        for (int i = 1; i < auxRegistro.length; i++) {
            registro += auxRegistro[i] + " ";
        }

        final String chave = randomString(registro.length());
        final String cifra = cifrar(registro.toCharArray(), chave.toCharArray());

        escreverRegistroOTP(id, chave, cifra);
        System.out.println("\nCriptografia realizada com sucesso.\n");
    }

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

        String registro = decifrar(cifra.toCharArray(), chave.toCharArray());
        registro = id + " " + registro;

        System.out.println("\nDecriptografia realizada com sucesso: " + registro + "\n");
    }

    private static String cifrar(char[] registro, char[] chave) {
        StringBuilder sb = new StringBuilder(registro.length);

        for (int i = 0; i < registro.length; i++) {
            sb.append((char) (chave[i] ^ registro[i]));
        }

        return sb.toString();
    }

    private static String decifrar(char[] cifra, char[] chave) {
        StringBuilder sb = new StringBuilder(cifra.length);

        for (int i = 0; i < cifra.length; i++) {
            sb.append((char) (chave[i] ^ cifra[i]));
        }

        return sb.toString();
    }

    private static String randomString(int length) {
        int randomNum;
        final int min = 0; // min ASCII character
        final int max = 127; // max ASCII character
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            sb.append((char) randomNum);
        }

        return sb.toString();
    }

    private static String lerRegistroOTP(int id) throws Exception {
        DataInputStream dis = new DataInputStream(new FileInputStream("otp_db.txt"));
        int auxId;
        int tamRegistro;
        boolean eof = false;
        String chaveCifra = "";

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

    private static String otpFromByteArray(byte ba[]) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        String chaveCrifra = dis.readUTF();
        chaveCrifra += dis.readUTF();

        bais.close();
        dis.close();

        return chaveCrifra;
    }

    private static boolean hasFile() {
        File f = new File(file);
        return f.exists() && !f.isDirectory();
    }
}
