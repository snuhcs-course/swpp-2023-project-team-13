import { NestFactory, Reflector } from '@nestjs/core';
import { AppModule } from './app.module';
import {
  ClassSerializerInterceptor,
  INestApplication,
  ValidationPipe,
} from '@nestjs/common';
import { setUpSwagger } from './core/swagger';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  await setUpSwagger(app);
  await appSetting(app);
  if (process.env.NODE_ENV !== 'test') {
    await app.listen(3000);
  }
}

export async function appSetting(app: INestApplication) {
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
    }),
  );
  app.useGlobalInterceptors(new ClassSerializerInterceptor(app.get(Reflector)));
}

bootstrap();
