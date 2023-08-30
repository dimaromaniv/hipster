import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWeatherResponse } from '../weather-response.model';
import { WeatherResponseService } from '../service/weather-response.service';

@Injectable({ providedIn: 'root' })
export class WeatherResponseRoutingResolveService implements Resolve<IWeatherResponse | null> {
  constructor(protected service: WeatherResponseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWeatherResponse | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((weatherResponse: HttpResponse<IWeatherResponse>) => {
          if (weatherResponse.body) {
            return of(weatherResponse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
