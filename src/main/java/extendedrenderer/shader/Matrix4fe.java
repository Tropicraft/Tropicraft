package extendedrenderer.shader;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.nio.FloatBuffer;

/**
 * Created by corosus on 08/05/17.
 */
public class Matrix4fe extends Matrix4f {

    byte properties;

    public Matrix4fe() {
        this.m00 = 1.0F;
        this.m11 = 1.0F;
        this.m22 = 1.0F;
        this.m33 = 1.0F;
    }

    public Matrix4fe(Matrix4fe mat) {
        this.setMatrix4f(mat);

        this.properties = mat.properties();
    }

    public static void get(Matrix4f m, int offset, FloatBuffer src) {
        m.m00 = src.get(offset);
        m.m01 = src.get(offset + 1);
        m.m02 = src.get(offset + 2);
        m.m03 = src.get(offset + 3);
        m.m10 = src.get(offset + 4);
        m.m11 = src.get(offset + 5);
        m.m12 = src.get(offset + 6);
        m.m13 = src.get(offset + 7);
        m.m20 = src.get(offset + 8);
        m.m21 = src.get(offset + 9);
        m.m22 = src.get(offset + 10);
        m.m23 = src.get(offset + 11);
        m.m30 = src.get(offset + 12);
        m.m31 = src.get(offset + 13);
        m.m32 = src.get(offset + 14);
        m.m33 = src.get(offset + 15);
    }

    private void setMatrix4f(Matrix4fe mat) {
        this._m00(mat.m00());
        this._m01(mat.m01());
        this._m02(mat.m02());
        this._m03(mat.m03());
        this._m10(mat.m10());
        this._m11(mat.m11());
        this._m12(mat.m12());
        this._m13(mat.m13());
        this._m20(mat.m20());
        this._m21(mat.m21());
        this._m22(mat.m22());
        this._m23(mat.m23());
        this._m30(mat.m30());
        this._m31(mat.m31());
        this._m32(mat.m32());
        this._m33(mat.m33());
    }

    void _properties(int properties) {
        //this.properties = (byte)properties;
        //force all the caches off
        this.properties = 0;
    }

    public byte properties() {
        return this.properties;
    }

    public float m00() {
        return this.m00;
    }

    public float m01() {
        return this.m01;
    }

    public float m02() {
        return this.m02;
    }

    public float m03() {
        return this.m03;
    }

    public float m10() {
        return this.m10;
    }

    public float m11() {
        return this.m11;
    }

    public float m12() {
        return this.m12;
    }

    public float m13() {
        return this.m13;
    }

    public float m20() {
        return this.m20;
    }

    public float m21() {
        return this.m21;
    }

    public float m22() {
        return this.m22;
    }

    public float m23() {
        return this.m23;
    }

    public float m30() {
        return this.m30;
    }

    public float m31() {
        return this.m31;
    }

    public float m32() {
        return this.m32;
    }

    public float m33() {
        return this.m33;
    }

    void _m00(float m00) {
        this.m00 = m00;
    }

    void _m01(float m01) {
        this.m01 = m01;
    }

    void _m02(float m02) {
        this.m02 = m02;
    }

    void _m03(float m03) {
        this.m03 = m03;
    }

    void _m10(float m10) {
        this.m10 = m10;
    }

    void _m11(float m11) {
        this.m11 = m11;
    }

    void _m12(float m12) {
        this.m12 = m12;
    }

    void _m13(float m13) {
        this.m13 = m13;
    }

    void _m20(float m20) {
        this.m20 = m20;
    }

    void _m21(float m21) {
        this.m21 = m21;
    }

    void _m22(float m22) {
        this.m22 = m22;
    }

    void _m23(float m23) {
        this.m23 = m23;
    }

    void _m30(float m30) {
        this.m30 = m30;
    }

    void _m31(float m31) {
        this.m31 = m31;
    }

    void _m32(float m32) {
        this.m32 = m32;
    }

    void _m33(float m33) {
        this.m33 = m33;
    }

    public Matrix4fe setPerspective(float fovy, float aspect, float zNear, float zFar) {
        return this.setPerspective(fovy, aspect, zNear, zFar, false);
    }

