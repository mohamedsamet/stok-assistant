import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {CommonModule} from "@angular/common";
import {LoginService} from "./login/login.service";
import {ToastComponent} from "./shared/toast/toast.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, ToastComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'stok-web';

  constructor(private loginService: LoginService) {
  }

  ngOnInit(): void {
    this.loginService.checkToken()
  }
}
