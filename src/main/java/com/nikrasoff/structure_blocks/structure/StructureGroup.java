package com.nikrasoff.structure_blocks.structure;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructureGroup {
    Array<StructureGroupEntry> structures = new Array<>();

    void addStructure(Structure structure, int weight){
        this.structures.add(new StructureGroupEntry(structure, weight));
    }

    void removeStructure(int idx){
        this.structures.removeIndex(idx);
    }

    StructureGroupEntry getStructure(int idx){
        return this.structures.get(idx);
    }

    public record StructureGroupEntry(Structure structure, int weight) implements Comparable<StructureGroupEntry>{
        @Override
        public int compareTo(@NotNull StructureGroup.StructureGroupEntry o) {
            return Integer.compare(weight, o.weight);
        }
    }
}
