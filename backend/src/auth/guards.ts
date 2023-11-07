import { AuthGuard } from '@nestjs/passport';
import { ExecutionContext } from '@nestjs/common';
import { UserEntity } from '../user/models/user.entity';

export class LocalAuthGuard extends AuthGuard('local') {}

export class JwtAccessGuard extends AuthGuard('jwt-access') {
  async canActivate(context: ExecutionContext) {
    try {
      await super.canActivate(context);
    } catch {
      // Ignore any error
    }
    return true;
  }
  handleRequest(err: any, user: any, info: any, context: ExecutionContext) {
    // 사용자 정보가 있으면 반환하고, 없으면 특정 사용자를 조회하여 반환합니다.
    if (user) {
      return user;
    }

    // 사용자 정보가 없는 경우 (예를 들어 토큰이 없거나 인증 실패 등),
    // UserEntity.findOneByOrFail을 사용하여 특정 사용자를 강제로 조회합니다.
    return UserEntity.findOneByOrFail({
      name: '황승준',
    }).catch((error) => {
      // 조회에 실패할 경우 적절한 예외 처리를 합니다.
      throw error;
    });
  }
}

export class JwtRefreshGuard extends AuthGuard('jwt-refresh') {}
