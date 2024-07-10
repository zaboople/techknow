module.exports = {
  root: true,
  env: { browser: true, es2020: true },
  extends: [
    'eslint:recommended',
    'plugin:react/recommended',
    'plugin:react/jsx-runtime',
    'plugin:react-hooks/recommended',
  ],

    overrides: [
        {
            files: ['**/*.+(ts|tsx)'],
            env: {
                browser: true,
            },
            parser: '@typescript-eslint/parser',
            parserOptions: {
                project: './tsconfig.eslint.json',
            },
            plugins: ['react', 'react-hooks'],
            extends: [
                'plugin:@typescript-eslint/recommended',
                'plugin:react/recommended',
                'plugin:react-hooks/recommended'
            ],
            rules: {
                'react/jsx-uses-react': 'error',
                'react/jsx-filename-extension': ['error', { extensions: ['.tsx'] }],
                '@typescript-eslint/no-unused-vars': ['error', { argsIgnorePattern: '^_' }],
                '@typescript-eslint/ban-ts-comment': 'off',
                'react/display-name': 'off',
                '@typescript-eslint/no-explicit-any': 'warn',
                'react/prop-types': 'off',
            },
        },
        {
            files: ['**/__tests__/**'],
            env: {},
            plugins: [],
            extends: ['plugin:testing-library/react'],
            rules: {
                '@typescript-eslint/no-var-requires': 'warn'
            },
        },
    ],
  ignorePatterns: ['dist', '.eslintrc.cjs'],
  parserOptions: { ecmaVersion: 'latest', sourceType: 'module' },
  settings: { react: { version: '18.2' } },
  plugins: ['react-refresh'],
  rules: {
    'react/jsx-no-target-blank': 'off',
    'react-refresh/only-export-components': [
      'warn',
      { allowConstantExport: true },
    ],
  },
}
