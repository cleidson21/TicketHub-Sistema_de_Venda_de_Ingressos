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

/**
 * Classe abstrata que serve como base para os controladores da aplicação TicketHub.
 * Define o metodo `updateLanguage`, que deve ser implementado pelas classes que estendem esta base.
 * Esse metodo é utilizado para atualizar o idioma da interface do usuário.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public abstract class BaseController {

    /**
     * Atualiza o idioma da interface do usuário.
     * Esse metodo deve ser implementado por classes concretas que herdam desta classe.
     */
    public abstract void updateLanguage();
}

