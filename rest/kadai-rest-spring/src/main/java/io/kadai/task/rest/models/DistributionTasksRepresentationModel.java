package io.kadai.task.rest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Map;

public class DistributionTasksRepresentationModel {

  /** The ID of the source workbasket. */
  @JsonProperty("sourceWorkbasketId")
  private final String sourceWorkbasketId;

  /** The list of task IDs to be distributed. */
  @JsonProperty("taskIds")
  private final List<String> taskIds;

  /** The list of destination workbasket IDs. */
  @JsonProperty("destinationWorkbasketIds")
  private final List<String> destinationWorkbasketIds;

  /** The name of the distribution strategy. */
  @JsonProperty("distributionStrategyName")
  private final String distributionStrategyName;

  /** Additional information for the distribution process. */
  @JsonProperty("additionalInformation")
  private final Map<String, Object> additionalInformation;

  @ConstructorProperties({
    "sourceWorkbasketId",
    "taskIds",
    "destinationWorkbasketIds",
    "distributionStrategyName",
    "additionalInformation"
  })
  public DistributionTasksRepresentationModel(
      String sourceWorkbasketId,
      List<String> taskIds,
      List<String> destinationWorkbasketIds,
      String distributionStrategyName,
      Map<String, Object> additionalInformation) {
    this.sourceWorkbasketId = sourceWorkbasketId;
    this.taskIds = taskIds;
    this.destinationWorkbasketIds = destinationWorkbasketIds;
    this.distributionStrategyName = distributionStrategyName;
    this.additionalInformation = additionalInformation;
  }

  public String getSourceWorkbasketId() {
    return sourceWorkbasketId;
  }

  public List<String> getTaskIds() {
    return taskIds;
  }

  public List<String> getDestinationWorkbasketIds() {
    return destinationWorkbasketIds;
  }

  public String getDistributionStrategyName() {
    return distributionStrategyName;
  }

  public Map<String, Object> getAdditionalInformation() {
    return additionalInformation;
  }
}
