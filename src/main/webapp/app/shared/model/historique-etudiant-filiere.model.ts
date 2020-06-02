import { Moment } from 'moment';
import { IEtudiant } from 'app/shared/model/etudiant.model';

export interface IHistoriqueEtudiantFiliere {
  id?: number;
  datedebut?: Moment;
  datefin?: Moment;
  etudiants?: IEtudiant[];
}

export class HistoriqueEtudiantFiliere implements IHistoriqueEtudiantFiliere {
  constructor(public id?: number, public datedebut?: Moment, public datefin?: Moment, public etudiants?: IEtudiant[]) {}
}
