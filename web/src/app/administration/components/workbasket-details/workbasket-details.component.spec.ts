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
import { WorkbasketDetailsComponent } from './workbasket-details.component';
import { Component, DebugElement, Input } from '@angular/core';
import { Actions, NgxsModule, Store } from '@ngxs/store';
import { Observable, of } from 'rxjs';
import { Workbasket } from '../../../shared/models/workbasket';
import { ACTION } from '../../../shared/models/action';
import { WorkbasketState } from '../../../shared/store/workbasket-store/workbasket.state';
import { DomainService } from '../../../shared/services/domain/domain.service';
import { ImportExportService } from '../../services/import-export.service';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { WorkbasketService } from '../../../shared/services/workbasket/workbasket.service';
import { RouterTestingModule } from '@angular/router/testing';
import { RequestInProgressService } from '../../../shared/services/request-in-progress/request-in-progress.service';
import { SelectedRouteService } from '../../../shared/services/selected-route/selected-route';
import { MatDialogModule } from '@angular/material/dialog';
import { selectedWorkbasketMock, workbasketReadStateMock } from '../../../shared/store/mock-data/mock-store';
import { StartupService } from '../../../shared/services/startup/startup.service';
import { KadaiEngineService } from '../../../shared/services/kadai-engine/kadai-engine.service';
import { WindowRefService } from '../../../shared/services/window/window.service';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTabsModule } from '@angular/material/tabs';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { CopyWorkbasket, CreateWorkbasket } from '../../../shared/store/workbasket-store/workbasket.actions';
import { take } from 'rxjs/operators';
import { MatTooltipModule } from '@angular/material/tooltip';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

@Component({ selector: 'kadai-administration-workbasket-information', template: '<div>i</div>' })
class WorkbasketInformationStub {
  @Input() workbasket: Workbasket;
  @Input() action: ACTION;
}

@Component({ selector: 'kadai-administration-workbasket-access-items', template: '' })
class WorkbasketAccessItemsStub {
  @Input() workbasket: Workbasket;
  @Input() action: ACTION;
  @Input() active: string;
  @Input() expanded: boolean;
}

@Component({ selector: 'kadai-administration-workbasket-distribution-targets', template: '' })
class WorkbasketDistributionTargetsStub {
  @Input() workbasket: Workbasket;
  @Input() action: ACTION;
  @Input() active: string;
}

const domainServiceSpy: Partial<DomainService> = {
  getSelectedDomain: jest.fn().mockReturnValue(of('A')),
  getSelectedDomainValue: jest.fn().mockReturnValue(of()),
  getDomains: jest.fn().mockReturnValue(of())
};

export const workbasketReadState = {
  selectedWorkbasket: selectedWorkbasketMock,
  action: ACTION.READ
};

describe('WorkbasketDetailsComponent', () => {
  let fixture: ComponentFixture<WorkbasketDetailsComponent>;
  let debugElement: DebugElement;
  let component: WorkbasketDetailsComponent;
  let store: Store;
  let actions$: Observable<any>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        NgxsModule.forRoot([WorkbasketState]),
        RouterTestingModule.withRoutes([]),
        MatDialogModule,
        MatIconModule,
        MatProgressBarModule,
        MatTabsModule,
        MatMenuModule,
        MatToolbarModule,
        MatTooltipModule,
        NoopAnimationsModule
      ],
      declarations: [WorkbasketDetailsComponent],
      providers: [
        {
          provide: DomainService,
          useValue: domainServiceSpy
        },
        ImportExportService,
        WorkbasketService,
        RequestInProgressService,
        SelectedRouteService,
        StartupService,
        KadaiEngineService,
        WindowRefService,
        WorkbasketAccessItemsStub,
        WorkbasketDistributionTargetsStub,
        WorkbasketInformationStub,
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(WorkbasketDetailsComponent);
    debugElement = fixture.debugElement;
    component = fixture.debugElement.componentInstance;
    store = TestBed.inject(Store);
    actions$ = TestBed.inject(Actions);
    store.reset({
      ...store.snapshot(),
      workbasket: workbasketReadStateMock
    });
    fixture.detectChanges();
  }));

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should render information component when workbasket details is opened', () => {
    component.workbasket = { workbasketId: '1' };
    fixture.detectChanges();
    const information = debugElement.nativeElement.querySelector('kadai-administration-workbasket-information');
    expect(information).toBeTruthy();
  });

  it('should render new workbasket when action is CREATE', (done) => {
    store
      .dispatch(new CreateWorkbasket())
      .pipe(take(1))
      .subscribe(() => {
        component.selectedWorkbasketAndComponentAndAction$.pipe(take(1)).subscribe((state) => {
          expect(state.selectedWorkbasket.workbasketId).toBeUndefined();
          done();
        });
      });
  });

  it('should render copied workbasket when action is COPY', (done) => {
    const workbasket = component.workbasket;
    store
      .dispatch(new CopyWorkbasket(component.workbasket))
      .pipe(take(1))
      .subscribe(() => {
        component.selectedWorkbasketAndComponentAndAction$.pipe(take(1)).subscribe((state) => {
          const workbasketCopy = state.selectedWorkbasket;
          expect(workbasketCopy.workbasketId).toBeUndefined();
          expect(workbasketCopy.key).toEqual(workbasket.key);
          expect(workbasketCopy.owner).toEqual(workbasket.owner);
          done();
        });
      });
  });

  it('should render workbasket when action is READ', () => {
    store.reset({
      ...store.snapshot(),
      workbasket: workbasketReadState
    });
    fixture.detectChanges();
    expect(component.workbasket).not.toBeUndefined();
    expect(component.workbasket).not.toBeNull();
    expect(component.workbasket).toEqual(selectedWorkbasketMock);
  });

  it('should select information tab when action is CREATE', (done) => {
    component.selectComponent(1);
    store
      .dispatch(new CreateWorkbasket())
      .pipe(take(1))
      .subscribe(() => {
        component.selectedTab$.pipe(take(1)).subscribe((tab) => {
          expect(tab).toEqual(0);
          done();
        });
      });
  });
});
