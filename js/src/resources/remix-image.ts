import type { HttpClient, RequestOptions, PollingOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { RemixImageParams, RemixImageResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/imagen_4/remix_image';

/**
 * Generates new images guided by 1-8 source images combined with a text prompt.
 * Supports output resolution control (1k/2k/4k) and format selection (png/jpg).
 */
export class RemixImage {
  constructor(private readonly http: HttpClient) {}

  /**
   * Transform source images guided by a text prompt and wait until complete.
   * @param params Remix-image parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed task with images.
   */
  async run(params: RemixImageParams, options?: RequestOptions & PollingOptions): Promise<RemixImageResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<RemixImageResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
  }

  /**
   * Transform source images guided by a text prompt; returns immediately with a task id.
   * @param params Remix-image parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: RemixImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  /**
   * Fetch the current status of a remix-image task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current remix-image task status.
   */
  async get(id: string, options?: RequestOptions): Promise<RemixImageResponse> {
    return this.http.request<RemixImageResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
