# Stock Asistant Module

This folder contains an isolated copy of the AI Assistant feature files.

## Purpose

- Keep AI Assistant files grouped in one external folder.
- Serve as a standalone reference/archive outside the main Angular app structure.

## Current Status

- The running Angular app uses the in-project module path:
  - `stok-web-master/src/app/stock_assistant`
- This external folder (`stock_asistant`) is available for organization and portability.

## Contents

- `ai-assistant.routes.ts`
- `ai-assistant-shell.component.ts`
- `ai-assistant-shell.component.html`
- `ai-assistant.component.scss`
- `components/`
- `models/`
- `pages/`
- `services/`

## Notes

If you want this external folder to be used directly at runtime by Angular, additional workspace/TypeScript path configuration is required.
