package fractal;

import shaders.ShaderProgram;

public class MirrorShader extends ShaderProgram
{

	private static final String VERTEX_FILE = "/fractal/mirrorVertex.txt";
	private static final String FRAGMENT_FILE = "/fractal/mirrorFragment.txt";

	private int location_mirrorX;
	private int location_mirrorY;

	public MirrorShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadMirrors(boolean mx, boolean my)
	{
		super.loadBoolean(location_mirrorX, mx);
		super.loadBoolean(location_mirrorY, my);
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_mirrorX = super.getUniformLocation("mirrorX");
		location_mirrorY = super.getUniformLocation("mirrorY");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
	}

}
