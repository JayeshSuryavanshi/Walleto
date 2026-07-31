# ---------- Stage 1: build the Angular app ----------
FROM node:20 AS build
WORKDIR /app

# Install dependencies first (better layer caching). .npmrc carries the
# legacy-peer-deps safety net.
COPY package.json package-lock.json* .npmrc ./
RUN npm install

# Copy sources and produce an optimized production build (esbuild builder).
COPY . .
RUN npx ng build --configuration production

# ---------- Stage 2: serve the built SPA with nginx ----------
FROM nginx:alpine
# @angular/build:application emits the browser bundle under dist/<project>/browser
COPY --from=build /app/dist/amigo-wallet-ui/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
