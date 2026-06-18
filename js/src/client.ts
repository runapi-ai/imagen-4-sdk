import { BaseClient, type ClientOptions } from '@runapi.ai/core';
import { TextToImage } from './resources/text-to-image';
import { RemixImage } from './resources/remix-image';

/**
 * Imagen 4 image generation API client.
 *
 * Three text-to-image quality tiers (imagen-4, imagen-4-fast, imagen-4-ultra)
 * and a dedicated remix model for guided image transformation from source images.
 *
 * @example
 * ```typescript
 * const client = new Imagen4Client({ apiKey: 'your-api-key' });
 *
 * const result = await client.textToImage.run({
 *   model: 'imagen-4',
 *   prompt: 'A cat in a spacesuit on the moon',
 * });
 * ```
 */
export class Imagen4Client extends BaseClient {
  /** Text-to-image generation across three quality tiers. */
  public readonly textToImage: TextToImage;
  /** Generate new images guided by one or more source images combined with a text prompt. */
  public readonly remixImage: RemixImage;

  constructor(options: ClientOptions = {}) {
    super(options);
    this.textToImage = new TextToImage(this.http);
    this.remixImage = new RemixImage(this.http);
  }
}
