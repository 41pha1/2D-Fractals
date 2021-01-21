package launcher;

import org.lwjgl.opengl.Display;

import fractal.Fbo;
import fractal.FractalRenderer;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class FractalMain
{
	public static float res = 1;

	public static void main(String[] args) throws InterruptedException
	{
		DisplayManager.createDisplay();
		FractalRenderer.init(new Loader(), res);
		Fbo screen = new Fbo((int) (Display.getWidth() * res), (int) (Display.getHeight() * res), Fbo.DEPTH_RENDER_BUFFER);
		Fbo output = new Fbo((int) (Display.getWidth() * res), (int) (Display.getHeight() * res));
		Fbo toBeMirrored = new Fbo((int) (Display.getWidth() * res), (int) (Display.getHeight() * res), Fbo.DEPTH_RENDER_BUFFER);
		output.resolveToFbo(screen);
		screen.resolveToScreen();
		DisplayManager.updateDisplay();
		while (!Display.isCloseRequested())
		{
			Thread.sleep(0);
			FractalRenderer.clear(screen.getColourTexture(), output);
			FractalRenderer.iterate(screen.getColourTexture(), output);
			output.resolveToFbo(toBeMirrored);
			FractalRenderer.mirror(toBeMirrored.getColourTexture(), output);
			output.resolveToFbo(screen);
			output.resolveToScreen();
			DisplayManager.updateDisplay();
		}
		FractalRenderer.cleanUp();
		screen.cleanUp();
		DisplayManager.closeDisplay();
		System.exit(1);
	}
}
