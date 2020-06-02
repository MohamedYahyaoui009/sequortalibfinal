import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IEtudiant } from 'app/shared/model/etudiant.model';

type EntityResponseType = HttpResponse<IEtudiant>;
type EntityArrayResponseType = HttpResponse<IEtudiant[]>;

@Injectable({ providedIn: 'root' })
export class EtudiantService {
  public resourceUrl = SERVER_API_URL + 'api/etudiants';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/etudiants';

  constructor(protected http: HttpClient) {}

  create(etudiant: IEtudiant): Observable<EntityResponseType> {
    return this.http.post<IEtudiant>(this.resourceUrl, etudiant, { observe: 'response' });
  }

  update(etudiant: IEtudiant): Observable<EntityResponseType> {
    return this.http.put<IEtudiant>(this.resourceUrl, etudiant, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEtudiant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtudiant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtudiant[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
