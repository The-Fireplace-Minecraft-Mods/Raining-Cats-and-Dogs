package the_fireplace.rcad;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

@Mod(RainingCatsAndDogs.MODID)
@Mod.EventBusSubscriber
public class RainingCatsAndDogs {
	public static final String MODID = "rcad";

	//public static Logger LOGGER = LogManager.getLogger(MODID);

	public RainingCatsAndDogs(){
		/*IWeather2Compat compat;
		if(Loader.isModLoaded("weather2")){
			compat = new Weather2Compat();
			compat.register();
		} else {*/
			MinecraftForge.EVENT_BUS.register(this);
		//}
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, cfg.COMMON_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverConfig);
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		World world = event.player.world;
		if (!world.isRemote) {
			long time = world.getGameTime();
			if (time % (cfg.time_between_animals * 20) == 0 && world.dimension.getType() == DimensionType.OVERWORLD && world.isThundering()) {
				BlockPos spawnPos = new BlockPos(event.player.getPosition().getX() + world.rand.nextInt(cfg.animal_spawn_radius * 2) - cfg.animal_spawn_radius, cfg.animal_spawn_height, event.player.getPosition().getZ() + world.rand.nextInt(cfg.animal_spawn_radius * 2) - cfg.animal_spawn_radius);
				if (world.rand.nextBoolean()) {
					OcelotEntity newCat = new OcelotEntity(EntityType.OCELOT, world);
					newCat.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
					world.addEntity(newCat);
					newCat.setPositionAndRotation(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), world.rand.nextFloat(), world.rand.nextFloat());
				} else {
					WolfEntity newWolf = new WolfEntity(EntityType.WOLF, world);
					newWolf.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
					world.addEntity(newWolf);
					newWolf.setPositionAndRotation(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), world.rand.nextFloat(), world.rand.nextFloat());
					if (cfg.angry_wolves && world.rand.nextInt(10) != 5)
						newWolf.setAngry(true);
				}
			}
		}
	}

	public void serverConfig(ModConfig.ModConfigEvent event) {
		if (event.getConfig().getType() == ModConfig.Type.COMMON)
			cfg.load();
	}

	public static class cfg {
		public static final CommonConfig COMMON;
		public static final ForgeConfigSpec COMMON_SPEC;
		static {
			final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
			COMMON_SPEC = specPair.getRight();
			COMMON = specPair.getLeft();
		}

		public static int time_between_animals;
		public static boolean angry_wolves;
		public static int animal_spawn_radius;
		public static int animal_spawn_height;

		public static void load() {
			time_between_animals = COMMON.time_between_animals.get();
			angry_wolves = COMMON.angry_wolves.get();
			animal_spawn_radius = COMMON.animal_spawn_radius.get();
			animal_spawn_height = COMMON.animal_spawn_height.get();
		}

		public static class CommonConfig {
			public ForgeConfigSpec.IntValue time_between_animals;
			public ForgeConfigSpec.BooleanValue angry_wolves;
			public ForgeConfigSpec.IntValue animal_spawn_radius;
			public ForgeConfigSpec.IntValue animal_spawn_height;

			CommonConfig(ForgeConfigSpec.Builder builder) {
				builder.push("general");
				time_between_animals = builder
						.comment("Time between animals (In seconds)")
						.translation("Time Between Animals")
						.defineInRange("time_between_animals", 5, 1, Integer.MAX_VALUE);
				angry_wolves = builder
						.comment("Can wolves be angry about falling from the sky?")
						.translation("Angry Wolves")
						.define("angry_wolves", true);
				animal_spawn_radius = builder
						.comment("Animal Spawn Radius around the player")
						.translation("Animal Spawn Radius")
						.defineInRange("animal_spawn_radius", 64, 1, Integer.MAX_VALUE);
				animal_spawn_height = builder
						.comment("Animal Spawn Height")
						.translation("Animal Spawn Height")
						.defineInRange("animal_spawn_height", 255, 2, Integer.MAX_VALUE);
				builder.pop();
			}
		}
	}
}
