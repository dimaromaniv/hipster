import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWeatherResponse, NewWeatherResponse } from '../weather-response.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWeatherResponse for edit and NewWeatherResponseFormGroupInput for create.
 */
type WeatherResponseFormGroupInput = IWeatherResponse | PartialWithRequiredKeyOf<NewWeatherResponse>;

type WeatherResponseFormDefaults = Pick<NewWeatherResponse, 'id'>;

type WeatherResponseFormGroupContent = {
  id: FormControl<IWeatherResponse['id'] | NewWeatherResponse['id']>;
  cloudyPercent: FormControl<IWeatherResponse['cloudyPercent']>;
  tempLevel: FormControl<IWeatherResponse['tempLevel']>;
  humidity: FormControl<IWeatherResponse['humidity']>;
  forecastForNextHours: FormControl<IWeatherResponse['forecastForNextHours']>;
  rainCounter: FormControl<IWeatherResponse['rainCounter']>;
  status: FormControl<IWeatherResponse['status']>;
};

export type WeatherResponseFormGroup = FormGroup<WeatherResponseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WeatherResponseFormService {
  createWeatherResponseFormGroup(weatherResponse: WeatherResponseFormGroupInput = { id: null }): WeatherResponseFormGroup {
    const weatherResponseRawValue = {
      ...this.getFormDefaults(),
      ...weatherResponse,
    };
    return new FormGroup<WeatherResponseFormGroupContent>({
      id: new FormControl(
        { value: weatherResponseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cloudyPercent: new FormControl(weatherResponseRawValue.cloudyPercent),
      tempLevel: new FormControl(weatherResponseRawValue.tempLevel),
      humidity: new FormControl(weatherResponseRawValue.humidity),
      forecastForNextHours: new FormControl(weatherResponseRawValue.forecastForNextHours),
      rainCounter: new FormControl(weatherResponseRawValue.rainCounter),
      status: new FormControl(weatherResponseRawValue.status),
    });
  }

  getWeatherResponse(form: WeatherResponseFormGroup): IWeatherResponse | NewWeatherResponse {
    return form.getRawValue() as IWeatherResponse | NewWeatherResponse;
  }

  resetForm(form: WeatherResponseFormGroup, weatherResponse: WeatherResponseFormGroupInput): void {
    const weatherResponseRawValue = { ...this.getFormDefaults(), ...weatherResponse };
    form.reset(
      {
        ...weatherResponseRawValue,
        id: { value: weatherResponseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WeatherResponseFormDefaults {
    return {
      id: null,
    };
  }
}
