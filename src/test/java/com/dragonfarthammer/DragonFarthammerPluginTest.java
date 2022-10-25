package com.dragonfarthammer;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DragonFarthammerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DragonFarthammerPlugin.class);
		RuneLite.main(args);
	}
}