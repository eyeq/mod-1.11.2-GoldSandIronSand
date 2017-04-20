package eyeq.goldsandironsand;

import eyeq.goldsandironsand.event.GoldSandIronSandEventHandler;
import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.UModelLoader;
import eyeq.util.client.model.gson.ItemmodelJsonFactory;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.item.crafting.UCraftingManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

import static eyeq.goldsandironsand.GoldSandIronSand.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class GoldSandIronSand {
    public static final String MOD_ID = "eyeq_goldsandironsand";

    @Mod.Instance(MOD_ID)
    public static GoldSandIronSand instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static float probGoldDust;
    public static float probIronSand;

    public static Item goldDust;
    public static Item ironSand;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GoldSandIronSandEventHandler());
        load(new Configuration(event.getSuggestedConfigurationFile()));
        addRecipes();
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        createFiles();
    }
	
    public static void load(Configuration config) {
        config.load();

        String category = "Float";
        probGoldDust = (float) config.get(category, "probGoldDust", 0.05).getDouble();
        probIronSand = (float) config.get(category, "probIronSand", 0.05).getDouble();

        if(config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        goldDust = new Item().setUnlocalizedName("goldDust").setCreativeTab(CreativeTabs.MATERIALS);
        ironSand = new Item().setUnlocalizedName("ironSand").setCreativeTab(CreativeTabs.MATERIALS);

        GameRegistry.register(goldDust, resource.createResourceLocation("gold_dust"));
        GameRegistry.register(ironSand, resource.createResourceLocation("iron_sand"));
    }

    public static void addRecipes() {
        GameRegistry.addRecipe(UCraftingManager.getRecipeCompress(Items.GOLD_NUGGET, goldDust));
        GameRegistry.addRecipe(UCraftingManager.getRecipeDecompress(goldDust, Items.GOLD_NUGGET));

        GameRegistry.addRecipe(UCraftingManager.getRecipeCompress(Items.field_191525_da, ironSand));
        GameRegistry.addRecipe(UCraftingManager.getRecipeDecompress(ironSand, Items.field_191525_da));
    }

	@SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        UModelLoader.setCustomModelResourceLocation(goldDust);
        UModelLoader.setCustomModelResourceLocation(ironSand);
    }
	
    public static void createFiles() {
    	File project = new File("../1.11.2-GoldSandIronSand");
    	
        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, goldDust, "Gold Dust");
        language.register(LanguageResourceManager.JA_JP, goldDust, "砂金");
        language.register(LanguageResourceManager.EN_US, ironSand, "Iron Sand");
        language.register(LanguageResourceManager.JA_JP, ironSand, "砂鉄");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        UModelCreator.createItemJson(project, goldDust, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
        UModelCreator.createItemJson(project, ironSand, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
    }
}
