/*
 * Tela.java
 *
 * Created on 20/10/2009, 22:17:56
 */
package jogo;

import elementos.Jogador;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Veja: http://zetcode.com/tutorials/javagamestutorial
 * Veja: http://www.cokeandcode.com/info/tut2d.html
 * 
 * @author Emerson Ribeiro de Mello
 */
public class Tela extends JPanel implements ActionListener {

    private Timer timer; // para ficar desenhando a Tela2 de tempos em tempos
    private Jogador j1; // para ilustrar o uso da classe Jogador
    private Jogador j2; // para ilustrar o uso da classe Jogador
    private Random r; // só para colocar os jogadores em posições aleatórias na tela
    public boolean jogoAtivo; // para saber se o jogo está rodando
    public boolean inicial; // para saber se é um novo jogo
    private Principal pai;
    private int movInimigo; // um contador para uma simples movimentacao dos inimigos

    public Tela(Principal p) {

        // Guardando um referencia ao JFrame PAI para pegar e modificar
        // alguns atributos que lá estão
        this.pai = p;

        this.r = new Random();
        this.movInimigo = 0;

        // atributos que poderão ser modificados pela classe Principal
        this.jogoAtivo = true;
        this.inicial = false;

        // trata eventos das teclas pressionadas
        this.addKeyListener(new TratadorEventoTeclado());

        /* definindo algumas propriedades do JPanel */
        this.setFocusable(true); // assume foco
        this.setBackground(Color.BLACK); // alterando a cor
        this.setDoubleBuffered(true); // tornando o redesenho mais rapido


        /* Irá atualizar a tela a cada 5 milisegundos */
        this.timer = new Timer(5, this);
        this.timer.start();
    }

    /**
     * É chamado sempre que começar uma nova rodada
     */
    public void novaRodada() {
        /* criando 2 jogadores e posicionando-os na tela de forma aleatória */
        int px = r.nextInt(this.getWidth() - 50);
        int py = r.nextInt(this.getHeight() - 50);

        this.j1 = new Jogador("penguin.png", this, px, py);

        px = r.nextInt(this.getWidth() - 50);
        py = r.nextInt(this.getHeight() - 50);
        this.j2 = new Jogador("joaninha.png", this, px, py);

    }

    /**
     * Irá desenhar os objetos na tela
     * Será chamado a cada 5 milisegundos, ou seja, a tela está em constante
     * atualização
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        if (jogoAtivo) { // o jogo está rodando!!!
            // Jogo acabou de ser carregado, mas ainda não foi iniciado um novo jogo
            // para criar os objetos somente 1 vez.
            if (inicial == true) {
                this.novaRodada();
                inicial = false;
            }

            // para garantir que eles nao sao nulos, antes de pintá-los na tela
            if ((j1 != null) && (j2 != null)) {
                g2d.drawImage(j1.getImagem(), j1.getX(), j1.getY(), this);
                g2d.drawImage(j2.getImagem(), j2.getX(), j2.getY(), this);
            }

        } else { // fim de jogo, imprima a mensagem

            // ativando o antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // modificando as características fonte
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metr = this.getFontMetrics(small);
            g2d.setColor(Color.white);
            g2d.setFont(small);

            String msg = "Fim de Jogo!";

            g2d.drawString(msg, (this.getWidth() - metr.stringWidth(msg)) / 2,
                    this.getHeight() / 2);

        }

    }

    /**
     * Irá tratar todos os eventos que ocorrerem no JPanel.
     * O pressionar de uma tecla é um evento a ser tratado.
     *
     * @param e evento que ocorreu na Tela2
     */
    public void actionPerformed(ActionEvent e) {
        // para movimentar o jogador pela tela

        // somente depois que o objeto for criado, ou seja, depois de um novo jogo
        if ((j1 != null) && (jogoAtivo == true)) {

            /* Movendo a joaninha
             * 
             * A tela é atualizada a cada 5 milisegundos. A joaninha irá se movimentar
             * a cada 25 milisegundos
             */
            movInimigo = (movInimigo % 25) + 1;

            if (movInimigo == 25) {
                this.movimentarInimigos();
            }

            // movendo o pinguim
            j1.mover();
            // para verificar se houve alguma colisao entre os jogadores
            this.verificaColisoes();
            // pintando a tela novamente
            repaint();
        }
    }

