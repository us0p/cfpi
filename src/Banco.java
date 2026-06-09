public abstract class Banco {

    private static int contadorId = 1;

    private int id;
    private String nome;
    private String pais;

    public Banco() {
        this.id = contadorId++;
    }

    public Banco(String nome, String pais) {
        this.id = contadorId++;
        this.nome = nome;
        this.pais = pais;
    }

    public Banco(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }
}