import { error, json } from "@sveltejs/kit";
import * as yup from 'yup';
import * as bcrypt from 'bcrypt';
import { getUserByEmailOrName, purifyIdentity } from "$lib/controllers/identity";
import { validateRequest } from '$lib/middlewares';
import { setAuthCookies } from '$lib/utils';
import type { RequestHandler } from '@sveltejs/kit';

type LoginParams = {
  login: string;
  password: string;
}

export const POST: RequestHandler = async ({ request, cookies, locals }) =>
  validateRequest<LoginParams>(request, yup.object().shape({
    login: yup.string().required('Insira um e-mail ou nome de usuário válidos'),
    password: yup.string().min(6, 'A senha deve ter pelo menos 6 dígitos').required('Insira uma senha'),
  }), async (body) => {
    if (!!locals.identity) throw error(401, 'Você já está logado');

    const identity = await getUserByEmailOrName(body.login);
    if (!identity) throw error(401, 'Endereço de e-mail, nome de usuário ou senha inválidos');

    const isValidPassword = await bcrypt.compare(body.password, identity.password);
    if (!isValidPassword) throw error(401, 'Endereço de e-mail, nome de usuário ou senha inválidos');

    const safeIdentity = purifyIdentity(identity);

    setAuthCookies(cookies, identity.token);
    return json({
      identity: safeIdentity
    });
});
