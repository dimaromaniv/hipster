import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WeatherResponseDetailComponent } from './weather-response-detail.component';

describe('WeatherResponse Management Detail Component', () => {
  let comp: WeatherResponseDetailComponent;
  let fixture: ComponentFixture<WeatherResponseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WeatherResponseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ weatherResponse: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WeatherResponseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WeatherResponseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load weatherResponse on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.weatherResponse).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
