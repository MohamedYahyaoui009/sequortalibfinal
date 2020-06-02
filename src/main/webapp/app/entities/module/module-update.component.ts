import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IModule, Module } from 'app/shared/model/module.model';
import { ModuleService } from './module.service';
import { IHistoriqueEtudiantModule } from 'app/shared/model/historique-etudiant-module.model';
import { HistoriqueEtudiantModuleService } from 'app/entities/historique-etudiant-module/historique-etudiant-module.service';
import { IHistoriqueEnseignantModule } from 'app/shared/model/historique-enseignant-module.model';
import { HistoriqueEnseignantModuleService } from 'app/entities/historique-enseignant-module/historique-enseignant-module.service';

type SelectableEntity = IHistoriqueEtudiantModule | IHistoriqueEnseignantModule;

@Component({
  selector: 'jhi-module-update',
  templateUrl: './module-update.component.html'
})
export class ModuleUpdateComponent implements OnInit {
  isSaving = false;
  historiqueetudiantmodules: IHistoriqueEtudiantModule[] = [];
  historiqueenseignantmodules: IHistoriqueEnseignantModule[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    semestre: [],
    historiqueEtudiantModule: [],
    historiqueEnseignantModule: []
  });

  constructor(
    protected moduleService: ModuleService,
    protected historiqueEtudiantModuleService: HistoriqueEtudiantModuleService,
    protected historiqueEnseignantModuleService: HistoriqueEnseignantModuleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ module }) => {
      this.updateForm(module);

      this.historiqueEtudiantModuleService
        .query()
        .subscribe((res: HttpResponse<IHistoriqueEtudiantModule[]>) => (this.historiqueetudiantmodules = res.body || []));

      this.historiqueEnseignantModuleService
        .query()
        .subscribe((res: HttpResponse<IHistoriqueEnseignantModule[]>) => (this.historiqueenseignantmodules = res.body || []));
    });
  }

  updateForm(module: IModule): void {
    this.editForm.patchValue({
      id: module.id,
      nom: module.nom,
      semestre: module.semestre,
      historiqueEtudiantModule: module.historiqueEtudiantModule,
      historiqueEnseignantModule: module.historiqueEnseignantModule
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const module = this.createFromForm();
    if (module.id !== undefined) {
      this.subscribeToSaveResponse(this.moduleService.update(module));
    } else {
      this.subscribeToSaveResponse(this.moduleService.create(module));
    }
  }

  private createFromForm(): IModule {
    return {
      ...new Module(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      semestre: this.editForm.get(['semestre'])!.value,
      historiqueEtudiantModule: this.editForm.get(['historiqueEtudiantModule'])!.value,
      historiqueEnseignantModule: this.editForm.get(['historiqueEnseignantModule'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModule>>): void {
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
