package io.servertap.api.v1;

import java.util.stream.Stream;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import io.servertap.api.v1.models.Player;
import io.servertap.api.v1.models.Ranking;
import io.servertap.api.v1.models.RankingRecord;
import work.xeltica.craft.core.stores.RankingStore;

public class XCoreApi {

    @OpenApi(
            path = "/v1/ranking",
            summary = "Gets all ranking.",
            tags = {"Ranking"},
            headers = {
            @OpenApiParam(name = "key")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = Player.class, isArray = true))
            }
    )
    public static void rankingsGet(Context ctx) {
        final var api = RankingStore.getInstance();

        // Ranking APIが存在しない = コアシステムが初期化されていない
        if (RankingStore.getInstance() == null) {
            throw new InternalServerErrorResponse("X-Core API is disabled.");
        }

        final var list = api.getAll().stream().map(r -> r.getName()).toList();

        ctx.json(list);
    }

    @OpenApi(
            path = "/v1/ranking/:name",
            method = HttpMethod.GET,
            summary = "Get ranking info",
            tags = {"Ranking"},
            headers = {
            @OpenApiParam(name = "key")
            },
            pathParams = {
                    @OpenApiParam(name = "name", description = "ranking ID")
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = Player.class))
            }
    )
    public static void rankingGet(Context ctx) {
        // Ranking APIが存在しない = コアシステムが初期化されていない
        if (RankingStore.getInstance() == null) {
            throw new InternalServerErrorResponse("X-Core API is disabled.");
        }

        final var api = RankingStore.getInstance();

        // Ranking APIが存在しない = コアシステムが初期化されていない
        if (api == null) {
            ctx.status(500);
            return;
        }
        
        final var name = ctx.pathParam("name");

        if (name.isEmpty()) {
            throw new BadRequestResponse("Ranking Name is required");
        }

        if (!api.has(name)) {
            throw new BadRequestResponse("No such ranking");
        }

        final var data = api.get(name);
        final var records = Stream.of(data.queryRanking())
            .map(r -> new RankingRecord(r.id(), r.score()))
            .toList();

        ctx.json(new Ranking(data.getName(), data.getDisplayName(), data.isPlayerMode(), data.getMode(), records));
    }
}
