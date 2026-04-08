# Stock Asistant Module

This folder is an external package/copy of the AI Assistant frontend module.

## Purpose

- Keep AI Assistant files grouped in one transfer-friendly folder.
- Share/import the feature into another app instance.

## Important

This folder is **not directly runnable** by Angular from workspace root.

To run successfully, copy it into the Angular app source tree:

- Source package: `stock_asistant/`
- Runtime target: `stok-web-master/src/app/stock_assistant/`

## Correct Setup 

1. Copy module files

- Copy all files/folders from:
  - `stock_asistant/`
- Into:
  - `stok-web-master/src/app/stock_assistant/`

2. Ensure route wiring in app routes

- File: `stok-web-master/src/app/app.routes.ts`
- Ensure AI route loads:

```ts
{
  path: "ai-assistant",
  loadChildren: () => import("./stock_assistant/ai-assistant.routes").then((m) => m.aiAssistantRoutes)
}
```

3. Ensure style import

- File: `stok-web-master/src/styles.scss`
- Ensure this line exists:

```scss
@import "./app/stock_assistant/ai-assistant.component.scss";
```

4. Start backend

- In `stok-master/`:

```bash
docker compose up -d
```

5. Start frontend

- In `stok-web-master/`:

```bash
npm install
npm start
```

6. Open app

- Main app: `http://localhost:4200`
- AI Dashboard: `http://localhost:4200/ai-assistant/dashboard`

## Backend dependency

AI Assistant data relies on secured backend endpoints under:

- `/api/stok/product/search`
- `/api/stok/client/search`
- `/api/stok/invoice/search`

So backend must be running and user must be authenticated in the app.
