import { INestApplication } from '@nestjs/common';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';

export async function setUpSwagger(app: INestApplication) {
  if (process.env.NODE_ENV === 'test') return;

  const config = new DocumentBuilder()
    .setTitle('fooriend-backend')
    .setDescription('푸렌드 API')
    .setVersion('1.0')
    .build();
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('swagger', app, document);
}
