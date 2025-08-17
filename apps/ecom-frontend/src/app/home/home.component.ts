import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FeaturedComponent } from './featured/featured.component';

@Component({
  selector: 'app-home',
  imports: [CommonModule, FeaturedComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {}
