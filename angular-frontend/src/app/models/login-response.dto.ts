import { LocalUser } from "./local-user";

export class LoginResponseDto {
    token!: string;
    user!: LocalUser;
}
