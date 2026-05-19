import type { AsyncTaskStatus } from '@runapi.ai/core';

export type Imagen4TextModel = 'imagen-4' | 'imagen-4-fast' | 'imagen-4-ultra';
export type Imagen4Model = Imagen4TextModel | 'imagen-4-pro-image-to-image';

export type TextAspectRatio = '1:1' | '16:9' | '9:16' | '3:4' | '4:3';
export type ProAspectRatio =
  | '1:1'
  | '2:3'
  | '3:2'
  | '3:4'
  | '4:3'
  | '4:5'
  | '5:4'
  | '9:16'
  | '16:9'
  | '21:9'
  | 'auto';

export type Resolution = '1K' | '2K' | '4K';
export type OutputFormat = 'png' | 'jpg';
export type NumImages = '1' | '2' | '3' | '4';

export interface BaseTextTextToImageParams {
  model: 'imagen-4' | 'imagen-4-ultra';
  prompt: string;
  callback_url?: string;
  negative_prompt?: string;
  aspect_ratio?: TextAspectRatio;
  seed?: string | number;
}

export interface FastTextTextToImageParams {
  model: 'imagen-4-fast';
  prompt: string;
  callback_url?: string;
  negative_prompt?: string;
  aspect_ratio?: TextAspectRatio;
  seed?: string | number;
  num_images?: NumImages;
}

export interface ProImageToImageParams {
  model: 'imagen-4-pro-image-to-image';
  prompt: string;
  callback_url?: string;
  image_input?: string[];
  aspect_ratio?: ProAspectRatio;
  resolution?: Resolution;
  output_format?: OutputFormat;
}

export type TextToImageParams = BaseTextTextToImageParams | FastTextTextToImageParams | ProImageToImageParams;

export interface TaskCreateResponse {
  id: string;
  status: AsyncTaskStatus;
}

export interface TextToImageResponse {
  id: string;
  status: AsyncTaskStatus;
  result_urls?: string[];
  error?: string;
  [key: string]: unknown;
}

export type CompletedTextToImageResponse = TextToImageResponse & {
  status: 'completed';
  result_urls: string[];
};
