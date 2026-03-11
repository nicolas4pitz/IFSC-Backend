package pbe.caixinha;

public class BancoBens {

    String descricao;
    String data;
    String local;
    String contato;


    public BancoBens(String descricao, String data, String local, String contato) {
        this.descricao = descricao;
        this.data = data;
        this.local = local;
        this.contato = contato;
    }


    @Override
    public String toString() {
        return "BancoBens{" +
                "descricao='" + descricao + '\'' +
                ", data='" + data + '\'' +
                ", local='" + local + '\'' +
                ", contato='" + contato + '\'' +
                '}';
    }
}
