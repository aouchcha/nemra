"use client";

import Link from "next/link";
import { MarketingShell } from "@/components/site";
import { buttonClass } from "@/components/ui";

export default function NotFound() {
  return (
    <MarketingShell>
      <main className="hero">
        <div className="hero-copy">
          <p className="eyebrow">404</p>
          <h1>Page not found</h1>
          <p className="muted">The route does not exist in the current frontend scaffold.</p>
          <Link href="/" className={buttonClass("primary")}>
            Go home
          </Link>
        </div>
      </main>
    </MarketingShell>
  );
}
