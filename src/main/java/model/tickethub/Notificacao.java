/**
 * Sistema Operacional: Windows 10 - 64 Bits
 * Versão Da Linguagem: Java JDK version "21.0.4" 2024-07-16
 * Autor: Cleidson Ramos de Carvalho
 * Componente Curricular: EXA 863 - MI Programação
 * Concluido em: 17/10/2024
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
import java.util.Date;

/**
 * Representa uma notificação que pode ser enviada para um usuário.
 * A notificação contém uma mensagem, a data de envio, o usuário destinatário e a situação do envio.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class Notificacao implements Serializable {

    private final String mensagem; // Mensagem da notificação
    private final Date dataEnvio; // Data de envio da notificação
    private final Usuario usuario; // Usuário que receberá a notificação
    private Boolean statusEnvio; // Situação do envio da notificação (enviada ou não)
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor da classe Notificacao que inicializa os atributos da notificação.
     * A data de envio é definida como a data atual e a situação de envio como false.
     * @param mensagem A mensagem a ser enviada na notificação.
     * @param usuario O usuário que receberá a notificação.
     */
    public Notificacao(String mensagem, Usuario usuario) {
        this.mensagem = mensagem;
        this.dataEnvio =  new Date(); // Define a data de envio como a data atual
        this.usuario = usuario;
        this.statusEnvio = false; // Inicialmente, a notificação não foi enviada
    }

    /**
     * Envia a notificação para o usuário. Simula o envio e atualiza a situação da notificação.
     * Após o envio, a notificação é salva em um arquivo.
     */
    public void enviar() {
        // Lógica para enviar a notificação (ex: enviar e-mail, mensagem, etc.)
        this.statusEnvio = true; // Simula o envio como concluído
        salvarNotificacaoEmArquivo(); // Salva a notificação em um arquivo
    }

    /**
     * Salva a notificação enviada em um arquivo de texto na pasta "notificacoes".
     * Cria a pasta se ela não existir. O nome do arquivo é baseado no e-mail do usuário e no timestamp.
     */
    private void salvarNotificacaoEmArquivo() {
        String pastaNotificacoes = "notificacoes"; // Pasta onde as notificações serão salvas
        File diretorio = new File(pastaNotificacoes);
        // Cria a pasta se não existir
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
        // Nome do arquivo com base na data e no nome do usuário
        String nomeArquivo = "Notificacao_" + usuario.getEmail() + "_" + System.currentTimeMillis() + ".txt";
        // Escreve as informações da notificação no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(diretorio, nomeArquivo)))) {
            writer.write("Mensagem: " + mensagem);
            writer.newLine();
            writer.write("Data de Envio: " + dataEnvio);
            writer.newLine();
            writer.write("Usuário: " + usuario.getNome());
            writer.newLine();
            writer.write("Status de Envio: " + statusEnvio);
        } catch (IOException e) {
            System.err.println("Erro ao salvar notificação em arquivo: " + e.getMessage());
        }
    }

    /**
     * Retorna a mensagem da notificação.
     * @return A mensagem da notificação.
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Retorna o usuário que receberá a notificação.
     * @return O usuário destinatário da notificação.
     */
    public Usuario getUsuario() {
        return usuario;
    }

}


