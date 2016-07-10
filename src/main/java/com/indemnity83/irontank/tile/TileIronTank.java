package com.indemnity83.irontank.tile;

import com.indemnity83.irontank.item.ItemTankChanger;
import com.indemnity83.irontank.reference.TankType;
import com.indemnity83.irontank.utility.LogHelper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import buildcraft.core.lib.fluids.Tank;
import buildcraft.factory.TileTank;

public class TileIronTank extends TileTank {

    public TankType type;
    private int _lockID = -1;
    private boolean _locked;

    public TileIronTank() {
        this(TankType.IRON);
    }

    public TileIronTank(TankType type) {
        this.type = type;

        int capacity = FluidContainerRegistry.BUCKET_VOLUME * type.capacity;
        this.tank.setCapacity(capacity);
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.type = TankType.values()[data.getInteger("type")];
        this._lockID = data.getInteger("lockID");
    }

    public void writeToNBT(NBTTagCompound data) {
        data.setInteger("type", type.ordinal());
        data.setInteger("lockID", _lockID);
        super.writeToNBT(data);
    }

    public void lock() {
        _locked = true;
    }

    public void unlock() {
        _locked = false;
        _lockID = -1;
    }

    public boolean isLocked() {
        return _locked;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource != null) {
            if (_locked) {
                int fluidID = resource.getFluidID();
                if (_lockID == -1) {
                    _lockID = fluidID;
                }
                if (_lockID != fluidID) {
                    return 0;
                }
            }
        }
        return super.fill(from, resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxEmpty, boolean doDrain) {
        if (_locked) {
            if (_lockID == -1) {
                FluidStack fluid = this.tank.getFluid();
                if (fluid != null) {
                    _lockID = fluid.getFluidID();
                }
            }
        }
        return super.drain(from, maxEmpty, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return super.drain(from, resource, doDrain);
    }


}
