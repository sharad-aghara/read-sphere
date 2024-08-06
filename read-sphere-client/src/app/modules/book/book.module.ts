import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BookRoutingModule } from './book-routing.module';
import { BookListComponent } from './pages/book-list/book-list.component';
import { BookCardComponent } from './components/book-card/book-card.component';
import { RatingComponent } from './components/rating/rating.component';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    BookRoutingModule,
    BookListComponent,
    BookCardComponent,
    RatingComponent
  ]
})
export class BookModule { }
