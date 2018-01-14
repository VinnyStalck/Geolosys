package com.oitsjustjose.geolosys.journeymap;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.util.Lib;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.display.DisplayType;
import journeymap.client.api.display.Waypoint;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventListener
{
    private IClientAPI jmAPI;

    public ForgeEventListener(IClientAPI jmAPI)
    {
        this.jmAPI = jmAPI;
    }

    @SubscribeEvent
    public void registerEvent(RightClickBlock event)
    {
        try
        {
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            if (state.getBlock() == Geolosys.getInstance().ORE_SAMPLE || state.getBlock() == Geolosys.getInstance().ORE_SAMPLE_VANILLA)
            {
                if (event.getEntityPlayer().isSneaking())
                {
                    if (jmAPI.playerAccepts(Lib.MODID, DisplayType.Waypoint))
                    {
                        String name = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName();
                        try
                        {
                            jmAPI.show(new Waypoint(Lib.MODID, name, event.getWorld().provider.getDimension(), event.getPos()));
                        }
                        catch (Throwable t)
                        {
                            Geolosys.getInstance().LOGGER.info(t.getMessage());
                        }
                    }
                }
            }
        }
        catch (NullPointerException ignored)
        {
        }
    }
}
