package gg.nova.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;

public final class NovaCosmetics {
    private NovaCosmetics() {}

    /**
     * Client-side cosmetic particles are rendered around the local player, so they
     * are visible while viewing the player in third person. They are not sent to servers.
     */
    public static void tick(Minecraft client) {
        Player player = client.player;
        if (player == null || client.level == null || player.isInvisible()) return;

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        float yaw = (float) Math.toRadians(player.getYRot());
        double backX = Math.sin(yaw);
        double backZ = -Math.cos(yaw);
        double sideX = Math.cos(yaw);
        double sideZ = Math.sin(yaw);
        double phase = player.tickCount * 0.12;

        if (ModuleManager.isEnabled("Crystal Wings") || ModuleManager.isEnabled("Energy Wings")) {
            boolean energy = ModuleManager.isEnabled("Energy Wings");
            for (int i = 0; i < 5; i++) {
                double spread = 0.35 + i * 0.16;
                double lift = 1.55 - i * 0.13 + Math.sin(phase + i * 0.45) * 0.05;
                for (int sign : new int[]{-1, 1}) {
                    double px = x + backX * 0.34 + sideX * spread * sign;
                    double pz = z + backZ * 0.34 + sideZ * spread * sign;
                    double py = y + lift;
                    if (energy) {
                        client.level.addParticle(ParticleTypes.END_ROD, px, py, pz, 0.0, 0.015, 0.0);
                    } else {
                        client.level.addParticle(ParticleTypes.CRIT, px, py, pz, 0.0, 0.025, 0.0);
                    }
                }
            }
        }

        if (ModuleManager.isEnabled("Crystal Aura") || ModuleManager.isEnabled("Portal Aura")) {
            boolean portal = ModuleManager.isEnabled("Portal Aura");
            for (int i = 0; i < 3; i++) {
                double angle = phase + i * (Math.PI * 2.0 / 3.0);
                double radius = 0.72;
                double px = x + Math.cos(angle) * radius;
                double pz = z + Math.sin(angle) * radius;
                double py = y + 0.85 + Math.sin(angle * 1.7) * 0.35;
                client.level.addParticle(portal ? ParticleTypes.PORTAL : ParticleTypes.END_ROD,
                    px, py, pz, 0.0, 0.025, 0.0);
            }
        }

        boolean moving = player.getDeltaMovement().horizontalDistanceSqr() > 0.0025;
        if (moving && ModuleManager.isEnabled("Starlight Trail")) {
            client.level.addParticle(ParticleTypes.END_ROD,
                x - backX * 0.28, y + 0.12, z - backZ * 0.28, 0.0, 0.025, 0.0);
        }
        if (moving && ModuleManager.isEnabled("Ender Trail")) {
            client.level.addParticle(ParticleTypes.PORTAL,
                x - backX * 0.3, y + 0.2, z - backZ * 0.3, 0.0, 0.02, 0.0);
        }
    }
}
