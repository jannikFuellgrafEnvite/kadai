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

import { Component, DebugElement, Input } from '@angular/core';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IconTypeComponent } from './icon-type.component';
import { WorkbasketType } from '../../../shared/models/workbasket-type';

@Component({ selector: 'svg-icon', template: '' })
class SvgIconStub {
  @Input() src;
  @Input() svgStyle;
  @Input() matTooltip;
}

describe('IconTypeComponent', () => {
  let fixture: ComponentFixture<IconTypeComponent>;
  let debugElement: DebugElement;
  let component: IconTypeComponent;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [IconTypeComponent],
      declarations: [SvgIconStub],
      providers: []
    }).compileComponents();

    fixture = TestBed.createComponent(IconTypeComponent);
    debugElement = fixture.debugElement;
    component = fixture.debugElement.componentInstance;
    fixture.detectChanges();
  }));

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should return icon path dependent on the type when calling getIconPath', () => {
    expect(component.getIconPath(WorkbasketType.PERSONAL)).toBe('user.svg');
    expect(component.getIconPath(WorkbasketType.GROUP)).toBe('users.svg');
    expect(component.getIconPath(WorkbasketType.TOPIC)).toBe('topic.svg');
    expect(component.getIconPath(WorkbasketType.CLEARANCE)).toBe('clearance.svg');
    expect(component.getIconPath(undefined)).toBe('asterisk.svg');
  });

  it('should display svg-icon', () => {
    expect(debugElement.nativeElement.querySelector('svg-icon')).toBeTruthy();
  });
});
