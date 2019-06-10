import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

enum VertexType {
	CENTER, TOP_LEFT, BOTTOM_RIGHT;
}

public abstract class Shape {
	Point2D.Double center; //中心の頂点座標
	Point p1, p2; //左上,右下の頂点座標
	Point2D.Double vec; //移動量のベクトル

	/*//図形位置の定数
	static final int CENTER = 0;
	static final int TOP_LEFT = 1;
	static final int BOTTOM_RIGHT = 2;*/

	/**
	 * 図形のx座標を取得します。
	 * @return 左上のx座標
	 */
	int getX() {
		return p1.x;
	}

	/**
	 * 図形のy座標を取得します。
	 * @return 左上のy座標
	 */
	int getY() {
		return p1.y;
	}

	/**
	 * 移動量vecを設定します。移動量はdeltaxまたはdeltayの大きい値を基準に正規化され、
	 * 1回のmove()メソッドの呼び出しでx方向またはy方向にdeltaピクセル移動します。
	 * @param deltax x方向の変量
	 * @param deltay y方向の変量
	 * @param delta 1回の移動量
	 */
	void setDirection(int deltax, int deltay, int delta) {
		//移動なし(0,0)を設定
		if (deltax == 0 && deltay == 0) {
			vec.x = 0;
			vec.y = 0;
		}
		//それ以外
		else {
			if (Math.abs(deltax) <= Math.abs(deltay)) {
				vec.x = deltax / Math.abs((double)deltay);
				if (deltay >= 0) {
					vec.y = 1.0;
				}
				else {
					vec.y = -1.0;
				}
			}
			else {
				vec.y = deltay / Math.abs(deltax);
				if (deltax >= 0) {
					vec.x = 1.0;
				}
				else {
					vec.x = -1.0;
				}
			}
		}
		
		vec.x *= delta;
		vec.y *= delta;
	}

	/**
	 * 図形のx方向の移動量を取得します。
	 * @return x方向の移動量
	 */
	double getDirectX() {
		return vec.x;
	}

	/**
	 * 図形のy方向の移動量を取得します。
	 * @return y方向の移動量
	 */
	double getDirectY() {
		return vec.y;
	}

	/**
	 * 図形をvecで指定された移動量だけ移動します。
	 */
	void move() {
		move(vec.x, vec.y);
	}

	/**
	 * 図形をx方向にxピクセル, y方向にyピクセル移動します。
	 * @param x x方向の移動量
	 * @param y y方向の移動量
	 */
	abstract void move(double x, double y);
	
	/**
	 * 図形の位置を(x, y)に設定します。
	 * @param x x座標
	 * @param y y座標
	 * @param vertex 設定対象の頂点
	 */
	abstract void setLocation(int x, int y, VertexType vertex);

	/**
	 * 図形を描画します。
	 * @param g Graphicsオブジェクト
	 */
	abstract void draw(Graphics g);
}