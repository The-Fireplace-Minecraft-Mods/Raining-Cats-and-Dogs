package the_fireplace.rcad;

import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;
import the_fireplace.rcad.compat.IWeather2Compat;
import the_fireplace.rcad.compat.Weather2Compat;

@Mod(modid = RainingCatsAndDogs.MODID, name = RainingCatsAndDogs.MODNAME, version = "${version}", acceptableRemoteVersions = "*", guiFactory = "the_fireplace.rcad.client.RCADConfigGui")
@Mod.EventBusSubscriber
public class RainingCatsAndDogs {
	public static final String MODNAME = "Raining Cats and Dogs";
	public static final String MODID = "rcad";

	@SuppressWarnings("deprecation")
	public static Logger LOGGER = FMLLog.getLogger();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		LOGGER = event.getModLog();
		IWeather2Compat compat;
		if(Loader.isModLoaded("weather2")){
			compat = new Weather2Compat();
			compat.register();
		} else {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		World world = event.player.world;
		if (!world.isRemote) {
			long time = world.getTotalWorldTime();
			if (time % (ConfigValues.time_between_animals * 20) == 0 && world.provider.getDimensionType() == DimensionType.OVERWORLD && world.isThundering()) {
				BlockPos spawnPos = new BlockPos(event.player.getPosition().getX() + world.rand.nextInt(ConfigValues.animal_spawn_radius * 2) - ConfigValues.animal_spawn_radius, ConfigValues.animal_spawn_height, event.player.getPosition().getZ() + world.rand.nextInt(ConfigValues.animal_spawn_radius * 2) - ConfigValues.animal_spawn_radius);
				if (world.rand.nextBoolean()) {
					EntityOcelot newCat = new EntityOcelot(world);
					newCat.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
					world.spawnEntity(newCat);
					newCat.setPositionAndRotation(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), world.rand.nextFloat(), world.rand.nextFloat());
				} else {
					EntityWolf newWolf = new EntityWolf(world);
					newWolf.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
					world.spawnEntity(newWolf);
					newWolf.setPositionAndRotation(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), world.rand.nextFloat(), world.rand.nextFloat());
					if (ConfigValues.angry_wolves && world.rand.nextInt(10) != 5)
						newWolf.setAngry(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if(event.getModID().equals(MODID))
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
	}

	@Config(modid = MODID)
	public static class ConfigValues {
		@Config.Comment("Time between animals (In seconds)")
		@Config.LangKey("time_between_animals")
		@Config.RangeInt(min = 1)
		public static int time_between_animals = 5;

		@Config.Comment("Can wolves be angry about falling from the sky?")
		@Config.LangKey("angry_wolves")
		public static boolean angry_wolves = true;

		@Config.Comment("Animal Spawn Radius around the player")
		@Config.LangKey("animal_spawn_radius")
		@Config.RangeInt(min = 1)
		public static int animal_spawn_radius = 64;

		@Config.Comment("Animal Spawn Height")
		@Config.LangKey("animal_spawn_height")
		@Config.RangeInt(min = 2)
		public static int animal_spawn_height = 255;
	}
}
