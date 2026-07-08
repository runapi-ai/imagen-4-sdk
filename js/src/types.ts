import type { AsyncTaskStatus } from '@runapi.ai/core';

/** Text-to-image model variants differing by quality and latency. */
export type Imagen4TextModel = 'imagen-4' | 'imagen-4-fast' | 'imagen-4-ultra';
/** The remix model that accepts source images for guided generation. */
export type Imagen4RemixModel = 'imagen-4-pro-remix-image';
/** Union of all Imagen 4 model identifiers. */
export type Imagen4Model = Imagen4TextModel | Imagen4RemixModel;

/** Aspect ratios for standard and ultra text-to-image generation. */
export type BaseTextAspectRatio = '1:1' | '16:9' | '9:16' | '3:4' | '4:3';
/** Aspect ratios for fast text-to-image generation. */
export type FastTextAspectRatio = BaseTextAspectRatio | 'auto';
/** Aspect ratios for text-to-image generation. */
export type TextAspectRatio = FastTextAspectRatio;
/** Extended aspect ratios for remix, including "auto" which infers from source images. */
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

/** Pixel resolution tier for remix output. */
export type OutputResolution = '1k' | '2k' | '4k';
/** Image encoding format for remix output. */
export type OutputFormat = 'png' | 'jpg';

/**
 * Parameters for imagen-4 (standard) and imagen-4-ultra (highest quality).
 * These models produce a single image per request.
 */
export interface BaseTextTextToImageParams {
  model: 'imagen-4' | 'imagen-4-ultra';
  prompt: string;
  callback_url?: string;
  /** Content to steer the model away from in the output. */
  negative_prompt?: string;
  aspect_ratio?: BaseTextAspectRatio;
  /** Fixed seed for reproducible generation. */
  seed?: number;
}

/** Parameters for imagen-4-fast, the lower-latency text-to-image tier. */
export interface FastTextTextToImageParams {
  model: 'imagen-4-fast';
  prompt: string;
  callback_url?: string;
  /** Content to steer the model away from in the output. */
  negative_prompt?: string;
  aspect_ratio?: FastTextAspectRatio;
  /** Fixed seed for reproducible generation. */
  seed?: number;
}

/**
 * Parameters for image remix -- guided generation from 1-8 source images
 * combined with a text prompt. Supports resolution and format control.
 */
export interface RemixImageParams {
  model: Imagen4RemixModel;
  prompt: string;
  callback_url?: string;
  /** 1-8 publicly accessible source image URLs. */
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

/** A generated image with its CDN URL and optional unprocessed origin URL. */
export interface Image {
  url: string;
  /** Unprocessed source URL when available (before CDN optimization). */
  origin_url?: string;
}

export interface TextToImageResponse {
  id: string;
  status: AsyncTaskStatus;
  images?: Image[];
  error?: string;
  [key: string]: unknown;
}

/**
 * Resolved response returned by `run()` after polling sees `status: 'completed'`.
 * Narrows the base response so `images` is guaranteed non-optional.
 */
export type CompletedTextToImageResponse = TextToImageResponse & {
  status: 'completed';
  images: Image[];
};

export type RemixImageResponse = TextToImageResponse;
export type CompletedRemixImageResponse = CompletedTextToImageResponse;
