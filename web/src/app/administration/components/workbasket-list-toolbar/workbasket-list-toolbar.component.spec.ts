/*
 * Copyright [2024] [envite consulting GmbH]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *
 */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { WorkbasketListToolbarComponent } from './workbasket-list-toolbar.component';
import { Component, DebugElement, EventEmitter, Input, Output } from '@angular/core';
import { Actions, NgxsModule, ofActionDispatched, Store } from '@ngxs/store';
import { Observable, of } from 'rxjs';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { WorkbasketState } from '../../../shared/store/workbasket-store/workbasket.state';
import { WorkbasketService } from '../../../shared/services/workbasket/workbasket.service';
import { DomainService } from '../../../shared/services/domain/domain.service';
import { CreateWorkbasket } from '../../../shared/store/workbasket-store/workbasket.actions';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Direction, Sorting, WorkbasketQuerySortParameter } from '../../../shared/models/sorting';
import { ACTION } from '../../../shared/models/action';
import { KadaiType } from '../../../shared/models/kadai-type';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { RouterTestingModule } from '@angular/router/testing';
import { RequestInProgressService } from '../../../shared/services/request-in-progress/request-in-progress.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

const getDomainFn = jest.fn().mockReturnValue(true);
const domainServiceMock: Partial<DomainService> = {
  getDomains: getDomainFn,
  getSelectedDomain: jest.fn().mockReturnValue(of('A'))
};

@Component({ selector: 'kadai-administration-import-export', template: '', standalone: true })
class ImportExportStub {
  @Input() currentSelection: KadaiType;
  @Input() parentComponent;
}

@Component({ selector: 'kadai-shared-sort', template: '', standalone: true })
class SortStub {
  @Input() sortingFields: Map<WorkbasketQuerySortParameter, string>;
  @Input() defaultSortBy: WorkbasketQuerySortParameter;
  @Output() performSorting = new EventEmitter<Sorting<WorkbasketQuerySortParameter>>();
}

@Component({ selector: 'kadai-shared-workbasket-filter', template: '', standalone: true })
class FilterStub {
  @Input() isExpanded = false;
}

const requestInProgressServiceSpy = jest.fn().mockImplementation(() => jest.fn().mockReturnValue(of()));

describe('WorkbasketListToolbarComponent', () => {
  let fixture: ComponentFixture<WorkbasketListToolbarComponent>;
  let debugElement: DebugElement;
  let component: WorkbasketListToolbarComponent;
  let store: Store;
  let actions$: Observable<any>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        WorkbasketListToolbarComponent,
        RouterTestingModule,
        NgxsModule.forRoot([WorkbasketState]),
        NoopAnimationsModule,
        MatIconModule,
        MatDialogModule,
        ImportExportStub, 
        SortStub, 
        FilterStub
      ],
      declarations: [],
      providers: [
        { provide: DomainService, useValue: domainServiceMock },
        { provide: RequestInProgressService, useValue: requestInProgressServiceSpy },
        WorkbasketService,
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(WorkbasketListToolbarComponent);
    debugElement = fixture.debugElement;
    component = fixture.debugElement.componentInstance;
    store = TestBed.inject(Store);
    actions$ = TestBed.inject(Actions);
    component.action = ACTION.COPY;
    fixture.detectChanges();
  }));

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  /* Typescript */

  it('should dispatch CreateWorkbasket when addWorkbasket is called', waitForAsync(() => {
    component.action = ACTION.COPY;
    let actionDispatched = false;
    actions$.pipe(ofActionDispatched(CreateWorkbasket)).subscribe(() => (actionDispatched = true));
    component.addWorkbasket();
    expect(actionDispatched).toBe(true);
  }));

  it('should not dispatch action in addWorkbasket when action is CREATE', waitForAsync(() => {
    component.action = ACTION.CREATE;
    let actionDispatched = false;
    actions$.pipe(ofActionDispatched(CreateWorkbasket)).subscribe(() => (actionDispatched = true));
    component.addWorkbasket();
    expect(actionDispatched).toBe(false);
  }));

  it('should emit value when sorting is called', (done) => {
    const mockSort: Sorting<WorkbasketQuerySortParameter> = {
      'sort-by': WorkbasketQuerySortParameter.KEY,
      order: Direction.ASC
    };
    let sort: Sorting<WorkbasketQuerySortParameter> = undefined;
    component.performSorting.subscribe((sortBy: Sorting<WorkbasketQuerySortParameter>) => {
      sort = sortBy;
      done();
    });
    component.sorting(mockSort);
    expect(sort).toMatchObject(mockSort);
  });

  /* HTML */

  it('should call AddWorkbasket() when add-workbasket button is clicked', async () => {
    const button = debugElement.nativeElement.querySelector('.workbasket-list-toolbar__add-button');
    expect(button).toBeTruthy();
    expect(button.textContent).toContain('add');
    expect(button.textContent).toContain('Add');
    component.addWorkbasket = jest.fn().mockImplementation();
    button.click();
    expect(component.addWorkbasket).toHaveBeenCalled();
  });

  it('should display import-export component', () => {
    expect(debugElement.nativeElement.querySelector('kadai-administration-import-export')).toBeTruthy();
  });

  it('should display sort component', () => {
    expect(debugElement.nativeElement.querySelector('kadai-shared-sort')).toBeTruthy();
  });

  it('should display filter component', () => {
    expect(debugElement.nativeElement.querySelector('kadai-shared-workbasket-filter')).toBeTruthy();
  });

  it('should show expanded filter component only when filter button is clicked', () => {
    const button = debugElement.nativeElement.querySelector('.filter__filter-button');
    expect(button).toBeTruthy();
    button.click();
    fixture.detectChanges();
    expect(component.isExpanded).toBe(true);
    expect(button.textContent).toBe('keyboard_arrow_up');
    button.click();
    fixture.detectChanges();
    expect(component.isExpanded).toBe(false);
    expect(button.textContent).toBe('keyboard_arrow_down');
  });
});
