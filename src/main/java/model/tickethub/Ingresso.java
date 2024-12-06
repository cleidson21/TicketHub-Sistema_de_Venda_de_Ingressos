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
 */
package model.tickethub;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um ingresso para um evento. Cada ingresso contém informações
 * sobre o evento, o preço, o assento e a situação do ingresso (ativo ou cancelado).
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class Ingresso implements Serializable {
    private Evento evento;   // Evento associado ao ingresso
    private final Double preco;    // Preço do ingresso
    private final String assento;  // Identificação do assento
    private Boolean status;  // Status do ingresso: true para ativo, false para cancelado
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que inicializa um ingresso com o evento, preço e assento fornecidos.
     * Valida os parâmetros antes de atribuí-los.
     * @param evento O evento ao qual o ingresso está associado
     * @param preco O preço do ingresso (não pode ser negativo)
     * @param assento A identificação do assento (não pode ser nulo ou vazio)
     * @throws IllegalArgumentException Se o evento for nulo, o preço for negativo ou o assento for inválido.
     */
    public Ingresso(Evento evento, Double preco, String assento) {
        // Verifica se cada um dos atributos não é nulo antes de criar o objeto.
        if (evento == null) {
            throw new IllegalArgumentException("O evento não pode ser nulo.");
        }
        if (preco == null || preco < 0) {
            throw new IllegalArgumentException("O preço do ingresso não pode ser nulo ou negativo.");
        }
        if (assento == null || assento.trim().isEmpty()) {
            throw new IllegalArgumentException("O assento não pode ser nulo ou vazio.");
        }
        this.evento = evento;
        this.preco = preco;
        this.assento = assento;
        this.status = true;  // O ingresso começa como ativo por padrão
    }

    // Getters e Setters para acessar e modificar os atributos privados

    /**
     * Retorna o evento associado ao ingresso.
     * @return o evento do ingresso
     */
    public Evento getEvento() {
        return evento;
    }

    /**
     * Define o evento associado ao ingresso. O evento não pode ser nulo.
     * @param evento o evento a ser associado
     * @throws IllegalArgumentException se o evento for nulo
     */
    public void setEvento(Evento evento) {
        // Verifica se o evento não é vazio.
        if (evento == null) {
            throw new IllegalArgumentException("O evento não pode ser nulo.");
        }
        this.evento = evento;
    }

    /**
     * Retorna o preço do ingresso.
     * @return o preço do ingresso
     */
    public Double getPreco() {
        return preco;
    }

    /**
     * Retorna o assento associado ao ingresso.
     * @return o assento do ingresso
     */
    public String getAssento() {
        return assento;
    }

    /**
     * Define o status do ingresso (ativo ou cancelado).
     * @param status o novo status do ingresso
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * Cancela o ingresso se o evento ainda estiver ativo e o ingresso não estiver cancelado.
     * @return true se o cancelamento for bem-sucedido, false caso contrário
     */
    public Boolean cancelar() {
        if (evento.isAtivo() && status) {
            setStatus(false);  // Marca o ingresso como cancelado
            return true;       // Retorna true para indicar que o cancelamento foi bem-sucedido
        } else {
            return false;      // Retorna false se não for possível cancelar
        }
    }

    // Métodos equals e hashCode

    /**
     * Compara o ingresso atual com outro objeto para verificar se são iguais.
     * @param o o objeto a ser comparado
     * @return true se os ingressos forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingresso ingresso = (Ingresso) o;
        return Objects.equals(evento, ingresso.evento) &&
                Objects.equals(preco, ingresso.preco) &&
                Objects.equals(assento, ingresso.assento);
    }

    /**
     * Retorna o código hash do ingresso, baseado no evento, preço e assento.
     * @return o código hash do ingresso
     */
    @Override
    public int hashCode() {
        return Objects.hash(evento, preco, assento);
    }
}