    public Matrix4fe setPerspective(float fovy, float aspect, float zNear, float zFar, boolean zZeroToOne) {
        //MemUtil.INSTANCE.zero(this);
        float h = (float)Math.tan((double)(fovy * 0.5F));
        this._m00(1.0F / (h * aspect));
        this._m11(1.0F / h);
        boolean farInf = zFar > 0.0F && Float.isInfinite(zFar);
        boolean nearInf = zNear > 0.0F && Float.isInfinite(zNear);
        float e;
        if(farInf) {
            e = 1.0E-6F;
            this._m22(e - 1.0F);
            this._m32((e - (zZeroToOne?1.0F:2.0F)) * zNear);
        } else if(nearInf) {
            e = 1.0E-6F;
            this._m22((zZeroToOne?0.0F:1.0F) - e);
            this._m32(((zZeroToOne?1.0F:2.0F) - e) * zFar);
        } else {
            this._m22((zZeroToOne?zFar:zFar + zNear) / (zNear - zFar));
            this._m32((zZeroToOne?zFar:zFar + zFar) * zNear / (zNear - zFar));
        }

        this._m23(-1.0F);
        //this._properties(1);
        return this;
    }

    public Matrix4fe rotate(float ang, float x, float y, float z, Matrix4fe dest) {
        return (this.properties & 4) != 0?dest.rotation(ang, x, y, z):
                ((this.properties & 8) != 0?this.rotateTranslation(ang, x, y, z, dest):
                        ((this.properties & 2) != 0?this.rotateAffine(ang, x, y, z, dest):
                                this.rotateGeneric(ang, x, y, z, dest)));
    }

    public Matrix4fe rotate(float angle, Vector3f axis) {
        return this.rotate(angle, axis.x, axis.y, axis.z);
    }

    private Matrix4fe rotateGeneric(float ang, float x, float y, float z, Matrix4fe dest) {
        float s = (float)Math.sin((double)ang);
        float c = (float)cosFromSin((double)s, (double)ang);
        float C = 1.0F - c;
        float xx = x * x;
        float xy = x * y;
        float xz = x * z;
        float yy = y * y;
        float yz = y * z;
        float zz = z * z;
        float rm00 = xx * C + c;
        float rm01 = xy * C + z * s;
        float rm02 = xz * C - y * s;
        float rm10 = xy * C - z * s;
        float rm11 = yy * C + c;
        float rm12 = yz * C + x * s;
        float rm20 = xz * C + y * s;
        float rm21 = yz * C - x * s;
        float rm22 = zz * C + c;
        float nm00 = this.m00 * rm00 + this.m10 * rm01 + this.m20 * rm02;
        float nm01 = this.m01 * rm00 + this.m11 * rm01 + this.m21 * rm02;
        float nm02 = this.m02 * rm00 + this.m12 * rm01 + this.m22 * rm02;
        float nm03 = this.m03 * rm00 + this.m13 * rm01 + this.m23 * rm02;
        float nm10 = this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12;
        float nm11 = this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12;
        float nm12 = this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12;
        float nm13 = this.m03 * rm10 + this.m13 * rm11 + this.m23 * rm12;
        dest._m20(this.m00 * rm20 + this.m10 * rm21 + this.m20 * rm22);
        dest._m21(this.m01 * rm20 + this.m11 * rm21 + this.m21 * rm22);
        dest._m22(this.m02 * rm20 + this.m12 * rm21 + this.m22 * rm22);
        dest._m23(this.m03 * rm20 + this.m13 * rm21 + this.m23 * rm22);
        dest._m00(nm00);
        dest._m01(nm01);
        dest._m02(nm02);
        dest._m03(nm03);
        dest._m10(nm10);
        dest._m11(nm11);
        dest._m12(nm12);
        dest._m13(nm13);
        dest._m30(this.m30);
        dest._m31(this.m31);
        dest._m32(this.m32);
        dest._m33(this.m33);
        dest._properties((byte)(this.properties & -14));
        return dest;
    }

    public Matrix4fe rotate(float ang, float x, float y, float z) {
        return this.rotate(ang, x, y, z, this);
    }

    public Matrix4fe rotation(float angle, float x, float y, float z) {
        float sin = (float)Math.sin((double)angle);
        float cos = (float)cosFromSin((double)sin, (double)angle);
        float C = 1.0F - cos;
        float xy = x * y;
        float xz = x * z;
        float yz = y * z;
        this._m00(cos + x * x * C);
        this._m10(xy * C - z * sin);
        this._m20(xz * C + y * sin);
        this._m30(0.0F);
        this._m01(xy * C + z * sin);
        this._m11(cos + y * y * C);
        this._m21(yz * C - x * sin);
        this._m31(0.0F);
        this._m02(xz * C - y * sin);
        this._m12(yz * C + x * sin);
        this._m22(cos + z * z * C);
        this._m32(0.0F);
        this._m03(0.0F);
        this._m13(0.0F);
        this._m23(0.0F);
        this._m33(1.0F);
        this._properties(2);
        return this;
    }

