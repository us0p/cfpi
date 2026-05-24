# Consultor de Finanças Pessoais Inteligente (CFPI)
O objetivo deste projeto é, auxiliar pessoas à organizar suas finanças 
pessoas para que possam alcançar os seus objetivos financeiros o mais 
rápido possível com previsibilidade e consistência.

## Modelo de Relacionamento de Entidades - (ERM)
Banco
    - nome
    - id
    - pais

Conta
    - tipo (poupança, corrente, investimento)
    - valorConta
    - id
    - numeroConta
    - nomeDono
    - moeda (BRL, USD, EUR, etc)
    - bancoID

Cartao
    - id
    - vencimento
    - limite
    - contaID

Transacao
    - id
    - descricao
    - moeda
    - contaID
    - tipo (credito, debito)
    - status (completeda, executando, completada)
    - cartaoID
    - data
    - valor
    - categoria (Transferência, Depósito, Saque, etc)

Investimento
    - id
    - nomeAtivo
    - total
    - moeda
    - contaID
    - tipoAtivo (Tesouro, Ação, FII, etc)
    - quantidade
    - valorTotalAtivo
    - imposto
    - data
    - valorRealizado (valor do lucro/prejuizo em caso de venda de ativos)
    - tipoAcao (compra, venda)
