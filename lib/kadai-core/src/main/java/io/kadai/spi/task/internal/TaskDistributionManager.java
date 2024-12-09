package io.kadai.spi.task.internal;

import io.kadai.common.api.KadaiEngine;
import io.kadai.common.api.exceptions.InvalidArgumentException;
import io.kadai.common.internal.util.SpiLoader;
import io.kadai.spi.task.api.TaskDistributionProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TaskDistributionManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskDistributionManager.class);
  private final List<TaskDistributionProvider> taskDistributionProviderList;

  public TaskDistributionManager(KadaiEngine kadaiEngine) {
    taskDistributionProviderList = SpiLoader.load(TaskDistributionProvider.class);
    for (TaskDistributionProvider taskDistributionProvider : taskDistributionProviderList) {
      taskDistributionProvider.initialize(kadaiEngine);
      LOGGER.info(
          "Registered TaskDistribution provider: {}",
          taskDistributionProvider.getClass().getName());
    }

    if (taskDistributionProviderList.isEmpty()) {
      LOGGER.info("No TaskDistribution Provider found. Running without Task routing");
    }
  }

  public TaskDistributionProvider getProviderByName(String name) {
    return taskDistributionProviderList.stream()
        .filter(provider -> provider.getClass().getSimpleName().equals(name))
        .findFirst()
        .orElseThrow(
            () ->
                new InvalidArgumentException(
                    "The distribution strategy '" + name + "' does not exist."));
  }

  public Map<String, List<String>> distributeTasks(
      List<String> taskIds, List<String> workbasketIds, Map<String, Object> additionalInformation) {

    if (taskIds == null || taskIds.isEmpty()) {
      throw new IllegalArgumentException("Task IDs list cannot be null or empty.");
    }
    if (workbasketIds == null || workbasketIds.isEmpty()) {
      throw new IllegalArgumentException("Workbasket IDs list cannot be null or empty.");
    }

    if (additionalInformation != null && !additionalInformation.isEmpty()) {
      additionalInformation.forEach((key, value) ->
          LOGGER.debug("Received additional information: key={}, value={}", key, value)
      );
    }

    Map<String, List<String>> distributedTaskIds = new HashMap<>();
    for (String workbasketId : workbasketIds) {
      distributedTaskIds.put(workbasketId, new ArrayList<>());
    }

    int workbasketCount = workbasketIds.size();
    for (int i = 0; i < taskIds.size(); i++) {
      String taskId = taskIds.get(i);
      String targetWorkbasketId = workbasketIds.get(i % workbasketCount);
      distributedTaskIds.get(targetWorkbasketId).add(taskId);
    }

    return distributedTaskIds;
  }
}
