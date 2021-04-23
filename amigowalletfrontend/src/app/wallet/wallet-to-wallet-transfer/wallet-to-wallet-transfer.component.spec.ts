import { async, ComponentFixture, TestBed } from '@angular/core/testing';



import { WalletToWalletTransferComponent } from './wallet-to-wallet-transfer.component';

describe('wallet-WalletToWalletTransferComponent', () => {
  let component: WalletToWalletTransferComponent;
  let fixture: ComponentFixture<WalletToWalletTransferComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WalletToWalletTransferComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WalletToWalletTransferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});