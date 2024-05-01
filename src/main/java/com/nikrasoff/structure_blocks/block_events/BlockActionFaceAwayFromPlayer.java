package com.nikrasoff.structure_blocks.block_events;

import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.Queue;
import com.nikrasoff.structure_blocks.util.DirectionVector;
import finalforeach.cosmicreach.blockevents.BlockEventTrigger;
import finalforeach.cosmicreach.blockevents.actions.ActionId;
import finalforeach.cosmicreach.blockevents.actions.IBlockAction;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Zone;

import java.util.Map;

@ActionId(
        id = "structure_blocks:face_away_from_player"
)
public class BlockActionFaceAwayFromPlayer implements IBlockAction {
    @Override
    public void act(BlockState blockState, BlockEventTrigger blockEventTrigger, Zone zone, Map<String, Object> map) {
        DirectionVector direction = DirectionVector.getClosestDirection(InGame.getLocalPlayer().getEntity().viewDirection.cpy().scl(-1));

        OrderedMap<String, String> newStateMap = new OrderedMap<>();
        newStateMap.put("facing", direction.getName());

        BlockState newBlockState = blockState.getVariantWithParams(newStateMap);
        BlockSetter.replaceBlock(zone, newBlockState, (BlockPosition) map.get("blockPos"), new Queue<>());
    }
}
