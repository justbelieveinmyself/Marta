import { LocalUser } from "./local-user";

export class LoginResponseDto {
    refreshToken!: string;
    user!: LocalUser;
}
