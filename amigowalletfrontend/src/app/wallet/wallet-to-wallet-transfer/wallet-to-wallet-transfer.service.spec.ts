import { TestBed } from '@angular/core/testing';
import { WalletToWalletTransferService } from './wallet-to-wallet-transfer.service';



describe('ToWalletServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WalletToWalletTransferService = TestBed.get(WalletToWalletTransferService);
    expect(service).toBeTruthy();
  });
});
