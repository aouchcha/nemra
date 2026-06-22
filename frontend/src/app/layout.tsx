import type { Metadata } from "next";
import { AuthProvider } from "@/contexts/auth-context";
import type { ReactNode } from "react";
import "./globals.css";

export const metadata: Metadata = {
  title: "Nemra",
  description: "Service marketplace frontend for the Nemra backend",
};

export default function RootLayout({ children }: Readonly<{ children: ReactNode }>) {
  return (
    <html lang="en">
      <body>
        <AuthProvider>{children}</AuthProvider>
      </body>
    </html>
  );
}
