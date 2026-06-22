"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { AppShell } from "@/components/site";
import { useAuth } from "@/contexts/auth-context";
import type { ReactNode } from "react";

export default function AppLayout({ children }: Readonly<{ children: ReactNode }>) {
  const router = useRouter();
  const { ready, isAuthenticated } = useAuth();

  useEffect(() => {
    if (ready && !isAuthenticated) {
      router.replace("/login");
    }
  }, [ready, isAuthenticated, router]);

  if (!ready) {
    return <main className="hero"><div className="hero-copy"><p className="muted">Loading session...</p></div></main>;
  }

  if (!isAuthenticated) {
    return null;
  }

  return <AppShell>{children}</AppShell>;
}
