package com.airhacks.satellite.statistics.boundary;

import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.monitor.LocalQueueStats;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author airhacks.com
 */
@Stateless
public class StatisticsProvider {

    @Inject
    @Any
    Instance<IMap<String, String>> mapCaches;

    @Inject
    @Any
    Instance<IQueue<String>> queueCaches;

    public JsonArray mapStatistics() {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        mapCaches.forEach(cache -> {
            builder.add(convert(cache));
        });
        return builder.build();
    }

    public JsonArray queueStatistics() {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        queueCaches.forEach(cache -> {
            builder.add(convert(cache));
        });
        return builder.build();
    }

    JsonObject convert(IMap<String, String> map) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        String serviceName = map.getServiceName();
        LocalMapStats stats = map.getLocalMapStats();
        return builder.add("serviceName", serviceName).
                add("backupCount", stats.getBackupCount()).
                add("backupEntryCount", stats.getBackupEntryCount()).
                add("backupEntryMemoryCost", stats.getBackupEntryMemoryCost()).
                add("creationTime", stats.getCreationTime()).
                add("dirtyEntryCount", stats.getDirtyEntryCount()).
                add("eventOperationCount", stats.getEventOperationCount()).
                add("getOperationCount", stats.getGetOperationCount()).
                add("heapCost", stats.getHeapCost()).
                add("hits", stats.getHits()).
                add("lastAccessTime", stats.getLastAccessTime()).
                add("lockedEntryCount", stats.getLockedEntryCount()).
                add("maxGetLatency", stats.getMaxGetLatency()).
                add("maxPutLatency", stats.getMaxPutLatency()).
                add("maxRemoveLatency", stats.getMaxRemoveLatency()).
                add("otherOperationCount", stats.getOtherOperationCount()).
                add("ownedEntryCount", stats.getOwnedEntryCount()).
                add("ownedEntryMemoryCost", stats.getOwnedEntryMemoryCost()).
                add("putOperationCount", stats.getPutOperationCount()).
                add("removeOperationCount", stats.getRemoveOperationCount()).
                add("totalGetLatency", stats.getTotalGetLatency()).
                add("totalPutLatency", stats.getTotalPutLatency()).
                add("totalRemoveLatency", stats.getTotalRemoveLatency()).build();
    }

    JsonObject convert(IQueue<String> map) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        String serviceName = map.getServiceName();
        LocalQueueStats stats = map.getLocalQueueStats();
        return builder.add("serviceName", serviceName).
                add("avgAge", stats.getAvgAge()).
                add("backupItemCount", stats.getBackupItemCount()).
                add("emptyPollOperationCount", stats.getEmptyPollOperationCount()).
                add("eventOperationCount", stats.getEventOperationCount()).
                add("maxAge", stats.getMaxAge()).
                add("minAge", stats.getMinAge()).
                add("offerOperationCount", stats.getOfferOperationCount()).
                add("otherOperationsCount", stats.getOtherOperationsCount()).
                add("ownedItemCount", stats.getOwnedItemCount()).
                add("pollOperationCount", stats.getPollOperationCount()).
                add("rejectedOfferOperationCount", stats.getRejectedOfferOperationCount()).
                build();
    }

}
