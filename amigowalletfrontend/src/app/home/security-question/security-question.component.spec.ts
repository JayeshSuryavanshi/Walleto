import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SecurityQuestionComponent } from './security-question.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { createTranslateLoader } from '../home.module';
import { UriService } from 'src/app/shared/uri.service';
import { LoggerService } from 'src/app/shared/logger.service';

describe('SecurityQuestionComponent', () => {
  let component: SecurityQuestionComponent;
  let fixture: ComponentFixture<SecurityQuestionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        ReactiveFormsModule,
        HttpClientModule,
        TranslateModule.forRoot({
          loader: {
            provide: TranslateLoader,
            useFactory: (createTranslateLoader),
            deps: [HttpClient]
          }
        })
      ],
      declarations: [ SecurityQuestionComponent ],
      providers: [UriService, LoggerService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecurityQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
