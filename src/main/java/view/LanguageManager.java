/**
 * Sistema Operacional: Windows 10 - 64 Bits
 * Versão da Linguagem: Java JDK version "21.0.4" 2024-07-16
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
package view;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Classe responsável pela gestão de idiomas da aplicação TicketHub.
 * Permite configurar o idioma e recuperar textos traduzidos com base em uma chave.
 * Utiliza a classe {@link ResourceBundle} para carregar os recursos de tradução.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class LanguageManager {

    /**
     * Objeto que armazena os recursos de tradução com base no idioma selecionado.
     */
    private static ResourceBundle bundle;

    /**
     * Configura o idioma da aplicação.
     * Carrega o arquivo de recursos correspondente ao {@code Locale} fornecido.
     *
     * @param locale O {@link Locale} que define o idioma desejado.
     *               Exemplo: {@code new Locale("pt", "BR")} para Português do Brasil.
     */
    public static void setLanguage(Locale locale) {
        // Carrega o arquivo de tradução com base no Locale
        bundle = ResourceBundle.getBundle("i18n.lang", locale);
    }

    /**
     * Recupera uma string traduzida com base na chave fornecida.
     * Deve ser chamada após o idioma ter sido configurado com {@code setLanguage}.
     *
     * @param key A chave da string no arquivo de recursos. Exemplo: "titulo.app".
     * @return A string traduzida correspondente à chave.
     * @throws IllegalStateException Se o mEtodo {@code setLanguage} não tiver sido chamado antes.
     */
    public static String getString(String key) {
        // Garante que o bundle foi inicializado
        if (bundle == null) {
            throw new IllegalStateException("O ResourceBundle não foi inicializado.");
        }
        // Retorna o valor correspondente à chave no arquivo de recursos
        return bundle.getString(key);
    }
}
