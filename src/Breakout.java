import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

class Breakout {
	static MainFrame mf;
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mf = new MainFrame();
				mf.setVisible(true);
			}
		});
	}
}

class MainFrame extends JFrame{
	MainPanel mp;

	MainFrame(){
		setTitle("Breakout");
		setSize(500,700);
		//初期画面表示位置を中央に
		setLocationRelativeTo(null);
		//CLOSEでプログラム終了
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mp = new MainPanel();
		getContentPane().add(mp);

		/*addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("keypress");
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					mp.moveBar(10);
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					mp.moveBar(-10);
				}
			}
		});*/

	}
}

class MainPanel extends JPanel implements Runnable {
	Thread t;
	int map[][] = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
					{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
	ArrayList<Block> block = new ArrayList<Block>();
	Ball ball;
	Block bar;
	Dimension d;
	int barShift = 0;
	boolean initialized = false;
	boolean rightKeyFlag = false;
	boolean leftKeyFlag = false;
	boolean endFlag = false;

	MainPanel() {
		//ダブルバッファリングを有効化
		super(true);

		//キーバインディングを設定
		setKeyBindings(KeyEvent.VK_RIGHT, false, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				barShift = 1;
				rightKeyFlag = true;
			}
		});
		setKeyBindings(KeyEvent.VK_RIGHT, true, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!leftKeyFlag) {
					barShift = 0;
				}
				rightKeyFlag = false;
			}
		});
		setKeyBindings(KeyEvent.VK_LEFT, false, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				barShift = -1;
				leftKeyFlag = true;
			}
		});
		setKeyBindings(KeyEvent.VK_LEFT, true, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!rightKeyFlag) {
					barShift = 0;
				}
				leftKeyFlag = false;
			}
		});

		t = new Thread(this);
		t.start();
	}

	protected void setKeyBindings(int keyCode, boolean onKeyRelease, Action action) {
		String key = String.valueOf(keyCode) + String.valueOf(onKeyRelease);
		getInputMap().put(KeyStroke.getKeyStroke(keyCode, 0, onKeyRelease), key);
		getActionMap().put(key, action);
	}

	@Override
	public void run() {
		while (true) {
			if (endFlag) {
				break;
			}

			repaint();

			try {
				Thread.sleep(2);
			}
			catch (InterruptedException e) {}
		}
	}

	protected void initialize(){
		d = getSize();

		//バー生成
		bar = new Block(d.width / 2 - 100 / 2, d.height - 20, 100, 20);

		int margin = 5; //ウインドウとの余白
		int gap = 3; //ブロック間の隙間
		int block_w = (d.width - 2 * margin - gap * (map[0].length - 1)) / map[0].length;
		int block_h = (int)(block_w * 0.5);
		//ブロック生成
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] == 1) {
					block.add(new Block(margin+(block_w+gap)*j,margin+(block_h+gap)*i, block_w, block_h));
				}
			}
		}

		//ボール生成
		ball = new Ball(d.width / 2, margin + (block_h + gap) * map.length + 10, 20);
		ball.setDirection(1, 1, 1);
	}

	@Override
	public void paintComponent(Graphics g) {
		//再描画時に背景色を塗りつぶす
		super.paintComponent(g);

		//初回なら初期化する
		if (!initialized) {
			initialize();
			initialized = true;
		}

		//ブロック描画
    	for (Block b : block) {
			b.draw(g);
		}

    	//バーの移動,描画
    	if (bar.p1.x >= 0 && barShift < 0) {
    		bar.move(barShift, 0);
    	}
    	else if (bar.p2.x <= d.width && barShift > 0) {
    		bar.move(barShift, 0);
    	}
		bar.draw(g);

		//ボールの移動,描画
		ball.move();
		ball.draw(g);

		//ボールと壁の衝突判定
		if (ball.p1.x <= 0 || ball.p2.x >= d.width) {
			ball.BoundX();
		}
		if (ball.p1.y <= 0) {
			ball.BoundY();
		}

		//クリア失敗判定
		if (ball.p2.y >= d.height) {
			Font f = new Font("Selif", Font.BOLD, 50);
			FontMetrics fm = g.getFontMetrics(f);
			g.setFont(f);
			g.setColor(Color.red);
			g.drawString("Falied", d.width / 2 - fm.stringWidth("Failed") / 2, d.height / 2 + fm.getDescent());
			endFlag = true;
		}

		//ボールとバーの衝突判定
		if (ball.getDirectX() >= 0) {
			if (ball.CollideX(bar.p1.x, bar.p1.y, bar.p2.y)) {
				ball.BoundX();
				ball.BoundY();
			}
		}
		else {
			if (ball.CollideX(bar.p2.x, bar.p1.y, bar.p2.y)) {
				ball.BoundX();
				ball.BoundY();
			}
		}
		if (ball.getDirectY() >= 0) {
			if (ball.CollideY(bar.p1.y, bar.p1.x, bar.p2.x)) {
				ball.BoundY();
			}
		}

		//ブロックとボールの衝突判定
		Iterator<Block> iter = block.iterator();
		Block b;
		while (iter.hasNext()) {
			b = iter.next();
			//右,垂直方向の移動による衝突
			if (ball.getDirectX() >= 0) {
				if (ball.CollideX(b.p1.x, b.p1.y, b.p2.y)) {
					ball.BoundX();
					iter.remove();
					break;
				}
			}
			//左方向の移動による衝突
			else {
				if (ball.CollideX(b.p2.x, b.p1.y, b.p2.y)) {
					ball.BoundX();
					iter.remove();
					break;
				}
			}

			//下,水平方向の移動による衝突
			if (ball.getDirectY() >= 0){
				if (ball.CollideY(b.p1.y, b.p1.x, b.p2.x)) {
					ball.BoundY();
					iter.remove();
				}
			}
			//上方向の移動による衝突
			else {
				if (ball.CollideY(b.p2.y, b.p1.x, b.p2.x)) {
					ball.BoundY();
					iter.remove();
				}
			}
		}

		//クリア判定
		if (block.isEmpty()) {
			Font f = new Font("Selif", Font.BOLD, 50);
			FontMetrics fm = g.getFontMetrics(f);
			g.setFont(f);
			g.setColor(Color.red);
			g.drawString("Cleared!", d.width / 2 - fm.stringWidth("Cleared!") / 2, d.height / 2 + fm.getDescent());
			endFlag = true;
		}
    }
}
