package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.common.api.GeolosysSaveData;
import com.oitsjustjose.geolosys.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.List;
import java.util.Random;

public class WorldGenMinableSafe extends WorldGenerator
{
    private final IBlockState oreBlock;
    /**
     * The number of blocks to generate.
     */
    private final int numberOfBlocks;
    private final List<IBlockState> matchers;
    private String dataName;

    public WorldGenMinableSafe(IBlockState state, int blockCount, List<IBlockState> matchers, String dataName)
    {
        this.oreBlock = state;
        this.numberOfBlocks = blockCount;
        this.matchers = matchers;
        this.dataName = dataName;
    }

    private boolean isInChunk(ChunkPos chunkPos, BlockPos pos)
    {
        int blockX = pos.getX();
        int blockZ = pos.getZ();
        return blockX >= chunkPos.getXStart() && blockX <= chunkPos.getXEnd() && blockZ >= chunkPos.getZStart() && blockZ <= chunkPos.getZEnd();
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        float f = rand.nextFloat() * (float) Math.PI;
        double d0 = (double) ((float) (position.getX() + 8) + MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
        double d1 = (double) ((float) (position.getX() + 8) - MathHelper.sin(f) * (float) this.numberOfBlocks / 8.0F);
        double d2 = (double) ((float) (position.getZ() + 8) + MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
        double d3 = (double) ((float) (position.getZ() + 8) - MathHelper.cos(f) * (float) this.numberOfBlocks / 8.0F);
        double d4 = (double) (position.getY() + rand.nextInt(3) - 2);
        double d5 = (double) (position.getY() + rand.nextInt(3) - 2);

        ToDoBlocks toDoBlocks = ToDoBlocks.getForWorld(worldIn, dataName);
        ChunkPos thisChunk = new ChunkPos(position);

        for (int i = 0; i < this.numberOfBlocks; ++i)
        {
            float f1 = (float) i / (float) this.numberOfBlocks;
            double d6 = d0 + (d1 - d0) * (double) f1;
            double d7 = d4 + (d5 - d4) * (double) f1;
            double d8 = d2 + (d3 - d2) * (double) f1;
            double d9 = rand.nextDouble() * (double) this.numberOfBlocks / 16.0D;
            double d10 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int k = MathHelper.floor(d7 - d11 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

            for (int l1 = j; l1 <= i1; ++l1)
            {
                double d12 = ((double) l1 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D)
                {
                    for (int i2 = k; i2 <= j1; ++i2)
                    {
                        double d13 = ((double) i2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D)
                        {
                            for (int j2 = l; j2 <= k1; ++j2)
                            {
                                double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);

                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D)
                                {
                                    BlockPos blockpos = new BlockPos(l1, i2, j2);

                                    if (isInChunk(thisChunk, blockpos) || worldIn.isChunkGeneratedAt(l1 >> 4, j2 >> 4))
                                    {
                                        IBlockState state = worldIn.getBlockState(blockpos);
                                        if (state != null)
                                        {
                                            for (IBlockState iBlockState : matchers)
                                            {
                                                if (Utils.doStatesMatch(iBlockState, state))
                                                {
                                                    if( GeolosysSaveData.get(worldIn).mineralMap.get(new ChunkPos(blockpos)) == null)
                                                    {
                                                        toDoBlocks.storePending(blockpos, this.oreBlock);
                                                    }
                                                    else
                                                    {
                                                        GeolosysSaveData.get(worldIn).mineralMap.get(new ChunkPos(blockpos)).add(blockpos);
                                                        worldIn.setBlockState(blockpos, this.oreBlock, 2 | 16);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        toDoBlocks.storePending(blockpos, this.oreBlock);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}