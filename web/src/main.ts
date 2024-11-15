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

import { enableProdMode, APP_INITIALIZER, importProvidersFrom } from '@angular/core';
import { environment } from 'environments/environment';
import { WindowRefService } from 'app/shared/services/window/window.service';
import { DomainService } from 'app/shared/services/domain/domain.service';
import { RequestInProgressService } from 'app/shared/services/request-in-progress/request-in-progress.service';
import { OrientationService } from 'app/shared/services/orientation/orientation.service';
import { SelectedRouteService } from 'app/shared/services/selected-route/selected-route';
import { StartupService } from 'app/shared/services/startup/startup.service';
import { KadaiEngineService } from 'app/shared/services/kadai-engine/kadai-engine.service';
import { FormsValidatorService } from './app/shared/services/forms-validator/forms-validator.service';
import { NotificationService } from './app/shared/services/notifications/notification.service';
import { ClassificationCategoriesService } from './app/shared/services/classification-categories/classification-categories.service';
import { SidenavService } from './app/shared/services/sidenav/sidenav.service';
import { provideHttpClient, withXsrfConfiguration } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxsModule } from '@ngxs/store';
import { STATES } from './app/shared/store';
import { NgxsReduxDevtoolsPluginModule } from '@ngxs/devtools-plugin';
import { NgxsRouterPluginModule } from '@ngxs/router-plugin';
import { AppComponent } from './app/app.component';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { provideRouter } from '@angular/router';
import { appRoutes } from 'app/app.routes';
import { ReactiveFormsModule } from '@angular/forms';
import { bootstrapApplication } from '@angular/platform-browser';
import { MasterAndDetailService } from 'app/shared/services/master-and-detail/master-and-detail.service';

export function startupServiceFactory(startupService: StartupService): () => Promise<any> {
  return (): Promise<any> => startupService.load();
}

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(appRoutes),
    provideHttpClient(withXsrfConfiguration({ cookieName: 'XSRF-TOKEN', headerName: 'X-XSRF-TOKEN' })),
    importProvidersFrom(
      BrowserAnimationsModule,
      ReactiveFormsModule,
      NgxsModule.forRoot(STATES, { developmentMode: !environment.production }),
      NgxsReduxDevtoolsPluginModule.forRoot({ disabled: environment.production }),
      NgxsRouterPluginModule.forRoot()
    ),
    provideCharts(withDefaultRegisterables()),
    {
      provide: APP_INITIALIZER,
      useFactory: startupServiceFactory,
      deps: [StartupService],
      multi: true
    },
    WindowRefService,
    DomainService,
    RequestInProgressService,
    OrientationService,
    SelectedRouteService,
    KadaiEngineService,
    FormsValidatorService,
    NotificationService,
    ClassificationCategoriesService,
    SidenavService, 
    StartupService,
    MasterAndDetailService
]});
