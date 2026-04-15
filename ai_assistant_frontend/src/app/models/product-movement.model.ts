export type MovementType =
  | 'IN'
  | 'OUT'
  | 'ADJUSTMENT'
  | 'SALE'
  | 'TRANSFORMATION_INPUT'
  | 'TRANSFORMATION_OUTPUT';

export type SourceType =
  | 'MANUAL'
  | 'INVOICE'
  | 'STATION'
  | 'SYSTEM';

export interface ProductMovementRequest {
  productPublicId: string;
  productName: string;
  movementType: MovementType;
  quantity: number;
  oldStock: number;
  newStock: number;
  sourceType: SourceType;
  sourceReference?: string | null;
  movementDate: string;
  comment?: string | null;
}

export interface ProductMovementResponse {
  publicId: string;
  productPublicId: string;
  productName: string;
  movementType: MovementType;
  quantity: number;
  oldStock: number;
  newStock: number;
  sourceType: SourceType;
  sourceReference?: string | null;
  movementDate: string;
  comment?: string | null;
  createdAt: string;
}

