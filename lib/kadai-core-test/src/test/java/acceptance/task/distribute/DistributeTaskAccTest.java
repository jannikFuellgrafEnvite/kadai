package acceptance.task.distribute;

import static io.kadai.testapi.DefaultTestEntities.defaultTask;
import static io.kadai.testapi.DefaultTestEntities.defaultTestClassification;
import static io.kadai.testapi.DefaultTestEntities.defaultTestObjectReference;
import static io.kadai.testapi.DefaultTestEntities.defaultTestWorkbasket;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.kadai.classification.api.ClassificationService;
import io.kadai.classification.api.models.ClassificationSummary;
import io.kadai.common.api.BulkOperationResults;
import io.kadai.common.api.exceptions.InvalidArgumentException;
import io.kadai.common.api.exceptions.KadaiException;
import io.kadai.task.api.TaskService;
import io.kadai.task.api.exceptions.InvalidTaskStateException;
import io.kadai.task.api.exceptions.TaskNotFoundException;
import io.kadai.task.api.models.ObjectReference;
import io.kadai.task.api.models.TaskSummary;
import io.kadai.testapi.KadaiInject;
import io.kadai.testapi.KadaiIntegrationTest;
import io.kadai.testapi.security.WithAccessId;
import io.kadai.workbasket.api.WorkbasketService;
import io.kadai.workbasket.api.exceptions.NotAuthorizedOnWorkbasketException;
import io.kadai.workbasket.api.exceptions.WorkbasketNotFoundException;
import io.kadai.workbasket.api.models.WorkbasketSummary;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@KadaiIntegrationTest
class DistributeTaskAccTest {
  @KadaiInject TaskService taskService;
  @KadaiInject WorkbasketService workbasketService;
  @KadaiInject ClassificationService classificationService;

  ClassificationSummary classificationSummary;
  ObjectReference objectReference;

  WorkbasketSummary workbasketSummary1;
  WorkbasketSummary workbasketSummary2;
  WorkbasketSummary workbasketSummary3;
  WorkbasketSummary workbasketSummary4;
  WorkbasketSummary workbasketSummary5;
  TaskSummary taskSummary1;
  TaskSummary taskSummary2;
  TaskSummary taskSummary3;
  TaskSummary taskSummary4;
  TaskSummary taskSummary5;
  TaskSummary taskSummary6;
  TaskSummary taskSummary7;

  @WithAccessId(user = "admin")
  @BeforeEach
  void setup() throws Exception {

    classificationSummary =
        defaultTestClassification().buildAndStoreAsSummary(classificationService);

    objectReference = defaultTestObjectReference().build();

    workbasketSummary1 = defaultTestWorkbasket().buildAndStoreAsSummary(workbasketService);
    workbasketSummary2 = defaultTestWorkbasket().buildAndStoreAsSummary(workbasketService);
    workbasketSummary3 = defaultTestWorkbasket().buildAndStoreAsSummary(workbasketService);
    workbasketSummary4 = defaultTestWorkbasket().buildAndStoreAsSummary(workbasketService);
    workbasketSummary5 = defaultTestWorkbasket().buildAndStoreAsSummary(workbasketService);

    List<String> workbasketTargetsFor1 =
        List.of(workbasketSummary2.getId(), workbasketSummary3.getId(), workbasketSummary4.getId());
    List<String> workbasketTargetsFor5 =
        List.of(workbasketSummary2.getId(), workbasketSummary3.getId(), workbasketSummary4.getId());

    workbasketService.setDistributionTargets(workbasketSummary1.getId(), workbasketTargetsFor1);
    workbasketService.setDistributionTargets(workbasketSummary5.getId(), workbasketTargetsFor5);

    taskSummary1 =
        defaultTask(classificationSummary, workbasketSummary1, objectReference)
            .buildAndStoreAsSummary(taskService);
    taskSummary2 =
        defaultTask(classificationSummary, workbasketSummary1, objectReference)
            .buildAndStoreAsSummary(taskService);
    taskSummary3 =
        defaultTask(classificationSummary, workbasketSummary1, objectReference)
            .buildAndStoreAsSummary(taskService);
    taskSummary4 =
        defaultTask(classificationSummary, workbasketSummary1, objectReference)
            .buildAndStoreAsSummary(taskService);
    taskSummary5 =
        defaultTask(classificationSummary, workbasketSummary1, objectReference)
            .buildAndStoreAsSummary(taskService);
    taskSummary6 =
        defaultTask(classificationSummary, workbasketSummary1, objectReference)
            .buildAndStoreAsSummary(taskService);
    taskSummary7 =
        defaultTask(classificationSummary, workbasketSummary5, objectReference)
            .buildAndStoreAsSummary(taskService);
  }

