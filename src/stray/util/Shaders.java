package stray.util;

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

	public static final String VERTBLUEPRINT = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE
			+ ";\n" + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "attribute vec2 "
			+ ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

			"uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n" +

			"void main() {\n" + "       vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
			+ "       vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
			+ "       gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
			+ "}";

	public static final String FRAGBLUEPRINT = "uniform sampler2D tex;\r\n" + "\r\n"
			+ "float threshold(in float thr1, in float thr2 , in float val) {\r\n"
			+ " if (val < thr1) {return 0.0;}\r\n" + " if (val > thr2) {return 1.0;}\r\n"
			+ " return val;\r\n" + "}\r\n" + "\r\n"
			+ "// averaged pixel intensity from 3 color channels\r\n"
			+ "float avg_intensity(in vec4 pix) {\r\n" + " return (pix.r + pix.g + pix.b)/3.;\r\n"
			+ "}\r\n" + "\r\n" + "vec4 get_pixel(in vec2 coords, in float dx, in float dy) {\r\n"
			+ " return texture2D(tex,coords + vec2(dx, dy));\r\n" + "}\r\n" + "\r\n"
			+ "// returns pixel color\r\n" + "float IsEdge(in vec2 coords){\r\n"
			+ "  float dxtex = 1.0 / 512.0 /*image width*/;\r\n"
			+ "  float dytex = 1.0 / 512.0 /*image height*/;\r\n" + "  float pix[9];\r\n"
			+ "  int k = -1;\r\n" + "  float delta;\r\n" + "\r\n"
			+ "  // read neighboring pixel intensities\r\n" + "  for (int i=-1; i<2; i++) {\r\n"
			+ "   for(int j=-1; j<2; j++) {\r\n" + "    k++;\r\n"
			+ "    pix[k] = avg_intensity(get_pixel(coords,float(i)*dxtex,\r\n"
			+ "                                          float(j)*dytex));\r\n" + "   }\r\n"
			+ "  }\r\n" + "\r\n" + "  // average color differences around neighboring pixels\r\n"
			+ "  delta = (abs(pix[1]-pix[7])+\r\n" + "          abs(pix[5]-pix[3]) +\r\n"
			+ "          abs(pix[0]-pix[8])+\r\n" + "          abs(pix[2]-pix[6])\r\n"
			+ "           )/4.;\r\n" + "\r\n"
			+ "  return threshold(0.25,0.4,clamp(1.8*delta,0.0,1.0));\r\n" + "}\r\n" + "\r\n"
			+ "void main()\r\n" + "{\r\n" + "  vec4 color = vec4(0.0,0.0,0.0,1.0);\r\n"
			+ "  color.g = IsEdge(gl_TexCoord[0].xy);\r\n" + "  gl_FragColor = color;\r\n" + "}";

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

	public static final String VERTGREY = "attribute vec4 a_position;\r\n" + 
			"attribute vec4 a_color;\r\n" + 
			"attribute vec2 a_texCoord0;\r\n" + 
			"\r\n" + 
			"uniform mat4 u_projTrans;\r\n" + 
			"\r\n" + 
			"varying vec4 v_color;\r\n" + 
			"varying vec2 v_texCoords;\r\n" + 
			"\r\n" + 
			"void main() {\r\n" + 
			"    v_color = a_color;\r\n" + 
			"    v_texCoords = a_texCoord0;\r\n" + 
			"    gl_Position = u_projTrans * a_position;\r\n" + 
			"}";

	public static final String FRAGGREY = "#ifdef GL_ES\r\n" + 
			"    precision mediump float;\r\n" + 
			"#endif\r\n" + 
			"\r\n" + 
			"varying vec4 v_color;\r\n" + 
			"varying vec2 v_texCoords;\r\n" + 
			"uniform sampler2D u_texture;\r\n" + 
			"uniform mat4 u_projTrans;\r\n" + 
			"\r\n" + 
			"void main() {\r\n" + 
			"        vec3 color = texture2D(u_texture, v_texCoords).rgb;\r\n" + 
			"        float gray = (color.r + color.g + color.b) / 3.0;\r\n" + 
			"        vec3 grayscale = vec3(gray);\r\n" + 
			"\r\n" + 
			"        gl_FragColor = vec4(grayscale, texture2D(u_texture, v_texCoords).a);\r\n" + 
			"}";
	
	public static final String VERTBLUR = "//combined projection and view matrix\r\n" + 
			"uniform mat4 u_projView;\r\n" + 
			"\r\n" + 
			"//\"in\" attributes from our SpriteBatch\r\n" + 
			"attribute vec2 Position;\r\n" + 
			"attribute vec2 TexCoord;\r\n" + 
			"attribute vec4 Color;\r\n" + 
			"\r\n" + 
			"//\"out\" varyings to our fragment shader\r\n" + 
			"varying vec4 vColor;\r\n" + 
			"varying vec2 vTexCoord;\r\n" + 
			" \r\n" + 
			"void main() {\r\n" + 
			"	vColor = Color;\r\n" + 
			"	vTexCoord = TexCoord;\r\n" + 
			"	gl_Position = u_projView * vec4(Position, 0.0, 1.0);\r\n" + 
			"}";
	
	public static final String FRAGBLUR = "//\"in\" attributes from our vertex shader\r\n" + 
			"varying vec4 vColor;\r\n" + 
			"varying vec2 vTexCoord;\r\n" + 
			"\r\n" + 
			"//declare uniforms\r\n" + 
			"uniform sampler2D u_texture;\r\n" + 
			"uniform float resolution;\r\n" + 
			"uniform float radius;\r\n" + 
			"uniform vec2 dir;\r\n" + 
			"\r\n" + 
			"void main() {\r\n" + 
			"    //this will be our RGBA sum\r\n" + 
			"    vec4 sum = vec4(0.0);\r\n" + 
			"\r\n" + 
			"    //our original texcoord for this fragment\r\n" + 
			"    vec2 tc = vTexCoord;\r\n" + 
			"\r\n" + 
			"    //the amount to blur, i.e. how far off center to sample from \r\n" + 
			"    //1.0 -> blur by one pixel\r\n" + 
			"    //2.0 -> blur by two pixels, etc.\r\n" + 
			"    float blur = radius/resolution; \r\n" + 
			"\r\n" + 
			"    //the direction of our blur\r\n" + 
			"    //(1.0, 0.0) -> x-axis blur\r\n" + 
			"    //(0.0, 1.0) -> y-axis blur\r\n" + 
			"    float hstep = dir.x;\r\n" + 
			"    float vstep = dir.y;\r\n" + 
			"\r\n" + 
			"    //apply blurring, using a 9-tap filter with predefined gaussian weights\r\n" + 
			"\r\n" + 
			"    sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.0162162162;\r\n" + 
			"    sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.0540540541;\r\n" + 
			"    sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.1216216216;\r\n" + 
			"    sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.1945945946;\r\n" + 
			"\r\n" + 
			"    sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.2270270270;\r\n" + 
			"\r\n" + 
			"    sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.1945945946;\r\n" + 
			"    sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.1216216216;\r\n" + 
			"    sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.0540540541;\r\n" + 
			"    sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.0162162162;\r\n" + 
			"\r\n" + 
			"    //discard alpha for our simple demo, multiply by vertex color and return\r\n" + 
			"    gl_FragColor = vColor * vec4(sum.rgb, 1.0);\r\n" + 
			"}";

}
