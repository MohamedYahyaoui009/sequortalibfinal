import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEnseignant, Enseignant } from 'app/shared/model/enseignant.model';
import { EnseignantService } from './enseignant.service';
import { IHistoriqueEnseignantModule } from 'app/shared/model/historique-enseignant-module.model';
import { HistoriqueEnseignantModuleService } from 'app/entities/historique-enseignant-module/historique-enseignant-module.service';
import { IHistoriqueEnseignantFiliere } from 'app/shared/model/historique-enseignant-filiere.model';
import { HistoriqueEnseignantFiliereService } from 'app/entities/historique-enseignant-filiere/historique-enseignant-filiere.service';

type SelectableEntity = IHistoriqueEnseignantModule | IHistoriqueEnseignantFiliere;

@Component({
  selector: 'jhi-enseignant-update',
  templateUrl: './enseignant-update.component.html'
})
export class EnseignantUpdateComponent implements OnInit {
  isSaving = false;
  historiqueenseignantmodules: IHistoriqueEnseignantModule[] = [];
  historiqueenseignantfilieres: IHistoriqueEnseignantFiliere[] = [];

  editForm = this.fb.group({
    id: [],
    grade: [null, [Validators.required]],
    historiqueEnseignantModule: [],
    historiqueEnseignantFiliere: []
  });

  constructor(
    protected enseignantService: EnseignantService,
    protected historiqueEnseignantModuleService: HistoriqueEnseignantModuleService,
    protected historiqueEnseignantFiliereService: HistoriqueEnseignantFiliereService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enseignant }) => {
      this.updateForm(enseignant);

      this.historiqueEnseignantModuleService
        .query()
        .subscribe((res: HttpResponse<IHistoriqueEnseignantModule[]>) => (this.historiqueenseignantmodules = res.body || []));

      this.historiqueEnseignantFiliereService
        .query()
        .subscribe((res: HttpResponse<IHistoriqueEnseignantFiliere[]>) => (this.historiqueenseignantfilieres = res.body || []));
    });
  }

  updateForm(enseignant: IEnseignant): void {
    this.editForm.patchValue({
      id: enseignant.id,
      grade: enseignant.grade,
      historiqueEnseignantModule: enseignant.historiqueEnseignantModule,
      historiqueEnseignantFiliere: enseignant.historiqueEnseignantFiliere
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enseignant = this.createFromForm();
    if (enseignant.id !== undefined) {
      this.subscribeToSaveResponse(this.enseignantService.update(enseignant));
    } else {
      this.subscribeToSaveResponse(this.enseignantService.create(enseignant));
    }
  }

  private createFromForm(): IEnseignant {
    return {
      ...new Enseignant(),
      id: this.editForm.get(['id'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      historiqueEnseignantModule: this.editForm.get(['historiqueEnseignantModule'])!.value,
      historiqueEnseignantFiliere: this.editForm.get(['historiqueEnseignantFiliere'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnseignant>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
