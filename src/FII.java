public class FII extends Investimento {

    private double taxaFIIatual; //precisa de entrada pra saber qual fundo e quanto tá a taxa
    private static int contadorId = 1;

    public FII() {
        super();
    }

    public FII(String nomeAtivo, double total, String moeda, Conta conta, double quantidade, double valorTotalAtivo, double imposto, String data, double valorRealizado, String tipoAcao) {
        super(nomeAtivo, total, moeda, conta, quantidade, valorTotalAtivo, imposto, data, valorRealizado, tipoAcao);
    }

    public FII(int id) {
        super(id);
    }
    public double getTaxaFIIatual(){
        return taxaFIIatual;
    }
}