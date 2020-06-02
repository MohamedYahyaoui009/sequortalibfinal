import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IEnseignant } from 'app/shared/model/enseignant.model';

type EntityResponseType = HttpResponse<IEnseignant>;
type EntityArrayResponseType = HttpResponse<IEnseignant[]>;

@Injectable({ providedIn: 'root' })
export class EnseignantService {
  public resourceUrl = SERVER_API_URL + 'api/enseignants';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/enseignants';

  constructor(protected http: HttpClient) {}

  create(enseignant: IEnseignant): Observable<EntityResponseType> {
    return this.http.post<IEnseignant>(this.resourceUrl, enseignant, { observe: 'response' });
  }

  update(enseignant: IEnseignant): Observable<EntityResponseType> {
    return this.http.put<IEnseignant>(this.resourceUrl, enseignant, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEnseignant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnseignant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnseignant[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