    public Matrix4fe rotateTranslation(float ang, float x, float y, float z, Matrix4fe dest) {
        float s = (float)Math.sin((double)ang);
        float c = (float)cosFromSin((double)s, (double)ang);
        float C = 1.0F - c;
        float xx = x * x;
        float xy = x * y;
        float xz = x * z;
        float yy = y * y;
        float yz = y * z;
        float zz = z * z;
        float rm00 = xx * C + c;
        float rm01 = xy * C + z * s;
        float rm02 = xz * C - y * s;
        float rm10 = xy * C - z * s;
        float rm11 = yy * C + c;
        float rm12 = yz * C + x * s;
        float rm20 = xz * C + y * s;
        float rm21 = yz * C - x * s;
        float rm22 = zz * C + c;
        dest._m20(rm20);
        dest._m21(rm21);
        dest._m22(rm22);
        dest._m00(rm00);
        dest._m01(rm01);
        dest._m02(rm02);
        dest._m03(0.0F);
        dest._m10(rm10);
        dest._m11(rm11);
        dest._m12(rm12);
        dest._m13(0.0F);
        dest._m30(this.m30);
        dest._m31(this.m31);
        dest._m32(this.m32);
        dest._m33(this.m33);
        dest._properties((byte)(this.properties & -14));
        return dest;
    }

    public Matrix4fe rotateAffine(float ang, float x, float y, float z, Matrix4fe dest) {
        float s = (float)Math.sin((double)ang);
        float c = (float)cosFromSin((double)s, (double)ang);
        float C = 1.0F - c;
        float xx = x * x;
        float xy = x * y;
        float xz = x * z;
        float yy = y * y;
        float yz = y * z;
        float zz = z * z;
        float rm00 = xx * C + c;
        float rm01 = xy * C + z * s;
        float rm02 = xz * C - y * s;
        float rm10 = xy * C - z * s;
        float rm11 = yy * C + c;
        float rm12 = yz * C + x * s;
        float rm20 = xz * C + y * s;
        float rm21 = yz * C - x * s;
        float rm22 = zz * C + c;
        float nm00 = this.m00 * rm00 + this.m10 * rm01 + this.m20 * rm02;
        float nm01 = this.m01 * rm00 + this.m11 * rm01 + this.m21 * rm02;
        float nm02 = this.m02 * rm00 + this.m12 * rm01 + this.m22 * rm02;
        float nm10 = this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12;
        float nm11 = this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12;
        float nm12 = this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12;
        dest._m20(this.m00 * rm20 + this.m10 * rm21 + this.m20 * rm22);
        dest._m21(this.m01 * rm20 + this.m11 * rm21 + this.m21 * rm22);
        dest._m22(this.m02 * rm20 + this.m12 * rm21 + this.m22 * rm22);
        dest._m23(0.0F);
        dest._m00(nm00);
        dest._m01(nm01);
        dest._m02(nm02);
        dest._m03(0.0F);
        dest._m10(nm10);
        dest._m11(nm11);
        dest._m12(nm12);
        dest._m13(0.0F);
        dest._m30(this.m30);
        dest._m31(this.m31);
        dest._m32(this.m32);
        dest._m33(this.m33);
        dest._properties((byte)(this.properties & -14));
        return dest;
    }

    public Matrix4f rotateAffine(float ang, float x, float y, float z) {
        return this.rotateAffine(ang, x, y, z, this);
    }

