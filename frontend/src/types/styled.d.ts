import 'styled-components';
import { Color } from './common';

interface Palette {
  50?: Color;
  100?: Color;
  200?: Color;
  300?: Color;
  400?: Color;
  500?: Color;
  600?: Color;
  700?: Color;
  800?: Color;
  900?: Color;
}

interface BreakPoints {
  sm?: number;
  md?: number;
  lg?: number;
  xl?: number;
  xxl?: number;
}

declare module 'styled-components' {
  export interface DefaultTheme {
    primary: Palette;
    red: Palette;
    green: Palette;
    black: Palette;
    gray: Palette;
    white: Color;
    modalOverlay: Color;
    shadow: Color;
    breakpoints: BreakPoints;
  }
}