  @WithAccessId(user = "admin")
  @Test
  void should_DistributeTasksCorrectly()
      throws NotAuthorizedOnWorkbasketException,
          WorkbasketNotFoundException,
          InvalidTaskStateException,
          TaskNotFoundException {
    taskService.distribute(workbasketSummary1.getId(), null);

    List<TaskSummary> tasksInWb1 =
        taskService.createTaskQuery().workbasketIdIn(workbasketSummary1.getId()).list();
    List<TaskSummary> tasksInWb2 =
        taskService.createTaskQuery().workbasketIdIn(workbasketSummary2.getId()).list();
    List<TaskSummary> tasksInWb3 =
        taskService.createTaskQuery().workbasketIdIn(workbasketSummary3.getId()).list();
    List<TaskSummary> tasksInWb4 =
        taskService.createTaskQuery().workbasketIdIn(workbasketSummary4.getId()).list();

    assertThat(tasksInWb1.size()).isEqualTo(0);
    assertThat(tasksInWb2.size()).isEqualTo(2);
    assertThat(tasksInWb3.size()).isEqualTo(2);
    assertThat(tasksInWb4.size()).isEqualTo(2);
  }

  @WithAccessId(user = "admin")
  @Test
  void should_ThrowError_When_TasksFromDifferentSourceWorkbaskets() {

    List<String> taskIds =
        Stream.of(taskSummary7, taskSummary1, taskSummary2).map(TaskSummary::getId).toList();

    assertThatThrownBy(() -> taskService.distribute(taskIds, null))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining("Not all tasks are in the same workbasket.");
  }

  @WithAccessId(user = "user-1-1")
  @Test
  void should_ThrowErrorNotAuthorizedException_When_NotAuthorized() {

    List<String> taskIds =
        Stream.of(taskSummary7, taskSummary1, taskSummary2).map(TaskSummary::getId).toList();

    String expectedWorkbasketId = workbasketSummary1.getId();

    assertThatThrownBy(() -> taskService.distribute(expectedWorkbasketId, null))
        .isInstanceOf(NotAuthorizedOnWorkbasketException.class)
        .hasMessageContaining(
            String.format(
                "Not authorized. The current user 'user-1-1' has no '[DISTRIBUTE]' permission(s) "
                    + "for Workbasket '%s'.",
                expectedWorkbasketId));
  }

  @WithAccessId(user = "admin")
  @Test
  void should_ThrowInvalidArgumentException_When_DistributionStrategyDoesNotExist() {

    String nonExistingStrategy = "NoExistingStrategy";

    assertThatThrownBy(
            () -> taskService.distribute(workbasketSummary1.getId(), nonExistingStrategy, null))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining(
            "The distribution strategy '%s' does not exist.", nonExistingStrategy);
  }

  @WithAccessId(user = "admin")
  @Test
  void should_DoNothing_When_TaskIdsAreEmptyViaSourceWorkbasketId() throws Exception {

    BulkOperationResults<String, KadaiException> result =
        taskService.distribute(workbasketSummary2.getId(), null);

    assertThat(result.getFailedIds()).isEmpty();

    List<TaskSummary> tasksInWb2 =
        taskService.createTaskQuery().workbasketIdIn(workbasketSummary2.getId()).list();
    List<TaskSummary> tasksInWb3 =
        taskService.createTaskQuery().workbasketIdIn(workbasketSummary3.getId()).list();

    assertThat(tasksInWb2).isEmpty();
    assertThat(tasksInWb3).isEmpty();
  }

  @WithAccessId(user = "admin")
  @Test
  void should_ThrowInvalidArgumentException_When_TaskIdsAreEmptyViaListOfTaskIds() {

    assertThatThrownBy(() -> taskService.distribute(List.of(), null))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining(
            "The source workbasket cannot be identified because the task list is empty.");
  }

  @WithAccessId(user = "admin")
  @Test
  void should_ThrowWorkbasketNotFoundException_When_WorkbasketDoesNotExist() {

    String nonExistentWorkbasketId = "NonExistentWorkbasket";

    assertThatThrownBy(() -> taskService.distribute(nonExistentWorkbasketId, null))
        .isInstanceOf(WorkbasketNotFoundException.class)
        .hasMessageContaining(
            String.format("Workbasket with id '%s' was not found.", nonExistentWorkbasketId));
  }
}
