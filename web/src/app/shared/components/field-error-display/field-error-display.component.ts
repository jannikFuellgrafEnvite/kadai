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

import { Component, OnInit, Input } from '@angular/core';
import { highlight } from 'app/shared/animations/validation.animation';
import { NgIf } from '@angular/common';

@Component({
    selector: 'kadai-shared-field-error-display',
    templateUrl: './field-error-display.component.html',
    animations: [highlight],
    styleUrls: ['./field-error-display.component.scss'],
    standalone: true,
    imports: [NgIf]
})
export class FieldErrorDisplayComponent implements OnInit {
  @Input()
  displayError: boolean;

  @Input()
  errorMessage: string;

  @Input()
  validationTrigger: boolean;

  ngOnInit() {}
}
