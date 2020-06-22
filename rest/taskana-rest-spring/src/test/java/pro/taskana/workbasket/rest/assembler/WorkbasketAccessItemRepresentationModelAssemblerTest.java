package pro.taskana.workbasket.rest.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pro.taskana.common.rest.TaskanaSpringBootTest;
import pro.taskana.workbasket.api.WorkbasketService;
import pro.taskana.workbasket.api.models.WorkbasketAccessItem;
import pro.taskana.workbasket.internal.models.WorkbasketAccessItemImpl;
import pro.taskana.workbasket.rest.models.WorkbasketAccessItemRepresentationModel;

/** Test for {@link WorkbasketAccessItemRepresentationModelAssembler}. */
@TaskanaSpringBootTest
class WorkbasketAccessItemRepresentationModelAssemblerTest {

  private final WorkbasketAccessItemRepresentationModelAssembler assembler;
  private final WorkbasketService workbasketService;

  @Autowired
  WorkbasketAccessItemRepresentationModelAssemblerTest(
      WorkbasketAccessItemRepresentationModelAssembler assembler,
      WorkbasketService workbasketService) {
    this.assembler = assembler;
    this.workbasketService = workbasketService;
  }

  @Test
  void should_ReturnRepresentationModel_When_ConvertingEntityToRepresentationModel() {
    // given
    WorkbasketAccessItemImpl accessItem =
        (WorkbasketAccessItemImpl) workbasketService.newWorkbasketAccessItem("1", "2");
    accessItem.setId("id");
    accessItem.setAccessName("accessName");
    accessItem.setWorkbasketKey("workbasketKey");
    accessItem.setPermDistribute(false);
    accessItem.setPermOpen(true);
    accessItem.setPermAppend(false);
    accessItem.setPermRead(false);
    accessItem.setPermTransfer(true);
    accessItem.setPermCustom1(false);
    accessItem.setPermCustom2(false);
    accessItem.setPermCustom3(true);
    accessItem.setPermCustom4(true);
    accessItem.setPermCustom5(true);
    accessItem.setPermCustom6(true);
    accessItem.setPermCustom7(true);
    accessItem.setPermCustom8(true);
    accessItem.setPermCustom9(true);
    accessItem.setPermCustom10(true);
    accessItem.setPermCustom11(true);
    accessItem.setPermCustom12(true);
    // when
    WorkbasketAccessItemRepresentationModel repModel = assembler.toModel(accessItem);
    // then
    testEquality(accessItem, repModel);
    testLinks(repModel);
  }

  @Test
  void should_Equal_When_ComparingEntityWithConvertedEntity() {
    // given
    WorkbasketAccessItemImpl accessItem =
        (WorkbasketAccessItemImpl) workbasketService.newWorkbasketAccessItem("1", "2");
    accessItem.setId("accessItemId");
    accessItem.setWorkbasketKey("workbasketKey");
    accessItem.setPermDistribute(false);
    accessItem.setAccessName("accessName");
    accessItem.setPermOpen(true);
    accessItem.setPermAppend(false);
    accessItem.setPermRead(false);
    accessItem.setPermTransfer(true);
    accessItem.setPermCustom1(false);
    accessItem.setPermCustom2(false);
    accessItem.setPermCustom3(true);
    accessItem.setPermCustom4(true);
    accessItem.setPermCustom5(true);
    accessItem.setPermCustom6(true);
    accessItem.setPermCustom7(true);
    accessItem.setPermCustom8(true);
    accessItem.setPermCustom9(true);
    accessItem.setPermCustom10(true);
    accessItem.setPermCustom11(true);
    accessItem.setPermCustom12(true);
    // when
    WorkbasketAccessItemRepresentationModel repModel = assembler.toModel(accessItem);
    WorkbasketAccessItem accessItem2 = assembler.toEntityModel(repModel);
    // then
    assertThat(accessItem)
        .hasNoNullFieldsOrProperties()
        .isNotSameAs(accessItem2)
        .isEqualTo(accessItem2);
  }

