import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class ShootingGame extends JFrame {
    //더블 버퍼
    public Image bufferImage;
    private Graphics screenGraphic;

    private Image main = new ImageIcon("src/images/main.png").getImage();  //Main화면
    private Image loading = new ImageIcon("src/images/loading.png").getImage();    //loading화면
    private Image play = new ImageIcon("src/images/play.jpg").getImage();  //game화면

    private boolean isMain, isLoading, isPlay;

    private GameEngine game = new GameEngine();

    public ShootingGame(){
        setTitle("M.E.D");
        setUndecorated(true);   //GUI 테투리 없게
        setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);

        init();
    }

    private void init(){    //초기화
        isMain = true;
        isLoading = false;
        isPlay = false;

        addKeyListener(new KeyListener());
    }

    public void paint(Graphics g){  //메인화면 출력
        bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage,0,0,null);
    }

    private void gameStart(){
        isMain = false;
        isLoading = true;

        Timer loadingTimer = new Timer();
        TimerTask loadingTask = new TimerTask() {
            @Override
            public void run() {
                isLoading = false;
                isPlay = true;
                game.start();
            }
        };
        loadingTimer.schedule(loadingTask,3000);    //로딩화면 --3초--> 게임화면

    }


    public void screenDraw(Graphics g){ //필요한 요소 그려줌
        if(isMain){
            g.drawImage(main,0,0,null);
        }
        if(isLoading){
            g.drawImage(loading,0,0,null);
        }
        if(isPlay){
            g.drawImage(play,0,0,null);
            game.gameDraw(g);
        }

        this.repaint();
    }


    class KeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_UP:     //W키를 누르면 위로
                    game.setUp(true);
                    break;
                case KeyEvent.VK_DOWN:     //S키를 누르면 아래로
                    game.setDown(true);
                    break;
                case KeyEvent.VK_R:
                    if(game.isOver()) game.reset();     //R키를 누르면 게임 다시 시작
                    break;
                case KeyEvent.VK_SPACE:     //Space키를 누르면 주먹이 나감
                    game.setShooting(true);
                    break;
                case KeyEvent.VK_ENTER:     //Enter누르면 게임방법화면으로
                    if (isMain) gameStart();
                    break;
                case KeyEvent.VK_ESCAPE:     //Esc누르면 종료
                    System.exit(0);
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_UP:
                    game.setUp(false);
                    break;
                case KeyEvent.VK_DOWN:
                    game.setDown(false);
                    break;
                case KeyEvent.VK_SPACE:
                    game.setShooting(false);
                    break;
            }
        }
    }
}
