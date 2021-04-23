import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransferToBankComponent} from './transfer-to-bank-component.component';

describe('TransferToBankComponentComponent', () => {
  let component: TransferToBankComponent;
  let fixture: ComponentFixture<TransferToBankComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransferToBankComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransferToBankComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
