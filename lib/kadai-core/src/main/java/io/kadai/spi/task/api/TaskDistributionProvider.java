package io.kadai.spi.task.api;

import io.kadai.common.api.KadaiEngine;
import java.util.List;
import java.util.Map;

public interface TaskDistributionProvider {

  void initialize(KadaiEngine kadaiEngine);

  Map<String, List<String>> distributeTasks(
      List<String> taskIds,
      List<String> destinationWorkbasketIds,
      Map<String, Object> additionalInformation);
}
