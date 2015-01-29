#version 130

//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform float time = 1.0;

//"in" varyings from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;


const float pi = 3.14159;
const float speed = 1.0;
const vec2 amplitude = vec2(1.0, 2.0);
const vec2 frequency = vec2(2.0, 1.0);

void main() {
    vec2 uv = gl_TexCoord[0].xy;
    vec2 temp = vec2(uv.x, uv.y);
   
    float angularFre = 2.0 * pi * frequency.x;
    uv.x += sin(temp.y * angularFre  + (time * (speed * 10.0))) * ((amplitude.x) / textureSize(u_texture, 0).x) * 10.0;
   
    vec4 color = texture2D(u_texture, uv);

    gl_FragColor = color;
}