
//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform LOWP float time;

//"in" varyings from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;

const float A = 0.03;
const float B = 200.0;
const float C = 5.0;

const float D = 0.003;
const float E = 13.0;
const float F = 9.0;

void main() {
    gl_FragColor = texture2D(u_texture, vec2(vTexCoord.x + (A * sin(B * vTexCoord.x) * sin(C * time)), vTexCoord.y + (D * sin(E * vTexCoord.y) * sin(F * time))));
}