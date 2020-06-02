import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEtudiant, Etudiant } from 'app/shared/model/etudiant.model';
import { EtudiantService } from './etudiant.service';
import { IHistoriqueEtudiantModule } from 'app/shared/model/historique-etudiant-module.model';
import { HistoriqueEtudiantModuleService } from 'app/entities/historique-etudiant-module/historique-etudiant-module.service';
import { IHistoriqueEtudiantFiliere } from 'app/shared/model/historique-etudiant-filiere.model';
import { HistoriqueEtudiantFiliereService } from 'app/entities/historique-etudiant-filiere/historique-etudiant-filiere.service';
import { IEtablissement } from 'app/shared/model/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/etablissement.service';

type SelectableEntity = IHistoriqueEtudiantModule | IHistoriqueEtudiantFiliere | IEtablissement;

@Component({
  selector: 'jhi-etudiant-update',
  templateUrl: './etudiant-update.component.html'
})
export class EtudiantUpdateComponent implements OnInit {
  isSaving = false;
  historiqueetudiantmodules: IHistoriqueEtudiantModule[] = [];
  historiqueetudiantfilieres: IHistoriqueEtudiantFiliere[] = [];
  etablissements: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    semestre: [null, [Validators.required]],
    section: [null, [Validators.required]],
    etat: [null, [Validators.required]],
    historiqueEtudiantModule: [],
    historiqueEtudiantFiliere: [],
    etablissement: []
  });

  constructor(
    protected etudiantService: EtudiantService,
    protected historiqueEtudiantModuleService: HistoriqueEtudiantModuleService,
    protected historiqueEtudiantFiliereService: HistoriqueEtudiantFiliereService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etudiant }) => {
      this.updateForm(etudiant);

      this.historiqueEtudiantModuleService
        .query()
        .subscribe((res: HttpResponse<IHistoriqueEtudiantModule[]>) => (this.historiqueetudiantmodules = res.body || []));

      this.historiqueEtudiantFiliereService
        .query()
        .subscribe((res: HttpResponse<IHistoriqueEtudiantFiliere[]>) => (this.historiqueetudiantfilieres = res.body || []));

      this.etablissementService.query().subscribe((res: HttpResponse<IEtablissement[]>) => (this.etablissements = res.body || []));
    });
  }

  updateForm(etudiant: IEtudiant): void {
    this.editForm.patchValue({
      id: etudiant.id,
      semestre: etudiant.semestre,
      section: etudiant.section,
      etat: etudiant.etat,
      historiqueEtudiantModule: etudiant.historiqueEtudiantModule,
      historiqueEtudiantFiliere: etudiant.historiqueEtudiantFiliere,
      etablissement: etudiant.etablissement
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etudiant = this.createFromForm();
    if (etudiant.id !== undefined) {
      this.subscribeToSaveResponse(this.etudiantService.update(etudiant));
    } else {
      this.subscribeToSaveResponse(this.etudiantService.create(etudiant));
    }
  }

  private createFromForm(): IEtudiant {
    return {
      ...new Etudiant(),
      id: this.editForm.get(['id'])!.value,
      semestre: this.editForm.get(['semestre'])!.value,
      section: this.editForm.get(['section'])!.value,
      etat: this.editForm.get(['etat'])!.value,
      historiqueEtudiantModule: this.editForm.get(['historiqueEtudiantModule'])!.value,
      historiqueEtudiantFiliere: this.editForm.get(['historiqueEtudiantFiliere'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtudiant>>): void {
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
