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

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um cartao de crédito utilizado no sistema de venda de ingressos.
 * Cada cartão contem informações como nome do titular, tipo, número, validade e CVC.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class Cartao implements Serializable {
    private String nomeTitular;  // Nome do titular do cartão
    private String tipoCartao;  // Tipo do cartão (Ex: Credito, Debito)
    private String numero;  // Número do cartão de crédito
    private String validade;  // Data de validade do cartão (MM/AA)
    private String cvc;  // Código de verificação do cartão (CVC)
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que inicializa o objeto Cartao com as informações fornecidas.
     *
     * @param nomeTitular O nome do titular do cartão
     * @param tipoCartao O tipo do cartão (Ex: Credito, Debito)
     * @param numero O número do cartão de crédito
     * @param validade A data de validade do cartão (MM/AA)
     * @param cvc O código de verificação do cartão (CVC)
     */
    public Cartao(String nomeTitular, String tipoCartao, String numero, String validade, String cvc) {
        this.nomeTitular = nomeTitular;
        this.tipoCartao = tipoCartao;
        this.numero = numero;
        this.validade = validade;
        this.cvc = cvc;
    }

    /**
     * Retorna o nome do titular do cartão.
     * @return O nome do titular do cartão.
     */
    public String getNomeTitular() {
        return nomeTitular;
    }

    /**
     * Define o nome do titular do cartão.
     * @param nomeTitular O novo nome do titular do cartão.
     */
    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    /**
     * Retorna o tipo do cartão.
     * @return O tipo do cartão.
     */
    public String getTipoCartao() {
        return tipoCartao;
    }

    /**
     * Define o tipo do cartão.
     * @param tipoCartao O novo tipo do cartão.
     */
    public void settipoCartao(String tipoCartao) {
        this.tipoCartao = tipoCartao;
    }

    /**
     * Retorna o número do cartão de crédito.
     * @return O número do cartão de crédito.
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Define o número do cartão de crédito.
     * @param numero O novo número do cartão de crédito.
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * Retorna a data de validade do cartão.
     * @return A data de validade do cartão (MM/AA).
     */
    public String getValidade() {
        return validade;
    }

    /**
     * Define a data de validade do cartão.
     * @param validade A nova data de validade do cartão (MM/AA).
     */
    public void setValidade(String validade) {
        this.validade = validade;
    }

    /**
     * Retorna o código de verificação do cartão (CVC
     * @return O código de verificação do cartão (CVC).
     */
    public String getCvc() {
        return cvc;
    }

    /**
     * Define o código de verificação do cartão (CVC).
     *
     * @param cvc O novo código de verificação do cartão (CVC).
     */
    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    /**
     * Sobrescreve o metodo equals para comparar dois objetos Cartao.
     *
     * @param o O objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cartao cartao = (Cartao) o;
        return Objects.equals(nomeTitular, cartao.nomeTitular) &&
                Objects.equals(tipoCartao, cartao.tipoCartao) &&
                Objects.equals(numero, cartao.numero) &&
                Objects.equals(validade, cartao.validade) &&
                Objects.equals(cvc, cartao.cvc);
    }

    /**
     * Sobrescreve o metodo hashCode para garantir que objetos iguais tenham o mesmo hash.
     *
     * @return O valor do hash code gerado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nomeTitular, tipoCartao, numero, validade, cvc);
    }
}
