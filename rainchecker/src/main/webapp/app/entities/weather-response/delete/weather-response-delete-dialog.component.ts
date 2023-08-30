import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWeatherResponse } from '../weather-response.model';
import { WeatherResponseService } from '../service/weather-response.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './weather-response-delete-dialog.component.html',
})
export class WeatherResponseDeleteDialogComponent {
  weatherResponse?: IWeatherResponse;

  constructor(protected weatherResponseService: WeatherResponseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.weatherResponseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
