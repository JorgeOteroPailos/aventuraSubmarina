module.exports = {
  darkMode: ["class"],
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        border: "hsl(210, 15%, 90%)",
        input: "hsl(210, 15%, 90%)",
        ring: "hsl(193, 70%, 45%)",
        background: "hsl(39, 27%, 94%)",
        foreground: "hsl(217, 30%, 12%)",
        primary: {
          DEFAULT: "hsl(193, 70%, 45%)",
          foreground: "hsl(0, 0%, 100%)",
        },
        secondary: {
          DEFAULT: "hsl(10, 77%, 61%)",
          foreground: "hsl(0, 0%, 100%)",
        },
        tertiary: {
          DEFAULT: "hsl(215, 60%, 38%)",
          foreground: "hsl(0, 0%, 100%)",
        },
        neutral: {
          DEFAULT: "hsl(39, 27%, 94%)",
          foreground: "hsl(217, 30%, 12%)",
        },
        success: {
          DEFAULT: "hsl(155, 50%, 38%)",
          foreground: "hsl(0, 0%, 100%)",
        },
        warning: {
          DEFAULT: "hsl(40, 85%, 55%)",
          foreground: "hsl(217, 30%, 12%)",
        },
        destructive: {
          DEFAULT: "hsl(0, 84%, 60%)",
          foreground: "hsl(0, 0%, 100%)",
        },
        muted: {
          DEFAULT: "hsl(210, 20%, 96%)",
          foreground: "hsl(210, 9%, 38%)",
        },
        accent: {
          DEFAULT: "hsl(210, 20%, 96%)",
          foreground: "hsl(217, 30%, 12%)",
        },
        popover: {
          DEFAULT: "hsl(0, 0%, 100%)",
          foreground: "hsl(217, 30%, 12%)",
        },
        card: {
          DEFAULT: "hsl(0, 0%, 100%)",
          foreground: "hsl(217, 30%, 12%)",
        },
        gray: {
          50: "hsl(210, 20%, 98%)",
          100: "hsl(210, 20%, 96%)",
          200: "hsl(210, 15%, 90%)",
          300: "hsl(210, 10%, 80%)",
          400: "hsl(210, 8%, 65%)",
          500: "hsl(210, 7%, 50%)",
          600: "hsl(210, 9%, 38%)",
          700: "hsl(210, 12%, 28%)",
          800: "hsl(210, 16%, 18%)",
          900: "hsl(210, 20%, 10%)",
        },
      },
      fontFamily: {
        sans: ['"Nunito Sans"', 'sans-serif'],
        mono: ['"Fira Code"', 'monospace'],
      },
      fontSize: {
        'h1': ['42px', { lineHeight: '1.2', letterSpacing: '-0.025em', fontWeight: '500' }],
        'h2': ['31px', { lineHeight: '1.3', fontWeight: '500' }],
        'h3': ['23px', { lineHeight: '1.4', fontWeight: '400' }],
        'body': ['16px', { lineHeight: '1.5', fontWeight: '300' }],
        'label': ['14px', { lineHeight: '1.5', fontWeight: '300' }],
      },
      spacing: {
        '4': '1rem',
        '8': '2rem',
        '12': '3rem',
        '16': '4rem',
        '24': '6rem',
        '32': '8rem',
      },
      borderRadius: {
        lg: '12px',
        md: '10px',
        sm: '8px',
      },
      backgroundImage: {
        'gradient-1': 'linear-gradient(135deg, hsl(193, 70%, 45%) 0%, hsl(215, 60%, 38%) 100%)',
        'gradient-2': 'linear-gradient(135deg, hsl(10, 77%, 61%) 0%, hsl(340, 70%, 50%) 100%)',
        'button-border-gradient': 'linear-gradient(120deg, hsl(193, 70%, 45%) 0%, hsl(10, 77%, 61%) 100%)',
      },
      keyframes: {
        "accordion-down": {
          from: { height: "0" },
          to: { height: "var(--radix-accordion-content-height)" },
        },
        "accordion-up": {
          from: { height: "var(--radix-accordion-content-height)" },
          to: { height: "0" },
        },
        "toast-in": {
          from: { transform: "translateX(100%)", opacity: "0" },
          to: { transform: "translateX(0)", opacity: "1" },
        },
        "toast-out": {
          from: { transform: "translateX(0)", opacity: "1" },
          to: { transform: "translateX(100%)", opacity: "0" },
        },
        "dice-roll": {
          "0%": { transform: "rotate(0deg)" },
          "100%": { transform: "rotate(360deg)" },
        },
      },
      animation: {
        "accordion-down": "accordion-down 0.2s ease-out",
        "accordion-up": "accordion-up 0.2s ease-out",
        "toast-in": "toast-in 0.3s ease-out",
        "toast-out": "toast-out 0.3s ease-in",
        "dice-roll": "dice-roll 0.6s ease-in-out",
      },
    },
  },
  plugins: [],
}
