# Modelo de Relacionamento de Entidades - (ERM)
## Banco
- nome
- id
- pais

## Conta
- id
- tipo (poupança, corrente, investimento)
- valorConta
- numeroConta
- nomeDono
- moeda (BRL, USD, EUR, etc)
- **Banco**

## Cartao
- id
- vencimento
- limite
- **Conta**

## Transacao
- id
- descricao
- moeda
- **Conta**
- tipo (credito, debito)
- status (completeda, executando, completada)
- **Cartao**
- data
- valor
- categoria (Transferência, Depósito, Saque, etc)

## Investimento
- id
- nomeAtivo
- total
- moeda
- **Conta**
- tipoAtivo (Tesouro, Ação, FII, etc)
- quantidade
- valorTotalAtivo
- imposto
- data
- valorRealizado (valor do lucro/prejuizo em caso de venda de ativos)
- tipoAcao (compra, venda)
