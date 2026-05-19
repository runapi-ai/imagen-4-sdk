import { describe, it, expect, vi } from 'vitest';
import { Imagen4Client } from '../../src/client';

describe('Imagen4 textToImage', () => {
  it('sends create request with generation params', async () => {
    const request = vi.fn().mockResolvedValue({ id: 'task_123', status: 'processing' });
    const client = new Imagen4Client({ apiKey: 'test' });
    (client as any).textToImage.http.request = request;

    await client.textToImage.create({
      model: 'imagen-4-pro-image-to-image',
      prompt: 'Restyle this image',
      image_input: ['https://upload.wikimedia.org/wikipedia/commons/a/a9/Example.jpg'],
      aspect_ratio: 'auto',
      resolution: '2K',
      output_format: 'png',
    });

    expect(request).toHaveBeenCalledWith('POST', '/api/v1/imagen_4/text_to_image', {
      body: {
        model: 'imagen-4-pro-image-to-image',
        prompt: 'Restyle this image',
        image_input: ['https://upload.wikimedia.org/wikipedia/commons/a/a9/Example.jpg'],
        aspect_ratio: 'auto',
        resolution: '2K',
        output_format: 'png',
      },
    });
  });
});
