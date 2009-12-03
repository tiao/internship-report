/*
 * Jogador.java
 *
 * Created on 20/10/2009, 22:17:56
 */

package elementos;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Classe Jogador contém o mínimo necessário para adicionar
 * um objeto da Tela e fazer com que este mova por toda sua
 * extensão.
 *
 * @author Emerson Ribeiro de Mello
 */
public class Jogador {

    private Image imagem; // onde será carregada a imagem do jogador (usado pela Tela)
    private JPanel tela; // para interagir com o JPanel onde está inserido
    private int x; // coordenada X atual do jogador
    private int y;  // coordenada Y atual do jogador
    private int largura; // largura da imagem que fora carregada
    private int altura; // altura da imagem que fora carregada
    private int dirX; // direção no eixo X que o objeto deverá se movimentar
    private int dirY; // direção no eixo Y que o objeto deverá se movimentar

    /**
     * * É necessário informar o nome da imagem que será carregada no jogador
     * @param nomeImagem caminho onde está localizada a imagem
     * @param jp referencia ao JPanel pai onde este objeto jogador está inserido
     * @param px posicao X inicial onde o jogador irá aparecer
     * @param py posicao Y inicial onde o jogador irá aparecer
     */
    public Jogador(String nomeImagem, JPanel jp, int px, int py) {
        ImageIcon iicon = new ImageIcon(this.getClass().getResource(nomeImagem));
        this.imagem = iicon.getImage();

        this.tela = jp;
        this.x = px;
        this.y = py;
        this.largura = imagem.getWidth(null);
        this.altura = imagem.getHeight(null);
    }

    /**
     * Método responsável pela movimentação do objeto jogador na tela.
     * Irá alterar os valores atuais dos atributos x e y.
     */
    public void mover() {
        // obtendo a largura e altura da tela
        int lTela = this.tela.getSize().width;
        int aTela = this.tela.getSize().height;

        //teste para evitar que ultrapasse os limites da tela - eixo X
        x += (((dirX > 0) && (x + dirX < lTela - this.largura)) || ((dirX < 0) && (x - dirX > 0))) ? dirX : 0;

        //teste para evitar que ultrapasse os limites da tela - eixo Y
        y += (((dirY > 0) && (y + dirY < aTela - this.altura)) || ((dirY < 0) && (y - dirY > 0))) ? dirY : 0;
    }

    /**
     * Para tratar a tecla que está pressionada. Enquanto a tecla de direção
     * estiver pressionada, o objeto será movido para a respectiva direção.
     * Neste método é alterado os atributos dirX e dirY que são usados pelo
     * método mover
     * @param evento tecla que foi pressionada
     */
    public void teclaPressionada(KeyEvent evento) {
        // obtendo o código da tecla que foi pressionado
        int tecla = evento.getKeyCode();

        switch (tecla) {
            case KeyEvent.VK_LEFT:
                dirX = -1; // movendo para a esquerda
                break;
            case KeyEvent.VK_RIGHT:
                dirX = 1; // movendo para a direita
                break;
            case KeyEvent.VK_UP:
                dirY = -1; // movendo para cima
                break;
            case KeyEvent.VK_DOWN:
                dirY = 1; // movendo para baixo
                break;
        }
    }

    /**
     * Para de mover o objeto na direção da tecla que fora solta.
     * @param evento tecla que foi solta
     */
    public void teclaSolta(KeyEvent evento) {
        // obtendo o código da tecla que foi solta
        int tecla = evento.getKeyCode();

        switch (tecla) {
            case KeyEvent.VK_LEFT:
                dirX = 0; // para de mover para  esquerda
                break;
            case KeyEvent.VK_RIGHT:
                dirX = 0; // para de mover para  a direita
                break;
            case KeyEvent.VK_UP:
                dirY = 0; // para de mover para  cima
                break;
            case KeyEvent.VK_DOWN:
                dirY = 0; // para de mover para baixo
                break;
        }
    }

    /**
     * Retorna a imagem para que possa ser desenhada na Tela
     * @return
     */
    public Image getImagem() {
        return imagem;
    }

    /**
     * Retorna a coordenada X para desenhar na posição correta na Tela
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Retorna a coordenada X para desenhar na posição correta na Tela
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Definindo uma nova coordenada X para o objeto
     * @param x posicao nova
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Definindo uma nova coordenada y para o objeto
     * @param y posicao nova
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Para incrementar/decrementar a atual coordenada
     * @param ix valor positivo, negativo ou zero
     */
    public void incrementaX(int ix) {
        this.x += ix;
    }

    /**
     * Para incrementar/decrementar a atual coordenada
     * @param ix valor positivo, negativo ou zero
     */
    public void incrementaY(int iy) {
        this.y += iy;
    }

    /**
     * Retorna a largura da imagem
     * @return largura
     */
    public int getLargura() {
        return largura;
    }

    /**
     * Retorna a altura da imamge
     * @return
     */
    public int getAltura() {
        return altura;
    }

    /**
     * Usado para detecção de colisões no jogo
     * @return Cria um retangulo com a posicao atual do jogador e com as dimensoes da
     * imagem que fora carregada para esse jogador.
     */
    public Rectangle getLimites() {
        return new Rectangle(x, y, largura, altura);
    }
}
