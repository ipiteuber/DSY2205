import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

// Pie de pagina del sitio
@Component({
  selector: 'app-footer',
  imports: [RouterLink],
  templateUrl: './footer.html',
  styleUrl: './footer.css',
})
export class Footer {
  readonly anio = new Date().getFullYear();
}
