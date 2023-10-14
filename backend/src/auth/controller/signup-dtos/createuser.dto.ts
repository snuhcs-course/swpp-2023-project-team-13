import { IsString, IsNotEmpty, IsAlphanumeric, MinLength } from 'class-validator';

export class CreateUserDto {
    @IsString()
    name: string;

    @IsString()
    @IsNotEmpty()
    username: string;

    @IsString()
    @IsNotEmpty()
    password: string;
}