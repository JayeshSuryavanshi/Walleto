/**
 * Local development environment (used by `ng serve` / `--configuration development`).
 *
 * Points directly at the locally-running wallet-api. The wallet-api enables CORS
 * for the Angular dev origin in development.
 */
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:3322/AmigoWallet',
};
