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

import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { MatTabNav, MatTabLink, MatTabNavPanel } from '@angular/material/tabs';
import { Routes } from '@angular/router';
import { TaskPriorityReportComponent } from '../task-priority-report/task-priority-report.component';
import { TaskReportComponent } from '../task-report/task-report.component';
import { WorkbasketReportComponent } from '../workbasket-report/workbasket-report.component';
import { ClassificationReportComponent } from '../classification-report/classification-report.component';
import { TimestampReportComponent } from '../timestamp-report/timestamp-report.component';

@Component({
  selector: 'kadai-monitor',
  templateUrl: './monitor.component.html',
  styleUrls: ['./monitor.component.scss'],
  standalone: true,
  imports: [MatTabNav, MatTabLink, RouterLink, MatTabNavPanel, RouterOutlet]
})
export class MonitorComponent implements OnInit {
  selectedTab = '';

  ngOnInit(): void {
    this.selectedTab = 'tasks-priority';
  }
}

const routes: Routes = [
  {
    path: '',
    component: MonitorComponent,
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'tasks-priority'
      },
      {
        path: 'tasks-priority',
        component: TaskPriorityReportComponent
      },
      {
        path: 'tasks-status',
        component: TaskReportComponent
      },
      {
        path: 'workbaskets',
        component: WorkbasketReportComponent
      },
      {
        path: 'classifications',
        component: ClassificationReportComponent
      },
      {
        path: 'timestamp',
        component: TimestampReportComponent
      }
    ]
  },
  {
    path: '',
    redirectTo: 'tasks-priority',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: ''
  }
];
