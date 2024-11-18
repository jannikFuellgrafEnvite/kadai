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

import { DebugElement } from '@angular/core';
import { Observable } from 'rxjs';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { Actions, NgxsModule, ofActionDispatched, Store } from '@ngxs/store';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../../shared/services/notifications/notification.service';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatTooltipModule } from '@angular/material/tooltip';
import { SettingsState } from '../../../shared/store/settings-store/settings.state';
import { SettingsComponent } from './settings.component';
import { settingsStateMock } from '../../../shared/store/mock-data/mock-store';
import { SetSettings } from '../../../shared/store/settings-store/settings.actions';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { RequestInProgressService } from '../../../shared/services/request-in-progress/request-in-progress.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

const notificationServiceSpy: Partial<NotificationService> = {
  showError: jest.fn(),
  showSuccess: jest.fn(),
  showDialog: jest.fn()
};

describe('SettingsComponent', () => {
  let fixture: ComponentFixture<SettingsComponent>;
  let debugElement: DebugElement;
  let component: SettingsComponent;
  let store: Store;
  let actions$: Observable<any>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        SettingsComponent,
        NgxsModule.forRoot([SettingsState]),
        FormsModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatTooltipModule,
        NoopAnimationsModule
      ],
      declarations: [],
      providers: [
        RequestInProgressService,
        {
          provide: NotificationService,
          useValue: notificationServiceSpy
        },
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SettingsComponent);
    debugElement = fixture.debugElement;
    component = fixture.debugElement.componentInstance;
    store = TestBed.inject(Store);
    actions$ = TestBed.inject(Actions);
    store.reset({
      ...store.snapshot(),
      settings: settingsStateMock
    });
    fixture.detectChanges();
  }));

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should show success when form is saved successfully', () => {
    const showSuccessSpy = jest.spyOn(notificationServiceSpy, 'showSuccess');
    component.onSave();
    expect(showSuccessSpy).toHaveBeenCalled();
  });

  it('should show error when an invalid form is tried to be saved', () => {
    component.settings['intervalHighPriority'] = [-100, 100];
    const showErrorSpy = jest.spyOn(notificationServiceSpy, 'showError');
    component.onSave();
    expect(showErrorSpy).toHaveBeenCalled();
  });

  it('should dispatch action onValidate() returns true', async () => {
    let isActionDispatched = false;
    actions$.pipe(ofActionDispatched(SetSettings)).subscribe(() => (isActionDispatched = true));
    component.onSave();
    expect(isActionDispatched).toBe(true);
  });
});
