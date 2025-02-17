import { AxiosResponse } from 'axios';
import { QueryFunction, QueryKey } from 'react-query';
import { QueryManagerReservationsSuccess, QueryReservationsSuccess } from 'types/response';
import api from './api';

export interface QueryMapReservationsParams {
  mapId: number;
  date: string;
}

export interface QuerySpaceReservationsParams extends QueryMapReservationsParams {
  spaceId: number;
}

interface ReservationParams {
  spaceId: number;
  reservation: {
    startDateTime: Date;
    endDateTime: Date;
    password: string;
    name: string;
    description: string;
  };
}

interface PostReservationParams extends ReservationParams {
  mapId: number;
}

interface PutReservationParams extends ReservationParams {
  mapId: number;
  reservationId: number;
}

interface DeleteReservationParams {
  mapId: number;
  spaceId: number;
  reservationId: number;
  password: string;
}

export const queryGuestReservations: QueryFunction<
  AxiosResponse<QueryReservationsSuccess>,
  [QueryKey, QuerySpaceReservationsParams]
> = ({ queryKey }) => {
  const [, data] = queryKey;
  const { mapId, spaceId, date } = data;

  return api.get(`/guests/maps/${mapId}/spaces/${spaceId}/reservations?date=${date}`);
};

export const queryManagerReservations: QueryFunction<
  AxiosResponse<QueryManagerReservationsSuccess>,
  [QueryKey, QueryMapReservationsParams]
> = ({ queryKey }) => {
  const [, data] = queryKey;
  const { mapId, date } = data;

  return api.get(`/managers/maps/${mapId}/spaces/reservations?date=${date}`);
};

export const postReservation = ({
  reservation,
  mapId,
  spaceId,
}: PostReservationParams): Promise<AxiosResponse<never>> =>
  api.post(`/guests/maps/${mapId}/spaces/${spaceId}/reservations`, reservation);

export const putReservation = ({
  reservation,
  mapId,
  spaceId,
  reservationId,
}: PutReservationParams): Promise<AxiosResponse<never>> =>
  api.put(`/guests/maps/${mapId}/spaces/${spaceId}/reservations/${reservationId}`, reservation);

export const deleteReservation = ({
  mapId,
  spaceId,
  reservationId,
  password,
}: DeleteReservationParams): Promise<AxiosResponse<never>> =>
  api.delete(`/guests/maps/${mapId}/spaces/${spaceId}/reservations/${reservationId}`, {
    data: { password },
  });
