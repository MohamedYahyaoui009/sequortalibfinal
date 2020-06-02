import { IFiliere } from 'app/shared/model/filiere.model';
import { IHistoriqueEtudiantModule } from 'app/shared/model/historique-etudiant-module.model';
import { IHistoriqueEnseignantModule } from 'app/shared/model/historique-enseignant-module.model';

export interface IModule {
  id?: number;
  nom?: string;
  semestre?: number;
  filieres?: IFiliere[];
  historiqueEtudiantModule?: IHistoriqueEtudiantModule;
  historiqueEnseignantModule?: IHistoriqueEnseignantModule;
}

export class Module implements IModule {
  constructor(
    public id?: number,
    public nom?: string,
    public semestre?: number,
    public filieres?: IFiliere[],
    public historiqueEtudiantModule?: IHistoriqueEtudiantModule,
    public historiqueEnseignantModule?: IHistoriqueEnseignantModule
  ) {}
}
