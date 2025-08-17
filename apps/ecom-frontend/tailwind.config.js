const { createGlobPatternsForDependencies } = require('@nx/angular/tailwind');
const { join } = require('path');

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    join(__dirname, 'src/**/!(*.stories|*.spec).{ts,html}'),
    ...createGlobPatternsForDependencies(__dirname),
  ],
  theme: {
    fontFamily: {
      sans: 'Inter var, ui-sans-serif, system-ui',
      serif: 'Inter var, ui-sans-serif, system-ui',
    },

    extend: {
      fontSize: {
        sm: '0.875rem',
        base: '1.3rem',
        xl: '1.55rem',
        '2xl': '1.563rem',
        '3xl': '1.853rem',
        '4xl': '2.441rem',
        '5xl': '3.052rem',
      },
    },
  },
  daisyui: {
    themes: [
      {
        // choose 'fantasy' them and custom some properties
        fantasy: {
          primary: '#228be6',
          'primary-content': 'white',
          secondary: '#F6F6F6',
          neutral: '#E8E8E8',
        },
      },
    ],
    base: true,
    styled: true,
    utils: true,
    prefix: '',
    logs: true,
    themeRoot: ':root',
  },
  plugins: [require('@tailwindcss/typography'), require('daisyui')],
};
