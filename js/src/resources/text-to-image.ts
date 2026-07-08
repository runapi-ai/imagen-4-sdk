import type { HttpClient, RequestOptions, PollingOptions, ActionSchema } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import { contract } from '../contract_gen';
import type { TextToImageParams, TextToImageResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/imagen_4/text_to_image';

/**
 * Generates images from a text prompt.
 * Three model tiers are available: imagen-4 (standard), imagen-4-fast (lower latency),
 * and imagen-4-ultra (highest quality).
 */
export class TextToImage {
  constructor(private readonly http: HttpClient) {}

  /**
   * Generate images from a text prompt and wait until complete.
   * @param params Text-to-image parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed task with images.
   */
  async run(params: TextToImageParams, options?: RequestOptions & PollingOptions): Promise<TextToImageResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<TextToImageResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
  }

  /**
   * Generate images from a text prompt; returns immediately with a task id.
   * @param params Text-to-image parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: TextToImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    validateParams(contract['text-to-image'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body,
      ...options,
    });
  }

  /**
   * Fetch the current status of a text-to-image task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current text-to-image task status.
   */
  async get(id: string, options?: RequestOptions): Promise<TextToImageResponse> {
    return this.http.request<TextToImageResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