  @Test
  void should_ReturnEntity_When_ConvertingRepresentationModelToEntity() {
    // given
    WorkbasketAccessItemRepresentationModel repModel =
        new WorkbasketAccessItemRepresentationModel();
    repModel.setAccessId("10");
    repModel.setWorkbasketKey("workbasketKey");
    repModel.setAccessItemId("120");
    repModel.setWorkbasketId("1");
    repModel.setAccessName("accessName");
    repModel.setPermRead(true);
    repModel.setPermAppend(false);
    repModel.setPermDistribute(false);
    repModel.setPermOpen(false);
    repModel.setPermTransfer(true);
    repModel.setPermCustom1(false);
    repModel.setPermCustom2(false);
    repModel.setPermCustom3(false);
    repModel.setPermCustom4(false);
    repModel.setPermCustom5(true);
    repModel.setPermCustom6(false);
    repModel.setPermCustom7(false);
    repModel.setPermCustom8(false);
    repModel.setPermCustom9(false);
    repModel.setPermCustom10(false);
    repModel.setPermCustom11(true);
    repModel.setPermCustom12(false);
    // when
    WorkbasketAccessItem accessItem = assembler.toEntityModel(repModel);
    // then
    testEquality(accessItem, repModel);
  }

  private void testEquality(
      WorkbasketAccessItem accessItem, WorkbasketAccessItemRepresentationModel repModel) {
    assertThat(accessItem).hasNoNullFieldsOrProperties();
    assertThat(repModel).hasNoNullFieldsOrProperties();
    assertThat(repModel.getAccessItemId()).isEqualTo(accessItem.getId());
    assertThat(repModel.getWorkbasketId()).isEqualTo(accessItem.getWorkbasketId());
    assertThat(repModel.getWorkbasketKey()).isEqualTo(accessItem.getWorkbasketKey());
    assertThat(repModel.getAccessId()).isEqualTo(accessItem.getAccessId());
    assertThat(repModel.getAccessName()).isEqualTo(accessItem.getAccessName());
    assertThat(repModel.isPermRead()).isEqualTo(accessItem.isPermRead());
    assertThat(repModel.isPermOpen()).isEqualTo(accessItem.isPermOpen());
    assertThat(repModel.isPermAppend()).isEqualTo(accessItem.isPermAppend());
    assertThat(repModel.isPermTransfer()).isEqualTo(accessItem.isPermTransfer());
    assertThat(repModel.isPermDistribute()).isEqualTo(accessItem.isPermDistribute());
    assertThat(repModel.isPermCustom1()).isEqualTo(accessItem.isPermCustom1());
    assertThat(repModel.isPermCustom2()).isEqualTo(accessItem.isPermCustom2());
    assertThat(repModel.isPermCustom3()).isEqualTo(accessItem.isPermCustom3());
    assertThat(repModel.isPermCustom4()).isEqualTo(accessItem.isPermCustom4());
    assertThat(repModel.isPermCustom5()).isEqualTo(accessItem.isPermCustom5());
    assertThat(repModel.isPermCustom6()).isEqualTo(accessItem.isPermCustom6());
    assertThat(repModel.isPermCustom7()).isEqualTo(accessItem.isPermCustom7());
    assertThat(repModel.isPermCustom8()).isEqualTo(accessItem.isPermCustom8());
    assertThat(repModel.isPermCustom9()).isEqualTo(accessItem.isPermCustom9());
    assertThat(repModel.isPermCustom10()).isEqualTo(accessItem.isPermCustom10());
    assertThat(repModel.isPermCustom11()).isEqualTo(accessItem.isPermCustom11());
    assertThat(repModel.isPermCustom12()).isEqualTo(accessItem.isPermCustom12());
  }

  private void testLinks(WorkbasketAccessItemRepresentationModel repModel) {}
}