    public Matrix4fe rotateX(float ang, Matrix4fe dest) {
        if((this.properties & 4) != 0) {
            return this;//dest.rotationX(ang);
        } else {
            float sin = (float)Math.sin((double)ang);
            float cos = (float)cosFromSin((double)sin, (double)ang);
            float rm21 = -sin;
            float nm10 = this.m10 * cos + this.m20 * sin;
            float nm11 = this.m11 * cos + this.m21 * sin;
            float nm12 = this.m12 * cos + this.m22 * sin;
            float nm13 = this.m13 * cos + this.m23 * sin;
            dest._m20(this.m10 * rm21 + this.m20 * cos);
            dest._m21(this.m11 * rm21 + this.m21 * cos);
            dest._m22(this.m12 * rm21 + this.m22 * cos);
            dest._m23(this.m13 * rm21 + this.m23 * cos);
            dest._m10(nm10);
            dest._m11(nm11);
            dest._m12(nm12);
            dest._m13(nm13);
            dest._m00(this.m00);
            dest._m01(this.m01);
            dest._m02(this.m02);
            dest._m03(this.m03);
            dest._m30(this.m30);
            dest._m31(this.m31);
            dest._m32(this.m32);
            dest._m33(this.m33);
            dest._properties((byte)(this.properties & -14));
            return dest;
        }
    }

    public Matrix4fe rotateX(float ang) {
        return this.rotateX(ang, this);
    }

    public Matrix4fe rotateY(float ang, Matrix4fe dest) {
        if((this.properties & 4) != 0) {
            return this;//dest.rotationY(ang);
        } else {
            float sin = (float)Math.sin((double)ang);
            float cos = (float)cosFromSin((double)sin, (double)ang);
            float rm02 = -sin;
            float nm00 = this.m00 * cos + this.m20 * rm02;
            float nm01 = this.m01 * cos + this.m21 * rm02;
            float nm02 = this.m02 * cos + this.m22 * rm02;
            float nm03 = this.m03 * cos + this.m23 * rm02;
            dest._m20(this.m00 * sin + this.m20 * cos);
            dest._m21(this.m01 * sin + this.m21 * cos);
            dest._m22(this.m02 * sin + this.m22 * cos);
            dest._m23(this.m03 * sin + this.m23 * cos);
            dest._m00(nm00);
            dest._m01(nm01);
            dest._m02(nm02);
            dest._m03(nm03);
            dest._m10(this.m10);
            dest._m11(this.m11);
            dest._m12(this.m12);
            dest._m13(this.m13);
            dest._m30(this.m30);
            dest._m31(this.m31);
            dest._m32(this.m32);
            dest._m33(this.m33);
            dest._properties((byte)(this.properties & -14));
            return dest;
        }
    }

    public Matrix4fe rotateY(float ang) {
        return this.rotateY(ang, this);
    }

    public Matrix4fe rotateZ(float ang, Matrix4fe dest) {
        if((this.properties & 4) != 0) {
            return this;//dest.rotationZ(ang);
        } else {
            float sin = (float)Math.sin((double)ang);
            float cos = (float)cosFromSin((double)sin, (double)ang);
            float rm10 = -sin;
            float nm00 = this.m00 * cos + this.m10 * sin;
            float nm01 = this.m01 * cos + this.m11 * sin;
            float nm02 = this.m02 * cos + this.m12 * sin;
            float nm03 = this.m03 * cos + this.m13 * sin;
            dest._m10(this.m00 * rm10 + this.m10 * cos);
            dest._m11(this.m01 * rm10 + this.m11 * cos);
            dest._m12(this.m02 * rm10 + this.m12 * cos);
            dest._m13(this.m03 * rm10 + this.m13 * cos);
            dest._m00(nm00);
            dest._m01(nm01);
            dest._m02(nm02);
            dest._m03(nm03);
            dest._m20(this.m20);
            dest._m21(this.m21);
            dest._m22(this.m22);
            dest._m23(this.m23);
            dest._m30(this.m30);
            dest._m31(this.m31);
            dest._m32(this.m32);
            dest._m33(this.m33);
            dest._properties((byte)(this.properties & -14));
            return dest;
        }
    }

    public Matrix4fe rotateZ(float ang) {
        return this.rotateZ(ang, this);
    }

    public static double cosFromSin(double sin, double angle) {
        double cos = Math.sqrt(1.0D - sin * sin);
        double a = angle + 1.5707963267948966D;
        double b = a - (double)((int)(a / 6.283185307179586D)) * 6.283185307179586D;
        if(b < 0.0D) {
            b += 6.283185307179586D;
        }

        return b >= 3.141592653589793D?-cos:cos;
    }

    public Matrix4fe scale(float xyz) {
        return this.scale(xyz, xyz, xyz);
    }

    public Matrix4fe scale(float x, float y, float z) {
        return this.scale(x, y, z, this);
    }

