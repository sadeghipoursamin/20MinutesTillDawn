package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * A simplified grayscale shader implementation
 */
public class GrayscaleShader {
    private static ShaderProgram grayscaleShader;

    /**
     * Get the grayscale shader, creating it if needed
     */
    public static ShaderProgram getShader() {
        if (grayscaleShader == null) {
            // Very simple vertex shader that just passes coordinates through
            String vertexShader =
                "attribute vec4 a_position;\n" +
                    "attribute vec4 a_color;\n" +
                    "attribute vec2 a_texCoord0;\n" +
                    "uniform mat4 u_projTrans;\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    v_color = a_color;\n" +
                    "    v_texCoords = a_texCoord0;\n" +
                    "    gl_Position = u_projTrans * a_position;\n" +
                    "}";

            // Simple fragment shader that converts to grayscale
            String fragmentShader =
                "#ifdef GL_ES\n" +
                    "precision mediump float;\n" +
                    "#endif\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 color = v_color * texture2D(u_texture, v_texCoords);\n" +
                    "    float gray = (color.r + color.g + color.b) / 3.0;\n" +
                    "    gl_FragColor = vec4(gray, gray, gray, color.a);\n" +
                    "}";

            // Create the shader with less strict compilation
            ShaderProgram.pedantic = false;
            grayscaleShader = new ShaderProgram(vertexShader, fragmentShader);

            // Check if compilation succeeded
            if (!grayscaleShader.isCompiled()) {
                Gdx.app.error("GrayscaleShader", "Compilation failed: " + grayscaleShader.getLog());
            }
            if (!grayscaleShader.isCompiled()) {
                Gdx.app.error("GrayscaleShader", "Compilation failed: " + grayscaleShader.getLog());
                // Add more detailed logging
                System.out.println("Shader compilation failed: " + grayscaleShader.getLog());
            } else {
                System.out.println("Shader compiled successfully!");
            }
        }

        return grayscaleShader;
    }

    /**
     * Dispose of shader resources
     */
    public static void dispose() {
        if (grayscaleShader != null) {
            grayscaleShader.dispose();
            grayscaleShader = null;
        }
    }
}
