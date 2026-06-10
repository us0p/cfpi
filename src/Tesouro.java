public class Tesouro extends Investimento {

    private static int contadorId = 1;

    private double taxaSelicAtual = 14.50;

    public Tesouro() {
        super();
    }

    public Tesouro(String nomeAtivo, double total, String moeda, Conta conta, double quantidade, double valorTotalAtivo, double imposto, String data, double valorRealizado, String tipoAcao) {
        super(nomeAtivo, total, moeda, conta, quantidade, valorTotalAtivo, imposto, data, valorRealizado, tipoAcao);
    }

    public Tesouro(int id) {
        super(id);
    }

    public double getTaxaSelicAtual() { return taxaSelicAtual; }
}