    public Matrix4fe scale(float x, float y, float z, Matrix4fe dest) {
        return (this.properties & 4) != 0?dest.scaling(x, y, z):this.scaleGeneric(x, y, z, dest);
    }

    public Matrix4fe scaling(float x, float y, float z) {
        //MemUtil.INSTANCE.identity(this);
        identity(this);
        this._m00(x);
        this._m11(y);
        this._m22(z);
        this._properties(2);
        return this;
    }

    private Matrix4fe scaleGeneric(float x, float y, float z, Matrix4fe dest) {
        dest._m00(this.m00 * x);
        dest._m01(this.m01 * x);
        dest._m02(this.m02 * x);
        dest._m03(this.m03 * x);
        dest._m10(this.m10 * y);
        dest._m11(this.m11 * y);
        dest._m12(this.m12 * y);
        dest._m13(this.m13 * y);
        dest._m20(this.m20 * z);
        dest._m21(this.m21 * z);
        dest._m22(this.m22 * z);
        dest._m23(this.m23 * z);
        dest._m30(this.m30);
        dest._m31(this.m31);
        dest._m32(this.m32);
        dest._m33(this.m33);
        dest._properties((byte)(this.properties & -14));
        return dest;
    }

    public final Matrix4fe identity() {
        identity(this);
        return this;
    }

    public final Matrix4fe identity(Matrix4fe dest) {
        dest.m00 = 1.0F;
        dest.m01 = 0.0F;
        dest.m02 = 0.0F;
        dest.m03 = 0.0F;
        dest.m10 = 0.0F;
        dest.m11 = 1.0F;
        dest.m12 = 0.0F;
        dest.m13 = 0.0F;
        dest.m20 = 0.0F;
        dest.m21 = 0.0F;
        dest.m22 = 1.0F;
        dest.m23 = 0.0F;
        dest.m30 = 0.0F;
        dest.m31 = 0.0F;
        dest.m32 = 0.0F;
        dest.m33 = 1.0F;
        return dest;
    }

    public final void zero(Matrix4fe dest) {
        dest.m00 = 0.0F;
        dest.m01 = 0.0F;
        dest.m02 = 0.0F;
        dest.m03 = 0.0F;
        dest.m10 = 0.0F;
        dest.m11 = 0.0F;
        dest.m12 = 0.0F;
        dest.m13 = 0.0F;
        dest.m20 = 0.0F;
        dest.m21 = 0.0F;
        dest.m22 = 0.0F;
        dest.m23 = 0.0F;
        dest.m30 = 0.0F;
        dest.m31 = 0.0F;
        dest.m32 = 0.0F;
        dest.m33 = 0.0F;
    }

    public FloatBuffer get(FloatBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }

    public FloatBuffer get(int index, FloatBuffer buffer) {
        //MemUtil.INSTANCE.put(this, index, buffer);
        //put0(this, buffer);
        if(index == 0) {
            this.put0(this, buffer);
        } else {
            this.putN(this, index, buffer);
        }
        return buffer;
    }

    private void putN(Matrix4f m, int offset, FloatBuffer dest) {
        dest.put(offset, m.m00);
        dest.put(offset + 1, m.m01);
        dest.put(offset + 2, m.m02);
        dest.put(offset + 3, m.m03);
        dest.put(offset + 4, m.m10);
        dest.put(offset + 5, m.m11);
        dest.put(offset + 6, m.m12);
        dest.put(offset + 7, m.m13);
        dest.put(offset + 8, m.m20);
        dest.put(offset + 9, m.m21);
        dest.put(offset + 10, m.m22);
        dest.put(offset + 11, m.m23);
        dest.put(offset + 12, m.m30);
        dest.put(offset + 13, m.m31);
        dest.put(offset + 14, m.m32);
        dest.put(offset + 15, m.m33);
    }

    private void put0(Matrix4f m, FloatBuffer dest) {
        dest.put(0, m.m00);
        dest.put(1, m.m01);
        dest.put(2, m.m02);
        dest.put(3, m.m03);
        dest.put(4, m.m10);
        dest.put(5, m.m11);
        dest.put(6, m.m12);
        dest.put(7, m.m13);
        dest.put(8, m.m20);
        dest.put(9, m.m21);
        dest.put(10, m.m22);
        dest.put(11, m.m23);
        dest.put(12, m.m30);
        dest.put(13, m.m31);
        dest.put(14, m.m32);
        dest.put(15, m.m33);
    }

