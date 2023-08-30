export interface IWeatherResponse {
  id: number;
  cloudyPercent?: number | null;
  tempLevel?: number | null;
  humidity?: number | null;
  forecastForNextHours?: number | null;
  rainCounter?: number | null;
  status?: string | null;
}

export type NewWeatherResponse = Omit<IWeatherResponse, 'id'> & { id: null };
