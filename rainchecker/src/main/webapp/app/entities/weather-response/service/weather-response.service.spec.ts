import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWeatherResponse } from '../weather-response.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../weather-response.test-samples';

import { WeatherResponseService } from './weather-response.service';

const requireRestSample: IWeatherResponse = {
  ...sampleWithRequiredData,
};

describe('WeatherResponse Service', () => {
  let service: WeatherResponseService;
  let httpMock: HttpTestingController;
  let expectedResult: IWeatherResponse | IWeatherResponse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WeatherResponseService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a WeatherResponse', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const weatherResponse = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(weatherResponse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WeatherResponse', () => {
      const weatherResponse = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(weatherResponse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WeatherResponse', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WeatherResponse', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WeatherResponse', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWeatherResponseToCollectionIfMissing', () => {
      it('should add a WeatherResponse to an empty array', () => {
        const weatherResponse: IWeatherResponse = sampleWithRequiredData;
        expectedResult = service.addWeatherResponseToCollectionIfMissing([], weatherResponse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weatherResponse);
      });

      it('should not add a WeatherResponse to an array that contains it', () => {
        const weatherResponse: IWeatherResponse = sampleWithRequiredData;
        const weatherResponseCollection: IWeatherResponse[] = [
          {
            ...weatherResponse,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWeatherResponseToCollectionIfMissing(weatherResponseCollection, weatherResponse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WeatherResponse to an array that doesn't contain it", () => {
        const weatherResponse: IWeatherResponse = sampleWithRequiredData;
        const weatherResponseCollection: IWeatherResponse[] = [sampleWithPartialData];
        expectedResult = service.addWeatherResponseToCollectionIfMissing(weatherResponseCollection, weatherResponse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weatherResponse);
      });

      it('should add only unique WeatherResponse to an array', () => {
        const weatherResponseArray: IWeatherResponse[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const weatherResponseCollection: IWeatherResponse[] = [sampleWithRequiredData];
        expectedResult = service.addWeatherResponseToCollectionIfMissing(weatherResponseCollection, ...weatherResponseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const weatherResponse: IWeatherResponse = sampleWithRequiredData;
        const weatherResponse2: IWeatherResponse = sampleWithPartialData;
        expectedResult = service.addWeatherResponseToCollectionIfMissing([], weatherResponse, weatherResponse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weatherResponse);
        expect(expectedResult).toContain(weatherResponse2);
      });

      it('should accept null and undefined values', () => {
        const weatherResponse: IWeatherResponse = sampleWithRequiredData;
        expectedResult = service.addWeatherResponseToCollectionIfMissing([], null, weatherResponse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weatherResponse);
      });

      it('should return initial array if no WeatherResponse is added', () => {
        const weatherResponseCollection: IWeatherResponse[] = [sampleWithRequiredData];
        expectedResult = service.addWeatherResponseToCollectionIfMissing(weatherResponseCollection, undefined, null);
        expect(expectedResult).toEqual(weatherResponseCollection);
      });
    });

    describe('compareWeatherResponse', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWeatherResponse(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWeatherResponse(entity1, entity2);
        const compareResult2 = service.compareWeatherResponse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWeatherResponse(entity1, entity2);
        const compareResult2 = service.compareWeatherResponse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWeatherResponse(entity1, entity2);
        const compareResult2 = service.compareWeatherResponse(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
