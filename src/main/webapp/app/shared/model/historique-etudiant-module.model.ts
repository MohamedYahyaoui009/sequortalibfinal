import { Moment } from 'moment';
import { IModule } from 'app/shared/model/module.model';
import { IEtudiant } from 'app/shared/model/etudiant.model';

export interface IHistoriqueEtudiantModule {
  id?: number;
  datedebut?: Moment;
  datefin?: Moment;
  note?: number;
  modules?: IModule[];
  etudiants?: IEtudiant[];
}

export class HistoriqueEtudiantModule implements IHistoriqueEtudiantModule {
  constructor(
    public id?: number,
    public datedebut?: Moment,
    public datefin?: Moment,
    public note?: number,
    public modules?: IModule[],
    public etudiants?: IEtudiant[]
  ) {}
}
