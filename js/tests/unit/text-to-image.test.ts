import { describe, it, expect, vi } from 'vitest';
import { Imagen4Client } from '../../src/client';

describe('Imagen4 textToImage', () => {
  it('sends create request with generation params', async () => {
    const request = vi.fn().mockResolvedValue({ id: 'task_123', status: 'processing' });
    const client = new Imagen4Client({ apiKey: 'test' });
    (client as any).textToImage.http.request = request;

    await client.textToImage.create({
      model: 'imagen-4-fast',
      prompt: 'A warm editorial photo',
      aspect_ratio: 'auto',
    });

    expect(request).toHaveBeenCalledWith('POST', '/api/v1/imagen_4/text_to_image', {
      body: {
        model: 'imagen-4-fast',
        prompt: 'A warm editorial photo',
        aspect_ratio: 'auto',
      },
    });
  });

  it('decodes completed responses with image objects', async () => {
    const request = vi.fn().mockResolvedValue({
      id: 'task_123',
      status: 'completed',
      images: [{ url: 'https://file.runapi.ai/generated/image.png' }],
    });
    const client = new Imagen4Client({ apiKey: 'test' });
    (client as any).textToImage.http.request = request;

    const result = await client.textToImage.get('task_123');

    expect(request).toHaveBeenCalledWith('GET', '/api/v1/imagen_4/text_to_image/task_123', {});
    expect(result.images).toEqual([{ url: 'https://file.runapi.ai/generated/image.png' }]);
  });
});
