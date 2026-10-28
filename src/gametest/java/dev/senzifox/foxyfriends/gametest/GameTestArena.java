package dev.senzifox.foxyfriends.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

final class GameTestArena {
	private static final int HALF_WIDTH = 4;
	private static final int HEIGHT = 3;

	private GameTestArena() {
	}

	static void buildCorridor(GameTestHelper helper, BlockPos start, int length) {
		for (int x = start.getX() - HALF_WIDTH; x <= start.getX() + HALF_WIDTH; x++) {
			for (int z = start.getZ() - 1; z <= start.getZ() + length; z++) {
				helper.setBlock(new BlockPos(x, start.getY() - 1, z), Blocks.STONE);
			}
		}

		clearBarriers(helper, start, length);
		helper.onEachTick(() -> clearBarriers(helper, start, length));
	}

	private static void clearBarriers(GameTestHelper helper, BlockPos start, int length) {
		for (int x = start.getX() - HALF_WIDTH; x <= start.getX() + HALF_WIDTH; x++) {
			for (int z = start.getZ() - 1; z <= start.getZ() + length; z++) {
				for (int y = start.getY(); y <= start.getY() + HEIGHT; y++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (helper.getBlockState(pos).is(Blocks.BARRIER)) {
						helper.setBlock(pos, Blocks.AIR);
					}
				}
			}
		}
	}
}
