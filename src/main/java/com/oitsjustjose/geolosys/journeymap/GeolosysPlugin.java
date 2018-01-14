package com.oitsjustjose.geolosys.journeymap;

import com.oitsjustjose.geolosys.util.Lib;
import journeymap.client.api.ClientPlugin;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.event.ClientEvent;
import net.minecraftforge.common.MinecraftForge;

@ClientPlugin
public class GeolosysPlugin implements IClientPlugin
{
    @Override
    public void initialize(IClientAPI api)
    {
        MinecraftForge.EVENT_BUS.register(new ForgeEventListener(api));
    }

    @Override
    public String getModId()
    {
        return Lib.MODID;
    }

    @Override
    public void onEvent(ClientEvent event)
    {
    }
}