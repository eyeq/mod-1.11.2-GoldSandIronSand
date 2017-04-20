package eyeq.goldsandironsand.event;

import eyeq.goldsandironsand.GoldSandIronSand;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Random;

public class GoldSandIronSandEventHandler {
    @SubscribeEvent
    public void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        World world = event.getWorld();
        if(world.isRemote) {
            return;
        }
        Random rand = world.rand;
        List<ItemStack> drops = event.getDrops();
        for(int i = 0; i < drops.size(); i++) {
            ItemStack itemStack = drops.get(i);
            Item item = itemStack.getItem();
            if(!(item instanceof ItemBlock)) {
                continue;
            }
            Block block = ((ItemBlock) item).getBlock();
            if(block == Blocks.SAND) {
                dropGoldDust(drops, rand);
                dropIronSand(drops, rand);
            } else if(block == Blocks.STONE) {
                switch(itemStack.getMetadata()) {
                case 1:
                case 3:
                case 5:
                    dropIronSand(drops, rand);
                    break;
                }
            }
        }
    }

    private void dropGoldDust(List<ItemStack> drops, Random rand) {
        if(rand.nextFloat() < GoldSandIronSand.probGoldDust) {
            drops.add(new ItemStack(GoldSandIronSand.goldDust));
        }
    }

    private void dropIronSand(List<ItemStack> drops, Random rand) {
        if(rand.nextFloat() < GoldSandIronSand.probIronSand) {
            drops.add(new ItemStack(GoldSandIronSand.ironSand));
        }
    }
}
