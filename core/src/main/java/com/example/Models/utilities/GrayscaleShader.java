package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class GrayscaleShader {
    private static ShaderProgram grayscaleShader;
    private static boolean initialized = false;


    public static void initialize() {
        if (initialized) return;

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
                "    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));\n" +
                "    gl_FragColor = vec4(gray, gray, gray, color.a);\n" +
                "}";

        ShaderProgram.pedantic = false;
        try {
            grayscaleShader = new ShaderProgram(vertexShader, fragmentShader);

            if (!grayscaleShader.isCompiled()) {
                Gdx.app.error("GrayscaleShader", "Compilation failed: " + grayscaleShader.getLog());
                // Create a fallback shader that does nothing
                grayscaleShader = createFallbackShader();
            } else {
                Gdx.app.log("GrayscaleShader", "Shader compiled successfully!");
            }
            initialized = true;
        } catch (Exception e) {
            Gdx.app.error("GrayscaleShader", "Error creating shader: " + e.getMessage());
            grayscaleShader = createFallbackShader();
        }
    }


    private static ShaderProgram createFallbackShader() {
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

        String fragmentShader =
            "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "uniform sampler2D u_texture;\n" +
                "\n" +
                "void main() {\n" +
                "    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
                "}";

        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            Gdx.app.error("GrayscaleShader", "Failed to compile fallback shader: " + shader.getLog());
            return null;
        }

        return shader;
    }


    public static ShaderProgram getShader() {
        if (!initialized) {
            initialize();
        }
        return grayscaleShader;
    }


    public static void dispose() {
        if (grayscaleShader != null) {
            grayscaleShader.dispose();
            grayscaleShader = null;
        }
        initialized = false;
    }
}
