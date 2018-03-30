package the_fireplace.rcad;

import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid=RainingCatsAndDogs.MODID, name=RainingCatsAndDogs.MODNAME, acceptableRemoteVersions = "*", guiFactory = "the_fireplace.rcad.client.RCADConfigGui")
@Mod.EventBusSubscriber
public class RainingCatsAndDogs {
	public static final String MODNAME = "Raining Cats and Dogs";
	public static final String MODID = "rcad";

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event){
		World world = event.player.world;
		if(!world.isRemote){
			long time = world.getTotalWorldTime();
			if(time % (ConfigValues.time_between_animals * 20) == 0 && world.provider.getDimensionType() == DimensionType.OVERWORLD && world.isThundering()){
				BlockPos spawnPos = new BlockPos(event.player.getPosition().getX()+world.rand.nextInt(ConfigValues.animal_spawn_radius*2)-ConfigValues.animal_spawn_radius, ConfigValues.animal_spawn_height, event.player.getPosition().getZ()+world.rand.nextInt(ConfigValues.animal_spawn_radius*2)-ConfigValues.animal_spawn_radius);
				if(world.rand.nextBoolean()){
					EntityOcelot newCat = new EntityOcelot(world);
					newCat.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
					world.spawnEntity(newCat);
					newCat.setPositionAndRotation(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), world.rand.nextFloat(), world.rand.nextFloat());
				} else {
					EntityWolf newWolf = new EntityWolf(world);
					newWolf.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
					world.spawnEntity(newWolf);
					newWolf.setPositionAndRotation(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), world.rand.nextFloat(), world.rand.nextFloat());
					if(ConfigValues.angry_wolves && world.rand.nextInt(10) != 5)
						newWolf.setAngry(true);
				}
			}
		}
	}

	@Config(modid = MODID)
	public static class ConfigValues{
		@Config.Comment("Time between animals (In seconds)")
		@Config.LangKey("time_between_animals")
		@Config.RangeInt(min=1)
		public static int time_between_animals = 5;

		@Config.Comment("Can wolves be angry about falling from the sky?")
		@Config.LangKey("angry_wolves")
		public static boolean angry_wolves = true;

		@Config.Comment("Animal Spawn Radius around the player")
		@Config.LangKey("animal_spawn_radius")
		@Config.RangeInt(min=1)
		public static int animal_spawn_radius = 64;

		@Config.Comment("Animal Spawn Height")
		@Config.LangKey("animal_spawn_height")
		@Config.RangeInt(min=2)
		public static int animal_spawn_height = 255;
	}
}
