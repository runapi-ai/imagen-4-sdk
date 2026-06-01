import type { AsyncTaskStatus } from '@runapi.ai/core';

export type Imagen4TextModel = 'imagen-4' | 'imagen-4-fast' | 'imagen-4-ultra';
export type Imagen4RemixModel = 'imagen-4-pro-remix-image';
export type Imagen4Model = Imagen4TextModel | Imagen4RemixModel;

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

export type OutputResolution = '1k' | '2k' | '4k';
export type OutputFormat = 'png' | 'jpg';
export type OutputCount = 1 | 2 | 3 | 4;

export interface BaseTextTextToImageParams {
  model: 'imagen-4' | 'imagen-4-ultra';
  prompt: string;
  callback_url?: string;
  negative_prompt?: string;
  aspect_ratio?: TextAspectRatio;
  seed?: number;
}

export interface FastTextTextToImageParams {
  model: 'imagen-4-fast';
  prompt: string;
  callback_url?: string;
  negative_prompt?: string;
  aspect_ratio?: TextAspectRatio;
  seed?: number;
  output_count?: OutputCount;
}

export interface RemixImageParams {
  model: Imagen4RemixModel;
  prompt: string;
  callback_url?: string;
  source_image_urls: string[];
  aspect_ratio?: ProAspectRatio;
  output_resolution?: OutputResolution;
  output_format?: OutputFormat;
}

export type TextToImageParams = BaseTextTextToImageParams | FastTextTextToImageParams;

export interface TaskCreateResponse {
  id: string;
  status: AsyncTaskStatus;
}

export interface Image {
  url: string;
  origin_url?: string;
}

export interface TextToImageResponse {
  id: string;
  status: AsyncTaskStatus;
  images?: Image[];
  error?: string;
  [key: string]: unknown;
}

export type CompletedTextToImageResponse = TextToImageResponse & {
  status: 'completed';
  images: Image[];
};

export type RemixImageResponse = TextToImageResponse;
export type CompletedRemixImageResponse = CompletedTextToImageResponse;
