import { describe, it, expect, vi } from 'vitest';
import { Imagen4Client } from '../../src/client';

describe('Imagen4 remixImage', () => {
  it('sends create request with source image params', async () => {
    const request = vi.fn().mockResolvedValue({ id: 'task_123', status: 'processing' });
    const client = new Imagen4Client({ apiKey: 'test' });
    (client as any).remixImage.http.request = request;

    await client.remixImage.create({
      model: 'imagen-4-pro-remix-image',
      prompt: 'Restyle this image',
      source_image_urls: ['https://upload.wikimedia.org/wikipedia/commons/a/a9/Example.jpg'],
      aspect_ratio: 'auto',
      output_resolution: '2k',
      output_format: 'png',
    });

    expect(request).toHaveBeenCalledWith('POST', '/api/v1/imagen_4/remix_image', {
      body: {
        model: 'imagen-4-pro-remix-image',
        prompt: 'Restyle this image',
        source_image_urls: ['https://upload.wikimedia.org/wikipedia/commons/a/a9/Example.jpg'],
        aspect_ratio: 'auto',
        output_resolution: '2k',
        output_format: 'png',
      },
    });
  });

  it('decodes completed responses with image objects', async () => {
    const request = vi.fn().mockResolvedValue({
      id: 'task_123',
      status: 'completed',
      images: [{ url: 'https://file.runapi.ai/remixed/image.png' }],
    });
    const client = new Imagen4Client({ apiKey: 'test' });
    (client as any).remixImage.http.request = request;

    const result = await client.remixImage.get('task_123');

    expect(request).toHaveBeenCalledWith('GET', '/api/v1/imagen_4/remix_image/task_123', {});
    expect(result.images).toEqual([{ url: 'https://file.runapi.ai/remixed/image.png' }]);
  });
});
