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
 */

package model.tickethub;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe responsável por gerenciar a lógica principal do sistema de venda de ingressos.
 * Implementa funcionalidades como cadastro de usuários, criação e edição de eventos,
 * além da compra e cancelamento de ingressos.
 *
 * @author Cleidson Ramos
 * @version 1.3
 */
public class Controller implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private List<Usuario> usuarios; // Lista de usuários cadastrados
    private List<Evento> eventos;   // Lista de eventos disponíveis

    /**
     * Construtor que inicializa listas de usuários e eventos.
     * Tenta restaurar dados previamente salvos e cadastra um administrador padrão.
     */
    public Controller() {
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
        restaurarDados();
        cadastrarAdminFixo();
    }

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param login Login do usuário.
     * @param senha Senha do usuário.
     * @param nome Nome completo do usuário.
     * @param cpf CPF do usuário.
     * @param email E-mail do usuário.
     * @param admin Define se o usuário é administrador.
     * @throws NullPointerException se algum campo obrigatório estiver vazio.
     * @throws RuntimeException se o login já estiver em uso.
     */
    public void cadastrarUsuario(String login, String senha, String nome, String cpf, String email, Boolean admin) {
        if (login == null || login.isEmpty() || senha == null || senha.isEmpty() ||
                nome == null || nome.isEmpty() || cpf == null || cpf.isEmpty() || email == null || email.isEmpty()) {
            throw new NullPointerException("Todos os campos devem ser preenchidos.");
        }
        Usuario usuario = new Usuario(login, senha, nome, cpf, email, admin);
        for (Usuario u : this.usuarios) {
            if (u.equals(usuario)) {
                throw new RuntimeException("Login já está cadastrado.");
            }
        }
        this.usuarios.add(usuario);
        salvarDados();
    }

    /**
     * Cadastra um novo evento no sistema.
     *
     * @param nomeEvento Nome do evento.
     * @param descricaoEvento Descrição do evento.
     * @param data Data do evento.
     * @param colunasStr Quantidade de colunas dos assentos.
     * @param fileirasStr Quantidade de fileiras dos assentos.
     * @throws IllegalArgumentException se algum campo obrigatório estiver vazio ou inválido.
     * @throws RuntimeException se o evento já estiver cadastrado.
     */
    public void cadastrarEvento(String nomeEvento, String descricaoEvento, Date data, String colunasStr, String fileirasStr) {
        if (nomeEvento == null || nomeEvento.isEmpty() ||
                descricaoEvento == null || descricaoEvento.isEmpty() || data == null) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
        }
        for (Evento e : eventos) {
            if (e.getNome().equals(nomeEvento)) {
                throw new RuntimeException("Evento já está cadastrado.");
            }
        }
        Evento evento = new Evento(nomeEvento, descricaoEvento, data);
        adicionarAssentoEvento(evento, colunasStr, fileirasStr);
        eventos.add(evento);
        salvarDados();
    }

    /**
     * Adiciona assentos a um evento específico.
     *
     * @param evento Evento ao qual os assentos serão adicionados.
     * @param colunasStr Número de colunas (como string).
     * @param fileirasStr Número de fileiras (como string).
     * @throws IllegalArgumentException se os valores forem inválidos ou não numéricos.
     */
    public void adicionarAssentoEvento(Evento evento, String colunasStr, String fileirasStr) {
        int colunas;
        int fileiras;
        try {
            colunas = Integer.parseInt(colunasStr);
            fileiras = Integer.parseInt(fileirasStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Colunas e fileiras devem ser números inteiros.");
        }
        if (colunas <= 0 || fileiras <= 0) {
            throw new IllegalArgumentException("As dimensões de assento devem ser positivas.");
        }
        evento.adicionarAssento(colunas, fileiras);
        salvarDados();
    }

    /**
     * Edita as informações de um evento.
     *
     * @param evento Evento a ser editado.
     * @param nomeEvento Novo nome do evento.
     * @param descricaoEvento Nova descrição do evento.
     * @param data Nova data do evento.
     */
    public void editarEvento(Evento evento, String nomeEvento, String descricaoEvento, Date data) {
        evento.setNome(nomeEvento);
        evento.setDescricao(descricaoEvento);
        evento.setData(data);
        salvarDados();
    }

    /**
     * Remove um evento da lista.
     *
     * @param eventIndex Índice do evento a ser removido.
     */
    public void removerEvento(int eventIndex) {
        eventos.remove(eventIndex);
        salvarDados();
    }

    /**
     * Finaliza a compra de um ingresso para um usuário.
     *
     * @param usuario Usuário que realizará a compra.
     * @param evento Evento desejado.
     * @param assento Assento escolhido.
     * @param metodoPagamento Metodo de pagamento.
     * @return Objeto da compra concluída.
     * @throws RuntimeException se o pagamento falhar.
     */
    public Compra comprarIngresso(Usuario usuario, Evento evento, String assento, String metodoPagamento) {
        Ingresso ingresso = new Ingresso(evento, 100.0, assento);
        Date data = new Date();
        Compra compra = new Compra(usuario, ingresso, data, metodoPagamento);
        if (compra.finalizarCompra()) {
            usuario.adicionarIngresso(compra);
            evento.removerAssento(assento);
            evento.setIniciadoVenda(true);
            salvarDados();
            return compra;
        } else {
            throw new RuntimeException("Pagamento não concluído!");
        }
    }

    /**
     * Cancela a compra de um ingresso de um usuário.
     *
     * @param usuario O usuário que deseja cancelar a compra
     * @param compra A compra a ser cancelada
     * @return true se o cancelamento for bem-sucedido, false caso contrário
     * @throws RuntimeException Se o usuário ou o ingresso não forem encontrados
     */
    public Boolean cancelarCompra(Usuario usuario, Compra compra) {

        if (usuario == null || compra == null) {
            throw new IllegalArgumentException("Usuário ou compra inválidos.");
        }

        // Verifica se a compra pertence ao usuário
        if (!usuario.getIngressos().contains(compra)) {
            throw new RuntimeException("A compra não pertence ao usuário.");
        }

        // Tenta cancelar o ingresso
        Ingresso ingresso = compra.getIngresso();
        if (ingresso.cancelar()) {
            // Re-adiciona o assento ao evento
            ingresso.getEvento().adicionarAssentoUnitario(ingresso.getAssento());
            Notificacao notificacao = new Notificacao("Compra cancelada com sucesso para o evento: " +
                    ingresso.getEvento().getNome(), usuario);
            notificacao.enviar();
            usuario.removeIngresso(compra); // Remove a compra da lista de compras
            salvarDados(); // Salva os dados após o cancelamento
            return true; // Retorna sucesso no cancelamento
        }
        return false; // Retorna falha no cancelamento
    }

    /**
     * Solicita feedback de eventos que já ocorreram.
     * Este metodo verifica os eventos que já passaram da data atual e, para cada
     * ingresso comprado por um usuário em eventos concluídos, gera um feedback
     * automaticamente com um comentário padrão.
     *
     * @param avaliacao O usuário para o qual será solicitado o feedback
     * @param compra A compra a ser avaliada
     */
    public void solicitarFeedback(Compra compra, String avaliacao) {
        FeedBack feedBack = new FeedBack(compra.getUsuario(), compra.getIngresso().getEvento(), avaliacao);
        // Exporta o feedback como um arquivo
        feedBack.salvarFeedback();
        // Salva no evento o comentario obtido
        compra.getIngresso().getEvento().arquivarFeedbacks(feedBack);
        // Atualiza que o ingresso comprado já teve seu comentario enviado.
        compra.setFeedBack(true);
        salvarDados();

    }

    /**
     * Salva os dados do controlador em um arquivo.
     */
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Backup/dados.dat"))) {
            oos.writeObject(this); // Serializa o objeto Controller
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage()); // Exibe mensagem de erro
        }
    }

    /**
     * Restaura os dados do controlador a partir de um arquivo.
     */
    private void restaurarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Backup/dados.dat"))) {
            Controller controllerRestaurado = (Controller) ois.readObject(); // Deserializa o objeto Controller
            this.usuarios = controllerRestaurado.usuarios; // Restaura a lista de usuários
            this.eventos = controllerRestaurado.eventos; // Restaura a lista de eventos
        } catch (FileNotFoundException e) {
            System.out.println("Nenhum dado anterior encontrado, criando novo sistema."); // Mensagem informativa
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao restaurar dados: " + e.getMessage()); // Exibe mensagem de erro
        }
    }

    /**
     * Altera um atributo de um usuário autenticado.
     *
     * @param usuario O usuário cujo atributo será alterado
     * @param nome O nome do usuario a ser definido
     * @param email O e-mail do usuario a ser definido
     * @param senha1 A senha do usuario a ser definido
     * @param senha2 A confirmacao da senha do usuário
     * @throws IllegalArgumentException se usuario, nome, e-mail e senhas estajam em branco ou vazio
     * @throws RuntimeException se o login ou a senha estiverem incorretos, ou se o tipo de dado não for reconhecido
     */
    public void alterarAtributoUsuario( Usuario usuario, String nome, String email, String senha1, String senha2) {
        if (usuario == null || nome == null || nome.isEmpty() || email == null || email.isEmpty()
                || senha1 == null || senha1.isEmpty() || senha2 == null || senha2.isEmpty()) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
        }else if (!senha1.equals(senha2)) {
            throw new RuntimeException("As senhas devem ser iguais");
        }else {
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha1);
            salvarDados();
        }
    }

    /**
     * Realiza o login de um usuário com base em seu login e senha.
     *
     * @param login O nome de usuário ou e-mail do usuário.
     * @param senha A senha do usuário.
     * @return O objeto Usuario correspondente caso o login seja bem-sucedido, ou null se não houver correspondência.
     */
    public Usuario logar(String login, String senha) {
        // Itera sobre a lista de usuários para encontrar uma correspondência com o login e a senha fornecidos.
        for (Usuario u : this.usuarios) {
            if (u.login(login,senha)) {
                // Retorna o usuário encontrado.
                return u;
            }
        }
        // Retorna null caso nenhum usuário seja encontrado.
        return null;
    }

    /**
     * Adiciona um cartão de crédito/débito a um usuário específico.
     *
     * @param usuario O usuário ao qual o cartão será adicionado.
     * @param nomeTitular O nome do titular do cartão.
     * @param tipoCartao O tipo do cartão (ex.: crédito ou débito).
     * @param numero O número completo do cartão.
     * @param validade A validade do cartão no formato MM/AA.
     * @param cvc O código de segurança do cartão.
     */
    public void adicionarCartaoUsuario(Usuario usuario, String nomeTitular, String tipoCartao, String numero, String validade, String cvc) {
        // Adiciona o cartão ao usuário fornecido.
        usuario.adicionarCartao(nomeTitular, tipoCartao, numero, validade, cvc);
        // Salva os dados atualizados.
        salvarDados();
    }

    /**
     * Remove um cartão de crédito/débito de um usuário específico com base nos últimos dígitos do número do cartão.
     *
     * @param usuario O usuário do qual o cartão será removido.
     * @param ultimosDigitos Os últimos 4 dígitos do número do cartão.
     * @throws IllegalArgumentException Se nenhum cartão correspondente for encontrado.
     */
    public void removerCartao(Usuario usuario, String ultimosDigitos) {
        // Obtém a lista de cartões do usuário.
        List<Cartao> cartoes = usuario.getCartoes();
        // Busca o cartão cujo número termina com os últimos 4 dígitos fornecidos.
        Cartao cartaoParaRemover = cartoes.stream()
                .filter(c -> c.getNumero().endsWith(ultimosDigitos))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado."));
        // Remove o cartão da lista de cartões do usuário.
        cartoes.remove(cartaoParaRemover);
        // Salva os dados atualizados.
        salvarDados();
    }

    /**
     * Obtém a lista de eventos cadastrados no sistema.
     *
     * @return Uma lista de objetos Evento.
     */
    public List<Evento> getEventos() {
        // Retorna a lista de eventos.
        return eventos;
    }

    /**
     * Cadastra um administrador padrão caso ainda não esteja presente no sistema.
     * O administrador padrão possui login "admin" e senha "senha123".
     */
    private void cadastrarAdminFixo() {
        // Verifica se já existe um administrador com as credenciais padrão.
        Usuario adminEncontrado = null;
        for (Usuario u : this.usuarios) {
            if (u.getLogin().equals("admin") && u.getSenha().equals("senha123")) {
                adminEncontrado = u;
            }
        }
        // Se não encontrado, cria um novo administrador com as credenciais padrão.
        if (adminEncontrado == null) {
            cadastrarUsuario("admin", "senha123", "admin", "00000000000", "admin@example.com", true);
        }
    }


}
