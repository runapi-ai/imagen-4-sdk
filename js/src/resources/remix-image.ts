import type { HttpClient, RequestOptions, PollingOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { RemixImageParams, RemixImageResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/imagen_4/remix_image';

export class RemixImage {
  constructor(private readonly http: HttpClient) {}

  async run(params: RemixImageParams, options?: RequestOptions & PollingOptions): Promise<RemixImageResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<RemixImageResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
  }

  async create(params: RemixImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  async get(id: string, options?: RequestOptions): Promise<RemixImageResponse> {
    return this.http.request<RemixImageResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
