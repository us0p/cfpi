public class Transferencia extends Transaçao {

    private static int contadorId = 1;

    private Conta contaDestino;

    public Transferencia() {
        super();
    }

    public Transferencia(String descricao, String moeda, Conta conta, String status, Cartao cartao, String data, double valor, String categoria, Conta contaDestino) {
        super(descricao, moeda, conta, status, cartao, data, valor, categoria);
        this.contaDestino = contaDestino;
    }

    public Transferencia(int id) {
        super(id);
    }

    public Conta getContaDestino() { return contaDestino; }
    public void setContaDestino(Conta contaDestino) { this.contaDestino = contaDestino; }

    @Override
    public void executar() {
        if (getConta() == null || contaDestino == null) {
            setStatus("FALHA");
            return;
        }
        if (getConta().getValorConta() < getValor()) {
            setStatus("SALDO_INSUFICIENTE");
            return;
        }
        getConta().setValorConta(getConta().getValorConta() - getValor());
        contaDestino.setValorConta(contaDestino.getValorConta() + getValor());
        setStatus("CONCLUIDO");
    }
}