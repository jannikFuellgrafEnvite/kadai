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

import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'orderBy',
  standalone: true
})
export class OrderBy implements PipeTransform {
  transform(records: Object[], sortKeys?: string[]): any {
    return records.sort((a, b) => {
      for (let i = 0; i < sortKeys.length; i++) {
        let sortKey = sortKeys[i];
        let direction = 1;
        if (sortKey.charAt(0) === '-') {
          direction = -1;
          sortKey = sortKey.substring(1);
        }
        const objectA = a[sortKey] ? a[sortKey].toLowerCase() : '';
        const objectB = b[sortKey] ? b[sortKey].toLowerCase() : '';
        if (objectA < objectB) {
          return -1 * direction;
        }
        if (objectA > objectB) {
          return direction;
        }
      }
      return 0;
    });
  }
}
