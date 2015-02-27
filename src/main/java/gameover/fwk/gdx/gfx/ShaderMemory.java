package gameover.fwk.gdx.gfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MRS.OMARTIN on 16/11/13.
 */
public class ShaderMemory {

    private Map<SpriteBatch, Array<ShaderProgram>> shadersPerSpriteBatches = new HashMap<SpriteBatch, Array<ShaderProgram>>();

    private Array<ShaderProgram> findShaders(SpriteBatch batch){
        Array<ShaderProgram> shaderPrograms = shadersPerSpriteBatches.get(batch);
        if(shaderPrograms==null){
            shaderPrograms = new Array<ShaderProgram>();
            shadersPerSpriteBatches.put(batch, shaderPrograms);
        }
        return shaderPrograms;
    }

    public ShaderProgram switchShader(SpriteBatch batch, ShaderProgram newShader){
        Array<ShaderProgram> shaders = findShaders(batch);
        ShaderProgram old = null;
        if(shaders.size>0){
            old = shaders.removeIndex(shaders.size - 1);
        }
        shaders.add(newShader);
        batch.setShader(newShader);
        return old;
    }

    public void setShader(SpriteBatch batch, ShaderProgram newShader){
        Array<ShaderProgram> shaders = findShaders(batch);
        shaders.add(newShader);
        batch.setShader(newShader);
    }

    public void removeCurrentShader(SpriteBatch batch){
        Array<ShaderProgram> shaders = findShaders(batch);
        shaders.removeIndex(shaders.size-1);
        batch.setShader(shaders.get(shaders.size-1));
    }

    public void clearShader(SpriteBatch batch){
        Array<ShaderProgram> shaders = findShaders(batch);
        shaders.clear();
        batch.setShader(null);
    }

}
