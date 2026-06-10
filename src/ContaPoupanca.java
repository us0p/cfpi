public class ContaPoupanca extends Conta {

    private static int contadorId = 1;

    public ContaPoupanca() {
        super();
    }

    public ContaPoupanca(double valorConta, String numeroConta, String nomeDono, String moeda, Banco banco, Cartao cartao) {
        super(valorConta, numeroConta, nomeDono, moeda, banco, cartao);
    }

    public ContaPoupanca(int id) {
        super(id);
    }
}