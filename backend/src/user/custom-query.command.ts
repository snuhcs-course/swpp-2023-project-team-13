import { Command, CommandRunner } from 'nest-commander';
import { UserFixture } from '../test/fixture/user.fixture';
import { UserEntity } from './models/user.entity';
import { RestaurantEntity } from '../review/models/restaurant.entity';
import { ExtractJwt } from 'passport-jwt';
import fromAuthHeaderWithScheme = ExtractJwt.fromAuthHeaderWithScheme;
import { ImageEntity } from '../review/models/image.entity';
import { ReviewEntity } from '../review/models/review.entity';

@Command({
  name: 'custom-query-command',
})
export class CustomQueryCommand extends CommandRunner {
  async run(
    passedParams: string[],
    options?: Record<string, any>,
  ): Promise<void> {
    // MyItem(LatLng(37.4791436,126.9527075), "Dos Tacos", "ChIJAQBE7YmffDURbru0nMzSg-o"),
    //     MyItem(LatLng(37.4788509,126.9541697), "동경산책", "ChIJJQ5EnD2hfDURp58ZApDTGCU"),
    //     MyItem(LatLng(37.477349,126.955729), "철판구이 고쿠", "ChIJQ0cqkcyhfDURRzbIS7i3HYo"),
    //     MyItem(LatLng(37.4790948, 126.95556), "멘쇼우라멘", "ChIJsdJX1fqhfDURujEHz2D9I6k"),
    //     MyItem(LatLng(37.4782239,126.9573202), "제일공간", "ChIJcWd1dn2hfDUR2_9ILmgwZ8Y"),
    //     MyItem(LatLng(37.4774917,126.9527019), "정가네낙지마당", "ChIJfYEJQIqffDURl20P0VPFbbs"),
    //     MyItem(LatLng(37.480378, 126.9534373), "Samcha", "ChIJ1-zoz4mffDURWcPDWpCVVR0"),
    //     MyItem(LatLng(37.4782267,126.9564157), "Meat Grillers", "ChIJeS2ZISegfDUR7HHYw4A_sps"),
    //     MyItem(LatLng(37.4777767,126.9568847), "나마라", "ChIJAVO_0tuhfDURi855uASAy9c"),
    //     MyItem(LatLng(37.47875099999999,126.9528016), "Hanoi Byeol", "ChIJf35IPIqffDURbn70T-Mun04"),
    //     MyItem(LatLng(37.4788394,126.9517714), "아띠 85도씨 베이커리", "ChIJWeB-KYqffDUROj36DHsB48Q"),
    //     MyItem(LatLng(37.4778999,126.9541072),"맨프롬오키나와","ChIJBdq8mh-ofDURQIkFvf5BkI4" )

    const restaurant = await RestaurantEntity.findOneByOrFail({
      googleMapPlaceId: 'ChIJsdJX1fqhfDURujEHz2D9I6k',
    });

    const images = [
      await ImageEntity.create({
        url: 'https://d2ghbk063u2bdn.cloudfront.net/mensyo1.jpg',
      }).save(),
      await ImageEntity.create({
        url: 'https://d2ghbk063u2bdn.cloudfront.net/mensyo2.jpg',
      }).save(),
      await ImageEntity.create({
        url: 'https://d2ghbk063u2bdn.cloudfront.net/mensyo3.jpg',
      }).save(),
    ];

    const user1 = await UserEntity.findOneByOrFail({
      name: '황승준',
    });

    const reviews = [
      await ReviewEntity.create({
        restaurant,
        user: user1,
        content:
          '멘쇼우 라멘의 깊은 맛에 반했습니다! 육수의 풍미가 일품이었고, 면발도 적절한 식감을 자랑했어요. 토핑도 신선했고, 서비스도 친절해서 다음에 또 방문하고 싶은 맛집이에요.',
        images: [images[0], images[1]],
        isPositive: true,
      }).save(),
      await ReviewEntity.create({
        restaurant,
        user: user1,
        content:
          '멘쇼우 라멘은 기대 이하였습니다. 육수가 너무 짜고 면도 다소 퍼진 느낌이었어요. 또한, 토핑도 아쉬운 부분이 많았고, 가격 대비 만족도가 떨어지네요. 서비스도 개선이 필요한 것 같습니다.',
        images: [images[2]],
        isPositive: false,
      }).save(),
    ];
  }
}
