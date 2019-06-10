import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Block extends Shape {
	int width, height; //幅,高さ

	/**
	 * Blockオブジェクトを生成します。
	 * @param x 左上のx座標
	 * @param y 左上のy座標
	 * @param width 幅
	 * @param height 高さ
	 */
	Block(int x, int y, int width, int height) {
		p1 = new Point(x, y);
		p2 = new Point(x + width, y + height);
		center = new Point2D.Double(x + width / 2, y + height / 2);
		this.width = width;
		this.height = height;

		//初期状態は移動しない
		vec = new Point2D.Double(0, 0);
	}
	
	@Override
	void move(double x, double y) {
		center.x += x;
		center.y += y;
		p1.x = (int)center.x - width / 2;
		p1.y = (int)center.y - height / 2;
		p2.x = p1.x + width;
		p2.y = p1.y + height;
	}

	@Override
	void setLocation(int x, int y, VertexType vertex) {
		if (vertex == VertexType.CENTER) {
			center.x = x;
			center.y = y;
			p1.x = x - width / 2;
			p1.y = y - height / 2;
			p2.x = p1.x + width;
			p2.y = p1.y + height;
		}
		else if (vertex == VertexType.TOP_LEFT) {
			p1.x = x;
			p1.y = y;
			p2.x = x + width;
			p2.y = y + height;
			center.x = x + width / 2;
			center.y = y + height / 2;
		}
		else if (vertex == VertexType.BOTTOM_RIGHT) {
			p2.x = x;
			p2.y = y;
			p1.x = x - width;
			p1.y = y - height;
			center.x = p1.x + width / 2;
			center.y = p1.y + height / 2;
		}
	}

	@Override
	void draw(Graphics g) {
		g.fillRect(p1.x, p1.y, width, height);
	}
}