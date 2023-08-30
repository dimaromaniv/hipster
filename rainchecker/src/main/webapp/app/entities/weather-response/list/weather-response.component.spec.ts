import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { WeatherResponseService } from '../service/weather-response.service';

import { WeatherResponseComponent } from './weather-response.component';

describe('WeatherResponse Management Component', () => {
  let comp: WeatherResponseComponent;
  let fixture: ComponentFixture<WeatherResponseComponent>;
  let service: WeatherResponseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'weather-response', component: WeatherResponseComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [WeatherResponseComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(WeatherResponseComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WeatherResponseComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(WeatherResponseService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.weatherResponses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to weatherResponseService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getWeatherResponseIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getWeatherResponseIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
