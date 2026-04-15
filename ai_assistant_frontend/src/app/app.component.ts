import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StockDashboardApiService, StockDashboardData } from './services/stock-dashboard-api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Stock Database Dashboard';
  loading = false;
  errorMessage: string | null = null;
  statusMessage = 'Ready';
  lastSync: Date | null = null;
  login = '';
  password = '';

  data: StockDashboardData = {
    productCount: 0,
    clientCount: 0,
    invoiceCount: 0,
    totalStockQuantity: 0,
    totalInvoicedAmount: 0,
    closedInvoiceCount: 0,
    topProducts: []
  };

  constructor(private dashboardApi: StockDashboardApiService) {
    if (this.dashboardApi.hasAuthToken()) {
      this.refreshDashboard();
    } else {
      this.statusMessage = 'Authentication required for stock API.';
    }
  }

  connectToStockApi(): void {
    this.loading = true;
    this.errorMessage = null;
    this.statusMessage = 'Authenticating...';

    this.dashboardApi.authenticate(this.login, this.password).subscribe({
      next: () => {
        this.statusMessage = 'Authenticated. Loading dashboard...';
        this.refreshDashboard();
      },
      error: (err) => {
        this.errorMessage = `Authentication failed: ${err?.message ?? 'Unknown error'}`;
        this.loading = false;
      }
    });
  }

  refreshDashboard(): void {
    this.loading = true;
    this.errorMessage = null;
    this.statusMessage = 'Loading stock database metrics...';

    this.dashboardApi.getDashboardData().subscribe({
      next: (response) => {
        this.data = response;
        this.lastSync = new Date();
        this.statusMessage = 'Dashboard synchronized with stock database.';
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = `Dashboard loading failed: ${err?.message ?? 'Unknown error'}`;
        this.loading = false;
      }
    });
  }
}

