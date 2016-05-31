package com.sematext.solr.redis.command;

import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCommands;
import java.util.Map;

public class ZRange implements Command<JedisCommands> {
  private static final Logger log = LoggerFactory.getLogger(ZRange.class);

  @Override
  public Map<String, Float> execute(final JedisCommands client, final SolrParams params) {
    final String key = ParamUtil.assertGetStringByName(params, "key");
    final long start = ParamUtil.tryGetIntByName(params, "range_start", 0);
    final long end = ParamUtil.tryGetIntByName(params, "range_end", -1);
	final boolean withScores = ParamUtil.tryGetBooleanByName(params, "with_scores", true);

    log.debug("Fetching ZRANGE from Redis for key: {} ({}, {})", key, start, end);

    if (withScores) {
      return ResultUtil.tupleIteratorToMap(client.zrangeWithScores(key, start, end));
    } else {
	  return ResultUtil.stringIteratorToMap(client.zrange(key, start, end));
    }
  }
}
