import { Routes } from '@angular/router';
import {LoginUserComponent} from "./login/login-user/login-user.component";
import {HomeComponent} from "./home/home.component";
import {TokenValidGuard} from "./route-guard/token-valid.guard";
import {ProductComponent} from "./home/product/product.component";
import {StationComponent} from "./home/station/station.component";
import {ClientComponent} from "./home/client/client.component";
import {InvoiceComponent} from "./home/invoice/invoice.component";

export const routes: Routes = [
  {
    path: "login",
    component: LoginUserComponent
  },
  {
    path: "",
    component: HomeComponent,
    canActivate: [TokenValidGuard],
    children: [
      {
        path: '',
        redirectTo: 'product',
        pathMatch: 'full'
      },
      {
        path: "product",
        component: ProductComponent
      },
      {
        path: "station",
        component: StationComponent
      },
      {
        path: "client",
        component: ClientComponent
      },
      {
        path: "invoice",
        component: InvoiceComponent,
        data: {isBl: false}
      },
      {
        path: "bl",
        component: InvoiceComponent,
        data: {isBl: true}
      },
      {
        path: "ai-assistant",
        loadChildren: () => import("./stock_assistant/ai-assistant.routes").then((m) => m.aiAssistantRoutes)
      },
    ]
  }
];
