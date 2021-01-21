package fractal;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolbox.Maths;

public class FractalRenderer
{
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static final int numberOfModes = 2;
	private static final Vector3f mode_0 = new Vector3f(1, 1, 1);
	private static final Vector3f mode_1 = new Vector3f(0.8f, 0.8f, 0);
	private static RawModel quad;
	private static Duplicator duplicator;
	private static Mirror mirror;
	private static Vector3f clearColor;
	private static int colorMode;
	private static Vector2f pos, scale, rot;
	private static boolean mirrorX, mirrorY, x, y, c, s, p, d, ccs, tab, fading;
	private static Preset current, next;
	private static float progress;
	private static int numberOfPresets, currentPreset;

	public static void init(Loader loader, float res)
	{
		numberOfPresets = (int) Loader.readObjectFromFile("numberOfPresets");
		System.out.println(numberOfPresets);
		quad = loader.loadToVAO(POSITIONS, 2);
		duplicator = new Duplicator((int) (DisplayManager.WIDTH * res), (int) (DisplayManager.HEIGHT * res));
		mirror = new Mirror((int) (DisplayManager.WIDTH * res), (int) (DisplayManager.HEIGHT * res));
		// pos = new Vector2f(1.176f, 0.45f);
		// rot = new Vector2f(135.8f, 0);
		// scale = new Vector2f(0.838f, 0.838f);
		pos = new Vector2f(-0.742f, -0.794f);
		rot = new Vector2f(-180, 0);
		scale = new Vector2f(0.81f, 0.81f);
		mirrorX = true;
		clearColor = new Vector3f();
		current = savePreset();
	}

	public static Fbo clear(int inputTexture, Fbo bg)
	{
		start();
		duplicator.render(inputTexture, clearColor, bg);
		end();
		return duplicator.getFbo();
	}

	public static Fbo mirror(int inputTexture, Fbo output)
	{
		start();
		mirror.render(inputTexture, mirrorX, mirrorY, output);
		end();
		return mirror.getFbo();
	}

	public static Fbo iterate(int inputTexture, Fbo bg)
	{
		updateInputs();
		start();
		Matrix4f transform = Maths.createTransformationMatrix(pos, scale, rot);
		duplicator.render(inputTexture, transform, mirrorX, mirrorY, colorMode, bg);
		end();
		return duplicator.getFbo();
	}

	public static void writePreset()
	{
		Loader.writeObjectToFile(savePreset(), "preset" + numberOfPresets);
		Loader.writeObjectToFile(++numberOfPresets, "numberOfPresets");
	}

	public static void deleteLastPreset()
	{
		Loader.writeObjectToFile(--numberOfPresets, "numberOfPresets");
	}

	public static Preset readPreset(int id)
	{
		return (Preset) Loader.readObjectFromFile("preset" + id);
	}

	public static Preset savePreset()
	{
		return new Preset(pos, rot, scale, mirrorX, mirrorY, colorMode);
	}

	public static void loadPreset(Preset p)
	{
		pos.set(p.pos);
		scale.set(p.scale);
		rot.set(p.rot);
		mirrorX = p.mx;
		mirrorY = p.my;
		colorMode = p.cMode;
	}

	public static Vector2f interpolate(Vector2f in1, Vector2f in2, float f)
	{
		return new Vector2f(in1.x * (1 - f) + in2.x * f, in1.y * (1 - f) + in2.y);
	}

	public static void updateInputs()
	{
		float minScale = 0.7f;
		float blendSpeed = 0.1f;
		float rotSpeed = 0.2f;
		float scaleSpeed = 0.002f;
		float translationSpeed = 0.002f;
		float dx = Mouse.getDX();
		float dy = Mouse.getDY();
		boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		boolean middleMouse = (Mouse.isButtonDown(2));
		boolean leftMouse = (Mouse.isButtonDown(0));
		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		if (fading)
		{
			progress += blendSpeed;
			pos.set(interpolate(current.pos, next.pos, progress));
			rot.set(interpolate(current.rot, next.rot, progress));
			scale.set(interpolate(current.scale, next.scale, progress));
			System.out.println(scale);
			if (progress > 1)
			{
				fading = false;
			}
		} else
		{
			current = savePreset();
		}

		if (!fading && Keyboard.isKeyDown(Keyboard.KEY_P))
		{
			if (!p)
			{
				fading = true;
				p = true;
				progress = 0;
				next = readPreset(currentPreset++);
				mirrorX = next.mx;
				mirrorY = next.my;
				colorMode = next.cMode;
				if (currentPreset >= numberOfPresets)
					currentPreset = 0;
			}
		} else
		{
			p = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			if (!s)
			{
				s = true;
				writePreset();
			}
		} else
		{
			s = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			if (!d)
			{
				d = true;
				deleteLastPreset();
			}
		} else
		{
			d = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_TAB))
		{
			if (!tab)
			{
				tab = true;
				ccs = !ccs;
			}
		} else
		{
			tab = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_C))
		{
			if (!c)
			{
				c = true;
				colorMode++;
				if (colorMode >= numberOfModes)
					colorMode = 0;
			}
		} else
		{
			c = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Y))
		{
			if (!y)
			{
				y = true;
				mirrorY = !mirrorY;
			}
		} else
		{
			y = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			if (!x)
			{
				x = true;
				mirrorX = !mirrorX;
			}
		} else
		{
			x = false;
		}
		if (shift)
		{
			dx /= 10f;
			dy /= 10f;
		}
		if (ccs)
		{
			if (middleMouse)
			{
				pos.x += dx * translationSpeed;
				pos.y += dy * translationSpeed;
			} else if (leftMouse)
			{
				scale.x += dx * scaleSpeed;
				scale.y += dy * scaleSpeed;
			} else
			{
				rot.x += dx * rotSpeed;
				rot.y += dy * rotSpeed;
			}
		} else
		{
			if (space)
			{
				pos.x += dx * translationSpeed;
				pos.y += dy * translationSpeed;
			} else
			{
				scale.x += dy * scaleSpeed;
				scale.y += dy * scaleSpeed;
				rot.x += dx * rotSpeed;
				rot.y = 0;
			}
		}

		if (scale.x < minScale)
			scale.x = minScale;
		if (scale.y < minScale)
			scale.y = minScale;

		switch (colorMode)
		{
			case 0:
				clearColor.set(mode_0);
				break;
			case 1:
				clearColor.set(mode_1);
				break;
		}
	}

	public static void cleanUp()
	{
		duplicator.cleanUp();
	}

	private static void start()
	{
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
