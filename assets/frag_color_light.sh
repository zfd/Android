precision mediump float;
varying  vec4 vaaColor;
varying vec4 vambient;
varying vec4 vdiffuse;
varying vec4 vspecular;
void main()                         
{
    vec4 finalColor = vaaColor;
    gl_FragColor = finalColor*vambient+finalColor*vspecular+finalColor*vdiffuse;
}              