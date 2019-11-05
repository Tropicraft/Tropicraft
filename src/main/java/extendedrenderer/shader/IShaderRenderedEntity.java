package extendedrenderer.shader;


import extendedrenderer.placeholders.Quaternion;

import javax.vecmath.Vector3f;

public interface IShaderRenderedEntity {

    Vector3f getPosition();
    //Quaternion getQuaternion();
    //Quaternion getQuaternionPrev();
    //Vector3f getScale();
    float getScale();
    //boolean hasCustomMatrix();


}
