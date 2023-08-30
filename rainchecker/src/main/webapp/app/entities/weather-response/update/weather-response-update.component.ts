import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { WeatherResponseFormService, WeatherResponseFormGroup } from './weather-response-form.service';
import { IWeatherResponse } from '../weather-response.model';
import { WeatherResponseService } from '../service/weather-response.service';

@Component({
  selector: 'jhi-weather-response-update',
  templateUrl: './weather-response-update.component.html',
})
export class WeatherResponseUpdateComponent implements OnInit {
  isSaving = false;
  weatherResponse: IWeatherResponse | null = null;

  editForm: WeatherResponseFormGroup = this.weatherResponseFormService.createWeatherResponseFormGroup();

  constructor(
    protected weatherResponseService: WeatherResponseService,
    protected weatherResponseFormService: WeatherResponseFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weatherResponse }) => {
      this.weatherResponse = weatherResponse;
      if (weatherResponse) {
        this.updateForm(weatherResponse);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const weatherResponse = this.weatherResponseFormService.getWeatherResponse(this.editForm);
    if (weatherResponse.id !== null) {
      this.subscribeToSaveResponse(this.weatherResponseService.update(weatherResponse));
    } else {
      this.subscribeToSaveResponse(this.weatherResponseService.create(weatherResponse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeatherResponse>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(weatherResponse: IWeatherResponse): void {
    this.weatherResponse = weatherResponse;
    this.weatherResponseFormService.resetForm(this.editForm, weatherResponse);
  }
}
