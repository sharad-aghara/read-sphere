import { NgClass, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PageResponseBookResponse } from '../../../../services/models';
import { BookResponse } from '../../../../services/models/book-response';
import { BookService } from '../../../../services/services';
import { BookCardComponent } from '../../components/book-card/book-card.component';
import { RatingComponent } from '../../components/rating/rating.component';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [NgIf, NgClass, BookCardComponent, RatingComponent, NgFor],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss',
})
export class BookListComponent implements OnInit  {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  size = 5;
  pages: any = [];
  message = '';
  level: 'success' | 'error' = 'success';

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.findAllBooks();
    console.log("find all books, ng onInit()")
  }

  private findAllBooks() {
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    })
      .subscribe({
        next: (books) => {
          this.bookResponse = books;
          console.log("BOOK.RESPONSE: -> ", this.bookResponse);
          this.pages = Array(this.bookResponse.totalPages)
            .fill(0)
            .map((x, i) => i);
        },
        error(err) {
            console.error('Error fetching books', err)
        },
      })
  }

  // gotToPage(page: number) {
  //   this.page = page;
  //   this.findAllBooks();
  // }

  // goToFirstPage() {
  //   this.page = 0;
  //   this.findAllBooks();
  // }

  // goToPreviousPage() {
  //   this.page --;
  //   this.findAllBooks();
  // }

  // goToLastPage() {
  //   this.page = this.bookResponse.totalPages as number - 1;
  //   this.findAllBooks();
  // }

  // goToNextPage() {
  //   this.page++;
  //   this.findAllBooks();
  // }

  // get isLastPage() {
  //   return this.page === this.bookResponse.totalPages as number - 1;
  // }


  // borrowBook(book: BookResponse) {
  //   this.message = '';
  //   this.level = 'success';
  //   this.bookService.borrowBook({
  //     'book-id': book.id as number
  //   }).subscribe({
  //     next: () => {
  //       this.level = 'success';
  //       this.message = 'Book has been successfully added to your list.';
  //     },
  //     error: (err) => {
  //       console.log(err);
  //       this.level = 'error';
  //       this.message = err.error.error
  //     }
  //   })
  // }

  // displayBookDetails(book: BookResponse) {
  //   this.router.navigate(['books', 'details', book.id]);
  // }

}
