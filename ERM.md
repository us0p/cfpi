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
- limiteCredito
- **Banco**

## Transacao
- id
- descricao
- **Conta**
- data
- valor
- categoria (Transferência, Depósito, Saque, etc)
- subtipos:
  - Debito: tipo (credito, avista)
  - Credito

## Investimento
- id
- nomeAtivo
- valor
- **Conta**
- quantidade
- valorTotalAtivo
- imposto
- data
- valorRealizado (valor do lucro/prejuizo em caso de venda de ativos)
- operacao (compra, venda)
- subtipos (com IMPOSTO_PADRAO sobre o ganho na venda do ativo):
  - Acao: 15%
  - CDB: 15%
  - CRA: 0% (isento)
  - CRI: 0% (isento)
  - Cripto: 15%
  - DEB: 15%
  - FII: 17,5%
  - LCA: 0% (isento)
  - LCI: 0% (isento)
  - PGBL: 15%
  - TesouroDireto: 15%
  - VGBL: 15%

## Objetivo
- id
- nome
- valor
