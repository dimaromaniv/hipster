import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../weather-response.test-samples';

import { WeatherResponseFormService } from './weather-response-form.service';

describe('WeatherResponse Form Service', () => {
  let service: WeatherResponseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WeatherResponseFormService);
  });

  describe('Service methods', () => {
    describe('createWeatherResponseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWeatherResponseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cloudyPercent: expect.any(Object),
            tempLevel: expect.any(Object),
            humidity: expect.any(Object),
            forecastForNextHours: expect.any(Object),
            rainCounter: expect.any(Object),
            status: expect.any(Object),
          })
        );
      });

      it('passing IWeatherResponse should create a new form with FormGroup', () => {
        const formGroup = service.createWeatherResponseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cloudyPercent: expect.any(Object),
            tempLevel: expect.any(Object),
            humidity: expect.any(Object),
            forecastForNextHours: expect.any(Object),
            rainCounter: expect.any(Object),
            status: expect.any(Object),
          })
        );
      });
    });

    describe('getWeatherResponse', () => {
      it('should return NewWeatherResponse for default WeatherResponse initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWeatherResponseFormGroup(sampleWithNewData);

        const weatherResponse = service.getWeatherResponse(formGroup) as any;

        expect(weatherResponse).toMatchObject(sampleWithNewData);
      });

      it('should return NewWeatherResponse for empty WeatherResponse initial value', () => {
        const formGroup = service.createWeatherResponseFormGroup();

        const weatherResponse = service.getWeatherResponse(formGroup) as any;

        expect(weatherResponse).toMatchObject({});
      });

      it('should return IWeatherResponse', () => {
        const formGroup = service.createWeatherResponseFormGroup(sampleWithRequiredData);

        const weatherResponse = service.getWeatherResponse(formGroup) as any;

        expect(weatherResponse).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWeatherResponse should not enable id FormControl', () => {
        const formGroup = service.createWeatherResponseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWeatherResponse should disable id FormControl', () => {
        const formGroup = service.createWeatherResponseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
