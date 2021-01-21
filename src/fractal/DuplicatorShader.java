package fractal;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import shaders.ShaderProgram;

public class DuplicatorShader extends ShaderProgram
{

	private static final String VERTEX_FILE = "/fractal/duplicatorVertex.txt";
	private static final String FRAGMENT_FILE = "/fractal/duplicatorFragment.txt";

	private int location_transformation;
	private int location_doClear;
	private int location_aspectRatio;
	private int location_mirrorX;
	private int location_mirrorY;
	private int location_clearColor;
	private int location_colorMode;

	public DuplicatorShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadDoClear(boolean clear)
	{
		super.loadBoolean(location_doClear, clear);
	}

	public void loadAspectRatio(float ratio)
	{
		super.loadFloat(location_aspectRatio, ratio);
	}

	public void loadTransformation(Matrix4f transformation)
	{
		super.loadMatrix(location_transformation, transformation);
	}

	public void loadColorMode(int mode)
	{
		super.loadInt(location_colorMode, mode);
	}

	public void loadClearColor(Vector3f col)
	{
		super.loadVector(location_clearColor, col);
	}

	public void loadMirrors(boolean mx, boolean my)
	{
		super.loadBoolean(location_mirrorX, mx);
		super.loadBoolean(location_mirrorY, my);
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_transformation = super.getUniformLocation("transformation");
		location_doClear = super.getUniformLocation("clear");
		location_aspectRatio = super.getUniformLocation("aspectRatio");
		location_mirrorX = super.getUniformLocation("mirrorX");
		location_mirrorY = super.getUniformLocation("mirrorY");
		location_clearColor = super.getUniformLocation("clearColor");
		location_colorMode = super.getUniformLocation("colorMode");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
	}

}
