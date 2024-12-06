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
import java.util.List;

/**
 * Representa um pagamento realizado por um usuário para a compra de um ingresso.
 * Contém o metodo de pagamento, o valor do ingresso, a situação do pagamento e o usuário associado.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class Pagamento implements Serializable {

    private final String metodoPagamento; // Metodo de pagamento escolhido
    private final Double valorIngresso; // Valor do ingresso
    private final Usuario usuario; // Usuário que está realizando o pagamento
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor da classe Pagamento que inicializa os atributos do pagamento.
     * A situação do pagamento é inicializado como false (não realizado).
     * @param metodoPagamento O metodo de pagamento escolhido (ex: boleto, cartão).
     * @param valorIngresso O valor do ingresso que será pago.
     * @param usuario O usuário que está realizando o pagamento.
     */
    public Pagamento(String metodoPagamento, Double valorIngresso, Usuario usuario) {
        this.metodoPagamento = metodoPagamento;
        this.valorIngresso = valorIngresso;
        this.usuario = usuario;
    }

    /**
     * Realiza o pagamento com base no metodo de pagamento escolhido.
     * Simula o processo de pagamento para boleto e cartão.
     * Caso o metodo de pagamento seja "boleto", o pagamento é marcado como concluído.
     * Caso seja "cartão", verifica se o usuário tem um cartão cadastrado para finalizar o pagamento.
     * @return true se o pagamento foi realizado com sucesso, false caso contrário.
     * @throws RuntimeException Se o metodo de pagamento for "cartão" e o usuário não tiver cartões cadastrados.
     * @throws IllegalArgumentException Se o metodo de pagamento for inválido.
     */
    public boolean realizarPagamento() {
        String metodoPagamentoTipo = metodoPagamento.substring(0, Math.min(6, metodoPagamento.length())).toLowerCase();
        switch (metodoPagamentoTipo.toLowerCase()) {
            case "boleto": // Caso de pagamento por boleto
                return true;

            case "cartao": // Caso de pagamento por cartão
                String ultimosDigitos = metodoPagamento.substring(metodoPagamento.lastIndexOf(" ") + 1);
                List<Cartao> cartoes = usuario.getCartoes();
                // Busca o cartão pelo número que termina com os últimos 4 dígitos
                Cartao cartaoSelecionado = cartoes.stream()
                        .filter(c -> c.getNumero().endsWith(ultimosDigitos))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado."));
                if (cartaoSelecionado != null) {
                    return true;
                }
                throw new RuntimeException("Usuário não tem cartão cadastrado!");

            default:
                throw new IllegalArgumentException("Método de pagamento inválido!");
        }
    }

}
