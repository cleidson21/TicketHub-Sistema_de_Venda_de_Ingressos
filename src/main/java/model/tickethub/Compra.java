/**
 * Sistema Operacional: Windows 10 - 64 Bits
 * Versão Da Linguagem: Java JDK version "21.0.4" 2024-07-16
 * Autor: Cleidson Ramos de Carvalho
 * Componente Curricular: EXA 863 - MI Programação
 * Concluído em: 05/12/2024
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
import java.util.Date;

/**
 * Representa uma compra de ingressos no sistema de venda de ingressos.
 * Uma compra contém informações sobre o usuário, o ingresso, a data da compra,
 * o pagamento e a situação do feedback.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class Compra implements Serializable {
    private final Usuario usuario; // O usuário que realizou a compra
    private final Ingresso ingresso; // O ingresso comprado
    private final Date data; // A data em que a compra foi realizada
    private final Pagamento pagamento; // O pagamento relacionado à compra
    private Boolean isFeedBack; // Indica se o feedback foi fornecido
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que inicializa o objeto Compra com as informações fornecidas.
     *
     * @param usuario         O usuário que realizou a compra
     * @param ingresso        O ingresso adquirido
     * @param data            A data da compra
     * @param metodoPagamento O metodo de pagamento utilizado
     */
    public Compra(Usuario usuario, Ingresso ingresso, Date data, String metodoPagamento) {
        this.usuario = usuario;
        this.ingresso = ingresso;
        this.data = data;
        this.isFeedBack = false; // Inicializa o Situação de feedback como "não fornecido"
        this.pagamento = new Pagamento(metodoPagamento, ingresso.getPreco(), usuario); // Cria um pagamento vinculado à compra
    }

    /**
     * Finaliza a compra verificando se o pagamento foi concluído.
     * Se o pagamento for realizado com sucesso, uma notificação é enviada ao usuário.
     *
     * @return true se a compra foi finalizada com sucesso, false caso contrário
     */
    public Boolean finalizarCompra() {
        if (pagamento.realizarPagamento()) { // Verifica se o pagamento foi concluído com sucesso
            // Cria e envia uma notificação informando o sucesso da compra
            Notificacao notificacao = new Notificacao(
                    "Compra finalizada com sucesso para o evento: " + ingresso.getEvento().getNome(),
                    usuario
            );
            notificacao.enviar();
            return true; // Compra finalizada com sucesso
        } else {
            return false; // Falha no pagamento
        }
    }

    /**
     * Retorna o usuário que realizou a compra.
     *
     * @return O usuário da compra
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Retorna o ingresso adquirido na compra.
     *
     * @return O ingresso adquirido
     */
    public Ingresso getIngresso() {
        return ingresso;
    }

    /**
     * Retorna a data em que a compra foi realizada.
     *
     * @return A data da compra
     */
    public Date getData() {
        return data;
    }

    /**
     * Retorna o status de feedback da compra.
     *
     * @return true se o feedback foi fornecido, false caso contrário
     */
    public Boolean getFeedBack() {
        return isFeedBack;
    }

    /**
     * Define o status de feedback da compra.
     *
     * @param feedBack O novo status de feedback
     */
    public void setFeedBack(Boolean feedBack) {
        isFeedBack = feedBack; // Atualiza o status de feedback
    }
}
