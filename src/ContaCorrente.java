public class ContaCorrente extends Conta {

    private static int contadorId = 1;

    public ContaCorrente() {
        super();
    }

    public ContaCorrente(double valorConta, String numeroConta, String nomeDono, String moeda, Banco banco, Cartao cartao) {
        super(valorConta, numeroConta, nomeDono, moeda, banco, cartao);
    }

    public ContaCorrente(int id) {
        super(id);
    }
}