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

    private Image mainScreen = new ImageIcon("src/images/main_screen.png").getImage();  //Main화면
    private Image loadingScreen = new ImageIcon("src/images/loading_screen.png").getImage();    //loading화면
    private Image gameScreen = new ImageIcon("src/images/game_screen.png").getImage();  //game화면

    private boolean isMainScreen, isLoadingScreen, isGameScreen;

    public static GameEngine game = new GameEngine();

    public ShootingGame(){
        setTitle("슈팅게임");
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
        isMainScreen = true;
        isLoadingScreen = false;
        isGameScreen = false;

        addKeyListener(new KeyListener());
    }

    public void paint(Graphics g){  //메인화면 출력
        bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage,0,0,null);
    }

    private void gameStart(){
        isMainScreen = false;
        isLoadingScreen = true;

        Timer loadingTimer = new Timer();
        TimerTask loadingTask = new TimerTask() {
            @Override
            public void run() {
                isLoadingScreen = false;
                isGameScreen = true;
            }
        };
        loadingTimer.schedule(loadingTask,3000);    //로딩화면 --3초--> 게임화면

        game.start();
    }

    public void screenDraw(Graphics g){ //필요한 요소 그려줌
        if(isMainScreen){
            g.drawImage(mainScreen,0,0,null);
        }
        if(isLoadingScreen){
            g.drawImage(loadingScreen,0,0,null);
        }
        if(isGameScreen){
            g.drawImage(gameScreen,0,0,null);
            game.gameDraw(g);
        }
        this.repaint();
    }


    class KeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_W:     //W키를 누르면 아래로
                    game.setUp(true);
                    break;
                case KeyEvent.VK_S:     //S키를 누르면 아래로
                    game.setDown(true);
                    break;
                case KeyEvent.VK_A:     //A키를 누르면 오른쪽
                    game.setLeft(true);
                    break;
                case KeyEvent.VK_D:     //D키를 누르면 오른쪽
                    game.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:     //Space키를 누르면 주먹이 나감
                    game.setShooting(true);
                    break;
                case KeyEvent.VK_ENTER:     //Enter누르면 게임방법화면으로
                    if (isMainScreen) gameStart();
                    break;
                case KeyEvent.VK_ESCAPE:     //Esc누르면 종료
                    System.exit(0);
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_W:
                    game.setUp(false);
                    break;
                case KeyEvent.VK_S:
                    game.setDown(false);
                    break;
                case KeyEvent.VK_A:
                    game.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    game.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    game.setShooting(false);
                    break;
            }
        }
    }
}
