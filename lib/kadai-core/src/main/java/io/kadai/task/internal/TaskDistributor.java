package io.kadai.task.internal;

import io.kadai.common.api.BulkOperationResults;
import io.kadai.common.api.exceptions.InvalidArgumentException;
import io.kadai.common.api.exceptions.KadaiException;
import io.kadai.common.internal.InternalKadaiEngine;
import io.kadai.spi.task.api.TaskDistributionProvider;
import io.kadai.spi.task.internal.TaskDistributionManager;
import io.kadai.task.api.exceptions.InvalidTaskStateException;
import io.kadai.task.api.exceptions.TaskNotFoundException;
import io.kadai.task.api.models.Task;
import io.kadai.task.api.models.TaskSummary;
import io.kadai.workbasket.api.WorkbasketPermission;
import io.kadai.workbasket.api.WorkbasketService;
import io.kadai.workbasket.api.exceptions.NotAuthorizedOnWorkbasketException;
import io.kadai.workbasket.api.exceptions.WorkbasketNotFoundException;
import io.kadai.workbasket.api.models.WorkbasketSummary;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskDistributor {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskDistributor.class);

  private final InternalKadaiEngine kadaiEngine;
  private final WorkbasketService workbasketService;
  private final TaskServiceImpl taskService;
  private final TaskDistributionManager taskDistributionManager;

  public TaskDistributor(InternalKadaiEngine kadaiEngine, TaskServiceImpl taskService) {
    this.kadaiEngine = kadaiEngine;
    this.taskService = taskService;
    this.workbasketService = kadaiEngine.getEngine().getWorkbasketService();
    this.taskDistributionManager = kadaiEngine.getTaskDistributionManager();
  }

  public BulkOperationResults<String, KadaiException> distribute(
      String sourceWorkbasketId,
      List<String> destinationWorkbasketIds,
      String distributionStrategyName,
      Map<String, Object> additionalInformation)
      throws InvalidArgumentException,
          NotAuthorizedOnWorkbasketException,
          WorkbasketNotFoundException,
          TaskNotFoundException,
          InvalidTaskStateException {

    checkAuthorization(sourceWorkbasketId);

    List<String> taskIds = getTaskIdsFromWorkbasket(sourceWorkbasketId);

    checkIfAllTasksInSameWorkbasket(taskIds);

    return distribute(
        taskIds, distributionStrategyName, destinationWorkbasketIds, additionalInformation);
  }

  BulkOperationResults<String, KadaiException> distribute(
      List<String> taskIds,
      List<String> destinationWorkbasketIds,
      String distributionStrategyName,
      Map<String, Object> additionalInformation)
      throws InvalidArgumentException,
          WorkbasketNotFoundException,
          NotAuthorizedOnWorkbasketException,
          TaskNotFoundException,
          InvalidTaskStateException {

    String sourceWorkbasketId = getSourceWorkbasketIdFromTaskList(taskIds);

    checkAuthorization(sourceWorkbasketId);
    checkIfAllTasksInSameWorkbasket(taskIds);

    return distribute(
        taskIds, distributionStrategyName, destinationWorkbasketIds, additionalInformation);
  }

  public BulkOperationResults<String, KadaiException> distribute(
      String sourceWorkbasketId, Map<String, Object> additionalInformation)
      throws InvalidArgumentException,
          WorkbasketNotFoundException,
          NotAuthorizedOnWorkbasketException,
          TaskNotFoundException,
          InvalidTaskStateException {

    checkAuthorization(sourceWorkbasketId);

    List<String> taskIds = getTaskIdsFromWorkbasket(sourceWorkbasketId);

    checkIfAllTasksInSameWorkbasket(taskIds);

    List<String> destinationWorkbasketIds = getDestinationWorkbasketIds(sourceWorkbasketId);

    return distribute(taskIds, null, destinationWorkbasketIds, additionalInformation);
  }

  BulkOperationResults<String, KadaiException> distribute(
      List<String> taskIds, Map<String, Object> additionalInformation)
      throws InvalidArgumentException,
          WorkbasketNotFoundException,
          NotAuthorizedOnWorkbasketException,
          TaskNotFoundException,
          InvalidTaskStateException {

    String sourceWorkbasketId = getSourceWorkbasketIdFromTaskList(taskIds);

    checkAuthorization(sourceWorkbasketId);
    checkIfAllTasksInSameWorkbasket(taskIds);

    List<String> destinationWorkbasketIds = getDestinationWorkbasketIds(sourceWorkbasketId);

    return distribute(taskIds, null, destinationWorkbasketIds, additionalInformation);
  }

  public BulkOperationResults<String, KadaiException> distribute(
      String sourceWorkbasketId,
      String distributionStrategyName,
      Map<String, Object> additionalInformation)
      throws InvalidArgumentException,
          WorkbasketNotFoundException,
          NotAuthorizedOnWorkbasketException,
          InvalidTaskStateException,
          TaskNotFoundException {

    checkAuthorization(sourceWorkbasketId);

    List<String> taskIds = getTaskIdsFromWorkbasket(sourceWorkbasketId);

    checkIfAllTasksInSameWorkbasket(taskIds);

    List<String> destinationWorkbasketIds = getDestinationWorkbasketIds(sourceWorkbasketId);
    return distribute(
        taskIds, distributionStrategyName, destinationWorkbasketIds, additionalInformation);
  }

  BulkOperationResults<String, KadaiException> distribute(
      List<String> taskIds,
      String distributionStrategyName,
      Map<String, Object> additionalInformation)
      throws InvalidArgumentException,
          WorkbasketNotFoundException,
          NotAuthorizedOnWorkbasketException,
          TaskNotFoundException,
          InvalidTaskStateException {

    String sourceWorkbasketId = getSourceWorkbasketIdFromTaskList(taskIds);

    checkAuthorization(sourceWorkbasketId);
    checkIfAllTasksInSameWorkbasket(taskIds);

    List<String> destinationWorkbasketIds = getDestinationWorkbasketIds(sourceWorkbasketId);

    return distribute(
        taskIds, distributionStrategyName, destinationWorkbasketIds, additionalInformation);
  }

  BulkOperationResults<String, KadaiException> distribute(
      String sourceWorkbasketId,
      List<String> destinationWorkbasketIds,
      Map<String, Object> additionalInformation)
      throws InvalidArgumentException,
          NotAuthorizedOnWorkbasketException,
          WorkbasketNotFoundException,
          InvalidTaskStateException,
          TaskNotFoundException {

    checkAuthorization(sourceWorkbasketId);

    List<String> taskIds = getTaskIdsFromWorkbasket(sourceWorkbasketId);

    checkIfAllTasksInSameWorkbasket(taskIds);

    return distribute(taskIds, null, destinationWorkbasketIds, additionalInformation);
  }

  BulkOperationResults<String, KadaiException> distribute(
      List<String> taskIds,
      List<String> destinationWorkbasketIds,
      Map<String, Object> additionalInformation)
      throws InvalidArgumentException,
          NotAuthorizedOnWorkbasketException,
          WorkbasketNotFoundException,
          TaskNotFoundException,
          InvalidTaskStateException {

    String sourceWorkbasketId = getSourceWorkbasketIdFromTaskList(taskIds);

    checkAuthorization(sourceWorkbasketId);
    checkIfAllTasksInSameWorkbasket(taskIds);

    return distribute(taskIds, null, destinationWorkbasketIds, additionalInformation);
  }

  private BulkOperationResults<String, KadaiException> distribute(
      List<String> taskIds,
      String distributionStrategyName,
      List<String> destinationWorkbasketIds,
      Map<String, Object> additionalInformation)
      throws InvalidArgumentException,
          NotAuthorizedOnWorkbasketException,
          WorkbasketNotFoundException,
          InvalidTaskStateException,
          TaskNotFoundException {
    BulkOperationResults<String, KadaiException> operationResults = new BulkOperationResults<>();

    try {
      kadaiEngine.openConnection();

      if (taskIds.isEmpty()) {
        return operationResults;
      }

      Map<String, List<String>> newTaskDistribution;

      if (distributionStrategyName != null) {
        TaskDistributionProvider taskDistributionProvider =
            taskDistributionManager.getProviderByName(distributionStrategyName);

        if (taskDistributionProvider == null) {
          throw new InvalidArgumentException(
              String.format(
                  "The distribution strategy '%s' does not exist.", distributionStrategyName));
        }

        LOGGER.info("Using TaskDistributionProvider: {}", distributionStrategyName);
        newTaskDistribution =
            taskDistributionProvider.distributeTasks(
                taskIds, destinationWorkbasketIds, additionalInformation);
      } else {
        LOGGER.info("No distribution strategy specified. Using default distribution logic.");
        newTaskDistribution =
            taskDistributionManager.distributeTasks(
                taskIds, destinationWorkbasketIds, additionalInformation);
      }

      if (newTaskDistribution == null || newTaskDistribution.isEmpty()) {
        throw new InvalidArgumentException(
            "The distribution strategy resulted in no task assignments. Please verify the input.");
      }

      for (Map.Entry<String, List<String>> entry : newTaskDistribution.entrySet()) {
        String newDestinationWorkbasketId = entry.getKey();
        List<String> taskIdsForDestination = entry.getValue();

        for (String taskId : taskIdsForDestination) {
          taskService.transfer(taskId, newDestinationWorkbasketId);
        }
      }
    } finally {
      kadaiEngine.returnConnection();
    }
    return operationResults;
  }

  private void checkIfAllTasksInSameWorkbasket(List<String> taskIds)
      throws TaskNotFoundException, NotAuthorizedOnWorkbasketException {

    String commonWorkbasketId = null;

    for (String taskId : taskIds) {
      Task task = taskService.getTask(taskId);

      String currentWorkbasketId = task.getWorkbasketSummary().getId();

      if (commonWorkbasketId == null) {
        commonWorkbasketId = currentWorkbasketId;
      } else if (!commonWorkbasketId.equals(currentWorkbasketId)) {
        throw new InvalidArgumentException("Not all tasks are in the same workbasket.");
      }
    }
  }

  String getSourceWorkbasketIdFromTaskList(List<String> taskIds)
      throws TaskNotFoundException, NotAuthorizedOnWorkbasketException, InvalidArgumentException {
    if (taskIds == null || taskIds.isEmpty()) {
      throw new InvalidArgumentException(
          "The source workbasket cannot be identified because " + "the task list is empty.");
    }

    Task task = taskService.getTask(taskIds.get(0));
    if (task == null
        || task.getWorkbasketSummary() == null
        || task.getWorkbasketSummary().getId() == null) {
      throw new InvalidArgumentException(
          "The source workbasket cannot be identified because " + "the task data is invalid.");
    }

    return task.getWorkbasketSummary().getId();
  }

  void checkAuthorization(String sourceWorkbasketId)
      throws NotAuthorizedOnWorkbasketException, WorkbasketNotFoundException {

    workbasketService.checkAuthorization(sourceWorkbasketId, WorkbasketPermission.DISTRIBUTE);
  }

  List<String> getTaskIdsFromWorkbasket(String sourceWorkbasketId) {
    return kadaiEngine
        .getEngine()
        .runAsAdmin(() -> taskService.createTaskQuery().workbasketIdIn(sourceWorkbasketId).list())
        .stream()
        .map(TaskSummary::getId)
        .toList();
  }

  List<String> getDestinationWorkbasketIds(String sourceWorkbasketId)
      throws NotAuthorizedOnWorkbasketException, WorkbasketNotFoundException {
    return workbasketService.getDistributionTargets(sourceWorkbasketId).stream()
        .map(WorkbasketSummary::getId)
        .toList();
  }
}
