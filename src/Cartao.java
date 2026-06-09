public abstract class Cartao {

    private static int contadorId = 1;

    private int id;
    private String vencimento;
    private double limite;
    private Conta conta;

    public Cartao() {
        this.id = contadorId++;
    }

    public Cartao(String vencimento, double limite, Conta conta) {
        this.id = contadorId++;
        this.vencimento = vencimento;
        this.limite = limite;
        this.conta = conta;
    }

    public Cartao(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getVencimento() {
        return vencimento;
    }
    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public double getLimite() {
        return limite;
    }
    public void setLimite(double limite) {
        this.limite = limite;
    }

    public Conta getConta() {
        return conta;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }
}