    /**
     * Fazendo a joaninha andar
     * Uma lógica simples e nada inteligente para movimentar
     */
    private void movimentarInimigos() {

        int ondex = r.nextInt(30);
        int ondey = r.nextInt(30);
        boolean increX = r.nextBoolean(); //se irá incrementar ou decrementar
        boolean increY = r.nextBoolean(); //se irá incrementar ou decrementar


        // se true, ele vai incrementar o valor sorteado em ondex - eixo X;
        if (increX == true) {
            if (j2.getX() + j2.getLargura() + ondex < this.getWidth()) {
                j2.incrementaX(ondex);
            } else {
                j2.setX(r.nextInt(this.getWidth() - j2.getLargura()));
            }
        } else { // se nao, ele vai decrementar
            if (j2.getX() - ondex > 0) { // se não ultrapassar o limite da tela
                j2.incrementaX(ondex * -1);
            } else {
                j2.setX(r.nextInt(this.getWidth() - j2.getLargura()));
            }
        }

        // se true, ele vai incrementar o valor sorteado em ondey - eixo Y;
        if (increY == true) {
            if (j2.getY() + j2.getAltura() + ondey < this.getHeight()) {
                j2.incrementaY(ondey);
            } else {
                j2.setY(r.nextInt(this.getHeight() - j2.getAltura()));
            }
        } else { // se nao, ele vai decrementar
            if (j2.getY() - ondey > 0) { // se não ultrapassar o limite da tela
                j2.incrementaY(ondey * -1);
            } else {
                j2.setY(r.nextInt(this.getHeight() - j2.getAltura()));
            }
        }

    }

    /**
     * Verifica se houve colisoes entre os objetos na tela.
     */
    private void verificaColisoes() {
        // descobrindo as dimensoes e posicao dos jogadores
        Rectangle rJ1 = j1.getLimites();
        Rectangle rJ2 = j2.getLimites();

        // usufruindo do método intersects da classe Rectangle para verificar
        // a colisão
        if (rJ1.intersects(rJ2)) {
            logicaDoJogo();
        }

    }

    /**
     * Uma lógica simples para o jogo, cujo objetivo é fazer com que o pinguim
     * ao passar por cima de uma joaninha, aumente o número de pontos e faça
     * nascer uma joaninha em outro local na tela
     */
    public void logicaDoJogo() {

        // incrementando o numero de pontos do jogador
        int pontos = new Integer(pai.jLPontos.getText()).intValue();
        pontos++;

        pai.jLPontos.setText("" + pontos);

        // fazendo a joaninha 'nascer' em outra parte da tela
        int px = r.nextInt(this.getWidth() - j2.getLargura()); // aletorio dentro do limite da tela
        int py = r.nextInt(this.getHeight() - j2.getAltura()); // aletorio dentro do limite da tela
        j2.setX(px);
        j2.setY(py);

        /*
         * O objetivo do jogo é marcar 10 pontos. Quando isso for atingido. FIM!
         */
        if (pontos == 10) {
            jogoAtivo = false;
        }




    }

    /**
     * Classe interna e privada para tratar os eventos de tecla pressionada
     * e tecla solta.
     * Neste exemplo, sempre que uma tecla for pressionada, o j1 irá se mover
     *
     * Uma classe interna consegue acessar todos os atributos (mesmo os privados)
     * da classe mais externa que a contém.
     *
     */
    private class TratadorEventoTeclado extends KeyAdapter {

        /**
         * Quando ocorrer o evento de tecla solta, será invocado o método teclaSolta
         * do objeto j1
         * @param e evento do teclado
         */
        @Override
        public void keyReleased(KeyEvent e) {
            if (j1 != null) {
                j1.teclaSolta(e);
            }
        }


        /**
         * Quando ocorrer o evento de tecla pressionada, será invocado o
         * método teclaPressionada do objeto j1
         * @param e evento do teclado
         */
        @Override
        public void keyPressed(KeyEvent e) {
            if (j1 != null) {
                j1.teclaPressionada(e);
            }
        }
    }
}
