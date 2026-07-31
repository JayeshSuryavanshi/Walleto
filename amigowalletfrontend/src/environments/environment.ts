/**
 * Production / default environment.
 *
 * `apiBaseUrl` is a RELATIVE path so that, in the Docker deployment, the browser
 * uses same-origin requests that nginx reverse-proxies to the wallet-api
 * (Docker service name). No CORS needed in prod.
 *
 * For local `ng serve` development this file is replaced with
 * `environment.development.ts` (see angular.json build > configurations > development).
 */
export const environment = {
  production: true,
  apiBaseUrl: '/AmigoWallet',
};
