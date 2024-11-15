import { Routes, Router, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { MonitorRoles } from './shared/roles/monitor.roles';
import { KadaiEngineService } from './shared/services/kadai-engine/kadai-engine.service';
import { BusinessAdminRoles } from './shared/roles/business-admin.roles';
import { UserRoles } from './shared/roles/user.roles';
import { catchError, map, Observable, of } from 'rxjs';
import { NoAccessComponent } from './shared/components/no-access/no-access.component';
import { AdministrationOverviewComponent } from './administration/components/administration-overview/administration-overview.component';
import { MonitorComponent } from './monitor/components/monitor/monitor.component';

const businessAdminGuard = (): boolean | UrlTree => {
  const kadaiEngineService = inject(KadaiEngineService);
  const router = inject(Router);

  if (kadaiEngineService.hasRole(Object.values(BusinessAdminRoles))) {
    return true;
  }

  return router.parseUrl('/kadai/workplace');
};

const monitorGuard = (): boolean | UrlTree => {
  const kadaiEngineService = inject(KadaiEngineService);
  const router = inject(Router);

  if (kadaiEngineService.hasRole(Object.values(MonitorRoles))) {
    return true;
  }

  return router.parseUrl('/kadai/workplace');
};

const userGuard = (): boolean | UrlTree => {
  const kadaiEngineService = inject(KadaiEngineService);
  const router = inject(Router);

  if (kadaiEngineService.hasRole(Object.values(UserRoles))) {
    return true;
  }

  return router.parseUrl('/kadai/no-role');
};

const historyGuard = (): Observable<boolean | UrlTree> => {
  const kadaiEngineService = inject(KadaiEngineService);
  const router = inject(Router);

  return kadaiEngineService.isHistoryProviderEnabled().pipe(
    map((value) => {
      if (value) {
        return value;
      }
      return router.parseUrl('/kadai/workplace');
    }),
    catchError(() => {
      return of(router.parseUrl('/kadai/workplace'));
    })
  );
};

export const appRoutes: Routes = [
  {
    path: 'kadai',
    children: [
      {
        path: 'administration',
        component: AdministrationOverviewComponent,
        canActivate: [businessAdminGuard],
      },
      {
        path: 'monitor',
        component: MonitorComponent,
        canActivate: [monitorGuard],
      },
      {
        path: 'no-role',
        component: NoAccessComponent,
      },
      {
        path: 'administration',
        redirectTo: 'administration/workbaskets',
      },
      {
        path: '**',
        redirectTo: 'administration/workbaskets',
      },
    ],
  },
  {
    path: 'no-role',
    component: NoAccessComponent,
  },
  {
    path: '**',
    redirectTo: 'kadai/administration/workbaskets',
  },
];