/**
 * Sistema Operacional: Windows 10 - 64 Bits
 * Versão Da Linguagem: Java JDK version "21.0.4" 2024-07-16
 * Autor: Cleidson Ramos de Carvalho
 * Componente Curricular: EXA 863 - MI Programação
 * Concluido em: 05/12/2024
 * Declaro que este código foi elaborado por mim de forma
 * individual e não contém nenhum trecho de código de outro
 * colega ou de outro autor, tais como provindos de livros e
 * apostilas, e páginas ou documentos eletrônicos da Internet.
 * Qualquer trecho de código de outra autoria que não a minha
 * está destacado com uma citação para o autor e a fonte do código,
 * e estou ciente que estes trechos não serão considerados para fins de avaliação.
 **/


package model.tickethub;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Classe que representa um evento. Cada evento possui um nome, descrição, data e uma lista de assentos disponíveis.
 * Além disso, armazena feedbacks recebidos sobre o evento.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class Evento implements Serializable {

    // Atributos da classe

    private String nome;  // Nome do evento
    private String descricao;  // Descrição do evento
    private Date data;  // Data em que o evento ocorre
    private final Set<String> assentosDisponiveis;  // Lista de assentos disponíveis para o evento
    private final List<FeedBack> feedbackRecebidos;  // Lista de feedbacks recebidos sobre o evento
    private Boolean iniciadoVenda;  // Flag que indica se a venda de ingressos foi iniciada
    @Serial
    private static final long serialVersionUID = 1L;  // ID para garantir a compatibilidade de versões durante a serialização

    /**
     * Construtor que inicializa o evento com o nome, descrição e data fornecidos.
     * @param nome O nome do evento
     * @param descricao A descrição do evento
     * @param data A data em que o evento ocorre
     */
    public Evento(String nome, String descricao, Date data) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.assentosDisponiveis = new HashSet<>();  // Inicializa a lista de assentos disponíveis
        this.feedbackRecebidos = new ArrayList<>();  // Inicializa a lista de feedbacks
        this.iniciadoVenda = false;  // Inicializa a flag de venda de ingressos
    }

    // Getters e Setters

    /**
     * Retorna o nome do evento.
     * @return O nome do evento
     */
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna a data do evento.
     * @return A data do evento
     */
    public Date getData() {
        return data;
    }

    /**
     * Define uma nova data do evento.
     * @param data A nova nova data do evento.
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Retorna se a venda do evento já foi iniciada.
     * @return A venda do evento já foi iniciada.
     */
    public Boolean getIniciadoVenda() {
        return iniciadoVenda;
    }

    /**
     * Define se a venda já foi iniciada.
     * @param iniciadoVenda O novo situação sobre a venda ter sido iniciada.
     */
    public void setIniciadoVenda(Boolean iniciadoVenda) {
        this.iniciadoVenda = iniciadoVenda;
    }

    /**
     * Retorna a descrição do evento.
     * @return A descrição do evento.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define uma nova descrição do evento.
     * @param descricao  A nova descrição do evento..
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Adiciona assentos disponíveis ao evento com base no número de colunas e fileiras.
     * Para cada coluna e fileira, cria um assento com a combinação "Letra da coluna + número da fileira".
     * @param colunas O número de colunas a serem adicionadas
     * @param fileiras O número de fileiras a serem adicionadas
     */
    public void adicionarAssento(int colunas, int fileiras) {
        // Loop para cada coluna, de 0 até colunas-1
        for (int col = 0; col < colunas; col++) {
            // Converte a coluna para uma letra (A, B, C, ...)
            char letraColuna = (char) ('A' + col);

            // Loop para cada fileira, de 0 até fileiras-1
            for (int fil = 0; fil < fileiras; fil++) {
                // Adiciona o assento no formato "A0", "A1", etc.
                assentosDisponiveis.add(letraColuna + String.valueOf(fil));
            }
        }
    }

    /**
     * Retorna a lista de assentos disponíveis para o evento.
     * @return A lista de assentos disponíveis
     */
    public Set<String> getAssentosDisponiveis() {
        return assentosDisponiveis;
    }

    /**
     * Retorna a quantidade total de assentos disponíveis para o evento.
     * @return A quantidade de assentos disponíveis
     */
    public Integer getQuantidadeAssentosDisponiveis() {
        return assentosDisponiveis.size();
    }

    /**
     * Retorna a lista de feedbacks recebidos sobre o evento.
     * @return A lista de feedbacks recebidos
     */
    public List<FeedBack> getFeedbackRecebidos() {
        return feedbackRecebidos;
    }

    /**
     * Remove um assento da lista de disponíveis, caso ele tenha sido ocupado.
     * @param assento O assento a ser removido
     * @throws RuntimeException Se o assento não estiver disponível na lista
     */
    public void removerAssento(String assento) {
        // Se o assento não estiver na lista, lança uma exceção
        if (!assentosDisponiveis.remove(assento)) {
            throw new RuntimeException("Assento não encontrado para remoção.");
        }
    }

    /**
     * Verifica se o evento continua ativo, ou seja, se a data atual é anterior à data do evento.
     * @return True se o evento estiver ativo, false caso contrário
     */
    public Boolean isAtivo() {
        Date dataAtual = new Date();  // Obtém a data atual
        // Retorna true se a data atual for anterior à data do evento
        return dataAtual.before(data);
    }

    /**
     * Adiciona um feedback recebido sobre o evento.
     * @param feedBack O feedback a ser adicionado
     */
    public void arquivarFeedbacks(FeedBack feedBack) {
        feedbackRecebidos.add(feedBack);  // Adiciona o feedback à lista
    }

    /**
     * Adiciona um assento unitário à lista de assentos disponíveis.
     * @param assento O código do assento a ser adicionado
     */
    public void adicionarAssentoUnitario(String assento) {
        assentosDisponiveis.add(assento);  // Adiciona o assento à lista
    }
}
