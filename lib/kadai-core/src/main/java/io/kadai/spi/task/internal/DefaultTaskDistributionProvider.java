package io.kadai.spi.task.internal;

import io.kadai.common.api.KadaiEngine;
import io.kadai.spi.task.api.TaskDistributionProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultTaskDistributionProvider implements TaskDistributionProvider {

  @Override
  public void initialize(KadaiEngine kadaiEngine) {
    // NOOP
  }

  @Override
  public Map<String, List<String>> distributeTasks(
      List<String> taskIds, List<String> workbasketIds, Map<String, Object> additionalInformation) {

    if (taskIds == null || taskIds.isEmpty()) {
      throw new IllegalArgumentException("Task Ids list cannot be null or empty.");
    }
    if (workbasketIds == null || workbasketIds.isEmpty()) {
      throw new IllegalArgumentException("Ids of destinationWorkbaskets cannot be null or empty.");
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
