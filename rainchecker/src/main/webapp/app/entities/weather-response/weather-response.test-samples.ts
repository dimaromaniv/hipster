import { IWeatherResponse, NewWeatherResponse } from './weather-response.model';

export const sampleWithRequiredData: IWeatherResponse = {
  id: 2599,
};

export const sampleWithPartialData: IWeatherResponse = {
  id: 94343,
  tempLevel: 36676,
  forecastForNextHours: 90080,
  rainCounter: 1356,
};

export const sampleWithFullData: IWeatherResponse = {
  id: 34969,
  cloudyPercent: 25564,
  tempLevel: 74232,
  humidity: 48466,
  forecastForNextHours: 57072,
  rainCounter: 32764,
  status: 'Proactive',
};

export const sampleWithNewData: NewWeatherResponse = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
