import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Players {
    int id;
    String selecao;
    int numeroCamisa;
    char posicao[] = new char[2];
    String nomePopular;
    Date aniversario;
    String nomeCamisa;
    String clube;
    Double height;
    Double weight;

    public Players(){
        
    }

    public Players(int id, String selecao, int numeroCamisa, char[] posicao, String nomePopular, Date aniversario, String nomeCamisa, String clube, Double height, Double weight){
        this.id = id;
        this.selecao = selecao;
        this.numeroCamisa = numeroCamisa;
        this.posicao = posicao;
        this.nomePopular = nomePopular;
        this.aniversario = aniversario;
        this.nomeCamisa = nomeCamisa;
        this.clube = clube;
        this.height = height;
        this.weight = weight;
    }

    //METODOS GET E SET
    public int getId(){
        return id;
    }

    public String getSelecao(){
        return selecao;
    }

    public int getNumeroCamisa(){
        return numeroCamisa;
    }

    public char[] getPosicao(){
        return posicao;
    }

    public String getNomePopular(){
        return nomePopular;
    }

    public Date getAniversario(){
        return aniversario;
    }

    public String getNomeCamisa(){
        return nomeCamisa;
    }

    public String getClube(){
        return clube;
    }

    public Double getHeight(){
        return height;
    }

    public Double getWeight(){
        return weight;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setSelecao(String selecao){
        this.selecao = selecao;
    }

    public void setNumeroCamisa(int numeroCamisa){
        this.numeroCamisa = numeroCamisa;
    }

    public void setNomePopular(String nomePopular){
        this.nomePopular = nomePopular;
    }

    public void setAniversario(Date aniversario){
        this.aniversario = aniversario;
    }

    public void setNomeCamisa(String nomeCamisa){
        this.nomeCamisa = nomeCamisa;
    }

    public void setPosicao(char []posicao){
        this.posicao = posicao;
    }

    public void setClube(String clube){
        this.clube = clube;
    }

    public void setHeight(Double height){
        this.height = height;
    }

    public void setWeight(Double weight){
        this.weight = weight;
    }

    /** Faltam o SET (aniversario) */

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(getId());
        dos.writeUTF(getSelecao());
        dos.writeInt(numeroCamisa);
        //VERIFICAR MELHORES FORMAS
        String string = new String(posicao);
        dos.writeUTF(string);
        dos.writeUTF(nomePopular);
        DateFormat dt = new SimpleDateFormat("dd.MM.yyyy");
        String dataFormatada = dt.format(aniversario);
        dos.writeUTF(dataFormatada);
        dos.writeUTF(nomeCamisa);
        dos.writeUTF(clube);
        dos.writeDouble(height);
        dos.writeDouble(weight);

        baos.close();
        dos.close();
        return baos.toByteArray();
    }

    public void fromByteArray(byte ba[]) throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        setId(dis.readInt());
        setSelecao(dis.readUTF());
        setNumeroCamisa(dis.readInt());
        char b[] = dis.readUTF().toCharArray();
        setPosicao(b);
        setNomePopular(dis.readUTF());
        //aniversario
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String dt = dis.readUTF();
        Date aniversario = formatter.parse(dt);
        setAniversario(aniversario);
        setNomeCamisa(dis.readUTF());
        setClube(dis.readUTF());
        setHeight(dis.readDouble());
        setWeight(dis.readDouble());
    }
    public String toString(){
        DateFormat dt = new SimpleDateFormat("dd.MM.yyyy");
        String dataFormatada = dt.format(aniversario);
        return id + " " + selecao + " " + numeroCamisa + " " + posicao[0] + posicao[1] + " " + nomePopular + " " + dataFormatada + " " + nomeCamisa + " " + clube 
               + " " + height + " " + weight;
    }
}
