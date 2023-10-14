import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { User } from './models/user.entity';
import { CreateUserDto } from '../auth/controller/signup-dtos/createuser.dto';

@Injectable()
export class UserService {
    constructor(
        @InjectRepository(User)
        private usersRepository: Repository<User>,
    ) {}

    async create(createUserDto: CreateUserDto): Promise<User> {
        const { name, username, password } = createUserDto;
        const user = this.usersRepository.create({ name, username, password });
        return this.usersRepository.save(user);
    }

    async findByUsername(username: string): Promise<User | undefined> {
        const user = await this.usersRepository.findOne({ where: { username } });
        return user ? user : undefined; // Return undefined when no user is found
    }
}
