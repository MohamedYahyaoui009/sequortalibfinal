import { IHistoriqueEtudiantModule } from 'app/shared/model/historique-etudiant-module.model';
import { IHistoriqueEtudiantFiliere } from 'app/shared/model/historique-etudiant-filiere.model';
import { IEtablissement } from 'app/shared/model/etablissement.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IEtudiant {
  id?: number;
  semestre?: number;
  section?: string;
  etat?: Status;
  historiqueEtudiantModule?: IHistoriqueEtudiantModule;
  historiqueEtudiantFiliere?: IHistoriqueEtudiantFiliere;
  etablissement?: IEtablissement;
}

export class Etudiant implements IEtudiant {
  constructor(
    public id?: number,
    public semestre?: number,
    public section?: string,
    public etat?: Status,
    public historiqueEtudiantModule?: IHistoriqueEtudiantModule,
    public historiqueEtudiantFiliere?: IHistoriqueEtudiantFiliere,
    public etablissement?: IEtablissement
  ) {}
}
