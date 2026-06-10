public class Saque extends Transaçao {

    private static int contadorId = 1;

    public Saque() {
        super();
    }

    public Saque(String descricao, String moeda, Conta conta, String status, Cartao cartao, String data, double valor, String categoria) {
        super(descricao, moeda, conta, status, cartao, data, valor, categoria);
    }

    public Saque(int id) {
        super(id);
    }

    @Override
    public void executar() {
        if (getConta() == null) {
            setStatus("FALHA");
            return;
        }
        if (getConta().getValorConta() < getValor()) {
            setStatus("SALDO_INSUFICIENTE");
            return;
        }
        getConta().setValorConta(getConta().getValorConta() - getValor());
        setStatus("CONCLUIDO");
    }
}