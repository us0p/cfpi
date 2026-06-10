public abstract class Investimento {

    private static int contadorId = 1;

    private int id;
    private String nomeAtivo;
    private double total;
    private String moeda;
    private Conta conta;
    private double quantidade;
    private double valorTotalAtivo;
    private double imposto;
    private String data;
    private double valorRealizado;
    private String tipoAcao;

    public Investimento() {
        this.id = contadorId++;
    }

    public Investimento(String nomeAtivo, double total, String moeda, Conta conta, double quantidade, double valorTotalAtivo, double imposto, String data, double valorRealizado, String tipoAcao) {
        this.id = contadorId++;
        this.nomeAtivo = nomeAtivo;
        this.total = total;
        this.moeda = moeda;
        this.conta = conta;
        this.quantidade = quantidade;
        this.valorTotalAtivo = valorTotalAtivo;
        this.imposto = imposto;
        this.data = data;
        this.valorRealizado = valorRealizado;
        this.tipoAcao = tipoAcao;
    }

    public Investimento(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNomeAtivo() {
        return nomeAtivo;
    }
    public void setNomeAtivo(String nomeAtivo) {
        this.nomeAtivo = nomeAtivo;
    }

    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
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

    public double getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorTotalAtivo() {
        return valorTotalAtivo;
    }
    public void setValorTotalAtivo(double valorTotalAtivo) {
        this.valorTotalAtivo = valorTotalAtivo;
    }

    public double getImposto() {
        return imposto;
    }
    public void setImposto(double imposto) {
        this.imposto = imposto;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public double getValorRealizado() {
        return valorRealizado;
    }
    public void setValorRealizado(double valorRealizado) {
        this.valorRealizado = valorRealizado;
    }

    public String getTipoAcao() {
        return tipoAcao;
    }
    public void setTipoAcao(String tipoAcao) {
        this.tipoAcao = tipoAcao;
    }
}