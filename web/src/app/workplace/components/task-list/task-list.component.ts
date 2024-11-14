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

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Task } from 'app/workplace/models/task';
import { ActivatedRoute, Router } from '@angular/router';
import { NgIf, NgFor } from '@angular/common';
import { MatSelectionList, MatListOption } from '@angular/material/list';
import { MatDivider } from '@angular/material/divider';
import { SvgIconComponent } from 'angular-svg-icon';
import { GermanTimeFormatPipe } from '../../../shared/pipes/german-time-format.pipe';

@Component({
  selector: 'kadai-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
  standalone: true,
  imports: [NgIf, MatSelectionList, NgFor, MatListOption, MatDivider, SvgIconComponent, GermanTimeFormatPipe]
})
export class TaskListComponent implements OnInit {
  @Input()
  tasks: Task[];

  @Input()
  selectedId: string;

  @Output()
  selectedIdChange = new EventEmitter<string>();

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {}

  selectTask(taskId: string) {
    this.selectedId = taskId;
    this.selectedIdChange.emit(taskId);
    this.router.navigate([{ outlets: { detail: `taskdetail/${this.selectedId}` } }], {
      relativeTo: this.route.parent,
      queryParamsHandling: 'merge'
    });
  }
}
