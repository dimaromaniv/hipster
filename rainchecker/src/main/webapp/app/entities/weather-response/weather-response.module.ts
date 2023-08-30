import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WeatherResponseComponent } from './list/weather-response.component';
import { WeatherResponseDetailComponent } from './detail/weather-response-detail.component';
import { WeatherResponseUpdateComponent } from './update/weather-response-update.component';
import { WeatherResponseDeleteDialogComponent } from './delete/weather-response-delete-dialog.component';
import { WeatherResponseRoutingModule } from './route/weather-response-routing.module';

@NgModule({
  imports: [SharedModule, WeatherResponseRoutingModule],
  declarations: [
    WeatherResponseComponent,
    WeatherResponseDetailComponent,
    WeatherResponseUpdateComponent,
    WeatherResponseDeleteDialogComponent,
  ],
})
export class WeatherResponseModule {}
