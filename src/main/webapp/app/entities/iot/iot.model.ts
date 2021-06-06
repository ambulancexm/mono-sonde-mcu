import { ISensor } from 'app/entities/sensor/sensor.model';
import { ITypeIot } from 'app/entities/type-iot/type-iot.model';

export interface IIot {
  id?: number;
  mac?: string | null;
  sensor?: ISensor | null;
  typeIots?: ITypeIot[] | null;
}

export class Iot implements IIot {
  constructor(public id?: number, public mac?: string | null, public sensor?: ISensor | null, public typeIots?: ITypeIot[] | null) {}
}

export function getIotIdentifier(iot: IIot): number | undefined {
  return iot.id;
}
