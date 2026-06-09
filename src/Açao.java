public class Açao extends Investimento {

    private double AçaoDesejada; //precisa de uma entrada pra colocar a taxa e a ação desejada
    private static int contadorId = 1;

    public Açao() {
        super();
    }

    public Açao(String nomeAtivo, double total, String moeda, Conta conta, double quantidade, double valorTotalAtivo, double imposto, String data, double valorRealizado, String tipoAcao) {
        super(nomeAtivo, total, moeda, conta, quantidade, valorTotalAtivo, imposto, data, valorRealizado, tipoAcao);
    }

    public Açao(int id) {
        super(id);
    }
    public double getAçaoDesejada(){
        return AçaoDesejada;
    }
}