    public Matrix4fe translate(Vector3f offset) {
        return this.translate(offset.x, offset.y, offset.z);
    }

    public Matrix4fe translate(float x, float y, float z) {
        if((this.properties & 4) != 0) {
            return this.translation(x, y, z);
        } else {
            this._m30(this.m00 * x + this.m10 * y + this.m20 * z + this.m30);
            this._m31(this.m01 * x + this.m11 * y + this.m21 * z + this.m31);
            this._m32(this.m02 * x + this.m12 * y + this.m22 * z + this.m32);
            this._m33(this.m03 * x + this.m13 * y + this.m23 * z + this.m33);
            this.properties &= -6;
            return this;
        }
    }

    public Matrix4fe translation(float x, float y, float z) {
        //MemUtil.INSTANCE.identity(this);
        identity(this);
        this._m30(x);
        this._m31(y);
        this._m32(z);
        this._properties(10);
        return this;
    }

    public Matrix4fe mul(Matrix4fe right) {
        return this.mulGeneric(right, this);
    }

    private Matrix4fe mulGeneric(Matrix4fe right, Matrix4fe dest) {
        float nm00 = this.m00 * right.m00() + this.m10 * right.m01() + this.m20 * right.m02() + this.m30 * right.m03();
        float nm01 = this.m01 * right.m00() + this.m11 * right.m01() + this.m21 * right.m02() + this.m31 * right.m03();
        float nm02 = this.m02 * right.m00() + this.m12 * right.m01() + this.m22 * right.m02() + this.m32 * right.m03();
        float nm03 = this.m03 * right.m00() + this.m13 * right.m01() + this.m23 * right.m02() + this.m33 * right.m03();
        float nm10 = this.m00 * right.m10() + this.m10 * right.m11() + this.m20 * right.m12() + this.m30 * right.m13();
        float nm11 = this.m01 * right.m10() + this.m11 * right.m11() + this.m21 * right.m12() + this.m31 * right.m13();
        float nm12 = this.m02 * right.m10() + this.m12 * right.m11() + this.m22 * right.m12() + this.m32 * right.m13();
        float nm13 = this.m03 * right.m10() + this.m13 * right.m11() + this.m23 * right.m12() + this.m33 * right.m13();
        float nm20 = this.m00 * right.m20() + this.m10 * right.m21() + this.m20 * right.m22() + this.m30 * right.m23();
        float nm21 = this.m01 * right.m20() + this.m11 * right.m21() + this.m21 * right.m22() + this.m31 * right.m23();
        float nm22 = this.m02 * right.m20() + this.m12 * right.m21() + this.m22 * right.m22() + this.m32 * right.m23();
        float nm23 = this.m03 * right.m20() + this.m13 * right.m21() + this.m23 * right.m22() + this.m33 * right.m23();
        float nm30 = this.m00 * right.m30() + this.m10 * right.m31() + this.m20 * right.m32() + this.m30 * right.m33();
        float nm31 = this.m01 * right.m30() + this.m11 * right.m31() + this.m21 * right.m32() + this.m31 * right.m33();
        float nm32 = this.m02 * right.m30() + this.m12 * right.m31() + this.m22 * right.m32() + this.m32 * right.m33();
        float nm33 = this.m03 * right.m30() + this.m13 * right.m31() + this.m23 * right.m32() + this.m33 * right.m33();
        dest._m00(nm00);
        dest._m01(nm01);
        dest._m02(nm02);
        dest._m03(nm03);
        dest._m10(nm10);
        dest._m11(nm11);
        dest._m12(nm12);
        dest._m13(nm13);
        dest._m20(nm20);
        dest._m21(nm21);
        dest._m22(nm22);
        dest._m23(nm23);
        dest._m30(nm30);
        dest._m31(nm31);
        dest._m32(nm32);
        dest._m33(nm33);
        dest._properties(0);
        return dest;
    }

