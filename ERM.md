# Modelo de Relacionamento de Entidades - (ERM)

```mermaid
erDiagram
    USUARIO {
        int id
        string nome
        string cpf
        string telefone
        string dataNascimento
    }

    BANCO {
        int id
        string nome
        int codigo
    }

    CONTA {
        int id
        string tipo "poupanca, corrente, investimento"
        double valorConta
        string numeroConta
        string moeda "BRL, USD, EUR, etc"
        double limiteCredito
        double limiteCreditoUtilizado
    }

    OBJETIVO {
        int id
        string nome
        double valor
    }

    TRANSACAO {
        int id
        string descricao
        string data
        double valor
        string categoria "Transferencia, Deposito, Saque, etc"
    }

    INVESTIMENTO {
        int id
        string nomeAtivo
        double valor
        double quantidade
        double imposto
        string data
        double valorRealizado
        string operacao "compra, venda"
    }

    USUARIO ||--o{ OBJETIVO : possui
    USUARIO o|--o{ CONTA : possui
    USUARIO }o--o{ BANCO : "possui contas em"
    BANCO o|--o{ CONTA : "associada a"
    CONTA o|--o{ TRANSACAO : registra
    CONTA o|--o{ INVESTIMENTO : registra
```

## Subtipos de Transacao
- Debito: tipo (credito, avista)
- Credito

## Subtipos de Investimento
(com IMPOSTO_PADRAO sobre o ganho na venda do ativo)
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
