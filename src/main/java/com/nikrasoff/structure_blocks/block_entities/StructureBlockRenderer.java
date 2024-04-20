package com.nikrasoff.structure_blocks.block_entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import finalforeach.cosmicreach.blocks.BlockPosition;
import ru.nern.becraft.bed.api.client.BlockEntityRenderer;

public class StructureBlockRenderer extends BlockEntityRenderer<StructureBlockEntity> {
    static ShapeRenderer shapeRenderer = new ShapeRenderer();

    public StructureBlockRenderer(){
        shapeRenderer.setColor(1, 0, 0, 1);
    }

    @Override
    public void render(StructureBlockEntity structureBlockEntity, Camera camera) {
        if (structureBlockEntity.size.lengthSq() < 0.1) return;
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        BlockPosition entityPos = structureBlockEntity.getBlockPos();
        shapeRenderer.box(entityPos.getGlobalX() + structureBlockEntity.offset.x + (structureBlockEntity.size.x < 0 ? 1 : 0),
                entityPos.getGlobalY() + structureBlockEntity.offset.y + (structureBlockEntity.size.y < 0 ? 1 : 0),
                entityPos.getGlobalZ() + structureBlockEntity.offset.z + (structureBlockEntity.size.z < 0 ? 1 : 0),
                structureBlockEntity.size.x, structureBlockEntity.size.y, -structureBlockEntity.size.z);
        shapeRenderer.end();
    }
}
