import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { SequortalibfinalTestModule } from '../../../test.module';
import { HistoriqueEtudiantModuleComponent } from 'app/entities/historique-etudiant-module/historique-etudiant-module.component';
import { HistoriqueEtudiantModuleService } from 'app/entities/historique-etudiant-module/historique-etudiant-module.service';
import { HistoriqueEtudiantModule } from 'app/shared/model/historique-etudiant-module.model';

describe('Component Tests', () => {
  describe('HistoriqueEtudiantModule Management Component', () => {
    let comp: HistoriqueEtudiantModuleComponent;
    let fixture: ComponentFixture<HistoriqueEtudiantModuleComponent>;
    let service: HistoriqueEtudiantModuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SequortalibfinalTestModule],
        declarations: [HistoriqueEtudiantModuleComponent],
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
        .overrideTemplate(HistoriqueEtudiantModuleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HistoriqueEtudiantModuleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HistoriqueEtudiantModuleService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HistoriqueEtudiantModule(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.historiqueEtudiantModules && comp.historiqueEtudiantModules[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new HistoriqueEtudiantModule(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.historiqueEtudiantModules && comp.historiqueEtudiantModules[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
