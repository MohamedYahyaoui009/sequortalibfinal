import { IHistoriqueEnseignantModule } from 'app/shared/model/historique-enseignant-module.model';
import { IHistoriqueEnseignantFiliere } from 'app/shared/model/historique-enseignant-filiere.model';

export interface IEnseignant {
  id?: number;
  grade?: string;
  historiqueEnseignantModule?: IHistoriqueEnseignantModule;
  historiqueEnseignantFiliere?: IHistoriqueEnseignantFiliere;
}

export class Enseignant implements IEnseignant {
  constructor(
    public id?: number,
    public grade?: string,
    public historiqueEnseignantModule?: IHistoriqueEnseignantModule,
    public historiqueEnseignantFiliere?: IHistoriqueEnseignantFiliere
  ) {}
}
