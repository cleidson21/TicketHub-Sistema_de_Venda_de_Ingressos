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

import java.io.*;
import java.util.Date;

/**
 * Representa um feedback dado por um usuário sobre um evento.
 * O feedback contém informações como o usuário que deu o feedback,
 * o evento relacionado, o comentário e a data em que foi feito.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class FeedBack implements Serializable {

    private final Usuario usuario; // Usuário que deu o feedback
    private final Evento evento; // Evento relacionado ao feedback
    private final String comentario; // Comentário do feedback
    private final Date dataFeedback; // Data do feedback
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que inicializa um feedback com as informações fornecidas.
     * A data do feedback é automaticamente definida para a data atual.
     * @param usuario O usuário que deu o feedback.
     * @param evento O evento relacionado ao feedback.
     * @param comentario O comentário do feedback.
     */
    public FeedBack(Usuario usuario, Evento evento, String comentario) {
        this.usuario = usuario;
        this.evento = evento;
        this.comentario = comentario;
        this.dataFeedback = new Date(); // Captura a data atual
    }

    /**
     * Retorna o usuário que deu o feedback.
     * @return O usuário do feedback.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Retorna o evento relacionado ao feedback.
     * @return O evento relacionado ao feedback.
     */
    public Evento getEvento() {
        return evento;
    }

    /**
     * Retorna o comentário do feedback.
     * @return O comentário do feedback.
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Retorna a data em que o feedback foi dado.
     * @return A data do feedback.
     */
    public Date getDataFeedback() {
        return dataFeedback;
    }

    /**
     * Salva o feedback em um arquivo .txt na pasta "Feedback".
     * Se a pasta não existir, ela será criada.
     */
    public void salvarFeedback() {
        String pastaFeedback = "Feedback"; // Pasta onde os feedbacks serão salvos
        File diretorio = new File(pastaFeedback);
        // Cria a pasta se não existir
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
        // Texto do feedback formatado
        String feedbackText = String.format("Usuario: %s\nEvento: %s\nAvaliacao: %s\nData: %s\n\n",
                usuario.getNome(), evento.getNome(), comentario, dataFeedback);

        // Escreve o feedback no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Feedback/feedback.txt", true))) {
            writer.write(feedbackText);
        } catch (IOException e) {
            System.out.println("Erro ao salvar feedback: " + e.getMessage());
        }
    }
}
