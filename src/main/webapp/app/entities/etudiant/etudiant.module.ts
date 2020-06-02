import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SequortalibfinalSharedModule } from 'app/shared/shared.module';
import { EtudiantComponent } from './etudiant.component';
import { EtudiantDetailComponent } from './etudiant-detail.component';
import { EtudiantUpdateComponent } from './etudiant-update.component';
import { EtudiantDeleteDialogComponent } from './etudiant-delete-dialog.component';
import { etudiantRoute } from './etudiant.route';

@NgModule({
  imports: [SequortalibfinalSharedModule, RouterModule.forChild(etudiantRoute)],
  declarations: [EtudiantComponent, EtudiantDetailComponent, EtudiantUpdateComponent, EtudiantDeleteDialogComponent],
  entryComponents: [EtudiantDeleteDialogComponent]
})
export class SequortalibfinalEtudiantModule {}
