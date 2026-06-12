package com.cfpi.dominio.entidades.objetivo;

import com.cfpi.dominio.Identificavel;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

public class Objetivo implements Identificavel {

    private static int contadorId = 1;

    private int id;
    private String nome;
    private double valor;
    private Usuario usuario;

    /**
     * Construtor de pesquisa/placeholder, usado apenas para representar uma
     * referência a um objetivo. Não aplica nenhuma validação.
     */
    public Objetivo() {
        this.id = contadorId++;
    }

    /**
     * Cria um objetivo com {@code nome} e {@code valor} informados,
     * associando-o ao {@code usuario}.
     *
     * <p><b>Validações previstas (a implementar):</b></p>
     * <ul>
     *   <li>{@code usuario}: não pode ser {@code null}.</li>
     *   <li>{@code valor}: deve ser maior que zero ({@code valor > 0}).</li>
     *   <li>{@code nome}: não pode coincidir (case-insensitive, após
     *       {@code trim()}) com o nome de nenhum objetivo já existente em
     *       {@code usuario.getObjetivos()}.</li>
     * </ul>
     *
     * <p><b>Efeito colateral:</b> registra este objetivo em
     * {@code usuario} via {@link Usuario#adicionarObjetivo(Objetivo)}.</p>
     *
     * @param nome    nome do objetivo.
     * @param valor   valor-alvo do objetivo (deve ser {@code > 0}).
     * @param usuario usuário dono do objetivo (não pode ser {@code null}).
     * @throws ValidacaoException     se {@code usuario} for {@code null} ou
     *         {@code valor <= 0}. (a ser lançada quando a validação for
     *         implementada)
     * @throws RegraNegocioException se já existir um objetivo de
     *         {@code usuario} com o mesmo {@code nome} (case-insensitive,
     *         após {@code trim()}). (a ser lançada quando a validação for
     *         implementada)
     */
    public Objetivo(String nome, double valor, Usuario usuario) {
        this.id = contadorId++;
        this.nome = nome;
        this.valor = valor;
        this.usuario = usuario;

        if (usuario != null) {
            usuario.adicionarObjetivo(this);
        }


         // Validação do valor
    if (valor <= 0) {
        throw new ValidacaoException("O valor do objetivo deve ser maior que zero.");
    }

    // Validação do nome duplicado
    String nomeTratado = nome.trim();
    boolean nomeExiste = usuario.getObjetivos();
        anyMatch(obj -> obj.getNome().trim().equalsIgnoreCase(nomeTratado));
    if (nomeExiste) {
        throw new RegraNegocioException(
            "Já existe um objetivo com o nome '" + nome + "' para este usuário."
        );
    }
        

    }

    /**
     * Construtor de pesquisa/placeholder, usado apenas para representar uma
     * referência a um objetivo já existente a partir do seu {@code id}. Não
     * aplica nenhuma validação e não altera a sequência automática de ids.
     */
    public Objetivo(int id) {
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
     * Define o nome do objetivo.
     *
     * <p><b>Validação prevista (a implementar):</b> se
     * {@code getUsuario() != null}, lança {@link RegraNegocioException}
     * caso já exista, em {@code getUsuario().getObjetivos()}, outro
     * objetivo (com {@code id} diferente do deste) cujo nome (após
     * {@code trim()}) coincida com {@code nome} ignorando
     * maiúsculas/minúsculas. Renomear para uma variação de
     * maiúsculas/minúsculas do nome atual deste próprio objetivo é
     * permitido. Se {@code getUsuario() == null}, não há checagem de
     * duplicidade.</p>
     *
     * @param nome novo nome do objetivo.
     * @throws RegraNegocioException se já existir outro objetivo do mesmo
     *         usuário com o mesmo nome. (a ser lançada quando a validação
     *         for implementada)
     */
    public void setNome(String nome) {
            if (getUsuario() != null) {
        String nomeTratado = nome.trim();

        for (Objetivo obj : getUsuario().getObjetivos()) {
            if (obj.getId() != this.id &&
                obj.getNome() != null &&
                obj.getNome().trim().equalsIgnoreCase(nomeTratado)) {

                throw new RegraNegocioException(
                    "Já existe outro objetivo com esse nome para este usuário."
                );
            }
        }
    }
        
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    /**
     * Define o valor-alvo do objetivo.
     *
     * <p><b>Validação prevista (a implementar):</b> {@code valor} deve ser
     * maior que zero.</p>
     *
     * @param valor novo valor-alvo (deve ser {@code > 0}).
     * @throws ValidacaoException se {@code valor <= 0}. (a ser lançada
     *         quando a validação for implementada)
     */
    public void setValor(double valor) {
        // Validação: o valor deve ser maior que zero
    if (valor <= 0) {
        throw new ValidacaoException("O valor deve ser maior que zero. Valor fornecido: " + valor);
        
        }
        this.valor = valor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Define o usuário dono do objetivo.
     *
     * <p><b>Validação prevista (a implementar):</b> {@code usuario} não
     * pode ser {@code null}.</p>
     *
     * <p>Ao contrário do construtor {@link #Objetivo(String, double,
     * Usuario)}, este setter <b>não</b> registra automaticamente este
     * objetivo na coleção de objetivos do novo {@code usuario}.</p>
     *
     * @param usuario novo usuário dono do objetivo (não pode ser
     *                {@code null}).
     * @throws ValidacaoException se {@code usuario} for {@code null}. (a
     *         ser lançada quando a validação for implementada)
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
