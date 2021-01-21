package fractal;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Mirror
{
	private ImageRenderer renderer;
	private MirrorShader shader;

	public Mirror(int width, int height)
	{
		shader = new MirrorShader();
		renderer = new ImageRenderer(width, height);
	}

	public void render(int texture, boolean mx, boolean my, Fbo out)
	{
		shader.start();
		shader.loadMirrors(mx, my);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad(out);
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
