import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { SequortalibfinalTestModule } from '../../../test.module';
import { HistoriqueEnseignantModuleComponent } from 'app/entities/historique-enseignant-module/historique-enseignant-module.component';
import { HistoriqueEnseignantModuleService } from 'app/entities/historique-enseignant-module/historique-enseignant-module.service';
import { HistoriqueEnseignantModule } from 'app/shared/model/historique-enseignant-module.model';

describe('Component Tests', () => {
  describe('HistoriqueEnseignantModule Management Component', () => {
    let comp: HistoriqueEnseignantModuleComponent;
    let fixture: ComponentFixture<HistoriqueEnseignantModuleComponent>;
    let service: HistoriqueEnseignantModuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SequortalibfinalTestModule],
        declarations: [HistoriqueEnseignantModuleComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: {
                subscribe: (fn: (value: Data) => void) =>
                  fn({
                    pagingParams: {
                      predicate: 'id',
                      reverse: false,
                      page: 0
                    }
                  })
              }
            }
          }
        ]
      })
        .overrideTemplate(HistoriqueEnseignantModuleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HistoriqueEnseignantModuleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HistoriqueEnseignantModuleService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HistoriqueEnseignantModule(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.historiqueEnseignantModules && comp.historiqueEnseignantModules[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HistoriqueEnseignantModule(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.historiqueEnseignantModules && comp.historiqueEnseignantModules[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
