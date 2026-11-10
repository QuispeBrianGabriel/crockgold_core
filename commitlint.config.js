export default {
    extends: ['@commitlint/config-conventional'],
    ignores: [
        (message) => /^(build|ci)\(deps(-dev)?\): Bump /.test(message),
    ],
};
