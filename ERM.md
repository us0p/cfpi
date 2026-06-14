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

    DEBITO {
        string tipo "credito, avista"
    }

    CREDITO {
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

    ACAO {
        double impostoPadrao "0.15 (15%)"
    }

    CDB {
        double impostoPadrao "0.15 (15%)"
    }

    CRA {
        double impostoPadrao "0 (isento)"
    }

    CRI {
        double impostoPadrao "0 (isento)"
    }

    CRIPTO {
        double impostoPadrao "0.15 (15%)"
    }

    DEB {
        double impostoPadrao "0.15 (15%)"
    }

    FII {
        double impostoPadrao "0.175 (17,5%)"
    }

    LCA {
        double impostoPadrao "0 (isento)"
    }

    LCI {
        double impostoPadrao "0 (isento)"
    }

    PGBL {
        double impostoPadrao "0.15 (15%)"
    }

    TESOURODIRETO {
        double impostoPadrao "0.15 (15%)"
    }

    VGBL {
        double impostoPadrao "0.15 (15%)"
    }

    USUARIO ||--o{ OBJETIVO : possui
    USUARIO o|--o{ CONTA : possui
    USUARIO }o--o{ BANCO : "possui contas em"
    BANCO o|--o{ CONTA : "associada a"
    CONTA o|--o{ TRANSACAO : registra
    CONTA o|--o{ INVESTIMENTO : registra

    TRANSACAO ||--|| DEBITO : "é um"
    TRANSACAO ||--|| CREDITO : "é um"

    INVESTIMENTO ||--|| ACAO : "é um"
    INVESTIMENTO ||--|| CDB : "é um"
    INVESTIMENTO ||--|| CRA : "é um"
    INVESTIMENTO ||--|| CRI : "é um"
    INVESTIMENTO ||--|| CRIPTO : "é um"
    INVESTIMENTO ||--|| DEB : "é um"
    INVESTIMENTO ||--|| FII : "é um"
    INVESTIMENTO ||--|| LCA : "é um"
    INVESTIMENTO ||--|| LCI : "é um"
    INVESTIMENTO ||--|| PGBL : "é um"
    INVESTIMENTO ||--|| TESOURODIRETO : "é um"
    INVESTIMENTO ||--|| VGBL : "é um"
```

## Heranças (generalização/especialização)

O Mermaid `erDiagram` não possui notação nativa para herança, então as
relações `"é um"` acima representam generalização/especialização
(subtipos), não associações entre instâncias distintas:

- `DEBITO` e `CREDITO` são subtipos de `TRANSACAO` e herdam todos os seus
  atributos (`id`, `descricao`, `data`, `valor`, `categoria`). `DEBITO`
  adiciona o atributo próprio `tipo`. `CREDITO` não adiciona atributos
  próprios.
- `ACAO`, `CDB`, `CRA`, `CRI`, `CRIPTO`, `DEB`, `FII`, `LCA`, `LCI`, `PGBL`,
  `TESOURODIRETO` e `VGBL` são subtipos de `INVESTIMENTO` e herdam todos os
  seus atributos. Cada subtipo não adiciona atributos de instância, apenas
  define a constante `impostoPadrao`, usada no cálculo do imposto sobre o
  ganho de capital em operações de venda.
