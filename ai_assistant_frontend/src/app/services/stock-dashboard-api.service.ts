import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';
import { environment } from '../../environments/environment';

interface LoginResponse {
  accessToken: string;
}

interface ProductResponse {
  products: Array<{ publicId: string; name: string; quantity: number; type: string }>;
  count: number;
}

interface ClientResponse {
  clients: Array<{ publicId: string; name: string }>;
  count: number;
}

interface InvoiceResponse {
  invoices: Array<{ publicId: string; totalNet: number; closed: boolean }>;
  count: number;
}

export interface StockDashboardData {
  productCount: number;
  clientCount: number;
  invoiceCount: number;
  totalStockQuantity: number;
  totalInvoicedAmount: number;
  closedInvoiceCount: number;
  topProducts: Array<{ name: string; quantity: number; type: string }>;
}

@Injectable({ providedIn: 'root' })
export class StockDashboardApiService {
  private readonly baseUrl = environment.stockApiBaseUrl;

  constructor(private http: HttpClient) {}

  authenticate(login: string, password: string): Observable<void> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/api/stok/auth/login`, { login, password }).pipe(
      map((response) => {
        localStorage.setItem('Authorization', response.accessToken);
      })
    );
  }

  hasAuthToken(): boolean {
    return !!localStorage.getItem('Authorization');
  }

  getDashboardData(): Observable<StockDashboardData> {
    return forkJoin({
      products: this.http.post<ProductResponse>(`${this.baseUrl}/api/stok/product/search`, {
        types: ['RAW', 'FINAL'],
        page: 0,
        pageSize: 500
      }, { headers: this.buildAuthHeaders() }),
      clients: this.http.post<ClientResponse>(`${this.baseUrl}/api/stok/client/search`, {
        page: 0,
        pageSize: 500
      }, { headers: this.buildAuthHeaders() }),
      invoices: this.http.post<InvoiceResponse>(`${this.baseUrl}/api/stok/invoice/search`, {
        page: 0,
        pageSize: 500
      }, { headers: this.buildAuthHeaders() })
    }).pipe(
      map(({ products, clients, invoices }) => {
        const totalStockQuantity = products.products.reduce((sum, p) => sum + (Number(p.quantity) || 0), 0);
        const totalInvoicedAmount = invoices.invoices.reduce((sum, i) => sum + (Number(i.totalNet) || 0), 0);
        const closedInvoiceCount = invoices.invoices.filter((i) => i.closed).length;
        const topProducts = [...products.products]
          .sort((a, b) => (Number(b.quantity) || 0) - (Number(a.quantity) || 0))
          .slice(0, 8)
          .map((p) => ({ name: p.name, quantity: p.quantity, type: p.type }));

        return {
          productCount: products.count,
          clientCount: clients.count,
          invoiceCount: invoices.count,
          totalStockQuantity,
          totalInvoicedAmount,
          closedInvoiceCount,
          topProducts
        };
      })
    );
  }

  private buildAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('Authorization');
    if (!token) {
      return new HttpHeaders();
    }
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }
}

