import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHistoriqueEtudiantModule } from 'app/shared/model/historique-etudiant-module.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { HistoriqueEtudiantModuleService } from './historique-etudiant-module.service';
import { HistoriqueEtudiantModuleDeleteDialogComponent } from './historique-etudiant-module-delete-dialog.component';

@Component({
  selector: 'jhi-historique-etudiant-module',
  templateUrl: './historique-etudiant-module.component.html'
})
export class HistoriqueEtudiantModuleComponent implements OnInit, OnDestroy {
  historiqueEtudiantModules?: IHistoriqueEtudiantModule[];
  eventSubscriber?: Subscription;
  currentSearch: string;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected historiqueEtudiantModuleService: HistoriqueEtudiantModuleService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    if (this.currentSearch) {
      this.historiqueEtudiantModuleService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe(
          (res: HttpResponse<IHistoriqueEtudiantModule[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
          () => this.onError()
        );
      return;
    }

    this.historiqueEtudiantModuleService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IHistoriqueEtudiantModule[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadPage(1);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInHistoriqueEtudiantModules();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IHistoriqueEtudiantModule): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInHistoriqueEtudiantModules(): void {
    this.eventSubscriber = this.eventManager.subscribe('historiqueEtudiantModuleListModification', () => this.loadPage());
  }

  delete(historiqueEtudiantModule: IHistoriqueEtudiantModule): void {
    const modalRef = this.modalService.open(HistoriqueEtudiantModuleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.historiqueEtudiantModule = historiqueEtudiantModule;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IHistoriqueEtudiantModule[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    this.router.navigate(['/historique-etudiant-module'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        search: this.currentSearch,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.historiqueEtudiantModules = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
