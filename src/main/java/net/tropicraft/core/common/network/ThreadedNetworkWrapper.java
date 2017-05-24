package net.tropicraft.core.common.network;

import com.google.common.base.Throwables;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Taken from <a href="https://github.com/SleepyTrousers/EnderCore">EnderCore</a>, with permission.
 * 
 * Licensed under CC0.
 */
public class ThreadedNetworkWrapper {

  private final SimpleNetworkWrapper parent;

  public ThreadedNetworkWrapper(String channelName) {
    parent = new SimpleNetworkWrapper(channelName);
  }

  private final class Wrapper<REQ extends IMessage, REPLY extends IMessage> implements IMessageHandler<REQ, REPLY> {

    private final IMessageHandler<REQ, REPLY> wrapped;

    public Wrapper(IMessageHandler<REQ, REPLY> wrapped) {
      this.wrapped = wrapped;
    }

    @Override
    public REPLY onMessage(final REQ message, final MessageContext ctx) {
      final IThreadListener target = ctx.side == Side.CLIENT ? Minecraft.getMinecraft() : FMLCommonHandler.instance().getMinecraftServerInstance();
      if (target != null) {
        target.addScheduledTask(new Runner(message, ctx));
      }
      return null;
    }

    @Override
    public String toString() {
      return wrapped.toString();
    }

    @Override
    public int hashCode() {
      return wrapped.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Wrapper) {
        return this.wrapped.equals(((Wrapper<?, ?>) obj).wrapped);
      } else {
        return this.wrapped.equals(obj);
      }
    }

    private final class Runner implements Runnable {
      private final REQ message;
      private final MessageContext ctx;

      public Runner(final REQ message, final MessageContext ctx) {
        this.message = message;
        this.ctx = ctx;
      }

      @Override
      public void run() {
        final REPLY reply = wrapped.onMessage(message, ctx);
        if (reply != null) {
          if (ctx.side == Side.CLIENT) {
            sendToServer(reply);
          } else {
            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (player != null) {
              sendTo(reply, player);
            }
          }
        }
      }

    }

  }

  public <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler,
      Class<REQ> requestMessageType, int discriminator, Side side) {
    registerMessage(instantiate(messageHandler), requestMessageType, discriminator, side);
  }

  static <REQ extends IMessage, REPLY extends IMessage> IMessageHandler<? super REQ, ? extends REPLY> instantiate(
      Class<? extends IMessageHandler<? super REQ, ? extends REPLY>> handler) {
    try {
      return handler.newInstance();
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public <REQ extends IMessage, REPLY extends IMessage> void registerMessage(IMessageHandler<? super REQ, ? extends REPLY> messageHandler,
      Class<REQ> requestMessageType, int discriminator, Side side) {
    parent.registerMessage((Wrapper<REQ, REPLY>) new Wrapper(messageHandler), requestMessageType, discriminator, side);
  }

  public Packet<?> getPacketFrom(IMessage message) {
    return parent.getPacketFrom(message);
  }

  public void sendToAll(IMessage message) {
    parent.sendToAll(message);
  }

  public void sendTo(IMessage message, EntityPlayerMP player) {
    parent.sendTo(message, player);
  }

  public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
    parent.sendToAllAround(message, point);
  }

  public void sendToDimension(IMessage message, int dimensionId) {
    parent.sendToDimension(message, dimensionId);
  }

  public void sendToServer(IMessage message) {
    parent.sendToServer(message);
  }
}
