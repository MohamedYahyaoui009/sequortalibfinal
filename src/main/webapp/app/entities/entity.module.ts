import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'module',
        loadChildren: () => import('./module/module.module').then(m => m.SequortalibfinalModuleModule)
      },
      {
        path: 'filiere',
        loadChildren: () => import('./filiere/filiere.module').then(m => m.SequortalibfinalFiliereModule)
      },
      {
        path: 'historique-etudiant-module',
        loadChildren: () =>
          import('./historique-etudiant-module/historique-etudiant-module.module').then(
            m => m.SequortalibfinalHistoriqueEtudiantModuleModule
          )
      },
      {
        path: 'historique-enseignant-module',
        loadChildren: () =>
          import('./historique-enseignant-module/historique-enseignant-module.module').then(
            m => m.SequortalibfinalHistoriqueEnseignantModuleModule
          )
      },
      {
        path: 'departement',
        loadChildren: () => import('./departement/departement.module').then(m => m.SequortalibfinalDepartementModule)
      },
      {
        path: 'historique-enseignant-filiere',
        loadChildren: () =>
          import('./historique-enseignant-filiere/historique-enseignant-filiere.module').then(
            m => m.SequortalibfinalHistoriqueEnseignantFiliereModule
          )
      },
      {
        path: 'historique-etudiant-filiere',
        loadChildren: () =>
          import('./historique-etudiant-filiere/historique-etudiant-filiere.module').then(
            m => m.SequortalibfinalHistoriqueEtudiantFiliereModule
          )
      },
      {
        path: 'etudiant',
        loadChildren: () => import('./etudiant/etudiant.module').then(m => m.SequortalibfinalEtudiantModule)
      },
      {
        path: 'enseignant',
        loadChildren: () => import('./enseignant/enseignant.module').then(m => m.SequortalibfinalEnseignantModule)
      },
      {
        path: 'etablissement',
        loadChildren: () => import('./etablissement/etablissement.module').then(m => m.SequortalibfinalEtablissementModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class SequortalibfinalEntityModule {}
