import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WeatherResponseFormService } from './weather-response-form.service';
import { WeatherResponseService } from '../service/weather-response.service';
import { IWeatherResponse } from '../weather-response.model';

import { WeatherResponseUpdateComponent } from './weather-response-update.component';

describe('WeatherResponse Management Update Component', () => {
  let comp: WeatherResponseUpdateComponent;
  let fixture: ComponentFixture<WeatherResponseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let weatherResponseFormService: WeatherResponseFormService;
  let weatherResponseService: WeatherResponseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WeatherResponseUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WeatherResponseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WeatherResponseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    weatherResponseFormService = TestBed.inject(WeatherResponseFormService);
    weatherResponseService = TestBed.inject(WeatherResponseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const weatherResponse: IWeatherResponse = { id: 456 };

      activatedRoute.data = of({ weatherResponse });
      comp.ngOnInit();

      expect(comp.weatherResponse).toEqual(weatherResponse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWeatherResponse>>();
      const weatherResponse = { id: 123 };
      jest.spyOn(weatherResponseFormService, 'getWeatherResponse').mockReturnValue(weatherResponse);
      jest.spyOn(weatherResponseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weatherResponse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weatherResponse }));
      saveSubject.complete();

      // THEN
      expect(weatherResponseFormService.getWeatherResponse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(weatherResponseService.update).toHaveBeenCalledWith(expect.objectContaining(weatherResponse));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWeatherResponse>>();
      const weatherResponse = { id: 123 };
      jest.spyOn(weatherResponseFormService, 'getWeatherResponse').mockReturnValue({ id: null });
      jest.spyOn(weatherResponseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weatherResponse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weatherResponse }));
      saveSubject.complete();

      // THEN
      expect(weatherResponseFormService.getWeatherResponse).toHaveBeenCalled();
      expect(weatherResponseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWeatherResponse>>();
      const weatherResponse = { id: 123 };
      jest.spyOn(weatherResponseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weatherResponse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(weatherResponseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
