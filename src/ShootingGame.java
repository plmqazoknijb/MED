import javax.swing.*;
import java.awt.*;

public class ShootingGame extends JFrame {
    //더블 버퍼
    public Image bufferImage;
    private Graphics screenGraphic;



    public ShootingGame(){
        setTitle("슈팅게임");
        setUndecorated(true);   //GUI 테투리 없게
        setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
    }
}
