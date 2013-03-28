package stage1;

import javax.microedition.lcdui.Image;
import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.PolygonMode;
import javax.microedition.m3g.Texture2D;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;

/**
 * Static class that handles creation of code-generated Meshes
 */
public class MeshFactory
{
  
    /**
     * 创建一个平面
     * @param 纹理文件名
     * @param cullFlags The flags for culling. See PolygonMode.
     * @return The finished textured mesh
     */
    public static Mesh createPlane(String texFilename, int cullFlags)
    {
        // The vertrices of the plane
        short vertrices[] = new short[] {-50, -50, 0,
                                       50, -50, 0,
                                       50, 50, 0,
                                       -50, 50, 0};

        // Texture coords of the plane
        short texCoords[] = new short[] {0, 255,
                                         255, 255,
                                         255, 0,
                                         0, 0};

        
        // The classes
        VertexArray vertexArray, texArray;
        IndexBuffer triangles;

        // Create the model's vertrices
        vertexArray = new VertexArray(vertrices.length/3, 3, 2);
        vertexArray.set(0, vertrices.length/3, vertrices);
        
        // Create the model's texture coords
        texArray = new VertexArray(texCoords.length / 2, 2, 2);
        texArray.set(0, texCoords.length / 2, texCoords);
        
        // Compose a VertexBuffer out of the previous vertrices and texture coordinates
        VertexBuffer vertexBuffer = new VertexBuffer();
        vertexBuffer.setPositions(vertexArray, 1.0f, null);
        vertexBuffer.setTexCoords(0, texArray, 1.0f/255.0f, null);
        
        // Create indices and face lengths
        int indices[] = new int[] {0, 1, 3, 2};
        int[] stripLengths = new int[] {4};
        
        // Create the model's triangles
        triangles = new TriangleStripArray(indices, stripLengths);

        // Create the appearance
        Appearance appearance = new Appearance();
        PolygonMode pm = new PolygonMode();
        pm.setCulling(cullFlags);
        appearance.setPolygonMode(pm);

        // Create and set the texture
        try
        {
            // Open image
            Image texImage = Image.createImage(texFilename);
            Texture2D theTexture = new Texture2D(new Image2D(Image2D.RGBA, texImage));
            
            // Replace the mesh's original colors (no blending)
            theTexture.setBlending(Texture2D.FUNC_REPLACE);
            
            // Set wrapping and filtering
            theTexture.setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP);
            theTexture.setFiltering(Texture2D.FILTER_BASE_LEVEL, Texture2D.FILTER_NEAREST);

            // Add texture to the appearance
            appearance.setTexture(0, theTexture);

        }
        catch(Exception e)
        {
            // Something went wrong
            //("Failed to create texture");
            //(e);
        }
        
        // Finally create the Mesh
        Mesh mesh = new Mesh(vertexBuffer, triangles, appearance);

        // All done
        return mesh;
    }
}
