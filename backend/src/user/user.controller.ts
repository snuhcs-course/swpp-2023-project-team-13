import { Controller, Post, Body, ConflictException } from '@nestjs/common';
import { UserService } from './user.service';
import { User } from './models/user.entity';
import {CreateUserDto} from "../auth/controller/signup-dtos/createuser.dto";

@Controller('user')
export class UserController {
    constructor(private readonly userService: UserService) {}


    @Post('signup')
    async signUp(@Body() createUserDto: CreateUserDto): Promise<User> {
        const existingUser = await this.userService.findByUsername(createUserDto.username);
        if (existingUser) {
            throw new ConflictException('Username already exists');
        }
        return this.userService.create(createUserDto);
    }

}
