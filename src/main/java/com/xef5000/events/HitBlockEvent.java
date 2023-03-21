package com.xef5000.events;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


@Cancelable
public class HitBlockEvent extends Event {

    public HitBlockEvent(BlockPos pos) {
        this.blockPos = pos;
    }
    public BlockPos blockPos;
}
