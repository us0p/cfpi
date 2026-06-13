package com.cfpi.dominio.entidades.usuario;

import com.cfpi.dominio.Identificavel;
import com.cfpi.dominio.arraydinamico.ArrayDinamico;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Usuario implements Identificavel {

    private static int contadorId = 1;
    private static final int CAPACIDADE_INICIAL_OBJETIVOS = 10;
    private static final int CAPACIDADE_INICIAL_CONTAS = 10;

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String dataNascimento;
    private ArrayDinamico<Objetivo> objetivos;
    private ArrayDinamico<Conta> contas;

    /**
     * Construtor de pesquisa/placeholder, usado apenas para representar uma
     * referência a um usuário. Não aplica nenhuma validação.
     */
    public Usuario() {
        this.id = contadorId++;
        this.objetivos = new ArrayDinamico<>(Objetivo.class, CAPACIDADE_INICIAL_OBJETIVOS);
        this.contas = new ArrayDinamico<>(Conta.class, CAPACIDADE_INICIAL_CONTAS);
    }

    /**
     * Cria um usuário com os dados informados.
     *
     * <p><b>Validações previstas (a implementar):</b></p>
     * <ul>
     *   <li>{@code nome}: após {@code trim()}, deve ter ao menos 3
     *       caracteres e conter apenas letras (Unicode) e espaços (permite
     *       nomes compostos, ex: "Ana Maria").</li>
     *   <li>{@code cpf}: deve ser uma string com exatamente 11 dígitos
     *       numéricos (regex {@code ^\d{11}$}).</li>
     *   <li>{@code telefone}: deve ser uma string com exatamente 11 dígitos
     *       numéricos (regex {@code ^\d{11}$}), representando DDD (2
     *       dígitos) + número (9 dígitos).</li>
     *   <li>{@code dataNascimento}: deve estar no formato ISO
     *       {@code yyyy-MM-dd} (validável via
     *       {@code java.time.LocalDate#parse(CharSequence)}) e deve ser
     *       estritamente anterior a {@code java.time.LocalDate#now()}
     *       (hoje e datas futuras são inválidas).</li>
     * </ul>
     *
     * <p><b>Efeito colateral:</b> inicializa as coleções internas de
     * objetivos e contas.</p>
     *
     * @param nome           nome completo do usuário.
     * @param cpf            CPF do usuário (somente dígitos, 11 caracteres).
     * @param telefone       telefone do usuário (somente dígitos, 11 caracteres).
     * @param dataNascimento data de nascimento no formato {@code yyyy-MM-dd}.
     * @throws ValidacaoException se algum dos campos acima for inválido. (a
     *         ser lançada quando a validação for implementada)
     */
    public Usuario(String nome, String cpf, String telefone, String dataNascimento) {
        this.id = contadorId++;

        validarNome(nome);
        validarCpf(cpf);
        validarTelefone(telefone);
        validarDataNascimento(dataNascimento);

        this.nome = nome.trim();
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.objetivos = new ArrayDinamico<>(Objetivo.class, CAPACIDADE_INICIAL_OBJETIVOS);
        this.contas = new ArrayDinamico<>(Conta.class, CAPACIDADE_INICIAL_CONTAS);
    }

    /**
     * Construtor de pesquisa/placeholder, usado apenas para representar uma
     * referência a um usuário já existente a partir do seu {@code id}. Não
     * aplica nenhuma validação e não altera a sequência automática de ids.
     */
    public Usuario(int id) {
        this.id = id;
        this.objetivos = new ArrayDinamico<>(Objetivo.class, CAPACIDADE_INICIAL_OBJETIVOS);
        this.contas = new ArrayDinamico<>(Conta.class, CAPACIDADE_INICIAL_CONTAS);
    }

    // -------------------------------------------------------------------------
    // Métodos privados de validação
    // -------------------------------------------------------------------------

    private static void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new ValidacaoException("O nome deve ter ao menos 3 caracteres.");
        }
        if (!nome.trim().matches("[\\p{L} ]+")) {
            throw new ValidacaoException("O nome deve conter apenas letras e espaços.");
        }
    }

    private static void validarCpf(String cpf) {
        if (cpf == null || !cpf.matches("^\\d{11}$")) {
            throw new ValidacaoException("O CPF deve conter exatamente 11 dígitos numéricos.");
        }
    }

    private static void validarTelefone(String telefone) {
        if (telefone == null || !telefone.matches("^\\d{11}$")) {
            throw new ValidacaoException("O telefone deve conter exatamente 11 dígitos numéricos.");
        }
    }

    private static void validarDataNascimento(String dataNascimento) {
        if (dataNascimento == null) {
            throw new ValidacaoException("A data de nascimento não pode ser nula.");
        }
        LocalDate data;
        try {
            data = LocalDate.parse(dataNascimento);
        } catch (DateTimeParseException e) {
            throw new ValidacaoException("A data de nascimento deve estar no formato yyyy-MM-dd.");
        }
        if (!data.isBefore(LocalDate.now())) {
            throw new ValidacaoException("A data de nascimento deve ser anterior à data atual.");
        }
    }

    // -------------------------------------------------------------------------

    @Override
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do usuário.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Usuario(String, String, String, String)} para
     * {@code nome}.</p>
     *
     * @param nome novo nome do usuário.
     * @throws ValidacaoException se {@code nome} for inválido. (a ser
     *         lançada quando a validação for implementada)
     */
    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do usuário.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Usuario(String, String, String, String)} para
     * {@code cpf}.</p>
     *
     * @param cpf novo CPF (somente dígitos, 11 caracteres).
     * @throws ValidacaoException se {@code cpf} for inválido. (a ser
     *         lançada quando a validação for implementada)
     */
    public void setCpf(String cpf) {
        validarCpf(cpf);
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o telefone do usuário.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Usuario(String, String, String, String)} para
     * {@code telefone}.</p>
     *
     * @param telefone novo telefone (somente dígitos, 11 caracteres).
     * @throws ValidacaoException se {@code telefone} for inválido. (a ser
     *         lançada quando a validação for implementada)
     */
    public void setTelefone(String telefone) {
        validarTelefone(telefone);
        this.telefone = telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    /**
     * Define a data de nascimento do usuário.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Usuario(String, String, String, String)} para
     * {@code dataNascimento} (formato ISO válido e estritamente anterior a
     * hoje).</p>
     *
     * @param dataNascimento nova data de nascimento, formato {@code yyyy-MM-dd}.
     * @throws ValidacaoException se {@code dataNascimento} for inválida. (a
     *         ser lançada quando a validação for implementada)
     */
    public void setDataNascimento(String dataNascimento) {
        validarDataNascimento(dataNascimento);
        this.dataNascimento = dataNascimento;
    }

    /**
     * Retorna o CPF do usuário formatado para exibição no padrão
     * {@code "xxx.xxx.xxx-xx"}.
     *
     * <p>Não altera o valor armazenado internamente em {@code cpf} (que
     * permanece como string crua de 11 dígitos); a formatação é apenas para
     * apresentação. Pressupõe que {@code cpf} já é uma string válida de 11
     * dígitos (garantido pelas validações de {@link #Usuario(String,
     * String, String, String)} / {@link #setCpf(String)} quando
     * implementadas).</p>
     *
     * @return o CPF formatado, ou {@code null} se {@code cpf} for {@code null}.
     */
    public String getCpfFormatado() {
        if (cpf == null) {
            return null;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    /**
     * Retorna o telefone do usuário formatado para exibição no padrão
     * {@code "(xx) xxxxx-xxxx"}.
     *
     * <p>Não altera o valor armazenado internamente em {@code telefone}.
     * Pressupõe que {@code telefone} já é uma string válida de 11 dígitos
     * (garantido pelas validações de {@link #Usuario(String, String,
     * String, String)} / {@link #setTelefone(String)} quando
     * implementadas).</p>
     *
     * @return o telefone formatado, ou {@code null} se {@code telefone} for
     *         {@code null}.
     */
    public String getTelefoneFormatado() {
        if (telefone == null) {
            return null;
        }
        return "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 7) + "-" + telefone.substring(7, 11);
    }

    public boolean adicionarObjetivo(Objetivo objetivo) {
        return objetivos.inserir(objetivo);
    }

    public Objetivo[] getObjetivos() {
        return objetivos.getArr();
    }

    /**
     * Pesquisa, dentre os objetivos deste usuário, aquele com o {@code id}
     * informado.
     *
     * @param id id do objetivo procurado.
     * @return o {@link Objetivo} correspondente, ou {@code null} se não
     *         encontrado.
     */
    public Objetivo pesquisarObjetivoPorId(int id) {
        for (Objetivo objetivo : objetivos.getArr()) {
            if (objetivo == null) break;
            if (objetivo.getId() == id) {
                return objetivo;
            }
        }
        return null;
    }

    /**
     * Pesquisa, dentre os objetivos deste usuário, aquele com o
     * {@code nome} informado.
     *
     * <p>Comparação case-insensitive, após {@code trim()} de ambos os
     * lados.</p>
     *
     * @param nome nome do objetivo procurado.
     * @return o {@link Objetivo} correspondente, ou {@code null} se não
     *         encontrado ou se {@code nome} for {@code null}.
     */
    public Objetivo pesquisarObjetivoPorNome(String nome) {
        if (nome == null) {
            return null;
        }
        String alvo = nome.trim();
        for (Objetivo objetivo : objetivos.getArr()) {
            if (objetivo == null) break;
            if (objetivo.getNome() != null && objetivo.getNome().trim().equalsIgnoreCase(alvo)) {
                return objetivo;
            }
        }
        return null;
    }

    public boolean adicionarConta(Conta conta) {
        return contas.inserir(conta);
    }

    public Conta[] getContas() {
        return contas.getArr();
    }

    /**
     * Pesquisa, dentre as contas deste usuário, aquela com o {@code id}
     * informado.
     *
     * @param id id da conta procurada.
     * @return a {@link Conta} correspondente, ou {@code null} se não
     *         encontrada.
     */
    public Conta pesquisarContaPorId(int id) {
        for (Conta conta : contas.getArr()) {
            if (conta == null) break;
            if (conta.getId() == id) {
                return conta;
            }
        }
        return null;
    }

    /**
     * Pesquisa, dentre as contas deste usuário, aquela com o
     * {@code numeroConta} informado.
     *
     * <p>Comparação exata (case-sensitive) de {@code numeroConta}, após
     * {@code trim()}.</p>
     *
     * @param numeroConta número da conta procurada.
     * @return a {@link Conta} correspondente, ou {@code null} se não
     *         encontrada ou se {@code numeroConta} for {@code null}.
     */
    public Conta pesquisarContaPorNumero(String numeroConta) {
        if (numeroConta == null) {
            return null;
        }
        String alvo = numeroConta.trim();
        for (Conta conta : contas.getArr()) {
            if (conta == null) break;
            if (conta.getNumeroConta() != null && conta.getNumeroConta().trim().equals(alvo)) {
                return conta;
            }
        }
        return null;
    }

    /**
     * Remove, das contas deste usuário, a conta com o {@code id} informado.
     *
     * <p><b>Mecânica prevista (a implementar):</b> localizar a conta com o
     * {@code id} informado em {@code contas} e removê-la do
     * {@link ArrayDinamico} interno (mesma mecânica de
     * {@code BancoStoreImpl#remover(int)}).</p>
     *
     * <p><b>Regra de negócio prevista (a implementar):</b> se a conta
     * encontrada possuir {@code conta.getLimiteCreditoUtilizado() > 0}
     * (pendência de limite de crédito), a remoção é rejeitada.</p>
     *
     * @param id id da conta a ser removida.
     * @return {@code true} se a conta foi encontrada e removida,
     *         {@code false} caso contrário.
     * @throws RegraNegocioException se a conta encontrada possuir limite de
     *         crédito utilizado pendente. (a ser lançada quando a regra for
     *         implementada)
     */
    public boolean removerConta(int id) {
        Conta alvo = null;
        int indice = -1;
        Conta[] arr = contas.getArr();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) break;
            if (arr[i].getId() == id) {
                alvo = arr[i];
                indice = i;
                break;
            }
        }

        if (alvo == null) {
            return false;
        }

        if (alvo.getLimiteCreditoUtilizado() > 0) {
            throw new RegraNegocioException(
                    "Não é possível remover a conta pois há limite de crédito utilizado pendente.");
        }

        return contas.remover(indice);
    }
}