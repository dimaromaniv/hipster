import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWeatherResponse } from '../weather-response.model';

@Component({
  selector: 'jhi-weather-response-detail',
  templateUrl: './weather-response-detail.component.html',
})
export class WeatherResponseDetailComponent implements OnInit {
  weatherResponse: IWeatherResponse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weatherResponse }) => {
      this.weatherResponse = weatherResponse;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
