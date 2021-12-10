import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameEngine extends Thread {
    private int delay = 20;
    private long pretime;
    private int cnt;
    private int score;

    private Image player = new ImageIcon("src/images/player.png").getImage();

    private int playerX, playerY;
    private int playerWidth = player.getWidth(null);
    private int playerHeight = player.getHeight(null);
    private int playerSpeed = 10;   //플레이 이동거리
    private int playerHp = 30;

    private boolean up, down, shooting;  //움직임
    private boolean isOver;

    private ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>();
    private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    private ArrayList<EnemyAttack> enemyAttackList = new ArrayList<EnemyAttack>();


    private PlayerAttack playerAttack;
    private Enemy enemy;
    private EnemyAttack enemyAttack;

    @Override
    public void run(){

        reset();
        while (true) {
            while (!isOver) {
                pretime = System.currentTimeMillis();
                if (System.currentTimeMillis() - pretime < delay) {   //현재시간 - cnt증가 전 시간
                    try {
                        Thread.sleep(delay - System.currentTimeMillis() + pretime);
                        keyProcess();
                        playerAttackProcess();
                        enemyAppearProcess();
                        enemyMoveProcess();
                        enemyAttackProcess();
                        cnt++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try{
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset(){
        isOver = false;
        cnt = 0;
        score = 0;
        playerX = 10;
        playerY = (Main.SCREEN_HEIGHT - playerHeight) / 2;
        playerHp = 30;

        playerAttackList.clear();
        enemyList.clear();
        enemyAttackList.clear();
    }

    //키 입력 메소드
    private void keyProcess(){
        if(up && playerY - playerSpeed > 0) playerY -= playerSpeed;
        if(down && playerY + playerHeight + playerSpeed < Main.SCREEN_HEIGHT) playerY += playerSpeed;
        if(shooting && cnt % 15 == 0){
            playerAttack = new PlayerAttack(playerX + 222, playerY+25);
            playerAttackList.add(playerAttack);
        }
    }

    //공격처리
    private void playerAttackProcess(){
        for (int i = 0; i < playerAttackList.size(); i++){
            playerAttack = playerAttackList.get(i);
            playerAttack.fire();

            for(int j = 0; j < enemyList.size(); j++){
                enemy =enemyList.get(j);
                if(playerAttack.x > enemy.x && playerAttack.x < enemy.x + enemy.width && playerAttack.y > enemy.y && playerAttack.y < enemy.y + enemy.height){
                    enemy.hp -= playerAttack.attack;
                    playerAttackList.remove(playerAttack);
                }
                if(enemy.hp <= 0){  //적 죽음
                    enemyList.remove(enemy);
                    score += 100;
                }
            }
        }
    }

    private void enemyAppearProcess(){
        if(cnt % 80 == 0){
            enemy = new Enemy(1120, (int)(Math.random()*621));    //화면끝에서 랜덤으로
            enemyList.add(enemy);
        }
    }

    private void enemyMoveProcess(){
        for (int i = 0; i<enemyList.size(); i++){
            enemy = enemyList.get(i);
            enemy.move();
        }
    }

    //적 공격
    private void enemyAttackProcess(){
        if(cnt % 50 ==0){
            enemyAttack = new EnemyAttack(enemy.x-79,enemy.y+35);
            enemyAttackList.add(enemyAttack);
        }

        for(int i = 0; i<enemyAttackList.size(); i++){
            enemyAttack = enemyAttackList.get(i);
            enemyAttack.fire();

            if(enemyAttack.x > playerX & enemyAttack.x < playerWidth && enemyAttack.y > playerY && enemyAttack.y < playerY + playerHeight){
                playerHp -= enemyAttack.attack;
                enemyAttackList.remove(enemyAttack);
                if(playerHp <= 0) isOver = true;
            }
        }
    }

    public void gameDraw(Graphics g){   //게임요소 그리기
        playerDraw(g);
        enemyDraw(g);
        infoDraw(g);
    }

    public void infoDraw(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.BOLD, 40));
        g.drawString("SCORE : "+score, 40,80);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.TYPE1_FONT, 30));
        g.drawString("press ESC to game over ", 880,60);
        if(isOver){
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.BOLD,80));
            g.drawString("GAME OVER",350,400);
            g.setFont(new Font("Arial",Font.CENTER_BASELINE,50));
            g.drawString("Press R to restart",390,450);
        }
    }

    public void playerDraw(Graphics g){
        g.drawImage(player,playerX,playerY,null);
        g.setColor(Color.green);  //체력 칸
        g.fillRect(playerX-1, playerY -40,playerHp*6, 15);
        for (int i = 0; i < playerAttackList.size(); i++){
            playerAttack = playerAttackList.get(i);
            g.drawImage(playerAttack.image, playerAttack.x, playerAttack.y, null);
        }
    }

    public void enemyDraw(Graphics g){
        for (int i = 0; i<enemyList.size(); i++){
            enemy = enemyList.get(i);
            g.drawImage(enemy.image, enemy.x,enemy.y,null);
            g.setColor(Color.red);  //체력 칸
            g.fillRect(enemy.x-1, enemy.y -40, enemy.hp*15, 15);
        }
        for (int i = 0; i<enemyAttackList.size(); i++){
            enemyAttack = enemyAttackList.get(i);
            g.drawImage(enemyAttack.image, enemyAttack.x,enemyAttack.y,null);
        }
    }

    public boolean isOver() {
        return isOver;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
}
