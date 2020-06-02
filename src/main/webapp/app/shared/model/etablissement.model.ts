import { IEtudiant } from 'app/shared/model/etudiant.model';
import { TypeCycle } from 'app/shared/model/enumerations/type-cycle.model';

export interface IEtablissement {
  id?: number;
  nom?: string;
  filiere?: string;
  cycle?: TypeCycle;
  etudiants?: IEtudiant[];
}

export class Etablissement implements IEtablissement {
  constructor(public id?: number, public nom?: string, public filiere?: string, public cycle?: TypeCycle, public etudiants?: IEtudiant[]) {}
}
