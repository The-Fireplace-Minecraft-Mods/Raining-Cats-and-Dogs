/*package the_fireplace.rcad.compat;

import CoroUtil.util.Vec3;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Level;
import the_fireplace.rcad.RainingCatsAndDogs;
import weather2.ServerTickHandler;
import weather2.weathersystem.WeatherManagerBase;

public class Weather2Compat implements IWeather2Compat {

	private WeatherManagerBase weatherManager;

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		World world = event.player.world;
		if (!world.isRemote) {
			long time = world.getTotalWorldTime();
			if(weatherManager == null)
				weatherManager = ServerTickHandler.getWeatherSystemForDim(world.provider.getDimension());
			if(weatherManager == null) {
				RainingCatsAndDogs.LOGGER.log(Level.ERROR, "Weather2 weathermanager for dimension %d is null!", world.provider.getDimension());
				return;
			}
			if (time % (RainingCatsAndDogs.ConfigValues.time_between_animals * 20) == 0 && world.provider.getDimensionType() == DimensionType.OVERWORLD && weatherManager.isPrecipitatingAt(new Vec3(event.player.getPosition()))) {
				BlockPos spawnPos = new BlockPos(event.player.getPosition().getX() + world.rand.nextInt(RainingCatsAndDogs.ConfigValues.animal_spawn_radius * 2) - RainingCatsAndDogs.ConfigValues.animal_spawn_radius, RainingCatsAndDogs.ConfigValues.animal_spawn_height, event.player.getPosition().getZ() + world.rand.nextInt(RainingCatsAndDogs.ConfigValues.animal_spawn_radius * 2) - RainingCatsAndDogs.ConfigValues.animal_spawn_radius);
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
					if (RainingCatsAndDogs.ConfigValues.angry_wolves && world.rand.nextInt(10) != 5)
						newWolf.setAngry(true);
				}
			}
		}
	}
}*/
