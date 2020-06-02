import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SequortalibfinalSharedModule } from 'app/shared/shared.module';
import { HistoriqueEnseignantModuleComponent } from './historique-enseignant-module.component';
import { HistoriqueEnseignantModuleDetailComponent } from './historique-enseignant-module-detail.component';
import { HistoriqueEnseignantModuleUpdateComponent } from './historique-enseignant-module-update.component';
import { HistoriqueEnseignantModuleDeleteDialogComponent } from './historique-enseignant-module-delete-dialog.component';
import { historiqueEnseignantModuleRoute } from './historique-enseignant-module.route';

@NgModule({
  imports: [SequortalibfinalSharedModule, RouterModule.forChild(historiqueEnseignantModuleRoute)],
  declarations: [
    HistoriqueEnseignantModuleComponent,
    HistoriqueEnseignantModuleDetailComponent,
    HistoriqueEnseignantModuleUpdateComponent,
    HistoriqueEnseignantModuleDeleteDialogComponent
  ],
  entryComponents: [HistoriqueEnseignantModuleDeleteDialogComponent]
})
export class SequortalibfinalHistoriqueEnseignantModuleModule {}
