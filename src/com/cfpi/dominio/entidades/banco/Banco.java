package com.cfpi.dominio.entidades.banco;

import com.cfpi.dominio.Identificavel;
import com.cfpi.dominio.excecoes.ValidacaoException;

public class Banco implements Identificavel {

    private static int contadorId = 1;

    private int id;
    private String nome;
    private int codigo;

    /**
     * Construtor de pesquisa/placeholder, usado apenas para representar uma
     * referência a um banco (ex: como retorno padrão antes de uma busca).
     * Não aplica nenhuma validação.
     */
    public Banco() {
        this.id = contadorId++;
    }

    /**
     * Cria um banco com nome e código informados.
     *
     * <p><b>Validação:</b> {@code nome}, após {@code trim()}, deve conter
     * apenas letras (Unicode) e espaços e ter mais de 3 caracteres;
     * {@code codigo} deve estar no intervalo [100, 999] (3 dígitos).</p>
     *
     * @param nome   nome do banco.
     * @param codigo código numérico do banco (3 dígitos).
     * @throws ValidacaoException se {@code nome} ou {@code codigo} forem
     *         inválidos conforme as regras acima.
     */
    public Banco(String nome, int codigo) {
        validarNome(nome);
        validarCodigo(codigo);

        this.id = contadorId++;
        this.nome = nome.trim();
        this.codigo = codigo;
    }

    // -------------------------------------------------------------------------
    // Métodos privados de validação
    // -------------------------------------------------------------------------

    private static void validarNome(String nome) {
        if (nome == null || nome.trim().length() <= 3) {
            throw new ValidacaoException("O nome do banco deve ter mais de 3 caracteres.");
        }
        if (!nome.trim().matches("[\\p{L} ]+")) {
            throw new ValidacaoException("O nome do banco deve conter apenas letras e espaços.");
        }
    }

    private static void validarCodigo(int codigo) {
        if (codigo < 100 || codigo > 999) {
            throw new ValidacaoException("O código do banco deve estar entre 100 e 999.");
        }
    }

    /**
     * Construtor de pesquisa/placeholder, usado apenas para representar uma
     * referência a um banco já existente a partir do seu {@code id} (ex:
     * para buscas em {@link BancoStore}). Não aplica nenhuma validação e
     * não altera a sequência automática de ids.
     */
    public Banco(int id) {
        this.id = id;
    }

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
     * Define o nome do banco.
     *
     * <p><b>Validação:</b> mesma regra do construtor {@link #Banco(String,
     * int)} — {@code nome}, após {@code trim()}, deve conter apenas letras
     * (Unicode) e espaços e ter mais de 3 caracteres.</p>
     *
     * @param nome novo nome do banco.
     * @throws ValidacaoException se {@code nome} for inválido.
     */
    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome.trim();
    }

    public int getCodigo() {
        return codigo;
    }

    /**
     * Define o código do banco.
     *
     * <p><b>Validação:</b> {@code codigo} deve estar no intervalo
     * [100, 999] (3 dígitos).</p>
     *
     * @param codigo novo código do banco.
     * @throws ValidacaoException se {@code codigo} estiver fora do
     *         intervalo [100, 999].
     */
    public void setCodigo(int codigo) {
        validarCodigo(codigo);
        this.codigo = codigo;
    }
}
