import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductMovementRequest, ProductMovementResponse } from '../models/product-movement.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductMovementApiService {
  private readonly baseUrl = `${environment.apiBaseUrl}/api/stock-movements`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<ProductMovementResponse[]> {
    return this.http.get<ProductMovementResponse[]>(this.baseUrl);
  }

  create(payload: ProductMovementRequest): Observable<ProductMovementResponse> {
    return this.http.post<ProductMovementResponse>(this.baseUrl, payload);
  }
}

