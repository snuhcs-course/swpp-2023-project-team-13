export function getEnvFilePath() {
  const env = process.env.NODE_ENV!;
  if (env === 'production' || env.includes('command')) {
    return '.env';
  } else if (env === 'test') {
    return '.env.test';
  } else {
    return '.env.dev';
  }
}
