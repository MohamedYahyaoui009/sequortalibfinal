import { IFiliere } from 'app/shared/model/filiere.model';

export interface IDepartement {
  id?: number;
  nom?: string;
  filieres?: IFiliere[];
}

export class Departement implements IDepartement {
  constructor(public id?: number, public nom?: string, public filieres?: IFiliere[]) {}
}
