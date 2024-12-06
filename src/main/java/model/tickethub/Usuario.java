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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa um usuário do sistema de venda de ingressos.
 * Cada usuário possui informações como login, senha, nome,
 * CPF, e-mail e situação de administrador.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class Usuario implements Serializable {
    private String login;   // Nome de usuario para o login
    private String senha;   // Senha de acesso do usuario
    private String nome;    // Nome completo do usuário
    private final String cpf;     // CPF do usuario
    private String email;   // Endereço de e-mail do usuario
    private final Boolean admin;  // Indica se o usuario é administrador (true) ou não (false)
    private final List<Cartao> cartoes;  // Lista de cartões de crédito do usuário
    private final List<Compra> ingressos; // Lista de Ingressos Comprados
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que inicializa o objeto Usuario com as informações fornecidas.
     *
     * @param login O nome de usuário para login
     * @param senha A senha de acesso
     * @param nome O nome completo do usuário
     * @param cpf O CPF do usuário
     * @param email O endereço de e-mail do usuário
     * @param admin Indica se o usuário é administrador
     */
    public Usuario(String login, String senha, String nome, String cpf, String email, Boolean admin) {
        if (!isCpfValido(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.admin = admin;
        this.cartoes = new ArrayList<>();  // Inicializa a lista de cartões vazia
        this.ingressos = new ArrayList<>(); // Inicializa a lista de ingressos comprados

    }

    // Métodos getter e setter para acessar e modificar os atributos privados

    /**
     * Retorna o login do usuário.
     * @return O login do usuário.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Retorna a senha do usuário.
     * @return A senha do usuário.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define uma nova senha para o usuário.
     * @param senha A nova senha do usuário.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Retorna o nome do usuário.
     * @return O nome do usuário.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define um novo nome para o usuário.
     * @param nome O novo nome do usuário.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o CPF do usuário.
     * @return O CPF do usuário.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Retorna o e-mail do usuário.
     * @return O e-mail do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define um novo e-mail para o usuário.
     * @param email O novo e-mail do usuário.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Cria e adiciona um cartão à lista de cartões do usuário.
     * @param nomeTitular O nome do titular do cartão a ser adicionado.
     * @param tipoCartao O nome do usuario do cartão a ser adicionado.
     * @param numero O número do cartão a ser adicionado.
     * @param validade A validade do cartão a ser adicionado.
     * @param cvc O CVC do cartão a ser adicionado.
     */
    public void adicionarCartao(String nomeTitular, String tipoCartao, String numero, String validade, String cvc) {
        if (!isCartaoValido(numero, validade, cvc)) {
            throw new IllegalArgumentException("Número do cartão, validade ou cvc inválido");
        }
        Cartao cartaoUsuario = new Cartao(nomeTitular, tipoCartao, numero, validade, cvc);
        cartoes.add(cartaoUsuario);
    }

    /**
     * Retorna a lista de cartões do usuário.
     * @return A lista de cartões do usuário.
     */
    public List<Cartao> getCartoes() {
        return cartoes;
    }

    /**
     * Sobrescreve o metodo equals para comparar dois objetos Usuario.
     * @param o O objeto a ser comparado.
     * @return True se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(login, usuario.login);
    }

    /**
     * Sobrescreve o metodo hashCode para garantir que objetos iguais tenham o mesmo hash.
     * @return O valor do hash code gerado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(login, nome, cpf, email, admin, cartoes);
    }

    /**
     * Verifica se o login e a senha fornecidos correspondem aos do usuário.
     * @param login O login fornecido.
     * @param senha A senha fornecida.
     * @return True se o login e senha estiverem corretos, false caso contrário.
     */
    public Boolean login(String login, String senha) {
        return getLogin().equals(login) && getSenha().equals(senha);
    }

    /**
     * Verifica se o CPF fornecido é válido.
     * @param cpf O CPF a ser verificado.
     * @return True se o CPF for válido, false caso contrário.
     */
    private Boolean isCpfValido(String cpf) {
        return cpf != null && cpf.length() == 11 && cpf.matches("\\d{11}");
    }

    /**
     * Verifica se o cartão fornecido é válido com base em seu número, validade e CVC.
     * @param numero O número do cartão.
     * @param validade A validade do cartão.
     * @param cvc O CVC do cartão.
     * @return True se o cartão for válido, false caso contrário.
     */
    private Boolean isCartaoValido(String numero, String validade, String cvc) {
        if (numero == null || numero.length() != 16 || !numero.matches("\\d{16}")) {
            return false;
        }
        if (validade == null || !validade.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        return cvc != null && cvc.length() == 3 && cvc.matches("\\d{3}");
    }

    public void adicionarIngresso(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("Compra não pode ser nula.");
        }
        ingressos.add(compra);
    }
    public void removeIngresso(Compra compra) {
        ingressos.remove(compra);
    }

    public List<Compra> getIngressos() {
        return ingressos;
    }

}
