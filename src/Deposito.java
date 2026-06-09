
public class Deposito extends Transaçao {

    private static int contadorId = 1;

    public Deposito() {
        super();
    }

    public Deposito(String descricao, String moeda, Conta conta, String status, Cartao cartao, String data, double valor, String categoria) {
        super(descricao, moeda, conta, status, cartao, data, valor, categoria);
    }

    public Deposito(int id) {
        super(id);
    }

    @Override
    public void executar() {
        if (getConta() == null) {
            setStatus("FALHA");
            return;
        }
        getConta().setValorConta(getConta().getValorConta() + getValor());
        setStatus("CONCLUIDO");
    }
}