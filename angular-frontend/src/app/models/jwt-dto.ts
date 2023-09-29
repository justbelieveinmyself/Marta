import { LocalUser } from "./local-user";

export class JwtDto {
    token!: string;
    user!: LocalUser;
}
