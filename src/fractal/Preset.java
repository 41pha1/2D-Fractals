package fractal;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;

public class Preset implements Serializable
{
	private static final long serialVersionUID = 1L;
	public Vector2f pos, rot, scale;
	public boolean mx, my;
	public int cMode;

	public Preset(Vector2f pos, Vector2f rot, Vector2f scale, boolean mx, boolean my, int cMode)
	{
		super();
		this.pos = new Vector2f();
		this.pos.set(pos);
		this.rot = new Vector2f();
		this.rot.set(rot);
		this.scale = new Vector2f();
		this.scale.set(scale);
		this.mx = mx;
		this.my = my;
		this.cMode = cMode;
	}
}
