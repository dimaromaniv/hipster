import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WeatherResponseComponent } from '../list/weather-response.component';
import { WeatherResponseDetailComponent } from '../detail/weather-response-detail.component';
import { WeatherResponseUpdateComponent } from '../update/weather-response-update.component';
import { WeatherResponseRoutingResolveService } from './weather-response-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const weatherResponseRoute: Routes = [
  {
    path: '',
    component: WeatherResponseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WeatherResponseDetailComponent,
    resolve: {
      weatherResponse: WeatherResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WeatherResponseUpdateComponent,
    resolve: {
      weatherResponse: WeatherResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WeatherResponseUpdateComponent,
    resolve: {
      weatherResponse: WeatherResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(weatherResponseRoute)],
  exports: [RouterModule],
})
export class WeatherResponseRoutingModule {}
