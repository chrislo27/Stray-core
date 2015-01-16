package stray.util.render;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shaders {

	private Shaders() {
	}

	public static final String VERTBAKE = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE
			+ ";\n" + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "attribute vec2 "
			+ ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

			"uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n" +

			"void main() {\n" + "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
			+ "	vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
			+ "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";
	public static final String FRAGBAKE = "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n"
			+ //
			"varying LOWP vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n"
			+ "uniform sampler2D u_texture;\n"
			+ "uniform sampler2D u_texture1;\n"
			+ "uniform sampler2D u_mask;\n"
			+ "void main(void) {\n"
			+ "	//sample the colour from the first texture\n"
			+ "	vec4 texColor0 = texture2D(u_texture, vTexCoord);\n"
			+ "\n"
			+ "	//sample the colour from the second texture\n"
			+ "	vec4 texColor1 = texture2D(u_texture1, vTexCoord);\n"
			+ "\n"
			+ "	//get the mask; we will only use the alpha channel\n"
			+ "	float mask = texture2D(u_mask, vTexCoord).a;\n"
			+ "\n"
			+ "	//interpolate the colours based on the mask\n"
			+ "	gl_FragColor = vColor * mix(texColor0, texColor1, mask);\n" + "}";

	public static final String VERTBLUEPRINT = "attribute vec4 a_position;\r\n"
			+ "attribute vec4 a_color;\r\n" + "attribute vec2 a_texCoord0;\r\n" + "\r\n"
			+ "uniform mat4 u_projTrans;\r\n" + "\r\n" + "varying vec4 v_color;\r\n"
			+ "varying vec2 v_texCoords;\r\n" + "\r\n" + "void main() {\r\n"
			+ "    v_color = a_color;\r\n" + "    v_texCoords = a_texCoord0;\r\n"
			+ "    gl_Position = u_projTrans * a_position;\r\n" + "}";

	public static final String FRAGBLUEPRINT = "#ifdef GL_ES\r\n"
			+ "    precision mediump float;\r\n" + "#endif\r\n" + "\r\n"
			+ "varying vec4 v_color;\r\n" + "varying vec2 v_texCoords;\r\n"
			+ "uniform sampler2D u_texture;\r\n" + "uniform mat4 u_projTrans;\r\n" + "\r\n"
			+ "void main() {\r\n"
			+ "        vec3 color = texture2D(u_texture, v_texCoords).rgb;\r\n"
			+ "        float gray = (color.r + color.g + color.b) / 3.0;\r\n"
			+ "        vec3 grayscale = vec3(gray);\r\n"
			+ "		 grayscale.b = grayscale.b + 0.25;\r\n"
			+ "        gl_FragColor = vec4(grayscale, texture2D(u_texture, v_texCoords).a);\r\n"
			+ "}";

	public static final String FRAGBLUEPRINT2 =
	// GL ES specific stuff
	"#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n#version 120"
			+ //
			"varying LOWP vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n"
			+ "uniform sampler2D u_texture;\n"
			+ "void main() {\n"
			+ "       vec4 texColor = texture2D(u_texture, vTexCoord);\n"
			+ "       \n"
			+ "if(texColor.r < 0.019608 || texColor.g < 0.019608 || texColor.b < 0.019608){ \n"
			+ "       gl_FragColor = vec4(0.0, 0.0, 0.0, texColor.a);}\n"
			+ "       gl_FragColor = vec4(0.019607, 0.015686, 0.564705, vColor.a);\n" + "}";

	public static final String VERTTOON = "#ifdef GL_ES\r\n" + "#define MED mediump\r\n"
			+ "#else\r\n" + "#define MED\r\n" + "#endif\r\n" + " \r\n"
			+ "attribute vec4 a_position;\r\n" + "attribute vec2 a_texCoord0;\r\n"
			+ "varying MED vec2 v_texCoord0;\r\n" + " \r\n" + "void main(){\r\n"
			+ "    v_texCoord0 = a_texCoord0;\r\n" + "    gl_Position = a_position;\r\n" + "}";

	public static final String FRAGTOON = "#ifdef GL_ES\r\n" + "#define LOWP lowp\r\n"
			+ "#define MED mediump\r\n" + "precision lowp float;\r\n" + "#else\r\n"
			+ "#define LOWP\r\n" + "#define MED\r\n" + "#endif\r\n" + " \r\n"
			+ "uniform sampler2D u_texture;\r\n" + "varying MED vec2 v_texCoord0;\r\n" + " \r\n"
			+ "float toonify(in float intensity) {\r\n" + "    if (intensity > 0.8)\r\n"
			+ "        return 1.0;\r\n" + "    else if (intensity > 0.5)\r\n"
			+ "        return 0.8;\r\n" + "    else if (intensity > 0.25)\r\n"
			+ "        return 0.3;\r\n" + "    else\r\n" + "        return 0.1;\r\n" + "}\r\n"
			+ " \r\n" + "void main(){\r\n"
			+ "    vec4 color = texture2D(u_texture, v_texCoord0);\r\n"
			+ "    float factor = toonify(max(color.r, max(color.g, color.b)));\r\n"
			+ "    gl_FragColor = vec4(factor*color.rgb, color.a);\r\n" + "}";

	public static final String VERTGREY = "attribute vec4 a_position;\r\n"
			+ "attribute vec4 a_color;\r\n" + "attribute vec2 a_texCoord0;\r\n" + "\r\n"
			+ "uniform mat4 u_projTrans;\r\n" + "\r\n" + "varying vec4 v_color;\r\n"
			+ "varying vec2 v_texCoords;\r\n" + "\r\n" + "void main() {\r\n"
			+ "    v_color = a_color;\r\n" + "    v_texCoords = a_texCoord0;\r\n"
			+ "    gl_Position = u_projTrans * a_position;\r\n" + "}";

	public static final String FRAGGREY = "#ifdef GL_ES\r\n" + "    precision mediump float;\r\n"
			+ "#endif\r\n" + "\r\n" + "varying vec4 v_color;\r\n" + "varying vec2 v_texCoords;\r\n"
			+ "uniform sampler2D u_texture;\r\n"
			+ "uniform mat4 u_projTrans;\r\n uniform float intensity;" + "\r\n"
			+ "void main() {\r\n" + "        vec4 color = texture2D(u_texture, v_texCoords);\r\n"
			+ "        float gray = (color.r + color.g + color.b) / 3.0;\r\n"
			+ "        vec3 grayscale = vec3(gray, gray, gray);\r\n"
			+ "		 vec3 finalcolor = mix(v_color.rgb, grayscale, intensity);\r\n"
			+ "        gl_FragColor = vec4(color.rgb, intensity);\r\n" + "}";

	public static final String VERTBLUR = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE
			+ ";\n" + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "attribute vec2 "
			+ ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

			"uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n" +

			"void main() {\n" + "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
			+ "	vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
			+ "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";

	public static final String FRAGBLUR = "#ifdef GL_ES\n" + 
			"#define LOWP lowp\n" + 
			"precision mediump float;\n" + 
			"#else\n" + 
			"#define LOWP \n" + 
			"#endif\n" + 
			"varying LOWP vec4 vColor;\n" + 
			"varying vec2 vTexCoord;\n" + 
			"\n" + 
			"uniform sampler2D u_texture;\n" + 
			"uniform float resolution;\n" + 
			"uniform float radius;\n" + 
			"uniform vec2 dir;\n" + 
			"\n" + 
			"void main() {\n" + 
			"	vec4 sum = vec4(0.0);\n" + 
			"	vec2 tc = vTexCoord;\n" + 
			"	float blur = radius/resolution; \n" + 
			"    \n" + 
			"    float hstep = dir.x;\n" + 
			"    float vstep = dir.y;\n" + 
			"    \n" + 
			"	sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.05;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.09;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.12;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.15;\n" + 
			"	\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.16;\n" + 
			"	\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.15;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.12;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.09;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.05;\n" + 
			"\n" + 
			"	gl_FragColor = vColor * vec4(sum.rgb, 1.0);\n" + 
			"}";

	public static final String VERTWARP = "uniform float screen;\r\n" + "\r\n" + "void main()\r\n"
			+ "{\r\n" + "   gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\r\n"
			+ "   gl_TexCoord[0] = vec4(gl_Position.x * 1.77, gl_Position.y, 1.0, 1.0);\r\n" + "}";

	public static final String FRAGWARP = "vec3 mod289(vec3 x) {\r\n"
			+ "  return x - floor(x * (1.0 / 289.0)) * 289.0;\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "vec4 mod289(vec4 x) {\r\n"
			+ "  return x - floor(x * (1.0 / 289.0)) * 289.0;\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "vec4 permute(vec4 x) {\r\n"
			+ "     return mod289(((x*34.0)+1.0)*x);\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "vec4 taylorInvSqrt(vec4 r)\r\n"
			+ "{\r\n"
			+ "  return 1.79284291400159 - 0.85373472095314 * r;\r\n"
			+ "}\r\n"
			+ "\r\n"
			+ "float snoise(vec3 v)\r\n"
			+ "  { \r\n"
			+ "  const vec2  C = vec2(1.0/6.0, 1.0/3.0) ;\r\n"
			+ "  const vec4  D = vec4(0.0, 0.5, 1.0, 2.0);\r\n"
			+ "\r\n"
			+ "// First corner\r\n"
			+ "  vec3 i  = floor(v + dot(v, C.yyy) );\r\n"
			+ "  vec3 x0 =   v - i + dot(i, C.xxx) ;\r\n"
			+ "\r\n"
			+ "// Other corners\r\n"
			+ "  vec3 g = step(x0.yzx, x0.xyz);\r\n"
			+ "  vec3 l = 1.0 - g;\r\n"
			+ "  vec3 i1 = min( g.xyz, l.zxy );\r\n"
			+ "  vec3 i2 = max( g.xyz, l.zxy );\r\n"
			+ "\r\n"
			+ "  //   x0 = x0 - 0.0 + 0.0 * C.xxx;\r\n"
			+ "  //   x1 = x0 - i1  + 1.0 * C.xxx;\r\n"
			+ "  //   x2 = x0 - i2  + 2.0 * C.xxx;\r\n"
			+ "  //   x3 = x0 - 1.0 + 3.0 * C.xxx;\r\n"
			+ "  vec3 x1 = x0 - i1 + C.xxx;\r\n"
			+ "  vec3 x2 = x0 - i2 + C.yyy; // 2.0*C.x = 1/3 = C.y\r\n"
			+ "  vec3 x3 = x0 - D.yyy;      // -1.0+3.0*C.x = -0.5 = -D.y\r\n"
			+ "\r\n"
			+ "// Permutations\r\n"
			+ "  i = mod289(i); \r\n"
			+ "  vec4 p = permute( permute( permute( \r\n"
			+ "             i.z + vec4(0.0, i1.z, i2.z, 1.0 ))\r\n"
			+ "           + i.y + vec4(0.0, i1.y, i2.y, 1.0 )) \r\n"
			+ "           + i.x + vec4(0.0, i1.x, i2.x, 1.0 ));\r\n"
			+ "\r\n"
			+ "// Gradients: 7x7 points over a square, mapped onto an octahedron.\r\n"
			+ "// The ring size 17*17 = 289 is close to a multiple of 49 (49*6 = 294)\r\n"
			+ "  float n_ = 0.142857142857; // 1.0/7.0\r\n"
			+ "  vec3  ns = n_ * D.wyz - D.xzx;\r\n"
			+ "\r\n"
			+ "  vec4 j = p - 49.0 * floor(p * ns.z * ns.z);  //  mod(p,7*7)\r\n"
			+ "\r\n"
			+ "  vec4 x_ = floor(j * ns.z);\r\n"
			+ "  vec4 y_ = floor(j - 7.0 * x_ );    // mod(j,N)\r\n"
			+ "\r\n"
			+ "  vec4 x = x_ *ns.x + ns.yyyy;\r\n"
			+ "  vec4 y = y_ *ns.x + ns.yyyy;\r\n"
			+ "  vec4 h = 1.0 - abs(x) - abs(y);\r\n"
			+ "\r\n"
			+ "  vec4 b0 = vec4( x.xy, y.xy );\r\n"
			+ "  vec4 b1 = vec4( x.zw, y.zw );\r\n"
			+ "\r\n"
			+ "  //vec4 s0 = vec4(lessThan(b0,0.0))*2.0 - 1.0;\r\n"
			+ "  //vec4 s1 = vec4(lessThan(b1,0.0))*2.0 - 1.0;\r\n"
			+ "  vec4 s0 = floor(b0)*2.0 + 1.0;\r\n"
			+ "  vec4 s1 = floor(b1)*2.0 + 1.0;\r\n"
			+ "  vec4 sh = -step(h, vec4(0.0));\r\n"
			+ "\r\n"
			+ "  vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy ;\r\n"
			+ "  vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww ;\r\n"
			+ "\r\n"
			+ "  vec3 p0 = vec3(a0.xy,h.x);\r\n"
			+ "  vec3 p1 = vec3(a0.zw,h.y);\r\n"
			+ "  vec3 p2 = vec3(a1.xy,h.z);\r\n"
			+ "  vec3 p3 = vec3(a1.zw,h.w);\r\n"
			+ "\r\n"
			+ "//Normalise gradients\r\n"
			+ "  vec4 norm = taylorInvSqrt(vec4(dot(p0,p0), dot(p1,p1), dot(p2, p2), dot(p3,p3)));\r\n"
			+ "  p0 *= norm.x;\r\n"
			+ "  p1 *= norm.y;\r\n"
			+ "  p2 *= norm.z;\r\n"
			+ "  p3 *= norm.w;\r\n"
			+ "\r\n"
			+ "// Mix final noise value\r\n"
			+ "  vec4 m = max(0.6 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);\r\n"
			+ "  m = m * m;\r\n"
			+ "  return 42.0 * dot( m*m, vec4( dot(p0,x0), dot(p1,x1), \r\n"
			+ "                                dot(p2,x2), dot(p3,x3) ) );\r\n"
			+ "  }\r\n"
			+ "\r\n"
			+ "///////////////////////////////////////\r\n"
			+ "//\r\n"
			+ "//         End Simplex Noise\r\n"
			+ "//\r\n"
			+ "///////////////////////////////////////\r\n"
			+ "\r\n"
			+ "uniform float time;\r\n"
			+ "uniform vec2 offset;\r\n"
			+ "\r\n"
			+ "float fbm(vec2 pos)\r\n"
			+ "{\r\n"
			+ "	float t = time / 20.0;\r\n"
			+ "	float base = 0.75;\r\n"
			+ "	float n = 0.0;// * snoise(vec3(base * pos.x, base * pos.y, t));\r\n"
			+ "	n += 0.5 * (snoise(vec3(2.0 * base * pos.x, 2.0 * base * pos.y, 1.4*t)));\r\n"
			+ "	n += 0.25 * (snoise(vec3(4.0 * base * pos.x, 4.0 * base * pos.y, 2.4*t)));\r\n"
			+ "    n += 0.125 * (snoise(vec3(8.0 * base * pos.x, 8.0 * base * pos.y, 3.4*t)));\r\n"
			+ "    n += 0.0625 * (snoise(vec3(16.0 * base * pos.x, 16.0 * base * pos.y, 4.4*t)));\r\n"
			+ "	n += 0.03125 * (snoise(vec3(32.0 * base * pos.x, 32.0 * base * pos.y, 5.4*t)));\r\n"
			+ "	n = (n + 1.0) / 2.0;\r\n" + "	return n * 0.7;\r\n" + "}\r\n" + "\r\n"
			+ "void main()\r\n" + "{\r\n" + "	vec2 p = gl_TexCoord[0].xy + offset;\r\n"
			+ "	vec2 q = vec2(fbm(p), fbm(p));\r\n" + "	float n = fbm(p + 4.0*q);\r\n" + "	\r\n"
			+ "	vec4 color1 = vec4(1.0, 0.65, 0.0, 1.0);\r\n"
			+ "	vec4 color2 = vec4(1.0, 0.0, 0.0, 1.0);\r\n" + "	\r\n"
			+ "	vec4 pcolor = color1*4.0;\r\n"
			+ "	vec4 qcolor = mix(color2, pcolor, q.x*q.y*4.0); \r\n" + "	\r\n"
			+ "	vec4 color = qcolor * n;\r\n"
			+ "	gl_FragColor = vec4(color.r, color.g, color.b, 1.0);\r\n" + "}";

}
