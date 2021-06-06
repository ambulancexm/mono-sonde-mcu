import { IIot } from 'app/entities/iot/iot.model';

export interface ITypeIot {
  id?: number;
  name?: string | null;
  iot?: IIot | null;
}

export class TypeIot implements ITypeIot {
  constructor(public id?: number, public name?: string | null, public iot?: IIot | null) {}
}

export function getTypeIotIdentifier(typeIot: ITypeIot): number | undefined {
  return typeIot.id;
}
