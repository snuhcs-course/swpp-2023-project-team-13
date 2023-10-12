import { config } from 'dotenv';
import { DataSourceOptions } from 'typeorm';
import { SnakeNamingStrategy } from 'typeorm-naming-strategies';
import { getEnvFilePath } from './getEnvFilePath';
import * as path from 'path';
import { User } from '../user/models/user.entity';

const envFilePath = getEnvFilePath();
config({ path: path.resolve(process.cwd(), envFilePath) });

const ormConfig: DataSourceOptions = {
  type: 'postgres',
  host: process.env.PG_HOST,
  port: +(process.env.PG_PORT ?? '5432'),
  username: process.env.PG_USER,
  password: process.env.PG_PASSWORD,
  database: process.env.PG_DBNAME,
  entities: [User],
  synchronize: true,
  migrationsTableName: 'migrations',
  migrations: ['dist/migrations/*.js'],
  migrationsRun: false,
  namingStrategy: new SnakeNamingStrategy(),
  logging: ['warn', 'error'],
  maxQueryExecutionTime: 3000,
};

export = ormConfig;
