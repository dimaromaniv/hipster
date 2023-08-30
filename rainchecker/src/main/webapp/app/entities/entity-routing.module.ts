import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'weather-response',
        data: { pageTitle: 'rainCheckerApp.weatherResponse.home.title' },
        loadChildren: () => import('./weather-response/weather-response.module').then(m => m.WeatherResponseModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
