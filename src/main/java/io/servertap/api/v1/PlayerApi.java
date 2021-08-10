package io.servertap.api.v1;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import io.servertap.Constants;
import io.servertap.PluginEntrypoint;
import io.servertap.api.v1.models.Player;

public class PlayerApi {

    @OpenApi(
            path = "/v1/players",
            summary = "Gets all currently online players",
            tags = {"Player"},
            headers = {
            @OpenApiParam(name = "key")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = Player.class, isArray = true))
            }
    )
    public static void playersGet(Context ctx) {
        ArrayList<Player> players = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach((player -> {
            Player p = new Player();
            p.setUuid(player.getUniqueId().toString());
            p.setDisplayName(player.getDisplayName());

            players.add(p);
        }));

        ctx.json(players);
    }

    @OpenApi(
            path = "/v1/players/:uuid",
            method = HttpMethod.GET,
            summary = "Gets a specific online player by their UUID",
            tags = {"Player"},
            headers = {
            @OpenApiParam(name = "key")
            },
            pathParams = {
                    @OpenApiParam(name = "uuid", description = "UUID of the player")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = Player.class))
            }
    )
    public static void playerGet(Context ctx) {
        Player p = new Player();

        if (ctx.pathParam("uuid").isEmpty()) {
            throw new BadRequestResponse(Constants.PLAYER_UUID_MISSING);
        }

        org.bukkit.entity.Player player = Bukkit.getPlayer(UUID.fromString(ctx.pathParam("uuid")));

        if (player == null) {
            throw new NotFoundResponse(Constants.PLAYER_NOT_FOUND);
        }

        p.setUuid(player.getUniqueId().toString());
        p.setDisplayName(player.getDisplayName());

        ctx.json(p);
    }

    @OpenApi(
            path = "/v1/players/all",
            summary = "Gets all players that have ever joined the server ",
            tags = {"Player"},
            headers = {
            @OpenApiParam(name = "key")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = io.servertap.api.v1.models.OfflinePlayer.class, isArray = true))
            }
    )
    public static void offlinePlayersGet(Context ctx) {

        ArrayList<io.servertap.api.v1.models.OfflinePlayer> players = new ArrayList<>();

        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();

        for (int i = 0; i < offlinePlayers.length; i++) {
            io.servertap.api.v1.models.OfflinePlayer p = new io.servertap.api.v1.models.OfflinePlayer();
            OfflinePlayer player = offlinePlayers[i];

            p.setDisplayName(player.getName());
            p.setUuid(player.getUniqueId().toString());
            p.setWhitelisted(player.isWhitelisted());
            p.setBanned(player.isBanned());
            p.setOp(player.isOp());

            if (PluginEntrypoint.getEconomy() != null) {
                p.setBalance(PluginEntrypoint.getEconomy().getBalance(offlinePlayers[i]));
            }

            players.add(p);

        }

        ctx.json(players);
    }
}
