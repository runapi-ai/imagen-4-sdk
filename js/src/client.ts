import { createHttpClient, type ClientOptions } from '@runapi.ai/core';
import { TextToImage } from './resources/text-to-image';
import { RemixImage } from './resources/remix-image';

export class Imagen4Client {
  public readonly textToImage: TextToImage;
  public readonly remixImage: RemixImage;

  constructor(options: ClientOptions = {}) {
    const http = createHttpClient(options);
    this.textToImage = new TextToImage(http);
    this.remixImage = new RemixImage(http);
  }
}
