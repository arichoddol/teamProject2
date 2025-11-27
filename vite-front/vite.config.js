import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    // { babel: { plugins: [['babel-plugin-react-compiler']] } }
  ],
  server: {
    port: 3000,
    proxy: {
      "/api": {
        target: "http://localhost:8088", // 백엔드 주소
        changeOrigin: true,
        secure: false,
      },
    },
  },
  define: {
    global: 'window'
  }
});