    public Matrix4fe mulAffine(Matrix4fe right, Matrix4fe dest) {
        float nm00 = this.m00 * right.m00() + this.m10 * right.m01() + this.m20 * right.m02();
        float nm01 = this.m01 * right.m00() + this.m11 * right.m01() + this.m21 * right.m02();
        float nm02 = this.m02 * right.m00() + this.m12 * right.m01() + this.m22 * right.m02();
        float nm03 = this.m03;
        float nm10 = this.m00 * right.m10() + this.m10 * right.m11() + this.m20 * right.m12();
        float nm11 = this.m01 * right.m10() + this.m11 * right.m11() + this.m21 * right.m12();
        float nm12 = this.m02 * right.m10() + this.m12 * right.m11() + this.m22 * right.m12();
        float nm13 = this.m13;
        float nm20 = this.m00 * right.m20() + this.m10 * right.m21() + this.m20 * right.m22();
        float nm21 = this.m01 * right.m20() + this.m11 * right.m21() + this.m21 * right.m22();
        float nm22 = this.m02 * right.m20() + this.m12 * right.m21() + this.m22 * right.m22();
        float nm23 = this.m23;
        float nm30 = this.m00 * right.m30() + this.m10 * right.m31() + this.m20 * right.m32() + this.m30;
        float nm31 = this.m01 * right.m30() + this.m11 * right.m31() + this.m21 * right.m32() + this.m31;
        float nm32 = this.m02 * right.m30() + this.m12 * right.m31() + this.m22 * right.m32() + this.m32;
        float nm33 = this.m33;
        dest._m00(nm00);
        dest._m01(nm01);
        dest._m02(nm02);
        dest._m03(nm03);
        dest._m10(nm10);
        dest._m11(nm11);
        dest._m12(nm12);
        dest._m13(nm13);
        dest._m20(nm20);
        dest._m21(nm21);
        dest._m22(nm22);
        dest._m23(nm23);
        dest._m30(nm30);
        dest._m31(nm31);
        dest._m32(nm32);
        dest._m33(nm33);
        dest._properties(2);
        return dest;
    }

    public Matrix4fe transpose3x3(Matrix4fe dest) {
        float nm00 = this.m00;
        float nm01 = this.m10;
        float nm02 = this.m20;
        float nm10 = this.m01;
        float nm11 = this.m11;
        float nm12 = this.m21;
        float nm20 = this.m02;
        float nm21 = this.m12;
        float nm22 = this.m22;
        dest._m00(nm00);
        dest._m01(nm01);
        dest._m02(nm02);
        dest._m10(nm10);
        dest._m11(nm11);
        dest._m12(nm12);
        dest._m20(nm20);
        dest._m21(nm21);
        dest._m22(nm22);
        dest._properties(0);
        return dest;
    }

    public Matrix4fe translationRotateScale(float tx, float ty, float tz, float qx, float qy, float qz, float qw, float sx, float sy, float sz) {
        float dqx = qx + qx;
        float dqy = qy + qy;
        float dqz = qz + qz;

        float q00 = dqx * qx;
        float q11 = dqy * qy;
        float q22 = dqz * qz;
        float q01 = dqx * qy;
        float q02 = dqx * qz;
        float q03 = dqx * qw;
        float q12 = dqy * qz;
        float q13 = dqy * qw;
        float q23 = dqz * qw;

        this._m00(sx - (q11 + q22) * sx);
        this._m01((q01 + q23) * sx);
        this._m02((q02 - q13) * sx);
        this._m03(0.0F);

        this._m10((q01 - q23) * sy);
        this._m11(sy - (q22 + q00) * sy);
        this._m12((q12 + q03) * sy);
        this._m13(0.0F);

        this._m20((q02 + q13) * sz);
        this._m21((q12 - q03) * sz);
        this._m22(sz - (q11 + q00) * sz);
        this._m23(0.0F);

        this._m30(tx);
        this._m31(ty);
        this._m32(tz);
        this._m33(1.0F);
        this._properties(2);
        return this;
    }

    public Vector3f getTranslation() {
        return new Vector3f(m30(), m31(), m32());
    }

    public Matrix4f toLWJGLMathMatrix() {
        Matrix4f mat = new Matrix4f();
        mat.m00 = this.m00;
        mat.m01 = this.m01;
        mat.m02 = this.m02;
        mat.m03 = this.m03;
        mat.m10 = this.m10;
        mat.m11 = this.m11;
        mat.m12 = this.m12;
        mat.m13 = this.m13;
        mat.m20 = this.m20;
        mat.m21 = this.m21;
        mat.m22 = this.m22;
        mat.m23 = this.m23;
        mat.m30 = this.m30;
        mat.m31 = this.m31;
        mat.m32 = this.m32;
        mat.m33 = this.m33;
        return mat;
    }
}
