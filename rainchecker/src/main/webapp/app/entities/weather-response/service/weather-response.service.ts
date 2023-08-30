import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWeatherResponse, NewWeatherResponse } from '../weather-response.model';

export type PartialUpdateWeatherResponse = Partial<IWeatherResponse> & Pick<IWeatherResponse, 'id'>;

export type EntityResponseType = HttpResponse<IWeatherResponse>;
export type EntityArrayResponseType = HttpResponse<IWeatherResponse[]>;

@Injectable({ providedIn: 'root' })
export class WeatherResponseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/weather-responses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(weatherResponse: NewWeatherResponse): Observable<EntityResponseType> {
    return this.http.post<IWeatherResponse>(this.resourceUrl, weatherResponse, { observe: 'response' });
  }

  update(weatherResponse: IWeatherResponse): Observable<EntityResponseType> {
    return this.http.put<IWeatherResponse>(`${this.resourceUrl}/${this.getWeatherResponseIdentifier(weatherResponse)}`, weatherResponse, {
      observe: 'response',
    });
  }

  partialUpdate(weatherResponse: PartialUpdateWeatherResponse): Observable<EntityResponseType> {
    return this.http.patch<IWeatherResponse>(`${this.resourceUrl}/${this.getWeatherResponseIdentifier(weatherResponse)}`, weatherResponse, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWeatherResponse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWeatherResponse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWeatherResponseIdentifier(weatherResponse: Pick<IWeatherResponse, 'id'>): number {
    return weatherResponse.id;
  }

  compareWeatherResponse(o1: Pick<IWeatherResponse, 'id'> | null, o2: Pick<IWeatherResponse, 'id'> | null): boolean {
    return o1 && o2 ? this.getWeatherResponseIdentifier(o1) === this.getWeatherResponseIdentifier(o2) : o1 === o2;
  }

  addWeatherResponseToCollectionIfMissing<Type extends Pick<IWeatherResponse, 'id'>>(
    weatherResponseCollection: Type[],
    ...weatherResponsesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const weatherResponses: Type[] = weatherResponsesToCheck.filter(isPresent);
    if (weatherResponses.length > 0) {
      const weatherResponseCollectionIdentifiers = weatherResponseCollection.map(
        weatherResponseItem => this.getWeatherResponseIdentifier(weatherResponseItem)!
      );
      const weatherResponsesToAdd = weatherResponses.filter(weatherResponseItem => {
        const weatherResponseIdentifier = this.getWeatherResponseIdentifier(weatherResponseItem);
        if (weatherResponseCollectionIdentifiers.includes(weatherResponseIdentifier)) {
          return false;
        }
        weatherResponseCollectionIdentifiers.push(weatherResponseIdentifier);
        return true;
      });
      return [...weatherResponsesToAdd, ...weatherResponseCollection];
    }
    return weatherResponseCollection;
  }
}
