package fractal;

import org.lwjgl.opengl.GL11;

public class ImageRenderer
{

	public Fbo fbo;

	public ImageRenderer(int width, int height)
	{
		this.fbo = new Fbo(width, height, Fbo.NONE);
	}

	public void renderQuad()
	{
		fbo.bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		fbo.unbindFrameBuffer();
	}

	public void renderQuad(Fbo bg)
	{
		bg.bindFrameBuffer();
		// GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		bg.unbindFrameBuffer();
	}

	public Fbo getFbo()
	{
		return fbo;
	}

	public void cleanUp()
	{
		if (fbo != null)
		{
			fbo.cleanUp();
		}
	}

}
