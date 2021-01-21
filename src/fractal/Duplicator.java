package fractal;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Duplicator
{
	private ImageRenderer renderer;
	private DuplicatorShader shader;

	private int w, h;

	public Duplicator(int width, int height)
	{
		this.w = width;
		this.h = height;
		shader = new DuplicatorShader();
		renderer = new ImageRenderer(width, height);
	}

	public void render(int texture, Matrix4f transform, boolean mx, boolean my, int colorMode, Fbo bg)
	{
		shader.start();
		shader.loadTransformation(transform);
		shader.loadAspectRatio(w / (float) h);
		shader.loadDoClear(false);
		shader.loadColorMode(colorMode);
		shader.loadMirrors(mx, my);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad(bg);
		shader.stop();
	}

	public void render(int texture, Vector3f clearColor, Fbo bg)
	{
		shader.start();
		shader.loadAspectRatio(w / (float) h);
		shader.loadDoClear(true);
		shader.loadClearColor(clearColor);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad(bg);
		shader.stop();
	}

	public Fbo getFbo()
	{
		return renderer.getFbo();
	}

	public void cleanUp()
	{
		renderer.cleanUp();
		shader.cleanUp();
	}
}
