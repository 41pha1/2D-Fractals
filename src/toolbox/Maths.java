package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Maths
{
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale, Vector2f rot)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rot.x), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rot.y), new Vector3f(1, 1, 0), matrix, matrix);
		return matrix;
	}
}
