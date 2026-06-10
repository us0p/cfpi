public abstract class Transaçao {

    private static int contadorId = 1;

    private int id;
    private String descricao;
    private String moeda;
    private Conta conta;
    private String status;
    private Cartao cartao;
    private String data;
    private double valor;
    private String categoria;

    public Transaçao() {
        this.id = contadorId++;
    }

    public Transaçao(String descricao, String moeda, Conta conta, String status, Cartao cartao, String data, double valor, String categoria) {
        this.id = contadorId++;
        this.descricao = descricao;
        this.moeda = moeda;
        this.conta = conta;
        this.status = status;
        this.cartao = cartao;
        this.data = data;
        this.valor = valor;
        this.categoria = categoria;
    }

    public Transaçao(int id) {
        this.id = id;
    }

    public abstract void executar();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMoeda() {
        return moeda;
    }
    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public Conta getConta() {
        return conta;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Cartao getCartao() {
        return cartao;
    }
    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}