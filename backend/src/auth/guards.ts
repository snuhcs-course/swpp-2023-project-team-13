import { AuthGuard } from '@nestjs/passport';

export class LocalAuthGuard extends AuthGuard('local') {}

export class JwtAccessGuard extends AuthGuard('jwt-access') {}