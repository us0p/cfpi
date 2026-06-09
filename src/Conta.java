public abstract class Conta {

    private static int contadorId = 1;

    private int id;
    private double valorConta;
    private String numeroConta;
    private String nomeDono;
    private String moeda;
    private Banco banco;
    private Cartao cartao;

    public Conta() {
        this.id = contadorId++;
    }

    public Conta(double valorConta, String numeroConta, String nomeDono, String moeda, Banco banco, Cartao cartao) {
        this.id = contadorId++;
        this.valorConta = valorConta;
        this.numeroConta = numeroConta;
        this.nomeDono = nomeDono;
        this.moeda = moeda;
        this.banco = banco;
        this.cartao = cartao;
    }

    public Conta(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id; }

    public double getValorConta() {
        return valorConta;
    }
    public void setValorConta(double valorConta) {
        this.valorConta = valorConta;
    }

    public String getNumeroConta() {
        return numeroConta;
    }
    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getNomeDono() {
        return nomeDono;
    }
    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getMoeda() {
        return moeda;
    }
    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public Banco getBanco() {
        return banco;
    }
    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Cartao getCartao() {
        return cartao;
    }
    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }
}