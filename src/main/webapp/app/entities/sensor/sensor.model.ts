import { IIot } from 'app/entities/iot/iot.model';
import { TypeSensor } from 'app/entities/enumerations/type-sensor.model';

export interface ISensor {
  id?: number;
  typeSensor?: TypeSensor | null;
  value?: number | null;
  iots?: IIot[] | null;
}

export class Sensor implements ISensor {
  constructor(public id?: number, public typeSensor?: TypeSensor | null, public value?: number | null, public iots?: IIot[] | null) {}
}

export function getSensorIdentifier(sensor: ISensor): number | undefined {
  return sensor.id;
}
