# Consultor de Finanças Pessoais Inteligente (CFPI)
O objetivo deste projeto é, auxiliar pessoas à organizar suas finanças 
pessoas para que possam alcançar os seus objetivos financeiros o mais 
rápido possível com previsibilidade e consistência.

## Testando

Os testes unitários são escritos com [JUnit 5](https://junit.org/junit5/) e
executados através do `JUnit Platform Console Standalone`, um `.jar`
autocontido que não depende de Maven/Gradle. O código de produção continua
sendo compilado apenas com `javac`.

Estrutura de pastas:

- `src/com/cfpi/...` — código de produção, compilado para `out/`.
- `src/test/com/cfpi/...` — testes unitários, espelhando a estrutura de
  `src/com/cfpi`, compilados para `out-test/`.
- `lib/` — dependências de teste (JUnit), não versionadas.

### 1. Baixar o JUnit Platform Console Standalone (apenas na primeira vez)

```bash
mkdir -p lib
curl -sL -o lib/junit-platform-console-standalone-1.10.2.jar \
  https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar
```

### 2. Compilar o código de produção

```bash
javac -d out $(find src/com/cfpi -name "*.java")
```

### 3. Compilar os testes

```bash
javac -cp "lib/junit-platform-console-standalone-1.10.2.jar:out" -d out-test $(find src/test -name "*.java")
```

### 4. Rodar todos os testes

```bash
java -jar lib/junit-platform-console-standalone-1.10.2.jar execute -cp "out:out-test" --scan-classpath out-test
```

### 5. Rodar os testes de uma única classe

Use a flag `--select-class` com o nome totalmente qualificado da classe de
teste:

```bash
java -jar lib/junit-platform-console-standalone-1.10.2.jar execute -cp "out:out-test" --select-class com.cfpi.dominio.arraydinamico.ArrayDinamicoTest
```

## Como contribuir

Toda contribuição é feita a partir da branch `develop`, através de uma branch
de feature/correção que depois vira um Pull Request de volta para `develop`.

### 1. Clonar o repositório

```bash
git clone git@github.com:us0p/cfpi.git
cd cfpi
```

### 2. Atualizar a branch `develop`

Antes de começar qualquer trabalho, garanta que sua `develop` local está em
dia com o repositório remoto:

```bash
git checkout develop
git pull origin develop
```

### 3. Criar uma nova branch a partir da `develop`

Crie uma branch com um nome descritivo para a sua mudança (ex.:
`feat/nome_da_feature`, `fix/descricao_do_bug`):

```bash
git checkout -b minha-branch develop
```

### 4. Desenvolver e commitar as mudanças

Faça as alterações necessárias, adicione os arquivos modificados e crie os
commits:

```bash
git add .
git commit -m "Descrição da mudança"
```

### 5. Enviar a branch para o repositório remoto

```bash
git push -u origin minha-branch
```

### 6. Abrir um Pull Request para a `develop`

Com a branch enviada, abra um Pull Request no GitHub da sua branch para a
`develop`. Você pode fazer isso pela interface do GitHub ou usando a
[GitHub CLI](https://cli.github.com/):

```bash
gh pr create --base develop --head minha-branch --title "Título do PR" --body "Descrição das mudanças"
```

Após a abertura, aguarde a revisão e aprovação para que o PR seja mesclado na
`develop`.

