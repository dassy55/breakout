import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

class Ball extends Shape {
	int size; //ボールのサイズ
	int radius; //ボールの半径

	/**
	 * Ballオブジェクトを生成します。
	 * @param x 左上のx座標
	 * @param y 左上のy座標
	 * @param size 直径
	 */
	Ball(int x, int y, int size) {
		p1 = new Point(x, y);
		p2 = new Point(x + size, y + size);
		center = new Point2D.Double(x + size / 2, y + size / 2);
		this.size = size;
		radius = size / 2;

		//初期状態は移動しない
		vec = new Point2D.Double(0, 0);
	}

	@Override
	void move(double x, double y) {
		center.x += x;
		center.y += y;
		p1.x = (int)center.x - radius;
		p1.y = (int)center.y - radius;
		p2.x = p1.x + size;
		p2.y = p1.y + size;
	}

	@Override
	void setLocation(int x, int y, VertexType vertex) {
		if (vertex == VertexType.CENTER) {
			center.x = x;
			center.y = y;
			p1.x = x - radius;
			p1.y = y - radius;
			p2.x = p1.x + size;
			p2.y = p1.y + size;
		}
		else if (vertex == VertexType.TOP_LEFT) {
			p1.x = x;
			p1.y = y;
			p2.x = x + size;
			p2.y = y + size;
			center.x = x + radius;
			center.y = y + radius;
		}
		else if (vertex == VertexType.BOTTOM_RIGHT) {
			p2.x = x;
			p2.y = y;
			p1.x = x - size;
			p1.y = y - size;
			center.x = p1.x + radius;
			center.y = p1.y + radius;
		}
	}

	@Override
	void draw(Graphics g) {
		g.fillOval((int)center.x - radius, (int)center.y - radius, size, size);
	}

	/**
	 * Ballオブジェクトをx方向に跳ね返します。
	 */
	void BoundX() {
		vec.x = -vec.x;
	}

	/**
	 * Ballオブジェクトをy方向に跳ね返します。
	 */
	void BoundY() {
		vec.y = -vec.y;
	}

	//x=xの線分とy1<=y<=y2の範囲で衝突するか判定
	boolean CollideX(int x, int y1, int y2) {
		if (center.y >= y1 && center.y <= y2) {
			if (Math.abs(x - center.x) <= radius) {
				return true;
			}
		}
		//端点の判定
		if (Math.hypot(x - center.x, y1 - center.y) <= radius) {
			return true;
		}
		if (Math.hypot(x - center.x, y2 - center.y) <= radius) {
			return true;
		}
		return false;
	}

	//y=yの線分とx1<=x<=x2の範囲で衝突するか判定
	boolean CollideY(int y, int x1, int x2) {
		if (center.x >= x1 && center.x <= x2) {
			if (Math.abs(y - center.y) <= radius) {
				return true;
			}
		}
		//端点の判定
		if (Math.hypot(x1 - center.x, y - center.y) <= radius) {
			return true;
		}
		if (Math.hypot(x2 - center.x, y - center.y) <= radius) {
			return true;
		}
		return false;
	}
}