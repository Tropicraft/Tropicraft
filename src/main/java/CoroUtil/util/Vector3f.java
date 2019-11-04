package CoroUtil.util;

public class Vector3f {

	public float x = 0;
	public float y = 0;
	public float z = 0;
	
	public Vector3f() {
		
	}
	
	public Vector3f(float x, float y, float z)
    {
		this.x = x;
		this.y = y;
		this.z = z;
    }
	
	public Vector3f(Vector3f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}
	
}
