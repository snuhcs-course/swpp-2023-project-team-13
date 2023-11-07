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
      googleMapPlaceId: 'ChIJQ0cqkcyhfDURRzbIS7i3HYo',
    });

    const images = [
      await ImageEntity.create({
        url: 'https://d2ghbk063u2bdn.cloudfront.net/goku1.jpg',
      }).save(),
      await ImageEntity.create({
        url: 'https://d2ghbk063u2bdn.cloudfront.net/goku2.jpg',
      }).save(),
      await ImageEntity.create({
        url: 'https://d2ghbk063u2bdn.cloudfront.net/goku3.jpg',
      }).save(),
    ];

    const user1 = await UserEntity.findOneByOrFail({
      name: '박명훈',
    });
    const user2 = await UserEntity.findOneByOrFail({
      name: '조용찬',
    });
    const user3 = await UserEntity.findOneByOrFail({
      name: '이미르',
    });

    const reviews = [
      await ReviewEntity.create({
        restaurant,
        user: user2,
        content:
          '고쿠 철판구이 집에서의 식사 경험은 일본의 정취를 그대로 느낄 수 있었던 소중한 시간이었습니다. 직원들은 손님을 맞이할 때부터 따뜻한 미소와 함께 정중한 일본식 인사로 분위기를 한층 더 일본스럽게 만들었습니다.\n' +
          '\n' +
          '식사는 철판 위에서 직접 요리하는 공연과도 같았어요. 셰프의 숙련된 손놀림은 단순한 식사를 넘어서 한 편의 예술작품을 보는 듯 했습니다. 고기는 바깥은 바삭하고 안은 육즙이 가득하여, 철판 위에서 구워지는 모습만으로도 입맛을 자극했습니다. 갓 구운 고기와 야채의 조화가 완벽했고, 집에서 만든 듯한 소스는 그 맛을 더욱 풍부하게 해주었습니다.\n' +
          '\n' +
          '가게 내부는 깔끔하고 정돈된 일본의 전통적인 분위기를 간직하고 있어, 음식과 함께 문화적 체험도 가능했습니다. 가격대가 다소 높은 편이지만, 그만큼의 가치는 충분히 제공하는 것 같습니다. 특별한 날, 또는 일본 철판구이를 정갈하게 즐기고 싶을 때 추천하고 싶은 장소입니다.',
        images: [images[0], images[1]],
        isPositive: true,
      }).save(),
      await ReviewEntity.create({
        restaurant,
        user: user1,
        content:
          '고쿠 철판구이에서의 경험은 기대에 못 미쳤습니다. 서비스는 무뚝뚝했고, 고기는 탄 맛이 강해 실망스러웠습니다. 분위기와 인테리어는 멋졌지만, 철판 요리의 맛과 질은 가격대비 만족스럽지 않았어요. 셰프의 요리 과정은 볼거리였으나, 기다린 시간에 비해 음식은 평범했습니다. 다른 곳에 비해 차별화된 맛이나 서비스를 제공받지 못한 것 같아 아쉬움이 크게 남는 방문이었습니다.',
        images: [images[2]],
        isPositive: false,
      }).save(),
    ];
  }
}
